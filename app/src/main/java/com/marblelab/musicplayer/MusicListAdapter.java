package com.marblelab.musicplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by abidh on 4/24/2017.
 */

public class MusicListAdapter extends BaseAdapter {
    private Context context;
    private List<Song> songList;
    private String index;

    public MusicListAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int i) {
        return songList.get(i);
    }

    @Override
    public long getItemId(int i) {
        index = String.valueOf(i);
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context).inflate(R.layout.music_list_adapter, null, true);

        final TextView mTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView mAlbum = (TextView) view.findViewById(R.id.tvAlbum);
        TextView mDuration = (TextView) view.findViewById(R.id.tvDuration);
        CardView mMusicCard = (CardView) view.findViewById(R.id.cvMusicCard);
        final Song song = (Song) getItem(i);

        mTitle.setText(song.getTitle());
        mAlbum.setText(song.getArtist());
        mDuration.setText(String.valueOf(song.getDuration()) + " min");
        mMusicCard.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                getItemId(i);
                if (PlayerActivity.mMediaPlayer != null) {
                    PlayerActivity.mMediaPlayer.reset();
                    PlayerActivity.playStatus = false;
                }
                Intent intent = new Intent(context, PlayerActivity.class);
                ActivityOptionsCompat activityOptionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,mTitle,"title");
                intent.putExtra("path", song.getSongPath());
                intent.putExtra("index", index);
                context.startActivity(intent,activityOptionsCompat.toBundle());
            }
        });
        return view;
    }
}
