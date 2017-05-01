package com.marblelab.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class PlayerActivity extends AppCompatActivity {

    public static MediaPlayer mMediaPlayer;
    public static boolean playStatus;
    public static int musicIndex;
    public static String currentTrackTitle;
    public static String currentTrackArtist;
    private static Activity mActivity;
    private static SeekBar mProgress;
    private ImageButton mPrevious, mNext, mPlayPause;
    private TextView mMusicTitle, mMusicArtist, mMusicAlbum, mMusicDuration, mMusicYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_player_details);

        mActivity = this;

        //getting intent data
        Intent intent = getIntent();
        final String path = intent.getStringExtra("path");
        final String pathIndex = intent.getStringExtra("index");

        //attaching to the xml layout files
        mPrevious = (ImageButton) findViewById(R.id.btPrev);
        mNext = (ImageButton) findViewById(R.id.btNext);
        mPlayPause = (ImageButton) findViewById(R.id.btPlayPause);
        mMusicTitle = (TextView) findViewById(R.id.tvMusicTitle);
        mMusicArtist = (TextView) findViewById(R.id.tvArtist);
        mMusicDuration = (TextView) findViewById(R.id.tvDuration);
        mMusicAlbum = (TextView) findViewById(R.id.tvAlbum);
        mMusicYear = (TextView) findViewById(R.id.tvYear);
        mProgress = (SeekBar) findViewById(R.id.pbProgressBar);

        //getting music index to play
        musicIndex = Integer.parseInt(pathIndex);
        mMusicTitle.setText(MusicListActivity.mMusicList.get(musicIndex).getTitle());
        mMusicArtist.setText("Artist\n " + MusicListActivity.mMusicList.get(musicIndex).getArtist());
        mMusicAlbum.setText("Album\n " + MusicListActivity.mMusicList.get(musicIndex).getAlbum());
        mMusicDuration.setText("Duration\n " + MusicListActivity.mMusicList.get(musicIndex).getDuration() + " min");
        mMusicYear.setText("Year\n " + MusicListActivity.mMusicList.get(musicIndex).getYear());

        playMusic(path);
        mPlayPause.setImageResource(R.drawable.ic_pause_black_48dp);


        //play button
        mPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playStatus) {
                    mPlayPause.setImageResource(R.drawable.ic_play_arrow_black_48dp);
                } else {
                    mPlayPause.setImageResource(R.drawable.ic_pause_black_48dp);
                }
                songPlayPause();
            }
        });

        //previous button
        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPreviousSong();
                mMusicTitle.setText(MusicListActivity.mMusicList.get(musicIndex).getTitle());
                mMusicArtist.setText("Artist\n " + MusicListActivity.mMusicList.get(musicIndex).getArtist());
                mMusicAlbum.setText("Album\n " + MusicListActivity.mMusicList.get(musicIndex).getAlbum());
                mMusicDuration.setText("Duration\n " + MusicListActivity.mMusicList.get(musicIndex).getDuration() + " min");
                mMusicYear.setText("Year\n " + MusicListActivity.mMusicList.get(musicIndex).getYear());

            }
        });

        //next song button
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextSong();
                mMusicTitle.setText(MusicListActivity.mMusicList.get(musicIndex).getTitle());
                mMusicArtist.setText("Artist\n " + MusicListActivity.mMusicList.get(musicIndex).getArtist());
                mMusicAlbum.setText("Album\n " + MusicListActivity.mMusicList.get(musicIndex).getAlbum());
                mMusicDuration.setText("Duration\n " + MusicListActivity.mMusicList.get(musicIndex).getDuration() + " min");
                mMusicYear.setText("Year\n " + MusicListActivity.mMusicList.get(musicIndex).getYear());

            }
        });
        mNext.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int seekto = 0;
                seekto = seekto + 5000;
                mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() + seekto);
                return true;
            }
        });

        mPrevious.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int seekto = 0;
                seekto = seekto + 5000;
                mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() - seekto);

                return true;
            }
        });

        //progressOrSeekBar Change Listeners to seekTo and auto play next song
        mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (seekBar.getProgress() + 10 >= mMediaPlayer.getDuration()) {
                    playNextSong();
                    mMusicTitle.setText(MusicListActivity.mMusicList.get(musicIndex).getTitle());
                    mMusicArtist.setText("Artist\n " + MusicListActivity.mMusicList.get(musicIndex).getArtist());
                    mMusicAlbum.setText("Album\n " + MusicListActivity.mMusicList.get(musicIndex).getAlbum());
                    mMusicDuration.setText("Duration\n " + MusicListActivity.mMusicList.get(musicIndex).getDuration() + " min");
                    mMusicYear.setText("Year\n " + MusicListActivity.mMusicList.get(musicIndex).getYear());

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.seekTo(seekBar.getProgress());
                mMediaPlayer.start();
                mProgress.setProgress(seekBar.getProgress());
                playStatus = true;
                if (playStatus) {
                    mPlayPause.setImageResource(R.drawable.ic_pause_black_48dp);
                } else mPlayPause.setImageResource(R.drawable.ic_play_arrow_black_48dp);
            }
        });


    }

    private static void updateProgressBar() {
        mProgress.setMax(mMediaPlayer.getDuration());
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    while (true) {
                        while (mMediaPlayer.isPlaying()) {
                            Thread.sleep(100);
                            mProgress.setProgress(mMediaPlayer.getCurrentPosition());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();


    }

    public static void songPlayPause() {
        if (playStatus) {
            mMediaPlayer.pause();
            playStatus = false;
        } else {
            mMediaPlayer.start();
            playStatus = true;

        }
    }

    public static void playPreviousSong() {
        if (musicIndex - 1 >= 0) {
            musicIndex--;
            String path = MusicListActivity.mMusicList.get(musicIndex).getSongPath();
            playMusic(path);

        } else {
            Toast.makeText(mActivity, "No Song Found!", Toast.LENGTH_SHORT).show();
        }
    }

    public static void playNextSong() {
        if (musicIndex + 1 < MusicListActivity.mMusicList.size()) {
            musicIndex++;
            String path = MusicListActivity.mMusicList.get(musicIndex).getSongPath();
            playMusic(path);

        } else {
            Toast.makeText(mActivity, "No Song Found!", Toast.LENGTH_SHORT).show();
            musicIndex = 0;
            playMusic(MusicListActivity.mMusicList.get(musicIndex).getSongPath());

        }
    }

    public static void playMusic(String path) {

        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            playStatus = false;
        }
        Uri uri = Uri.parse(path);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(mActivity, uri);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            playStatus = true;

            updateProgressBar();
            currentTrackTitle = MusicListActivity.mMusicList.get(musicIndex).getTitle();
            currentTrackArtist = MusicListActivity.mMusicList.get(musicIndex).getArtist();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
