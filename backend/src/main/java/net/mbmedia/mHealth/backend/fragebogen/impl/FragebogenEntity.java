package net.mbmedia.mHealth.backend.fragebogen.impl;

import javax.persistence.*;

import java.nio.charset.StandardCharsets;

import static net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenEntity.TABLE_NAME;

@Entity
@Table(name = TABLE_NAME)
public class FragebogenEntity
{
    public static final String TABLE_NAME = "frageboegen";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titel")
    private String titel;

    @Column(name = "beschreibung")
    private String beschreibung;

    @Column(name = "author")
    private String author;

    @Column(name = "json")
    private byte[] json;

    private FragebogenEntity()
    {
    }

    private FragebogenEntity(Long id, String titel, String beschreibung, String author, byte[] json)
    {
        this.id = id;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.author = author;
        this.json = json;
    }

    public static class BUILDER
    {
        private Long id;
        private String titel;
        private String beschreibung;
        private String author;
        private byte[] json;

        public BUILDER withID(Long id)
        {
            this.id = id;
            return this;
        }

        public BUILDER withTitel(String titel)
        {
            this.titel = titel;
            return this;
        }

        public BUILDER withBeschreibung(String beschreibung)
        {
            this.beschreibung = beschreibung;
            return this;
        }

        public BUILDER withAuthor(String author)
        {
            this.author = author;
            return this;
        }

        public BUILDER withJson(byte[] json)
        {
            this.json = json;
            return this;
        }

        public BUILDER withJson(String json)
        {
            return withJson(json.getBytes(StandardCharsets.UTF_8));
        }

        public FragebogenEntity build()
        {
            return new FragebogenEntity(this.id, this.titel, this.beschreibung, this.author, this.json);
        }
    }

    public Long getId()
    {
        return id;
    }

    public String getTitel()
    {
        return titel;
    }

    public String getBeschreibung()
    {
        return beschreibung;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getJson()
    {
        return new String(json, StandardCharsets.UTF_8);
    }

    void setId(Long id)
    {
        this.id = id;
    }

    void setTitel(String titel)
    {
        this.titel = titel;
    }

    void setBeschreibung(String beschreibung)
    {
        this.beschreibung = beschreibung;
    }

    void setAuthor(String author)
    {
        this.author = author;
    }

    void setJson(byte[] json)
    {
        this.json = json;
    }

    public FragebogenEntity.BUILDER toBuilder()
    {
        return new FragebogenEntity.BUILDER()
                .withID(id)
                .withTitel(titel)
                .withBeschreibung(beschreibung)
                .withJson(json)
                .withAuthor(author);
    }
}
