package net.mbmedia.mHealth.backend.kontakt.impl;

import javax.persistence.*;

import static net.mbmedia.mHealth.backend.kontakt.impl.KontaktEntity.TABLE_NAME;

@Entity
@Table(name = TABLE_NAME)
public class KontaktEntity
{
    public static final String TABLE_NAME = "kontakte";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "art")
    private String art;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "userID")
    private String userID;

    public KontaktEntity()
    {
    }

    KontaktEntity(Long id, String name, String art, String email, String phone, String userID)
    {
        this.id = id;
        this.name = name;
        this.art = art;
        this.email = email;
        this.phone = phone;
        this.userID = userID;
    }

    public static class BUILDER
    {
        private Long id;
        private String name;
        private String art;
        private String email;
        private String phone;
        private String userID;

        public BUILDER withID(Long id)
        {
            this.id = id;
            return this;
        }

        public BUILDER withName(String name)
        {
            this.name = name;
            return this;
        }

        public BUILDER withArt(String art)
        {
            this.art = art;
            return this;
        }

        public BUILDER withEmail(String email)
        {
            this.email = email;
            return this;
        }

        public BUILDER withPhone(String phone)
        {
            this.phone = phone;
            return this;
        }

        public BUILDER withUserID(String uuid)
        {
            this.userID = uuid;
            return this;
        }

        public KontaktEntity build()
        {
            return new KontaktEntity(this.id, this.name, this.art, this.email, this.phone, this.userID);
        }
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getArt()
    {
        return art;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPhone()
    {
        return phone;
    }

    public String getUserID()
    {
        return userID;
    }

    void setId(Long id)
    {
        this.id = id;
    }

    void setName(String name)
    {
        this.name = name;
    }

    void setArt(String art)
    {
        this.art = art;
    }

    void setEmail(String email)
    {
        this.email = email;
    }

    void setPhone(String phone)
    {
        this.phone = phone;
    }

    void setUserID(String uuid)
    {
        this.userID = uuid;
    }
}
