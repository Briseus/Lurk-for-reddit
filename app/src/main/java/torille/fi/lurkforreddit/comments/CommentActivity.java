package torille.fi.lurkforreddit.comments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.parceler.Parcels;

import torille.fi.lurkforreddit.R;
import torille.fi.lurkforreddit.data.Post;

/**
 * Created by eva on 2/13/17.
 */

public class CommentActivity extends AppCompatActivity {

    public static final String EXTRA_CLICKED_POST = "post";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comments);

        Post originalPost = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_CLICKED_POST));

        Toolbar toolbar = (Toolbar) findViewById(R.id.appBarLayout);
        toolbar.setTitle(originalPost.getPostDetails().getSubreddit());

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initFragment(CommentFragment.newInstance(originalPost));
    }

    private void initFragment(Fragment commentFragment) {
        // Add the NotesDetailFragment to the layout
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, commentFragment);
        transaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}