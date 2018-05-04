package com.gboxapps.tmdbtest.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.gboxapps.tmdbtest.R;
import com.gboxapps.tmdbtest.adapters.MovieAdapter;
import com.gboxapps.tmdbtest.model.Movie;
import com.gboxapps.tmdbtest.parsers.MoviesParser;
import com.gboxapps.tmdbtest.services.ApiClient;
import com.gboxapps.tmdbtest.services.MovieApiInterfaceV4;
import com.gboxapps.tmdbtest.util.AppBarStateChangeListener;
import com.gboxapps.tmdbtest.util.Constants;
import com.gboxapps.tmdbtest.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScroll;

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private MovieApiInterfaceV4 movieApiInterfaceV4;

    private List<Movie> movies;
    private LinearLayoutManager mLayoutManager;
    private MovieAdapter movieAdapter;

    private int currentPage = 1;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private int pastVisiblesItems = 0;
    private boolean isLoading = false;

    private Callback<Response> callbackList = new Callback<Response>() {
        @Override
        public void success(Response response, Response response2) {
            String json = Util.getString(response.getBody());

            movies = MoviesParser.parseMovies(json);
            movieAdapter = new MovieAdapter(movies, MainActivity.this, new MovieAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Movie item, View view) {

                }
            });
            recyclerView.setAdapter(movieAdapter);
            movieAdapter.notifyDataSetChanged();

            isLoading = false;
        }

        @Override
        public void failure(RetrofitError error) {
            String errord = error.getMessage();
            isLoading = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        movieApiInterfaceV4 = ApiClient.getMovieV4ServiceInterface(this);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        initToolbar();
        setupWidgets();
        getMovieList();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));

        final Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/SourceSansPro-Bold.ttf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsingToolbarTextStyle);
        collapsingToolbarLayout.setExpandedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleMarginBottom(70);
    }

    public void setupWidgets() {
        recyclerView.setNestedScrollingEnabled(false);

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state.name().equals("EXPANDED") || state.name().equals("IDLE")) {
                    fab.hide();
//                    toolbarTitle.setText("");
//                    toolbarTitle.setVisibility(View.GONE);
                } else {
                    fab.show();
//                    toolbarTitle.setText(getResources().getString(R.string.main_booking));
//                    toolbarTitle.setVisibility(View.VISIBLE);
//                    toolbarTitle.startAnimation(fadeIn);
                }
            }
        });

        nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {

                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();
                        pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                        if (!isLoading) {

                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                                isLoading = true;
                                currentPage++;
                                movieApiInterfaceV4.getListMovies(Constants.API_KEY, String.valueOf(currentPage), Util.getIsoCode(MainActivity.this), callbackList);

                            }
                        }
                    }
                }
            }
        });
    }

    public void getMovieList() {
        isLoading = true;
        movieApiInterfaceV4.getListMovies(Constants.API_KEY, String.valueOf(currentPage), Util.getIsoCode(this), callbackList);
    }

}
