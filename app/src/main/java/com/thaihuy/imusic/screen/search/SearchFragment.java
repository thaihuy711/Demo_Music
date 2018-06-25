package com.thaihuy.imusic.screen.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.thaihuy.imusic.R;
import com.thaihuy.imusic.data.model.MoreData;
import com.thaihuy.imusic.data.model.Song;
import com.thaihuy.imusic.data.repository.SongRepository;
import com.thaihuy.imusic.data.source.remote.SongRemoteDataSource;
import com.thaihuy.imusic.screen.BaseFragment;
import com.thaihuy.imusic.screen.EndlessRecyclerViewScrollListener;
import com.thaihuy.imusic.screen.player.MusicFragment;
import com.thaihuy.imusic.utils.Constant;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends BaseFragment
        implements SearchView.OnQueryTextListener, SearchContract.View,
        SongAdapter.ItemClickListener, SearchView.OnCloseListener {

    private RecyclerView mRecyclerViewSearch;
    private Toolbar mToolbar;
    private SearchContract.Presenter mPresenter;
    private MoreData mMoreData;
    private SongAdapter mSongAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Song> mSongs = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initViews(view);
        setHasOptionsMenu(true);
        SongRepository songRepository =
                SongRepository.getInstance(SongRemoteDataSource.getInstance(), null);
        mPresenter = new SearchPresenter(songRepository);
        mPresenter.setView(this);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onClose() {
        mSongAdapter.clearData();
        mMoreData = null;
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (mSongAdapter.checkData()) {
            mSongAdapter.clearData();
        }
        mPresenter.searchSong(Constant.SEARCH_URL + query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void showSong(MoreData moreData) {

        mMoreData = new MoreData();
        mMoreData.setSongList(moreData.getSongList());
        mMoreData.setNextHref(moreData.getNextHref());
        mSongAdapter.addData(moreData.getSongList());
        mRecyclerViewSearch.addOnScrollListener(
                new EndlessRecyclerViewScrollListener((LinearLayoutManager) mLayoutManager) {
                    @Override
                    public void onLoadMore() {
                        if (mMoreData != null) {
                            loadNextDataFromApi(mMoreData.getNextHref());
                        }
                    }
                });
    }

    private void loadNextDataFromApi(String nextHref) {
        mPresenter.loadMoreDataSongList(nextHref);
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void updateSongList(MoreData moreData) {
        mMoreData.getSongList().addAll(moreData.getSongList());
        mMoreData.setNextHref(moreData.getNextHref());
        mSongAdapter.addData(moreData.getSongList());
    }

    @Override
    public void onItemClicked(int position) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down);
        fragmentTransaction.replace(R.id.main, MusicFragment.newInstance(mSongs, position, false),
                MusicFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void initViews(View view) {
        mRecyclerViewSearch = view.findViewById(R.id.recyclerView_search);
        mToolbar = view.findViewById(R.id.tool_bar);
        mToolbar.setTitle(getContext().getResources().getString(R.string.search));
        mToolbar.setTitleTextColor(getContext().getResources().getColor(R.color.colorGray));
        mToolbar.setTitleMarginStart((int) getResources().getDimension(R.dimen.dp_80));
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(mToolbar);
        mSongAdapter = new SongAdapter(getContext(), this);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewSearch.setLayoutManager(mLayoutManager);
        mRecyclerViewSearch.setAdapter(mSongAdapter);
        mMoreData = new MoreData();
    }
}
