package com.jueggs.popularmovies.model;

import java.util.Date;

public class Token
{
    private String token;
    private boolean success;
    private Date expiration;

    public Token()
    {
    }

    public Token(String token)
    {
        this.token = token;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public Date getExpiration()
    {
        return expiration;
    }

    public void setExpiration(Date expiration)
    {
        this.expiration = expiration;
    }
}
