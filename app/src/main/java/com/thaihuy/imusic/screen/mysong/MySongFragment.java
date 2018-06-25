package com.thaihuy.imusic.screen.mysong;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.thaihuy.imusic.R;
import com.thaihuy.imusic.data.model.MoreData;
import com.thaihuy.imusic.data.model.Song;
import com.thaihuy.imusic.data.repository.SongRepository;
import com.thaihuy.imusic.data.source.local.SongLocalDataSource;
import com.thaihuy.imusic.screen.BaseFragment;
import com.thaihuy.imusic.screen.player.MusicFragment;
import com.thaihuy.imusic.screen.songgenres.SongGenresAdapter;
import com.thaihuy.imusic.utils.Constant;
import com.thaihuy.imusic.utils.PermissionUtils;
import java.util.ArrayList;
import java.util.List;

public class MySongFragment extends BaseFragment
        implements MySongContract.View, SongGenresAdapter.ItemClickListener {

    private List<Song> mSongs;
    private RecyclerView mRecyclerView;
    private SongGenresAdapter mSongGenresAdapter;
    private MySongContract.Presenter mPresenter;
    private View mView;
    private MoreData mMoreData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_song, container, false);
        mSongs = new ArrayList<>();
        mRecyclerView = mView.findViewById(R.id.recyclerViewMySong);
        SongRepository songRepository =
                SongRepository.getInstance(null, SongLocalDataSource.getInstance());
        mPresenter = new MySongPresenter(songRepository);
        mPresenter.setView(this);
        mPresenter.getSongFromLocal(getContext());
        return mView;
    }

    @Override
    public void showSongsFromLocal(MoreData moreData) {
        mMoreData = moreData;
        mSongGenresAdapter = new SongGenresAdapter(getContext(), this);
        mSongGenresAdapter.addData(mMoreData.getSongList());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mSongGenresAdapter);
    }

    @Override
    public void onItemClicked(int position) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down);
        MusicFragment playMusicFragment = (MusicFragment) fragmentManager.findFragmentByTag(
                MusicFragment.class.getSimpleName());
        if (playMusicFragment == null) {
            fragmentTransaction.replace(R.id.main, MusicFragment.newInstance(mSongs, position,true),
                    Constant.TAG_PLAY_FRAGMENT);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            Toast.makeText(getContext(), "Loi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showNoTracks() {

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed() && PermissionUtils.requestPermission(getActivity())) {
            mPresenter.getSongFromLocal(getContext());
        }
    }
}
