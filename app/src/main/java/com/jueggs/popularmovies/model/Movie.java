package com.jueggs.popularmovies.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import com.jueggs.popularmovies.data.favourites.schematic.FavouriteColumns;

import java.util.Date;

public class Movie implements Parcelable
{
    public static final int ENCODE_BIT_SHIFT = 16;
    public static final int MAX_GENRE_IDS = 4;

    private int dbId;
    private int movieId;
    private String title;
    private Date releaseDate;
    private String posterPath;
    private float voteAverage;
    private String overview;
    private int[] genreIds;
    private boolean adult;
    private String originalTitle;
    private String originalLanguage;

    public Movie()
    {
    }

    protected Movie(Parcel in)
    {
        dbId = in.readInt();
        movieId = in.readInt();
        title = in.readString();
        releaseDate = (Date) in.readSerializable();
        posterPath = in.readString();
        voteAverage = in.readFloat();
        overview = in.readString();
        genreIds = in.createIntArray();
        adult = in.readByte() != 0;
        originalTitle = in.readString();
        originalLanguage = in.readString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(dbId);
        dest.writeInt(movieId);
        dest.writeString(title);
        dest.writeSerializable(releaseDate);
        dest.writeString(posterPath);
        dest.writeFloat(voteAverage);
        dest.writeString(overview);
        dest.writeIntArray(genreIds);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>()
    {
        @Override
        public Movie createFromParcel(Parcel in)
        {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size)
        {
            return new Movie[size];
        }
    };

    public int getDbId()
    {
        return dbId;
    }

    public void setDbId(int dbId)
    {
        this.dbId = dbId;
    }

    public int getMovieId()
    {
        return movieId;
    }

    public void setMovieId(int movieId)
    {
        this.movieId = movieId;
    }

    public String getOverview()
    {
        return overview;
    }

    public void setOverview(String overview)
    {
        this.overview = overview;
    }

    public String getPosterPath()
    {
        return posterPath;
    }

    public void setPosterPath(String posterPath)
    {
        this.posterPath = posterPath.substring(1);
    }

    public Date getReleaseDate()
    {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public float getVoteAverage()
    {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage)
    {
        this.voteAverage = voteAverage;
    }

    public boolean isAdult()
    {
        return adult;
    }

    public void setAdult(boolean adult)
    {
        this.adult = adult;
    }

    public int[] getGenreIds()
    {
        return genreIds;
    }

    public void setGenreIds(int[] genreIds)
    {
        this.genreIds = genreIds;
    }

    //save up to 4 genre ids as 16 bit chunks in a long value
    public long encodeGenreIds()
    {
        long encoded = 0;

        for (int i = MAX_GENRE_IDS - 1; i >= 0; i--)
        {
            int shift = i * ENCODE_BIT_SHIFT;
            int id = getGenreIds()[i];
            encoded = ((encoded >> shift) | (0xffff & id)) << shift;
        }
        return encoded;
    }

    public int[] decodeGenreIds(long encoded)
    {
        int[] ids = new int[MAX_GENRE_IDS];

        for (int i = 0; i < MAX_GENRE_IDS; i++)
        {
            int shift = i * ENCODE_BIT_SHIFT;
            ids[i] = (int) ((encoded >> shift) & 0xffff);
        }
        return ids;
    }

    public String getOriginalLanguage()
    {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage)
    {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle()
    {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle)
    {
        this.originalTitle = originalTitle;
    }

    public ContentValues toContentValues()
    {
        ContentValues values = new ContentValues();
        values.put(FavouriteColumns.ADULT, isAdult());
        values.put(FavouriteColumns.GENRE_IDS, encodeGenreIds());
        values.put(FavouriteColumns.MOVIE_ID, getMovieId());
        values.put(FavouriteColumns.ORIG_LANG, getOriginalLanguage());
        values.put(FavouriteColumns.ORIG_TITLE, getOriginalTitle());
        values.put(FavouriteColumns.OVERVIEW, getOverview());
        values.put(FavouriteColumns.POSTER_PATH, getPosterPath());
        values.put(FavouriteColumns.TITLE, getTitle());
        values.put(FavouriteColumns.REL_DATE, getReleaseDate().getTime());
        values.put(FavouriteColumns.VOTE_AVERAGE, getVoteAverage());
        return values;
    }
}
