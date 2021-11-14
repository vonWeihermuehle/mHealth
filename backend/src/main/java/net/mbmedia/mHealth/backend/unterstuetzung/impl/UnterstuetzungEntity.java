package net.mbmedia.mHealth.backend.unterstuetzung.impl;

import javax.persistence.*;

import static net.mbmedia.mHealth.backend.unterstuetzung.impl.UnterstuetzungEntity.TABLE_NAME;


@Entity
@Table(name = TABLE_NAME)
public class UnterstuetzungEntity
{
    public final static String TABLE_NAME = "unterstuetzung";

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titel")
    private String titel;

    @Column(name = "text")
    private String text;

    @Column(name = "author")
    private String author;

    @Column(name = "empfaenger")
    private String empfaenger;

    public UnterstuetzungEntity()
    {
    }

    private UnterstuetzungEntity(String titel, String text, String author, String empfaenger)
    {
        this.titel = titel;
        this.text = text;
        this.author = author;
        this.empfaenger = empfaenger;
    }

    public static class BUILDER
    {
        private String titel;
        private String text;
        private String author;
        private String empfaenger;

        public BUILDER withTitel(String titel)
        {
            this.titel = titel;
            return this;
        }

        public BUILDER withText(String text)
        {
            this.text = text;
            return this;
        }

        public BUILDER withAuthor(String author)
        {
            this.author = author;
            return this;
        }

        public BUILDER withEmpfaenger(String empfaenger)
        {
            this.empfaenger = empfaenger;
            return this;
        }

        public UnterstuetzungEntity build()
        {
            return new UnterstuetzungEntity(titel, text, author, empfaenger);
        }
    }

    private void setId(Long id)
    {
        this.id = id;
    }

    void setTitel(String titel)
    {
        this.titel = titel;
    }

    void setText(String text)
    {
        this.text = text;
    }

    private void setAuthor(String author)
    {
        this.author = author;
    }

    private void setEmpfaenger(String empfaenger)
    {
        this.empfaenger = empfaenger;
    }

    public Long getId()
    {
        return id;
    }

    public String getTitel()
    {
        return titel;
    }

    public String getText()
    {
        return text;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getEmpfaenger()
    {
        return empfaenger;
    }
}
