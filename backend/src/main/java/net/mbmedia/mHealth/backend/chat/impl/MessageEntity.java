package net.mbmedia.mHealth.backend.chat.impl;

import javax.persistence.*;
import java.time.LocalDateTime;

import static net.mbmedia.mHealth.backend.chat.impl.MessageEntity.TABLE_NAME;

@Entity
@Table(name = TABLE_NAME)
public class MessageEntity implements Comparable<MessageEntity>
{
    public static final String TABLE_NAME = "chat";

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nachricht")
    private String nachricht;

    @Column(name = "author")
    private String authorID;

    @Column(name = "empfaenger")
    private String empfaengerID;

    @Column(name = "erstellt")
    private LocalDateTime erstellt;

    @Column(name = "gelesen")
    private Boolean gelesen;

    public MessageEntity()
    {
    }

    private MessageEntity(String nachricht, String authorID, String empfaengerID, LocalDateTime erstellt, Boolean gelesen)
    {
        this.nachricht = nachricht;
        this.authorID = authorID;
        this.empfaengerID = empfaengerID;
        this.erstellt = erstellt;
        this.gelesen = gelesen;
    }

    @Override
    public int compareTo(MessageEntity o)
    {
        return getErstellt().compareTo(o.getErstellt());
    }

    public static class BUILDER{
        private String nachricht;
        private String authorID;
        private String empfaengerID;
        private LocalDateTime erstellt;
        private Boolean gelesen;

        public BUILDER withNachricht(String nachricht)
        {
            this.nachricht = nachricht;
            return this;
        }

        public BUILDER withAuthorID(String authorID)
        {
            this.authorID = authorID;
            return this;
        }

        public BUILDER withEmpfaengerID(String empfaengerID)
        {
            this.empfaengerID = empfaengerID;
            return this;
        }

        public BUILDER withErstellt(LocalDateTime erstellt)
        {
            this.erstellt = erstellt;
            return this;
        }

        public BUILDER withGelesen(boolean bool)
        {
            this.gelesen = bool;
            return this;
        }

        public MessageEntity build()
        {
            return new MessageEntity(nachricht, authorID, empfaengerID, erstellt, gelesen);
        }
    }

    public Long getId()
    {
        return id;
    }

    public String getNachricht()
    {
        return nachricht;
    }

    public String getAuthorID()
    {
        return authorID;
    }

    public String getEmpfaengerID()
    {
        return empfaengerID;
    }

    public LocalDateTime getErstellt()
    {
        return erstellt;
    }

    private void setId(Long id)
    {
        this.id = id;
    }

    private void setNachricht(String nachricht)
    {
        this.nachricht = nachricht;
    }

    private void setAuthorID(String authorID)
    {
        this.authorID = authorID;
    }

    private void setEmpfaengerID(String empfaengerID)
    {
        this.empfaengerID = empfaengerID;
    }

    private void setErstellt(LocalDateTime erstellt)
    {
        this.erstellt = erstellt;
    }


}
