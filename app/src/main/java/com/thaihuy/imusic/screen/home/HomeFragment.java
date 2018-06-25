package com.thaihuy.imusic.screen.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.thaihuy.imusic.R;
import com.thaihuy.imusic.screen.BaseFragment;
import com.thaihuy.imusic.screen.songgenres.SongGenresFragment;
import com.thaihuy.imusic.utils.Constant;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private View mView;
    private FragmentManager mFragmentManager;
    private SongGenresFragment mSongGenresFragment;
    private Bundle mBundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(mView);
        return mView;
    }

    private void initViews(View view) {
        int[] idImageView = {
                R.id.image_song_alternativerock, R.id.image_song_ambient, R.id.image_song_classical,
                R.id.image_song_country
        };
        for (int id : idImageView) {
            View v = view.findViewById(id);
            v.setOnClickListener(this);
        }

        mFragmentManager = getActivity().getSupportFragmentManager();
        mSongGenresFragment = new SongGenresFragment();
        mBundle = new Bundle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_song_alternativerock:
                FragmentTransaction alternativerockTransaction =
                        getChildFragmentManager().beginTransaction();
                mBundle.putString("genre", Constant.Genres.ALTERNATIVEROCK);
                mSongGenresFragment.setArguments(mBundle);
                alternativerockTransaction.add(R.id.content_home, mSongGenresFragment);
                alternativerockTransaction.addToBackStack(null);
                alternativerockTransaction.commit();
                break;
            case R.id.image_song_country:
                FragmentTransaction countryTransaction =
                        getChildFragmentManager().beginTransaction();
                mBundle.putString("genre", Constant.Genres.COUNTRY);
                mSongGenresFragment.setArguments(mBundle);
                countryTransaction.add(R.id.content_home, mSongGenresFragment);
                countryTransaction.addToBackStack(null);
                countryTransaction.commit();
                break;
            case R.id.image_song_ambient:
                FragmentTransaction ambientTransaction =
                        getChildFragmentManager().beginTransaction();
                mBundle.putString("genre", Constant.Genres.AMBIENT);
                mSongGenresFragment.setArguments(mBundle);
                ambientTransaction.add(R.id.content_home, mSongGenresFragment);
                ambientTransaction.addToBackStack(null);
                ambientTransaction.commit();
                break;
            case R.id.image_song_classical:
                FragmentTransaction classicalTransaction =
                        getChildFragmentManager().beginTransaction();
                mBundle.putString("genre", Constant.Genres.CLASSICAL);
                mSongGenresFragment.setArguments(mBundle);
                classicalTransaction.add(R.id.content_home, mSongGenresFragment);
                classicalTransaction.addToBackStack(null);
                classicalTransaction.commit();
                break;
        }
    }
}
