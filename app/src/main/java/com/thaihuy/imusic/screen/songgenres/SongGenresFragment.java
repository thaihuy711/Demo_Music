package com.thaihuy.imusic.screen.songgenres;

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
import com.thaihuy.imusic.data.model.Song;
import com.thaihuy.imusic.data.repository.SongRepository;
import com.thaihuy.imusic.data.source.local.SongLocalDataSource;
import com.thaihuy.imusic.data.source.remote.SongRemoteDataSource;
import com.thaihuy.imusic.screen.BaseFragment;
import com.thaihuy.imusic.screen.player.MusicFragment;
import com.thaihuy.imusic.utils.ToastUtils;
import java.util.List;

public class SongGenresFragment extends BaseFragment
        implements SongGenresContract.View, SongGenresAdapter.ItemClickListener {

    private SongGenresPresenter mPresenter;
    private RecyclerView mRecyclerView;
    private List<Song> mSongs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_song, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerViewMySong);
        String genre = getArguments().getString("genre");
        SongRepository songRepository =
                SongRepository.getInstance(SongRemoteDataSource.getInstance(),
                        SongLocalDataSource.getInstance());
        mPresenter = new SongGenresPresenter(songRepository);
        mPresenter.setView(this);
        mPresenter.getSongsByGenre(genre);
        return view;
    }

    @Override
    public void showGenres(List<Song> songs) {
        mSongs = songs;
        SongGenresAdapter adapter = new SongGenresAdapter(getContext(), this);
        adapter.addData(songs);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showError(String message) {
        ToastUtils.quickToast(getActivity(), message, Toast.LENGTH_SHORT);
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
        super.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
        super.onStop();
    }

    @Override
    public void onItemClicked(int position) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down);
        fragmentTransaction.replace(R.id.main, MusicFragment.newInstance(mSongs, position,false),
                MusicFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
