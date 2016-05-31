package com.jueggs.popularmovies.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TrailerRoot
{
    private int id;
    private List<Trailer> results;
}
