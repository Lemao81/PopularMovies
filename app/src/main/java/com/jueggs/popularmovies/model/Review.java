package com.jueggs.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable
{
    private int movieId;
    private String author;
    private String review;

    public Review()
    {
    }

    protected Review(Parcel in)
    {
        movieId = in.readInt();
        author = in.readString();
        review = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>()
    {
        @Override
        public Review createFromParcel(Parcel in)
        {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size)
        {
            return new Review[size];
        }
    };

    public int getMovieId()
    {
        return movieId;
    }

    public void setMovieId(int movieId)
    {
        this.movieId = movieId;
    }

    public String getReview()
    {
        return review;
    }

    public void setReview(String review)
    {
        this.review = review;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(movieId);
        dest.writeString(author);
        dest.writeString(review);
    }
}
