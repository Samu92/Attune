package es.app.attune.attune.Classes;

import com.orm.SugarRecord;

import java.util.List;

import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Samuel on 02/02/2018.
 */

public class Song extends SugarRecord<Song> {
    /*
    *
    * artists, available_markets, disc_number, duration_ms, explicit,
    * external_urls, href, id, is_playable, linked_from, name, preview_url, track_number, type, uri
    *
    * */
    private String id;
    private List<ArtistSimple> artists;
    private List<String> is_playable;
    private int disc_number;
    private long duration_ms;
    private String name;
    private String uri;
    private String type;

    public Song() {
        this.id = id;
        this.name = name;


    }




}
