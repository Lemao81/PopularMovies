package com.jueggs.popularmovies.model;

public class Login
{
    private String username;
    private String password;
    private Token token;
    private boolean authenticated;
    private String sessionId;

    public boolean isAuthenticated()
    {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated)
    {
        this.authenticated = authenticated;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public Token getToken()
    {
        return token;
    }

    public void setToken(Token token)
    {
        this.token = token;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
