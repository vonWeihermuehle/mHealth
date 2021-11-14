package net.mbmedia.mHealth.backend.chat.impl.TO;

import java.util.Objects;

public class ChatTO
{
    private String name;
    private String username;
    private String uuid;
    private final Boolean ungeleseneNachrichten;

    public ChatTO(String name, String username, String uuid, Boolean ungeleseneNachrichten)
    {
        this.name = name;
        this.username = username;
        this.uuid = uuid;
        this.ungeleseneNachrichten = ungeleseneNachrichten;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatTO chatTO = (ChatTO) o;
        return Objects.equals(name, chatTO.name) && Objects.equals(username, chatTO.username) && Objects.equals(uuid, chatTO.uuid);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, username, uuid);
    }
}
