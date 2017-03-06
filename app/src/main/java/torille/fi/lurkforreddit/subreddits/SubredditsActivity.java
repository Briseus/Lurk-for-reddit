package torille.fi.lurkforreddit.subreddits;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import torille.fi.lurkforreddit.R;
import torille.fi.lurkforreddit.customTabs.CustomTabActivityHelper;
import torille.fi.lurkforreddit.data.Subreddit;
import torille.fi.lurkforreddit.retrofit.RedditAuthService;
import torille.fi.lurkforreddit.retrofit.RedditClient;
import torille.fi.lurkforreddit.retrofit.RedditToken;
import torille.fi.lurkforreddit.subreddit.SubredditFragment;
import torille.fi.lurkforreddit.utils.MediaHelper;
import torille.fi.lurkforreddit.utils.NetworkHelper;
import torille.fi.lurkforreddit.utils.SharedPreferencesHelper;

/**
 * Created by eva on 2/8/17.
 */

public class SubredditsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String STATE = NetworkHelper.nextStateId();
    private static final String REDIRECT_URI = "lurk://redirecturi";
    private String CLIENT_ID;
    private static final String RESPONSE_TYPE = "code";
    private static final String DURATION = "permanent";
    private static final String SCOPE = "identity,mysubreddits,read,account";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.appBarLayout);
        setSupportActionBar(toolbar);

        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (null == savedInstanceState) {
            initFrontpage();
        }
    }

    private void initFragment(Fragment subredditsFragment) {
        // Add the NotesFragment to the layout
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, subredditsFragment);
        transaction.commit();
    }

    private void initFrontpage() {
        Subreddit frontpage = new Subreddit();
        frontpage.setUrl("");
        initFragment(SubredditFragment.newInstance(frontpage));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        checkIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkIntent(getIntent());
    }

    private void checkIntent(@Nullable Intent intent) {
        if (intent != null) {
            Uri uri = getIntent().getData();
            Log.d("Login", "Uri is " + uri);
            if (uri != null) {
                String state = uri.getQueryParameter("state");
                if (state.equals(STATE)) {
                    String code = uri.getQueryParameter("code");
                    if (code != null) {
                        Log.d("Login", "Code was " + code);
                        getToken(code);
                    } else if (uri.getQueryParameter("error") != null){
                        // show an error message here
                        Log.e("Login", "Got error " + uri.getQueryParameter("error"));
                        Toast.makeText(this, "Got error " + uri.getQueryParameter("error"), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e("Login", state + " does not match " + STATE);
                    Toast.makeText(this,  state + " does not match " + STATE, Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    private void getToken(String code) {
        final String grant_type = "authorization_code";
        CLIENT_ID = getResources().getString(R.string.client_id);
        RedditClient redditClient = RedditAuthService.createService(RedditClient.class, CLIENT_ID, "");
        Call<RedditToken> call = redditClient.getUserAuthToken(grant_type, code, REDIRECT_URI);
        call.enqueue(new Callback<RedditToken>() {
            @Override
            public void onResponse(Call<RedditToken> call, Response<RedditToken> response) {
                if (response.isSuccessful()) {
                    Log.d("Login", "Got " + response.body().toString());
                    SharedPreferencesHelper.setToken(response.body().getAccess_token());
                    SharedPreferencesHelper.setRefreshToken(response.body().getRefresh_token());
                    SharedPreferencesHelper.loggedIn(true);
                    Toast.makeText(SubredditsActivity.this, "Logged in successfully!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SubredditsActivity.this, "Failed to log in" + response.errorBody(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RedditToken> call, Throwable t) {
                Log.e("Error", t.toString());
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_frontpage:
                initFrontpage();
                return true;
            case R.id.action_subreddits:
                initFragment(SubredditsFragment.newInstance());
                return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_login:
                logIn();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logIn() {
        CLIENT_ID = getResources().getString(R.string.client_id);

        final String url = RedditAuthService.API_BASE_URL
                + "authorize.compact?client_id=" + CLIENT_ID
                + "&response_type=" + RESPONSE_TYPE
                + "&state=" + STATE
                + "&redirect_uri=" + REDIRECT_URI
                + "&duration=" + DURATION
                + "&scope=" + SCOPE;
        CustomTabActivityHelper helper = new CustomTabActivityHelper();

        CustomTabActivityHelper.openCustomTab(this,
                MediaHelper.createCustomTabIntent(this,
                        helper.getSession()),
                url,
                new CustomTabActivityHelper.CustomTabFallback() {
                    @Override
                    public void openUri(Activity activity, String uri) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                });
    }
}