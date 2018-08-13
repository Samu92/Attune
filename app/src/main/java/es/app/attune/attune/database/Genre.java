package es.app.attune.attune.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class Genre {
    @Index(unique = true)
    @NotNull
    @Id
    private String id;
    @Index(unique = true)
    @NotNull
    private String name;
    @NotNull
    private boolean manual;

    @Generated(hash = 390443588)
    public Genre(@NotNull String id, @NotNull String name, boolean manual) {
        this.id = id;
        this.name = name;
        this.manual = manual;
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

    public boolean getManual() {
        return this.manual;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }
    
}
