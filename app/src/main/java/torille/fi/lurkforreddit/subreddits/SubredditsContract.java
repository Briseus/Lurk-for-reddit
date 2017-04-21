package torille.fi.lurkforreddit.subreddits;

import java.util.List;

import torille.fi.lurkforreddit.data.models.view.Subreddit;

/**
 * Created by eva on 2/8/17.
 */

public interface SubredditsContract {

    interface View {

        void setProgressIndicator(boolean active);

        void showSubreddits(List<Subreddit> subreddits);

        void loadSelectedSubreddit(Subreddit subreddit);

        void onError(String errorText);
    }

    interface UserActionsListener {

        void loadSubreddits(boolean b);

        void openSubreddit(Subreddit subreddit);

    }

}
