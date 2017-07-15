package torille.fi.lurkforreddit.utils

import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.format.DateUtils
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import torille.fi.lurkforreddit.data.models.jsonResponses.CommentChild
import torille.fi.lurkforreddit.data.models.jsonResponses.PostDetails
import torille.fi.lurkforreddit.data.models.jsonResponses.PostResponse
import torille.fi.lurkforreddit.data.models.jsonResponses.SubredditChildren
import torille.fi.lurkforreddit.data.models.view.*
import java.util.*

/**
 * Contains utility classes for parsing text
 */

object TextHelper {

    fun getLastFourChars(url: String): String {
        return url.substring(url.length - 4)
    }

    fun fromHtml(html: String): Spanned {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            return Html.fromHtml(html)
        }
    }

    fun formatScore(score: Int): String {
        val value = score.toString()
        if (score < 1000) {
            return value
        } else if (score < 10000) {
            return value[0] + "." + value[1] + "k"
        } else if (score < 100000) {
            return value[0] + "" + value[1] + "k"
        } else if (score < 10000000) {
            return value[0] + "" + value[1] + "" + value[2] + "k"
        } else {
            return value
        }
    }

    fun flatten(list: List<CommentChild>, level: Int): List<Comment> {
        val comments = ArrayList<Comment>(20)
        return Observable.fromArray(list)
                .subscribeOn(Schedulers.computation())
                .map { Observable.fromIterable(it) }
                .concatMap { commentChildObservable -> formatCommentData(commentChildObservable, level) }
                .collectInto(comments) { comments1, comments2 ->
                    comments.addAll(comments2)
                }
                .blockingGet()
    }

    fun flattenAdditionalComments(list: List<CommentChild>, level: Int): List<Comment> {
        val additionalComments: MutableList<Comment> = flatten(list, level).toMutableList()

        var i = 0
        val size = additionalComments.size
        while (i < size) {
            var j = 0
            val nestedSize = additionalComments.size
            while (j < nestedSize) {
                if (additionalComments[i].name == additionalComments[j].parentId) {
                    additionalComments[j] = additionalComments[j].copy(commentLevel = level + 1)
                }
                j++
            }
            i++
        }

        return additionalComments.toList()
    }

    private fun formatCommentData(commentChildObservable: Observable<CommentChild>, level: Int): Observable<List<Comment>> {
        return commentChildObservable
                .map({ it.data!! })
                .map { comment ->
                    var commentText: CharSequence = ""
                    var author: CharSequence = ""
                    var kind: kind = kind.MORE

                    if (comment.bodyHtml.isNotEmpty()) {
                        kind = torille.fi.lurkforreddit.data.models.view.kind.DEFAULT
                        commentText = formatTextToHtml(comment.bodyHtml)
                    } else if (comment.id == "_") {
                        commentText = "Continue this thread"
                    } else if (comment.children.isNotEmpty()) {
                        commentText = "Load more comments (" + comment.count + ")"
                    }

                    val responseAuthor = comment.author
                    if (responseAuthor.isNotEmpty() && comment.stickied) {
                        author = "<font color='#64FFDA'> Sticky post </font>" + responseAuthor
                    } else if (responseAuthor.isNotEmpty()) {
                        author = responseAuthor
                    }

                    author = fromHtml(author.toString() + " " + DateUtils.getRelativeTimeSpanString(comment.createdUtc * 1000, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS))

                    if (comment.edited) {
                        author = author.toString() + " (edited)"
                    }

                    val formatScore = formatScore(comment.score)

                    val formattedComment = Comment(
                            id = comment.id,
                            parentId = comment.parentId,
                            name = comment.name,
                            kind = kind,
                            author = author,
                            childCommentIds = comment.children,
                            commentLevel = level,
                            commentLinkId = comment.linkId,
                            commentText = commentText,
                            formattedScore = formatScore,
                            formattedTime = "",
                            replies = null
                    )

                    if (comment.replies != null) {
                        val commentChildList = comment.replies.commentData.commentChildren
                        val commentList = ArrayList<Comment>(commentChildList.size)
                        commentList.add(formattedComment)

                        Observable.fromArray(commentChildList)
                                .map { commentChildren -> Observable.fromIterable(commentChildren) }
                                .concatMap { commentChildObservable2 -> formatCommentData(commentChildObservable2, level + 1) }
                                .collectInto<List<Comment>>(commentList) { comments1, comments2 -> commentList.addAll(comments2) }
                                .map { _ ->
                                    commentList
                                }.blockingGet()
                    } else {
                        Arrays.asList(formattedComment)
                    }
                }
    }

    private val funcFormatPost = Function { postDetails: PostDetails ->
        val formatScore = TextHelper.formatScore(postDetails.score)
        var selfText: CharSequence = ""
        var previewImageUrl: String = ""
        val thumbnail = postDetails.thumbnail
        var title: CharSequence = TextHelper.fromHtml(postDetails.title)
        var flair: Spanned = SpannableStringBuilder.valueOf("")

        // sometimes formatting title can result in empty if it has <----- at start
        // etc
        if (title.isEmpty()) {
            title = postDetails.title
        }

        if (postDetails.selftextHtml != null) {
            selfText = formatTextToHtml(postDetails.selftextHtml)
        }

        if (postDetails.stickied) {
            flair = TextHelper.fromHtml("Stickied")
        }
        if (postDetails.linkFlairText != null) {
            flair = TextHelper.fromHtml(flair.toString() + " " + postDetails.linkFlairText)
        }

        when (thumbnail) {
        //"default", "self", "", "spoiler", "image" -> previewImageUrl = ""
            "nsfw" -> {
                flair = TextHelper.fromHtml("<font color='#FF1744'>NSFW </font>" + flair)
            }
            else -> previewImageUrl = DisplayHelper.getBestPreviewPicture(postDetails)
        }

        /*if (postDetails.isOver18()) {
        title = TextHelper.fromHtml("<font color='#FF1744'> NSFW </font>" + postDetails.title());
        }*/

        val numberOfComments = postDetails.numberOfComments
        Post(
                id = postDetails.name,
                thumbnail = thumbnail,
                domain = postDetails.domain,
                url = postDetails.url,
                score = formatScore,
                flairText = flair,
                selfText = selfText,
                isSelf = postDetails.isSelf,
                numberOfComments = numberOfComments.toString(),
                title = title,
                previewImage = previewImageUrl,
                permaLink = postDetails.permalink,
                author = postDetails.author,
                createdUtc = postDetails.createdUtc
        )
    }
    val funcFormatPostResponse = { postResponseObservable: Observable<PostResponse> ->
        postResponseObservable
                .map({ it.postDetails })
                .map(funcFormatPost)
    }

    val funcFormatPostDetails = { postDetailsObservable: Observable<PostDetails> ->
        postDetailsObservable
                .map(funcFormatPost)
    }

    fun formatSubreddit(subredditChildren: Observable<SubredditChildren>): Observable<Subreddit> {

        return subredditChildren
                .map { it.subreddit }
                .map { subredditResponse ->
                    // TODO("If subreddit isnt found return something else than empty")
                    if (subredditResponse.id.isNullOrBlank()) {
                        Subreddit()
                    } else {
                        Subreddit(
                                url = subredditResponse.url,
                                bannerUrl = subredditResponse.banner,
                                id = subredditResponse.id!!,
                                keyColor = subredditResponse.keyColor,
                                displayName = subredditResponse.displayName
                        )
                    }

                }
    }

    fun formatSearchResult(observable: Observable<SubredditChildren>): Observable<SearchResult> {
        return observable.map { subredditChildren ->
            val subredditChildObservable = Observable.fromArray(subredditChildren)
            Observable.zip<Subreddit, SubredditChildren, SearchResult>(
                    formatSubreddit(subredditChildObservable),
                    subredditChildObservable,
                    io.reactivex.functions.BiFunction({
                        subreddit: Subreddit, subredditChild: SubredditChildren ->
                        val subredditResponse = subredditChild.subreddit
                        val descriptionHtml = subredditResponse.descriptionHtml

                        val formattedTitle: CharSequence
                        val formattedSubscription: String
                        val formattedDescription: CharSequence

                        val infoText = subredditResponse.subscribers.toString() + " subscribers, Community since " + DateUtils.getRelativeTimeSpanString(subredditResponse.createdUtc * 1000)

                        if (subredditResponse.over18) {
                            formattedTitle = TextHelper.fromHtml(subredditResponse.url + "<font color='#FF1744'> NSFW</font>")
                        } else {
                            formattedTitle = subredditResponse.url
                        }

                        if (subredditResponse.subscribed) {
                            formattedSubscription = "Subscribed"
                        } else {
                            formattedSubscription = "Not subscribed"
                        }

                        if (!descriptionHtml.isNullOrEmpty()) {
                            formattedDescription = formatTextToHtml(descriptionHtml)
                        } else {
                            formattedDescription = "No description"
                        }

                        SearchResult(
                                title = formattedTitle,
                                description = formattedDescription,
                                infoText = infoText,
                                subscriptionInfo = formattedSubscription,
                                subreddit = subreddit
                        )
                    })
            ).blockingSingle()

        }
    }

    /**
     * Trims trailing whitespace. Removes any of these characters:
     * 0009, HORIZONTAL TABULATION
     * 000A, LINE FEED
     * 000B, VERTICAL TABULATION
     * 000C, FORM FEED
     * 000D, CARRIAGE RETURN
     * 001C, FILE SEPARATOR
     * 001D, GROUP SEPARATOR
     * 001E, RECORD SEPARATOR
     * 001F, UNIT SEPARATOR

     * @return "" if source is null, otherwise string with all trailing whitespace removed
     */

    private fun trimTrailingWhitespace(source: CharSequence?): CharSequence {

        if (source == null)
            return ""

        var i = source.length

        // loop back to the first non-whitespace character
        while (--i >= 0 && Character.isWhitespace(source[i])) {
        }

        return source.subSequence(0, i + 1)
    }

    fun formatTextToHtml(bodyText: String?): CharSequence {
        if (bodyText == null) {
            return ""
        }
        val htmlText = fromHtml(bodyText)
        return trimTrailingWhitespace(htmlText)
    }
}
