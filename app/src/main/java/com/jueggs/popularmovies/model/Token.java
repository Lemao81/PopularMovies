package com.jueggs.popularmovies.model;

public class Token
{
    private String token;
    private boolean success;

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
}
