package torille.fi.lurkforreddit.subreddit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import torille.fi.lurkforreddit.data.Post;
import torille.fi.lurkforreddit.data.PostDetails;
import torille.fi.lurkforreddit.data.RedditRepository;
import torille.fi.lurkforreddit.data.Subreddit;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of {@link SubredditPresenter}
 */

public class SubredditPresenterTest {
    private static String AFTER = "t3_5tkppe";

    private static Subreddit SUBREDDIT_WORLDNEWS = new Subreddit("2qh13", "World News", "/r/worldnews", "t5_2qh13", "", "worldnews", "", false, 15325795, true, (long) 1201231119, null, null);

    private static PostDetails POSTDETAILS_1_WORLDNEWS = new PostDetails(
            "worldnews",
            "",
            null,
            0,
            "askanier",
            "t3_5tich5",
            11970,
            "",
            "t5_2qh13",
            "http://www.bbc.com/news/world-asia-38947451?ns_mchannel=social&amp;ns_campaign=bbc_breaking&amp;ns_source=twitter&amp;ns_linkname=news_central",
            "Awesome title",
            null,
            "bbc.com",
            "5tich5",
            false,
            (long) 1486885305,
            (long) 1486856505,
            false,
            null,
            "/r/worldnews/comments/5tich5/north_korea_test_fires_ballistic_missile/",
            1874,
            null,
            "",
            "",
            "");

    private static PostDetails POSTDETAILS_2_WORLDNEWS = new PostDetails(
            "worldnews",
            "",
            null,
            0,
            "vich523",
            "t3_5tkad1",
            34,
            "",
            "t5_2qh13",
            "http://www.forbes.com/sites/panosmourdoukoutas/2017/02/11/china-tells-india-to-stay-off-its-indian-ocean-colony-sri-lanka/#7e8d62105f3a",
            "China Tells India To Stay Off Its Indian Ocean 'Colony,' Sri Lanka",
            null,
            "forbes.com",
            "5tkad1",
            false,
            (long) 1486913738,
            (long) 1486884938,
            false,
            null,
            "/r/worldnews/comments/5tkad1/china_tells_india_to_stay_off_its_indian_ocean/",
            25,
            null,
            null,
            null,
            null);

    private static Post POST_1_WORLDNEWS = new Post("t3", POSTDETAILS_1_WORLDNEWS);
    private static Post POST_2_WORLDNEWS = new Post("t3", POSTDETAILS_2_WORLDNEWS);
    private static List<Post> POSTS = Arrays.asList(POST_1_WORLDNEWS, POST_2_WORLDNEWS);

    @Mock
    private RedditRepository mRedditRepository;

    @Mock
    private SubredditContract.View mSubredditView;

    @Captor
    private ArgumentCaptor<RedditRepository.LoadSubredditPostsCallback> mLoadPostsCallbackArgumentCaptor;

    private SubredditPresenter mSubredditPresenter;

    @Before
    public void setupSubredditPresenter() {

        MockitoAnnotations.initMocks(this);

        mSubredditPresenter = new SubredditPresenter(mRedditRepository, mSubredditView);

    }

    @Test
    public void loadPostsFromRepositoryAndLoadIntoView() {
        mSubredditPresenter.loadPosts(SUBREDDIT_WORLDNEWS.getUrl());

        verify(mRedditRepository).getSubredditPosts(any(String.class), mLoadPostsCallbackArgumentCaptor.capture());
        mLoadPostsCallbackArgumentCaptor.getValue().onPostsLoaded(POSTS, AFTER);

        verify(mSubredditView).setProgressIndicator(false);
        verify(mSubredditView).showPosts(POSTS, AFTER);
    }

    @Test
    public void loadMorePostsFromRepositoryAndLoadIntoView() {
        mSubredditPresenter.loadMorePosts(SUBREDDIT_WORLDNEWS.getUrl(), AFTER);

        verify(mRedditRepository).getMoreSubredditPosts(any(String.class), any(String.class), mLoadPostsCallbackArgumentCaptor.capture());
        mLoadPostsCallbackArgumentCaptor.getValue().onPostsLoaded(POSTS, AFTER);

        verify(mSubredditView).setListProgressIndicator(false);
        verify(mSubredditView).addMorePosts(POSTS, AFTER);
    }

    @Test
    public void clickOnBrowser_ShowCustomTabsUi() {
        mSubredditPresenter.openCustomTabs(POSTDETAILS_1_WORLDNEWS.getUrl());
        verify(mSubredditView).showCustomTabsUI(any(String.class));
    }

    @Test
    public void clickOnComments_ShowCommentsUi() {
        mSubredditPresenter.openComments(POST_1_WORLDNEWS);
        verify(mSubredditView).showCommentsUI(any(Post.class));
    }
}