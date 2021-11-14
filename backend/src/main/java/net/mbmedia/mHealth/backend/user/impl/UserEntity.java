package net.mbmedia.mHealth.backend.user.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class UserEntity
{

    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "vorname")
    private String vorname;

    @Column(name = "nachname")
    private String nachname;

    @Column(name = "email")
    private String email;

    @Column(name = "therapeut")
    private Boolean therapeut;

    @Column(name = "passwort")
    private String passwort;

    @Column(name = "username")
    private String username;

    @Column(name = "Lat")
    private Double lat;

    @Column(name = "Lng")
    private Double lng;

    @Column(name = "techuser")
    private Boolean techUser;

    @Column(name = "schwellwert")
    private Integer schwellwert;

    public UserEntity(){}

    public UserEntity(String uuid, String vorname, String nachname, String email, Boolean therapeut, String passwort, String username, Double lat, Double lng, Boolean techUser, Integer schwellwert)
    {
        this.uuid = uuid;
        this.vorname = vorname;
        this.nachname = nachname;
        this.email = email;
        this.therapeut = therapeut;
        this.passwort = passwort;
        this.username = username;
        this.lat = lat;
        this.lng = lng;
        this.techUser = techUser;
        this.schwellwert = schwellwert;
    }

    public String getUuid()
    {
        return uuid;
    }

    public String getVorname()
    {
        return vorname;
    }

    public String getNachname()
    {
        return nachname;
    }

    public String getEmail()
    {
        return email;
    }

    public Boolean isTherapeut()
    {
        if(therapeut == null)
        {
            return false;
        }
        return therapeut;
    }

    public String getPasswort()
    {
        return passwort;
    }

    public String getUsername()
    {
        return username;
    }

    public Double getLat()
    {
        return lat;
    }

    public Double getLng()
    {
        return lng;
    }

    public Boolean isTechUser()
    {
        if(techUser == null)
        {
            return false;
        }
        return techUser;
    }

    public Integer getSchwellwert()
    {
        return schwellwert == null ? 0 : schwellwert;
    }

    private void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    private void setVorname(String vorname)
    {
        this.vorname = vorname;
    }

    private void setNachname(String nachname)
    {
        this.nachname = nachname;
    }

    private void setEmail(String email)
    {
        this.email = email;
    }

    private void setTherapeut(Boolean therapeut)
    {
        this.therapeut = therapeut;
    }

    void setPasswort(String passwort)
    {
        this.passwort = passwort;
    }

    private void setUsername(String username)
    {
        this.username = username;
    }

    void setLat(Double lat)
    {
        this.lat = lat;
    }

    void setLng(Double lng)
    {
        this.lng = lng;
    }

    private void setTechUser(Boolean techUser)
    {
        this.techUser = techUser;
    }

    void setSchwellwert(Integer schwellwert)
    {
        this.schwellwert = schwellwert;
    }

    public static class BUILDER
    {
        private String uuid;
        private String vorname;
        private String nachname;
        private String email;
        private Boolean therapeut;
        private String passwort;
        private String username;
        private Double lat;
        private Double lng;
        private Boolean techUser;
        private Integer schwellwert;

        public BUILDER withUUID(String uuid)
        {
            this.uuid = uuid;
            return this;
        }

        public BUILDER withVorname(String vorname)
        {
            this.vorname = vorname;
            return this;
        }

        public BUILDER withNachname(String nachname)
        {
            this.nachname = nachname;
            return this;
        }

        public BUILDER withEmail(String email)
        {
            this.email = email;
            return this;
        }

        public BUILDER withTherapeut(boolean therapeut)
        {
            this.therapeut = therapeut;
            return this;
        }

        public BUILDER withPasswort(String passwort)
        {
            this.passwort = passwort;
            return this;
        }

        public BUILDER withUsername(String username)
        {
            this.username = username;
            return this;
        }

        public BUILDER withLat(Double lat)
        {
            this.lat = lat;
            return this;
        }

        public BUILDER withLng(Double lng)
        {
            this.lng = lng;
            return this;
        }

        public BUILDER withTechUser(Boolean techUser)
        {
            this.techUser = techUser;
            return this;
        }

        public BUILDER withSchwellwert(Integer schwellwert)
        {
            this.schwellwert = schwellwert;
            return this;
        }

        public UserEntity build()
        {
            return new UserEntity(this.uuid, this.vorname, this.nachname, this.email, this.therapeut, this.passwort, this.username, this.lat, this.lng, this.techUser, this.schwellwert);
        }
    }

    public UserEntity removePasswort()
    {
        this.passwort = null;
        return this;
    }

    public String getFullName()
    {
        return nachname + " " + vorname;
    }

    public UserEntity.BUILDER toBuilder()
    {
        return new UserEntity.BUILDER()
                .withUUID(uuid)
                .withVorname(vorname)
                .withNachname(nachname)
                .withEmail(email)
                .withTherapeut(therapeut)
                .withPasswort(passwort)
                .withUsername(username)
                .withLat(lat)
                .withLng(lng)
                .withTechUser(techUser)
                .withSchwellwert(schwellwert);
    }
}
