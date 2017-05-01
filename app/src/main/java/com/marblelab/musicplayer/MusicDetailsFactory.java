package com.marblelab.musicplayer;

import android.media.MediaMetadataRetriever;
import android.net.Uri;

import java.io.File;

/**
 * Created by abidh on 4/23/2017.
 */

public class MusicDetailsFactory {
    public static MusicDetailsFactory instance;
    private Song song;
    private File music;

    public static synchronized MusicDetailsFactory getInstance() {
        if (instance == null) {
            instance = new MusicDetailsFactory();
        }
        return instance;
    }

    public void setMusic(File music) {
        this.music = music;
    }

    public Song getSongDetails() {

        song = new Song(getTitle(),
                getMusicAlbum(),
                getMusicArtist(),
                Double.parseDouble(getMusicDuration()),
                getMusicSize(),
                getMusicPath(),
                getYear());
        return song;
    }

    private String getYear() {
        String year;
        MediaMetadataRetriever yearRetriver = getMediaMetadataRetriever();
        year = yearRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);
        return year;
    }

    private String getTitle() {
        return music.getName();
    }

    private Double getMusicSize() {
        Double size = null;
        //TODO size getting function to be implemented
        return size;
    }

    private String getMusicArtist() {
        String artist = null;
        MediaMetadataRetriever durationRetriever = getMediaMetadataRetriever();

        artist = durationRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        return artist;
    }

    private String getMusicAlbum() {
        String album = null;
        MediaMetadataRetriever durationRetriever = getMediaMetadataRetriever();

        album = durationRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        return album;
    }

    private String getMusicDuration() {
        String duration;
        MediaMetadataRetriever durationRetriever = getMediaMetadataRetriever();

        duration = durationRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        duration = String.format("%.2f", ((Double.valueOf(Integer.parseInt(duration))) / 60000));

        return duration;
    }

    private String getMusicPath() {
        return music.getPath();
    }

    private MediaMetadataRetriever getMediaMetadataRetriever() {
        Uri uri = Uri.parse(music.getPath());
        MediaMetadataRetriever Retriever = new MediaMetadataRetriever();
        Retriever.setDataSource(MusicListActivity.context, uri);
        return Retriever;
    }
}
