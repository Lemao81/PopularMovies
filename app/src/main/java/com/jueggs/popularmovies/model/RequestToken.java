package com.jueggs.popularmovies.model;

import lombok.Data;

import java.util.Date;

@Data
public class RequestToken
{
    private boolean success;
    private String expiresAt;
    private String requestToken;
}
