package com.thaihuy.imusic.screen.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thaihuy.imusic.R;
import com.thaihuy.imusic.data.model.MoreData;
import com.thaihuy.imusic.data.model.Song;
import com.thaihuy.imusic.data.repository.SongRepository;
import com.thaihuy.imusic.data.source.remote.SongRemoteDataSource;
import com.thaihuy.imusic.screen.BaseFragment;
import com.thaihuy.imusic.screen.main.MainActivity;
import com.thaihuy.imusic.screen.service.MusicService;
import com.thaihuy.imusic.utils.Constant;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;

public class MusicFragment extends BaseFragment
        implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MusicContract.View {

    private static final String ARGUMENT_COLLECTION = "ARGUMENT_COLLECTION";
    private static final int DELAY_MILISECONDS = 100;

    private View mView;
    private CircleImageView mCircleImageViewSong;
    private ImageView mImageBack, mImagePrevious, mImagePlay, mImageNext, mImageShuffle,
            mImageRepeat;
    private TextView mTextSongTittle, mTextArtistName, mTextSongProgress, mTextAllTime;
    private List<Song> mSongs;
    private int mSongPosition;
    private MusicService mMusicService;
    private boolean mIsBound;
    private MusicContract.Presenter mPresenter;
    private SeekBar mSeekBarProgress;
    private Utilities mUtilities;
    private Handler mHandler;
    private MusicAdapter mMusicAdapter;
    private CircleImageView mCircleImageItemSongParent;
    private TextView mTextSongParent, mTextArtistParent;
    private ImageView mImageNextParent, mImagePreviousParent, mImagePlayParent, mImageDowload;
    private MoreData mMoreData;
    private boolean isLocalSong;
    private String mSetup;

    public static MusicFragment newInstance(List<Song> songs, int position, boolean isLocalTrack) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARGUMENT_COLLECTION, (ArrayList<? extends Parcelable>) songs);
        args.putInt(Constant.POSITION, position);
        args.putBoolean(Constant.ISLOCALTRACK, isLocalTrack);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_play_song, container, false);
        initViews(mView);
        SongRepository songRepository =
                SongRepository.getInstance(SongRemoteDataSource.getInstance(), null);
        mPresenter = new MusicPresenter(songRepository);
        ((MainActivity) getActivity()).SetNavigationVisibiltity(false);
        Bundle args = getArguments();
        if (args != null) {
            mSongs = args.getParcelableArrayList(ARGUMENT_COLLECTION);
            mSongPosition = args.getInt(Constant.POSITION);
            isLocalSong = args.getBoolean(Constant.ISLOCALTRACK, false);
            Intent intent = MusicService.getSongsIntent(getActivity(), mSongs, mSongPosition);
            getActivity().startService(intent);
            getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        } else {
            Intent intent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        //        getActivity().unbindService(mServiceConnection);
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                getActivity().onBackPressed();
                break;
            case R.id.image_next:
                mMusicService.nextMusic();
                setUpViews();
                break;
            case R.id.image_previous:
                mMusicService.previousMusic();
                setUpViews();
                break;
            case R.id.image_button_next_item:
                mMusicService.nextMusic();
                setUpViews();
                break;
            case R.id.image_button_previous_item:
                mMusicService.previousMusic();
                setUpViews();
                break;
            case R.id.image_download:
                startDownload();
                break;
            case R.id.image_play:
                if (mMusicService.isPlaying()) {
                    mMusicService.pauseMusic();
                    mImagePlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                    mImagePlayParent.setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_play));
                } else {
                    mMusicService.playMusic();
                    mImagePlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                    mImagePlayParent.setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_pause));
                }
                break;
            case R.id.image_button_play_item:
                if (mMusicService.isPlaying()) {
                    mMusicService.pauseMusic();
                    mImagePlayParent.setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_play));
                    mImagePlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                } else {
                    mMusicService.playMusic();
                    mImagePlayParent.setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_pause));
                    mImagePlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                }
                break;
            //            case R.id.image_repeat:
            //                mMusicService.setupMusic();
            //                setupMusic(mMusicService.getSetup());
            //                break;
        }
    }

    private void setupMusic(String setup) {
        switch (setup) {
            case Constant.REPEAT:
                mImageRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat));
                break;
            case Constant.REPEAT_ONE:
                mImageRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_one));

                break;
            case Constant.NON_REPEAT:
                mImageRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_non_repeat));
                break;
        }
        mSetup = setup;
    }

    public void startDownload() {
        mPresenter.downloadSong(getContext(),
                mSongs.get(mSongPosition).getStreamUrl() + Constant.CLIENT_ID,
                mSongs.get(mSongPosition).getTitle());
        Toast.makeText(getContext(), R.string.starting_download, Toast.LENGTH_SHORT).show();
    }

    private void initViews(View view) {
        mTextSongTittle = view.findViewById(R.id.text_song_name);
        mTextArtistName = view.findViewById(R.id.text_artist_name);
        mImageBack = view.findViewById(R.id.image_back);
        mImagePrevious = view.findViewById(R.id.image_previous);
        mImagePlay = view.findViewById(R.id.image_play);
        mImageNext = view.findViewById(R.id.image_next);
        mImageShuffle = view.findViewById(R.id.image_shuffle);
        mImageRepeat = view.findViewById(R.id.image_repeat);
        mCircleImageViewSong = view.findViewById(R.id.image_artist);
        mTextSongProgress = view.findViewById(R.id.text_song_progress);
        mTextAllTime = view.findViewById(R.id.text_all_time);
        mSeekBarProgress = view.findViewById(R.id.seek_progress);
        mImageDowload = view.findViewById(R.id.image_download);

        mCircleImageItemSongParent = getActivity().findViewById(R.id.image_artist_item);
        mTextSongParent = getActivity().findViewById(R.id.text_song_name_item);
        mTextArtistParent = getActivity().findViewById(R.id.text_artist_name_item);
        mImagePreviousParent = getActivity().findViewById(R.id.image_button_previous_item);
        mImageNextParent = getActivity().findViewById(R.id.image_button_next_item);
        mImagePlayParent = getActivity().findViewById(R.id.image_button_play_item);
        mImageDowload.setOnClickListener(this);

        mImageBack.setOnClickListener(this);
        mImageShuffle.setOnClickListener(this);
        mImagePrevious.setOnClickListener(this);
        mImagePlay.setOnClickListener(this);
        mImageNext.setOnClickListener(this);
        mImageRepeat.setOnClickListener(this);
        mImageNextParent.setOnClickListener(this);
        mImagePreviousParent.setOnClickListener(this);
        mImagePlayParent.setOnClickListener(this);
        mSeekBarProgress.setOnSeekBarChangeListener(this);

        mUtilities = new Utilities();
        mHandler = new Handler();
    }

    private void setUpViews() {
        mTextSongTittle.setText(mMusicService.getSongName());
        mTextArtistName.setText(mMusicService.getArtistName());
        mTextSongParent.setText(mMusicService.getSongName());
        mTextArtistParent.setText(mMusicService.getArtistName());
        Glide.with(getActivity())
                .load(mMusicService.getArtwork())
                .apply(new RequestOptions().placeholder(R.drawable.st))
                .into(mCircleImageViewSong);
        Glide.with(getActivity())
                .load(mMusicService.getArtwork())
                .apply(new RequestOptions().placeholder(R.drawable.st))
                .into(mCircleImageItemSongParent);
        updateProgressBar();
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, DELAY_MILISECONDS);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mMusicService.getSongDuration();
            long currentDuration = mMusicService.getCurrentPosition();
            String currentPosition = mUtilities.milliSecondsToTimer(currentDuration);
            String totalTime = mUtilities.milliSecondsToTimer(totalDuration);
            if (currentDuration != 0) {
                mTextAllTime.setText(mUtilities.milliSecondsToTimer(totalDuration));
            }
            mTextSongProgress.setText(mUtilities.milliSecondsToTimer(currentDuration));
            int progress = (mUtilities.getProgressPercentage(currentDuration, totalDuration));
            mSeekBarProgress.setProgress(progress);
            if (currentPosition.equals(totalTime) && currentDuration != 0) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                mImagePlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
            }
            mHandler.postDelayed(this, DELAY_MILISECONDS);
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) iBinder;
            mMusicService = binder.getService();
            setUpViews();
            mIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIsBound = false;
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mHandler.removeCallbacks(mUpdateTimeTask);
            mMusicService.fastForward(mUtilities.progressToTimer(seekBar.getProgress(),
                    mMusicService.getSongDuration()));
            mTextSongProgress.setText(
                    mUtilities.milliSecondsToTimer(mMusicService.getCurrentPosition()));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        updateProgressBar();
    }

    @Override
    public void downloadSuccess(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void downloadFail(String message) {

    }

    @Override
    public void updateSongList(MoreData moreData) {
        mMoreData.getSongList().addAll(moreData.getSongList());
        //        mCollection.setNextHref(collection.getNextHref());
        //        mMusicAdapter.addData(collection.getTrackList());
        //        mPlayMusicService.updateTrackList(mMusicAdapter.getData());
    }
}
