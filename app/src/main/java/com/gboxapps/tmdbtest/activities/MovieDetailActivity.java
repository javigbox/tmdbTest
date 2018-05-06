package com.gboxapps.tmdbtest.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.gboxapps.tmdbtest.R;
import com.gboxapps.tmdbtest.model.Movie;
import com.gboxapps.tmdbtest.util.Constants;
import com.gboxapps.tmdbtest.util.Util;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MovieDetailActivity extends AppCompatActivity {

    //UI Elements
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.poster)
    ImageView mImagePoster;

    @BindView(R.id.overview)
    TextView overView;

    @BindView(R.id.year)
    TextView year;

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    //Movie elements
    private Movie movie;
    private String posterTransitionName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);

        //getting intent extras
        movie = getIntent().getParcelableExtra("movie");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            posterTransitionName = getIntent().getStringExtra("poster_transition_name");
            mImagePoster.setTransitionName(posterTransitionName);
        }

        initToolbar();
        setupWidgets();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        //Necessary to change typeface of Activity (lib. calligraphy)
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /***
     * Instantiates toolbar components
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //TODO: 5.1 Important properties to show in the search results are the movie title (title)...
        collapsingToolbarLayout.setTitle(movie.getTitle());

        final Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/SourceSansPro-Bold.ttf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsingToolbarTextStyle);
        collapsingToolbarLayout.setExpandedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleMarginBottom(70);
    }

    /**
     * Instantiates UI components
     */
    public void setupWidgets(){

        //TODO: 5.2  ...the year of release ( year only ), the overview ( overview ) without truncation and a picture.
        Picasso.with(this)
                .load(Constants.BASE_URL_IMAGES + movie.getPoster_path())
                .noFade()
                .into(mImagePoster, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        supportStartPostponedEnterTransition();
                    }
                });

        overView.setText(movie.getOverview());
        year.setText(Util.getYearFromDate(movie.getRelease_date()));

    }
}
