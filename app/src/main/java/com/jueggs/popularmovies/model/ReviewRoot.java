package com.jueggs.popularmovies.model;

import lombok.Data;

import java.util.List;

@Data
public class ReviewRoot
{
    private Integer id;
    private Integer page;
    private List<Review> results;
    private Integer totalPages;
    private Integer totalResults;
}
