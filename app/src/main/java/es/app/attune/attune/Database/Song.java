package es.app.attune.attune.Database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;

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

    @NotNull
    private float acousticness;

    @NotNull
    private float danceability;

    @NotNull
    private float energy;

    @NotNull
    private float instrumentalness;

    @NotNull
    private float liveness;

    @NotNull
    private float loudness;

    @NotNull
    private int popularity;

    @NotNull
    private float speechiness;

    @NotNull
    private float valence;
    
    private String date;

    @Generated(hash = 1860946579)
    public Song(@NotNull String id, @NotNull String idPlaylist,
            @NotNull String idSpotify, @NotNull String urlSpotify,
            @NotNull String genreId, @NotNull String name, long duration,
            float tempo, @NotNull String artist, @NotNull String image,
            String previewUrl, float acousticness, float danceability, float energy,
            float instrumentalness, float liveness, float loudness, int popularity,
            float speechiness, float valence, String date) {
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
        this.acousticness = acousticness;
        this.danceability = danceability;
        this.energy = energy;
        this.instrumentalness = instrumentalness;
        this.liveness = liveness;
        this.loudness = loudness;
        this.popularity = popularity;
        this.speechiness = speechiness;
        this.valence = valence;
        this.date = date;
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

    public float getAcousticness() {
        return this.acousticness;
    }

    public void setAcousticness(float acousticness) {
        this.acousticness = acousticness;
    }

    public float getDanceability() {
        return this.danceability;
    }

    public void setDanceability(float danceability) {
        this.danceability = danceability;
    }

    public float getEnergy() {
        return this.energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public float getInstrumentalness() {
        return this.instrumentalness;
    }

    public void setInstrumentalness(float instrumentalness) {
        this.instrumentalness = instrumentalness;
    }

    public float getLiveness() {
        return this.liveness;
    }

    public void setLiveness(float liveness) {
        this.liveness = liveness;
    }

    public float getLoudness() {
        return this.loudness;
    }

    public void setLoudness(float loudness) {
        this.loudness = loudness;
    }

    public int getPopularity() {
        return this.popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public float getSpeechiness() {
        return this.speechiness;
    }

    public void setSpeechiness(float speechiness) {
        this.speechiness = speechiness;
    }

    public float getValence() {
        return this.valence;
    }

    public void setValence(float valence) {
        this.valence = valence;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

  
}
