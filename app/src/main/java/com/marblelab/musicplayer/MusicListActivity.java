package com.marblelab.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicListActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 1;

    public static List<Song> mMusicList;
    public static Context context;
    private ListView mMusicListView;
    private MusicListAdapter mMusicListViewAdapter;
    private SwipeRefreshLayout mRefresh;

    private ImageButton mPlayPause, mPrevious, mNext;
    private SeekBar mProgress;
    private TextView mTitle, mArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        mPrevious = (ImageButton) findViewById(R.id.btPrevMini);
        mPlayPause = (ImageButton) findViewById(R.id.btPlayPauseMini);
        mNext = (ImageButton) findViewById(R.id.btNextMini);
        mTitle = (TextView) findViewById(R.id.tvTitleMini);
        mArtist = (TextView) findViewById(R.id.tvArtistMini);
        mProgress = (SeekBar) findViewById(R.id.sbProgressMini);
        mMusicListView = (ListView) findViewById(R.id.lvMusicList);
        mRefresh = (SwipeRefreshLayout) findViewById(R.id.srRefresh);

        mMusicList = new ArrayList<>();

        mMusicListViewAdapter = new MusicListAdapter(this, mMusicList);
        mMusicListView.setAdapter((ListAdapter) mMusicListViewAdapter);


        if (ContextCompat.checkSelfPermission(MusicListActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MusicListActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            new getMusicList().execute();
        } else {
            new getMusicList().execute();
        }

        mMusicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (PlayerActivity.mMediaPlayer != null) {
                    PlayerActivity.mMediaPlayer.reset();
                    PlayerActivity.playStatus = false;
                }
                String path = (mMusicList.get(position)).getSongPath();
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                intent.putExtra("path", path);
                intent.putExtra("index", String.valueOf(position));
                startActivity(intent);

            }
        });

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getMusicList().execute();
            }
        });

        mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (PlayerActivity.mMediaPlayer != null) {
                    if (seekBar.getProgress() + 10 >= PlayerActivity.mMediaPlayer.getDuration()) {
                        PlayerActivity.playNextSong();
                        mTitle.setText(mMusicList.get(PlayerActivity.musicIndex).getTitle());
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (PlayerActivity.mMediaPlayer != null) {
                    PlayerActivity.mMediaPlayer.pause();
                }

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (PlayerActivity.mMediaPlayer != null) {
                    PlayerActivity.mMediaPlayer.seekTo(seekBar.getProgress());
                    mProgress.setProgress(seekBar.getProgress());
                    PlayerActivity.playStatus = true;
                    if (PlayerActivity.playStatus) {
                        mPlayPause.setImageResource(R.drawable.ic_pause_black_48dp);
                        PlayerActivity.mMediaPlayer.start();
                    } else {
                        mPlayPause.setImageResource(R.drawable.ic_play_arrow_black_48dp);
                        PlayerActivity.mMediaPlayer.stop();
                    }
                }
            }
        });

        mPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PlayerActivity.mMediaPlayer != null) {
                    if (PlayerActivity.playStatus) {
                        mPlayPause.setImageResource(R.drawable.ic_play_arrow_black_48dp);
                    } else {
                        mPlayPause.setImageResource(R.drawable.ic_pause_black_48dp);
                    }
                    PlayerActivity.songPlayPause();
                }

            }
        });
        //previous button
        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PlayerActivity.mMediaPlayer != null) {
                    PlayerActivity.playPreviousSong();
                    mTitle.setText(mMusicList.get(PlayerActivity.musicIndex).getTitle());
                }
            }
        });
        //next song button
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerActivity.playNextSong();
                mTitle.setText(mMusicList.get(PlayerActivity.musicIndex).getTitle());
            }
        });
    }

    private void updateProgressBar() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    while (true) {
                        while (PlayerActivity.mMediaPlayer.isPlaying()) {
                            Thread.sleep(100);
                            mProgress.setProgress(PlayerActivity.mMediaPlayer.getCurrentPosition());
                        }

                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PlayerActivity.mMediaPlayer != null) {
            if (PlayerActivity.playStatus) {
                mPlayPause.setImageResource(R.drawable.ic_pause_black_48dp);
            } else {
                mPlayPause.setImageResource(R.drawable.ic_play_arrow_black_48dp);
            }
            mTitle.setText(PlayerActivity.currentTrackTitle);
            mArtist.setText(PlayerActivity.currentTrackArtist);
            mProgress.setMax(PlayerActivity.mMediaPlayer.getDuration());
            mProgress.setProgress(PlayerActivity.mMediaPlayer.getCurrentPosition());
            updateProgressBar();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PlayerActivity.mMediaPlayer.release();
    }

    class getMusicList extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            String path = Environment.getExternalStorageDirectory().toString().trim() + "/Song";

            File directory = new File(path);
            File[] directoryFiles = directory.listFiles();
            Log.e("Path", String.valueOf(directoryFiles));

            if (String.valueOf(directory) != null && String.valueOf(directoryFiles) != null) {
                for (File file : directoryFiles) {
                    try {

                        MusicDetailsFactory.getInstance().setMusic(file);
                        mMusicList.add(MusicDetailsFactory.getInstance().getSongDetails());

                        ((BaseAdapter) mMusicListViewAdapter).notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } else {
                //Toast.makeText(getApplicationContext(),"Empty Directory",Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRefresh.setRefreshing(false);
        }
    }
}
