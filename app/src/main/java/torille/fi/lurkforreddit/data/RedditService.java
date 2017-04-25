package torille.fi.lurkforreddit.data;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import torille.fi.lurkforreddit.data.models.jsonResponses.PostListing;
import torille.fi.lurkforreddit.data.models.jsonResponses.RedditToken;
import torille.fi.lurkforreddit.data.models.jsonResponses.SubredditChildren;
import torille.fi.lurkforreddit.data.models.jsonResponses.SubredditListing;

/**
 * Created by eva on 22.4.2017.
 */

public interface RedditService {

    interface Auth {
        @FormUrlEncoded
        @POST("access_token")
        Call<RedditToken> getAuthToken(@Field("grant_type") String grant_type, @Field("device_id") String UUID);

        @FormUrlEncoded
        @POST("access_token")
        Call<RedditToken> getUserAuthToken(@Field("grant_type") String grant_type, @Field("code") String code, @Field("redirect_uri") String redirectUri);

        @FormUrlEncoded
        @POST("access_token")
        Call<RedditToken> refreshUserToken(@Field("grant_type") String grant_type, @Field("refresh_token") String token);

    }

    interface Reddit {
        @GET("subreddits/mine/subscriber")
        Call<SubredditListing> getMySubreddits(@Query("limit") int count);

        @GET("subreddits/default")
        Call<SubredditListing> getDefaultSubreddits(@Query("limit") int count);

        @GET("{subreddit}")
        Call<PostListing> getSubreddit(@Path(value = "subreddit", encoded = true) String subredditUrl);

        @GET("{subreddit}")
        Call<PostListing> getSubredditNextPage(@Path(value = "subreddit", encoded = true) String subredditUrl, @Query("after") String afterPage);

        @GET("{comments}")
        Call<ResponseBody> getComments(@Path(value = "comments", encoded = true) String commentUrl);

        @GET("api/morechildren")
        Call<ResponseBody> getMoreComments(@Query(value = "link_id") String parentId, @Query(value = "children") String childId, @Query(value = "api_type") String json);

        @GET("subreddits/search")
        Call<SubredditListing> searchSubreddits(@Query(value = "q") String searchQuery, @Query(value = "sort") String sortBy);

        @GET("subreddits/search")
        Call<SubredditListing> searchSubredditsNextPage(@Query(value = "q") String searchQuery, @Query(value = "sort") String sortBy, @Query(value = "after") String after);

        @GET("{subreddit}/about")
        Call<SubredditChildren> getSubredditInfo(@Path(value = "subreddit", encoded = true) String subredditName);

    }
}