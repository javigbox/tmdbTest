package com.gboxapps.tmdbtest.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gboxapps.tmdbtest.R;
import com.gboxapps.tmdbtest.util.AppBarStateChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar();
        setupWidgets();
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

    public void setupWidgets(){
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
    }

}
