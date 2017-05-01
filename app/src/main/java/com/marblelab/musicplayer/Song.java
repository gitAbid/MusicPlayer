package com.marblelab.musicplayer;

/**
 * Created by Abid Hasan on 4/20/2017.
 */

public class Song {
    private String title;
    private String album;
    private String artist;
    private Double duration;
    private Double size;
    private String songPath;
    private String year;

    public Song(String title, String album, String artist, Double duration, Double size, String songPath, String year) {
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
        this.size = size;
        this.songPath = songPath;
        this.year = year;

    }


    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public Double getDuration() {
        return duration;
    }

    public Double getSize() {
        return size;
    }

    public String getSongPath() {
        return songPath;
    }

    @Override
    public String toString() {
        return "\nTitle='" + title + '\'' +
                "\nAlbum='" + album + '\'' +
                "\nArtist='" + artist + '\'' +
                "\nDuration=" + duration +
                "\nSize=" + size +
                "\nSongPath='" + songPath + '\'' + "\n";
    }

    public String getYear() {
        return year;
    }
}
