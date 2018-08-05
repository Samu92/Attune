package es.app.attune.attune.Database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    private String name;

    private String email;

    @Generated(hash = 502240408)
    public User(@NotNull String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    @Generated(hash = 586692638)
    public User() {
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

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
