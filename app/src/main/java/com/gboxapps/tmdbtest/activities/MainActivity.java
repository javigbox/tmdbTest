package com.gboxapps.tmdbtest.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.gboxapps.tmdbtest.R;
import com.gboxapps.tmdbtest.adapters.MovieAdapter;
import com.gboxapps.tmdbtest.model.Movie;
import com.gboxapps.tmdbtest.parsers.MoviesParser;
import com.gboxapps.tmdbtest.services.ApiClient;
import com.gboxapps.tmdbtest.services.MovieApiInterface;
import com.gboxapps.tmdbtest.util.AppBarStateChangeListener;
import com.gboxapps.tmdbtest.util.Constants;
import com.gboxapps.tmdbtest.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    private SearchView searchView;
    private boolean isToolbarExpanded = true;

    private MovieApiInterface movieApiInterface;

    private List<Movie> movies = new ArrayList<>();
    private List<Movie> moviesSearch = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private MovieAdapter movieAdapter;

    private int currentPage = 1;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private int pastVisiblesItems = 0;
    private boolean isLoading = false;
    private boolean isSearchActive = false;
    private boolean isNewSearch = false;
    private String query = "";

    private MenuItem searchItem;

    private Callback<Response> callbackList = new Callback<Response>() {
        @Override
        public void success(Response response, Response response2) {
            String json = Util.getString(response.getBody());

            List<Movie> resultList = MoviesParser.parseMovies(json);
            movies.addAll(resultList);
            movieAdapter = new MovieAdapter(movies, MainActivity.this, new MovieAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, Movie item, View view, ImageView imageView) {
                    Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                    intent.putExtra("movie", item);
                    intent.putExtra("poster_transition_name", ViewCompat.getTransitionName(imageView));

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            MainActivity.this,
                            imageView,
                            ViewCompat.getTransitionName(imageView));

                    startActivity(intent, options.toBundle());
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

    private Callback<Response> callbackSearch = new Callback<Response>() {
        @Override
        public void success(Response response, Response response2) {
            String json = Util.getString(response.getBody());

            List<Movie> resultList = MoviesParser.parseMovies(json);
            moviesSearch.addAll(resultList);
            if (isNewSearch) {

                movieAdapter = new MovieAdapter(resultList, MainActivity.this, new MovieAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, Movie item, View view, ImageView poster) {
                        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                        intent.putExtra("movie", item);
                        intent.putExtra("poster_transition_name", ViewCompat.getTransitionName(poster));

                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                MainActivity.this,
                                poster,
                                ViewCompat.getTransitionName(poster));

                        startActivity(intent, options.toBundle());
                    }
                });

            } else {
                movieAdapter = new MovieAdapter(moviesSearch, MainActivity.this, new MovieAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, Movie item, View view, ImageView poster) {
                        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                        intent.putExtra("movie", item);
                        intent.putExtra("poster_transition_name", ViewCompat.getTransitionName(poster));

                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                MainActivity.this,
                                poster,
                                ViewCompat.getTransitionName(poster));

                        startActivity(intent, options.toBundle());
                    }
                });
            }
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

        movieApiInterface = ApiClient.getMovieServiceInterface(this);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        initToolbar();
        setupWidgets();
        getMovieList();
    }

    @Override
    public void onBackPressed() {
        if (!isToolbarExpanded) {
            appBarLayout.setExpanded(true);
            nestedScroll.scrollTo(0, 0);
        } else
            super.onBackPressed();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        searchItem = menu.findItem(R.id.action_search);
        searchItem.setEnabled(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:

                SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

                searchView = null;
                if (item != null) {
                    searchView = (SearchView) item.getActionView();
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            currentPage = 1;
                            query = newText;
                            movies.clear();
                            moviesSearch.clear();
                            if (query.equals(""))
                                movieApiInterface.getPopularMovies(Constants.API_KEY, String.valueOf(currentPage),
                                        Util.getIsoCode(MainActivity.this), Util.getIsoCode(MainActivity.this), callbackList);
                            else {
                                isNewSearch = true;
                                movieApiInterface.searchMovies(Constants.API_KEY, String.valueOf(currentPage), query, Util.getIsoCode(MainActivity.this), callbackSearch);
                            }
                            return false;
                        }
                    });
                    MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionExpand(MenuItem item) {
                            isSearchActive = true;
                            appBarLayout.setExpanded(true);
                            nestedScroll.scrollTo(0, 0);
                            return true;
                        }

                        @Override
                        public boolean onMenuItemActionCollapse(MenuItem item) {
                            isSearchActive = false;
                            currentPage = 1;
                            appBarLayout.setExpanded(true);
                            nestedScroll.scrollTo(0, 0);
                            return true;
                        }
                    });

                }
                if (searchView != null) {
                    searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                    isToolbarExpanded = true;
                    fab.hide();
                } else {
                    isToolbarExpanded = false;
                    fab.show();
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
                                if (isSearchActive) {
                                    isNewSearch = false;
                                    movieApiInterface.searchMovies(Constants.API_KEY, String.valueOf(currentPage), query, Util.getIsoCode(MainActivity.this), callbackSearch);
                                } else
                                    movieApiInterface.getPopularMovies(Constants.API_KEY, String.valueOf(currentPage),
                                            Util.getIsoCode(MainActivity.this), Util.getIsoCode(MainActivity.this), callbackList);

                            }
                        }
                    }
                }
            }
        });
    }

    public void getMovieList() {
        isLoading = true;
        movieApiInterface.getPopularMovies(Constants.API_KEY, String.valueOf(currentPage),
                Util.getIsoCode(MainActivity.this), Util.getIsoCode(MainActivity.this), callbackList);
    }

    @OnClick({R.id.fab, R.id.fab_bar})
    public void search() {
        searchItem.setEnabled(true);
        ((ActionMenuItemView) findViewById(R.id.action_search)).callOnClick();
        searchItem.setEnabled(false);
    }

}
