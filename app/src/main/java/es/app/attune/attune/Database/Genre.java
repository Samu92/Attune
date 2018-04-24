package es.app.attune.attune.Database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class Genre {
    @Id(autoincrement = true)
    private long id;
    @NotNull
    private String name;

    @Generated(hash = 429024282)
    public Genre(long id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 235763487)
    public Genre() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
