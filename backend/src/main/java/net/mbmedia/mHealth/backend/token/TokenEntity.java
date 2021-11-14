package net.mbmedia.mHealth.backend.token;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = TokenEntity.TABLE_NAME)
public class TokenEntity
{
    public static final String TABLE_NAME = "token";

    @Id
    @Column(name = "token")
    private String token;

    @Column(name = "expiration")
    private Date expireDate;

    public TokenEntity(){}

    public TokenEntity(String token, Date expireDate)
    {
        this.token = token;
        this.expireDate = expireDate;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public Date getExpireDate()
    {
        return expireDate;
    }

    public void setExpireDate(Date expireDate)
    {
        this.expireDate = expireDate;
    }
}
