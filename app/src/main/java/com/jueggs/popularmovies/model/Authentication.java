package com.jueggs.popularmovies.model;

import lombok.Data;

@Data
public class Authentication
{
    private boolean success;
    private String requestToken;
}
