package es.app.attune.attune.Database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class Genre {
    @Index(unique = true)
    @NotNull
    @Id
    private String id;
    @Index(unique = true)
    @NotNull
    private String name;
    @Generated(hash = 904715804)
    public Genre(@NotNull String id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 235763487)
    public Genre() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
