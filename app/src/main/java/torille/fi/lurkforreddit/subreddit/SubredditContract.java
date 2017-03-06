package torille.fi.lurkforreddit.subreddit;

import android.support.annotation.NonNull;

import java.util.List;

import torille.fi.lurkforreddit.data.Post;

/**
 * This specifies the contract between the view and the presenter.
 */

public interface SubredditContract {

    interface View {

        void setProgressIndicator(boolean active);

        void showPosts(List<Post> posts, String nextpage);

        void setListProgressIndicator(boolean active);

        void addMorePosts(List<Post> posts, String nextpage);

        void showCustomTabsUI(String url);

        void showMedia(Post post);

        void showCommentsUI(Post clickedPost);
    }

    interface UserActionsListener {

        void openComments(@NonNull Post clickedPost);

        void openCustomTabs(@NonNull String url);

        void openMedia(@NonNull Post post);

        void loadPosts(@NonNull String subredditUrl);

        void loadMorePosts(@NonNull String subredditUrl, @NonNull String nextpage);
    }
}