package com.thaihuy.imusic.screen.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.NotificationTarget;
import com.thaihuy.imusic.R;
import com.thaihuy.imusic.data.model.Song;
import com.thaihuy.imusic.screen.main.MainActivity;
import com.thaihuy.imusic.utils.Constant;
import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener {

    private static final int NOTIFICATION_ID = 101;
    private static final int PRIORITY_RECEIVE = 2;

    private IBinder mIBinder = new LocalBinder();
    private MediaPlayer mMediaPlayer;
    private boolean mIsMediaReady;
    private List<Song> mSongs;
    private int mSongPosition;
    private boolean isShuffle, isRepeat, isRepeatOne, isNonRepeat;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private boolean isLocalTrack;

    public MusicService() {
    }

    public static Intent getSongsIntent(Context context, List<Song> songs, int position) {
        Intent intent = new Intent(context, MusicService.class);
        intent.putParcelableArrayListExtra(Constant.EXTRAS_SONG,
                (ArrayList<? extends Parcelable>) songs);
        intent.putExtra(Constant.POSITION, position);
        return intent;
    }

    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        registerBroadcastReceive();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSongs = intent.getParcelableArrayListExtra(Constant.EXTRAS_SONG);
        mSongPosition = intent.getIntExtra(Constant.POSITION, 0);
        isLocalTrack = intent.getBooleanExtra(Constant.ISLOCALTRACK, false);
        if (mSongs != null) {
            initMediaPlayer();
            buildNotification();
        }
        return START_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playMusic();
    }

    public void initMediaPlayer() {
        mIsMediaReady = false;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.reset();
        try {
            if (isLocalTrack) {
                mMediaPlayer.setDataSource(mSongs.get(mSongPosition).getStreamUrl());
            } else {
                mMediaPlayer.setDataSource(
                        mSongs.get(mSongPosition).getStreamUrl() + Constant.CLIENT_ID);
            }
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            Log.d("ABC", e.getMessage());
            Toast.makeText(this, R.string.service_has_stopped, Toast.LENGTH_SHORT).show();
            stopSelf();
        }
    }

    public String getArtistName() {
        return mSongs.get(mSongPosition) != null ? mSongs.get(mSongPosition)
                .getArtist()
                .getUsername() : "";
    }

    public String getSongName() {
        return mSongs.get(mSongPosition) != null ? mSongs.get(mSongPosition).getTitle() : "";
    }

    public String getArtwork() {
        return mSongs.get(mSongPosition) != null ? mSongs.get(mSongPosition).getArtworkUrl() : "";
    }

    public int getSongDuration() {
        return mIsMediaReady ? mMediaPlayer.getDuration() : 0;
    }

    public int getCurrentPosition() {
        return mIsMediaReady ? mMediaPlayer.getCurrentPosition() : 0;
    }

    public void fastForward(int position) {
        mMediaPlayer.seekTo(position);
    }

    public void playMusic() {
        if (!mMediaPlayer.isPlaying()) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    mMediaPlayer.start();
                }
            });
            thread.start();
            mIsMediaReady = true;
        }
        updateNotification(Constant.ACTION_PLAY);
    }

    public void pauseMusic() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
        updateNotification(Constant.ACTION_PAUSE);
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public void nextMusic() {
        if (mSongPosition == mSongs.size() - 1) {
            mSongPosition = 0;
        } else {
            mSongPosition++;
        }
        mMediaPlayer.reset();
        initMediaPlayer();
        updateNotification(Constant.ACTION_NEXT);
    }

    public void previousMusic() {
        if (mSongPosition == 0) {
            mSongPosition = mSongs.size() - 1;
        } else {
            mSongPosition--;
        }
        mMediaPlayer.reset();
        initMediaPlayer();
        updateNotification(Constant.ACTION_PREVIOUS);
    }

    public void stopMusic() {
        if (mMediaPlayer == null) {
            return;
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    private void buildNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("PlayMusic", "PlayMusic");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
        mNotification =
                new Notification.Builder(getApplicationContext()).setSmallIcon(R.drawable.ic_music)
                        .setContentTitle(getSongName())
                        .setContentText(getArtistName())
                        .setContentIntent(pendingIntent)
                        .build();
        mNotification.contentView = remoteViews;
        mNotification.contentView.setTextViewText(R.id.text_song_notification, getSongName());
        mNotification.contentView.setTextViewText(R.id.text_artist_notification, getArtistName());
        NotificationTarget notificationTarget =
                new NotificationTarget(this, R.id.image_song_notification,
                        mNotification.contentView, mNotification, NOTIFICATION_ID);
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(getArtwork())
                .apply(new RequestOptions().placeholder(R.drawable.bg_circle))
                .into(notificationTarget);
        mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
        setListener(mNotification.contentView);
        startForeground(NOTIFICATION_ID, mNotification);
    }

    public void setListener(RemoteViews views) {
        Intent previous = new Intent(Constant.ACTION_PREVIOUS);
        Intent pause = new Intent(Constant.ACTION_PAUSE);
        Intent next = new Intent(Constant.ACTION_NEXT);
        Intent play = new Intent(Constant.ACTION_PLAY);

        PendingIntent pPrevious = PendingIntent.getBroadcast(getApplicationContext(), 0, previous,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.image_previous_notification, pPrevious);

        PendingIntent pPause = PendingIntent.getBroadcast(getApplicationContext(), 0, pause,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.image_pause_notification, pPause);

        PendingIntent pNext = PendingIntent.getBroadcast(getApplicationContext(), 0, next,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.image_next_notification, pNext);

        PendingIntent pPlay = PendingIntent.getBroadcast(getApplicationContext(), 0, play,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.image_play_notification, pPlay);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                switch (intent.getAction()) {
                    case Constant.ACTION_PREVIOUS:
                        previousMusic();
                        break;
                    case Constant.ACTION_NEXT:
                        nextMusic();
                        break;
                    case Constant.ACTION_PLAY:
                        playMusic();
                        break;
                    case Constant.ACTION_PAUSE:
                        pauseMusic();
                        break;
                }
            }
        }
    };

    private void registerBroadcastReceive() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_PLAY);
        intentFilter.addAction(Constant.ACTION_NEXT);
        intentFilter.addAction(Constant.ACTION_PAUSE);
        intentFilter.addAction(Constant.ACTION_PREVIOUS);

        intentFilter.setPriority(PRIORITY_RECEIVE);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void updateNotification(String action) {
        if (action.equals(Constant.ACTION_NEXT) || action.equals(Constant.ACTION_PREVIOUS)) {
            mNotification.contentView.setTextViewText(R.id.text_song_notification, getSongName());
            mNotification.contentView.setTextViewText(R.id.text_artist_notification,
                    getArtistName());
            NotificationTarget notificationTarget =
                    new NotificationTarget(this, R.id.image_song_notification,
                            mNotification.contentView, mNotification, NOTIFICATION_ID);
            String art = getArtwork();
            if (art.equals(null)) {
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(R.mipmap.ic_launcher)
                        .into(notificationTarget);
            } else {
                Glide.with(getApplicationContext()).asBitmap().load(art).into(notificationTarget);
            }
        } else if (action.equals(Constant.ACTION_PLAY)) {
            mNotification.contentView.setTextViewText(R.id.text_song_notification, getSongName());
            mNotification.contentView.setTextViewText(R.id.text_artist_notification,
                    getArtistName());
            NotificationTarget notificationTarget =
                    new NotificationTarget(this, R.id.image_song_notification,
                            mNotification.contentView, mNotification, NOTIFICATION_ID);
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(mSongs.get(mSongPosition).getArtworkUrl())
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher))
                    .into(notificationTarget);
            mNotification.contentView.setViewVisibility(R.id.image_play_notification, View.GONE);
            mNotification.contentView.setViewVisibility(R.id.image_pause_notification,
                    View.VISIBLE);
            mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        } else if (action.equals(Constant.ACTION_PAUSE)) {
            mNotification.contentView.setViewVisibility(R.id.image_pause_notification, View.GONE);
            mNotification.contentView.setViewVisibility(R.id.image_play_notification, View.VISIBLE);
            mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        }
    }
}
