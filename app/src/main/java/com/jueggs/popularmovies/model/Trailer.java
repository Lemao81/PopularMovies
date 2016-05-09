package com.jueggs.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable
{
    private int movieId;
    private String language;
    private String region;
    private String key;
    private String name;
    private int size;

    public Trailer()
    {
    }

    protected Trailer(Parcel in)
    {
        movieId = in.readInt();
        language = in.readString();
        region = in.readString();
        key = in.readString();
        name = in.readString();
        size = in.readInt();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>()
    {
        @Override
        public Trailer createFromParcel(Parcel in)
        {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size)
        {
            return new Trailer[size];
        }
    };

    public int getMovieId()
    {
        return movieId;
    }

    public void setMovieId(int id)
    {
        this.movieId = id;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
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
        dest.writeString(language);
        dest.writeString(region);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeInt(size);
    }
}
