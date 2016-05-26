package com.jueggs.popularmovies.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Ranking
{
    private int page;
    private List<Movie> results;
    private int totalResults;
    private int totalPages;
}
