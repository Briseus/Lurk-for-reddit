package torille.fi.lurkforreddit.data;

import android.support.annotation.Nullable;
import android.text.Spanned;

import com.google.gson.annotations.SerializedName;

import org.parceler.ParcelConstructor;

/**
 * Model containing the details of each post
 */
@org.parceler.Parcel
public class PostDetails {

    String subreddit;
    String selftext;
    @Nullable
    @SerializedName("selftext_html")
    String selftextHtml;
    int likes;
    String author;
    String name;
    int score;
    String thumbnail;
    @SerializedName("subreddit_id")
    String subredditId;
    String url;
    String title;
    @Nullable
    @SerializedName("post_hint")
    String postHint;
    String domain;
    String id;
    @SerializedName("is_self")
    public Boolean isSelf;
    @SerializedName("created")
    long created;
    @SerializedName("created_utc")
    long createdUtc;
    boolean stickied;
    @Nullable
    String distinguished;
    String permalink;
    @SerializedName("num_comments")
    int numberOfComments;
    @Nullable
    @SerializedName("preview")
    ImagePreview images;
    @Nullable
    String previewImage;
    @Nullable
    String previewText;
    @Nullable
    String previewScore;
    @Nullable
    private transient Spanned previewTitle;

    public PostDetails() {
    }

    @ParcelConstructor
    public PostDetails(String subreddit, String selftext, @Nullable String selftextHtml, int likes, String author, String name, int score, @Nullable String thumbnail, String subredditId, String url, String title, @Nullable String postHint, String domain, String id, Boolean isSelf, long created, long createdUtc, boolean stickied, @Nullable String distinguished, String permalink, int numberOfComments, ImagePreview images, @Nullable String previewImage, @Nullable String previewText, String previewScore) {
        this.subreddit = subreddit;
        this.selftext = selftext;
        this.selftextHtml = selftextHtml;
        this.likes = likes;
        this.author = author;
        this.name = name;
        this.score = score;
        this.thumbnail = thumbnail;
        this.subredditId = subredditId;
        this.url = url;
        this.title = title;
        this.postHint = postHint;
        this.domain = domain;
        this.id = id;
        this.isSelf = isSelf;
        this.created = created;
        this.createdUtc = createdUtc;
        this.stickied = stickied;
        this.distinguished = distinguished;
        this.permalink = permalink;
        this.numberOfComments = numberOfComments;
        this.images = images;
        this.previewImage = previewImage;
        this.previewText = previewText;
        this.previewScore = previewScore;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getSelftext() {
        return selftext;
    }

    public void setSelftext(String selftext) {
        this.selftext = selftext;
    }

    @Nullable
    public String getSelftextHtml() {
        return selftextHtml;
    }

    public void setSelftextHtml(@Nullable String selftextHtml) {
        this.selftextHtml = selftextHtml;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSubredditId() {
        return subredditId;
    }

    public void setSubredditId(String subredditId) {
        this.subredditId = subredditId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Nullable
    public String getPostHint() {
        return postHint;
    }

    public void setPostHint(@Nullable String postHint) {
        this.postHint = postHint;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getSelf() {
        return isSelf;
    }

    public void setSelf(Boolean self) {
        isSelf = self;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getCreatedUtc() {
        return createdUtc;
    }

    public void setCreatedUtc(long createdUtc) {
        this.createdUtc = createdUtc;
    }

    public boolean isStickied() {
        return stickied;
    }

    public void setStickied(boolean stickied) {
        this.stickied = stickied;
    }

    @Nullable
    public String getDistinguished() {
        return distinguished;
    }

    public void setDistinguished(@Nullable String distinguished) {
        this.distinguished = distinguished;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    @Nullable
    public ImagePreview getImages() {
        return images;
    }

    public void setImages(@Nullable ImagePreview images) {
        this.images = images;
    }

    @Nullable
    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(@Nullable String previewImage) {
        this.previewImage = previewImage;
    }

    @Nullable
    public String getPreviewText() {
        return previewText;
    }

    public void setPreviewText(@Nullable String previewText) {
        this.previewText = previewText;
    }

    @Nullable
    public String getPreviewScore() {
        return previewScore;
    }

    public void setPreviewScore(@Nullable String previewScore) {
        this.previewScore = previewScore;
    }

    @Nullable
    public Spanned getPreviewTitle() {
        return previewTitle;
    }

    public void setPreviewTitle(@Nullable Spanned previewTitle) {
        this.previewTitle = previewTitle;
    }
}
