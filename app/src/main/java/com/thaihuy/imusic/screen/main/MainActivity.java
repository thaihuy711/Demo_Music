package com.thaihuy.imusic.screen.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.thaihuy.imusic.R;
import com.thaihuy.imusic.screen.BaseActivity;
import com.thaihuy.imusic.screen.player.MusicFragment;

public class MainActivity extends BaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int DELAY_TIME = 2000;

    private BottomNavigationView mBottomNavigationView;
    private UnSwipeViewpager mViewPager;
    private ConstraintLayout mRelativeLayout;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().getExtras() != null) {
            String a = getIntent().getStringExtra("PlayMusic");
            if (a.equals("PlayMusic")) {
                showFragment();
            }
        }
        initView();
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mainAdapter);
        mViewPager.setPagingEnabled(false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showFragment();
    }

    private void initView() {
        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mViewPager = findViewById(R.id.view_pager);
        mRelativeLayout = findViewById(R.id.relative_item_player);

        mRelativeLayout.setOnClickListener(this);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                mViewPager.setCurrentItem(Tab.HOME);
                break;
            case R.id.action_my_song:
                mViewPager.setCurrentItem(Tab.MY_SONG);
                break;
            case R.id.action_search:
                mViewPager.setCurrentItem(Tab.SEARCH);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_item_player:
                showFragment();
                mBottomNavigationView.setVisibility(View.GONE);
                break;
        }
    }

    @IntDef({ Tab.HOME, Tab.MY_SONG, Tab.SEARCH })
    public @interface Tab {
        int HOME = 0;
        int MY_SONG = 1;
        int SEARCH = 2;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        MusicFragment fragment =
                (MusicFragment) fm.findFragmentByTag(MusicFragment.class.getSimpleName());
        if (fragment != null && !fragment.isHidden()) {
            mRelativeLayout.setVisibility(View.VISIBLE);
            mBottomNavigationView.setVisibility(View.VISIBLE);
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_out_down, R.anim.slide_out_up);
            fragmentTransaction.hide(fragment);
            fragmentTransaction.commit();
            return;
        } else {
            for (Fragment frag : fm.getFragments()) {
                if (frag.isVisible()) {
                    FragmentManager childFm = frag.getChildFragmentManager();
                    if (childFm.getBackStackEntryCount() > 0) {
                        childFm.popBackStack();
                        return;
                    }
                }
            }
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.Please_press_Back_again_to_exit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, DELAY_TIME);
    }

    public void SetNavigationVisibiltity(boolean b) {
        if (b) {
            mBottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            mBottomNavigationView.setVisibility(View.GONE);
        }
    }

    private void showFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down);
        MusicFragment playMusicFragment = (MusicFragment) fragmentManager.findFragmentByTag(
                MusicFragment.class.getSimpleName());
        if (playMusicFragment != null && playMusicFragment.isHidden()) {
            fragmentTransaction.show(playMusicFragment);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.add(R.id.main, new MusicFragment(),
                    MusicFragment.class.getSimpleName());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
