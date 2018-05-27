package es.app.attune.attune.Database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;

import kaaes.spotify.webapi.android.models.ArtistSimple;

@Entity
public class Song {
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @NotNull
    private String idPlaylist;

    @NotNull
    private String idSpotify;

    @NotNull
    private String urlSpotify;

    @NotNull
    private String genreId;

    @NotNull
    private String name;

    @NotNull
    private long duration;

    @NotNull
    private float tempo;

    @NotNull
    private String artist;

    @NotNull
    private String image;
    
    private String previewUrl;

    @Generated(hash = 1677807930)
    public Song(@NotNull String id, @NotNull String idPlaylist,
            @NotNull String idSpotify, @NotNull String urlSpotify,
            @NotNull String genreId, @NotNull String name, long duration,
            float tempo, @NotNull String artist, @NotNull String image,
            String previewUrl) {
        this.id = id;
        this.idPlaylist = idPlaylist;
        this.idSpotify = idSpotify;
        this.urlSpotify = urlSpotify;
        this.genreId = genreId;
        this.name = name;
        this.duration = duration;
        this.tempo = tempo;
        this.artist = artist;
        this.image = image;
        this.previewUrl = previewUrl;
    }

    @Generated(hash = 87031450)
    public Song() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdPlaylist() {
        return this.idPlaylist;
    }

    public void setIdPlaylist(String idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public String getIdSpotify() {
        return this.idSpotify;
    }

    public void setIdSpotify(String idSpotify) {
        this.idSpotify = idSpotify;
    }

    public String getUrlSpotify() {
        return this.urlSpotify;
    }

    public void setUrlSpotify(String urlSpotify) {
        this.urlSpotify = urlSpotify;
    }

    public String getGenreId() {
        return this.genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public float getTempo() {
        return this.tempo;
    }

    public void setTempo(float tempo) {
        this.tempo = tempo;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPreviewUrl() {
        return this.previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

}
