package com.jueggs.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.jueggs.popularmovies.util.Utils;

import java.util.Arrays;
import java.util.Date;

public class Movie implements Parcelable
{
    public static final int ENCODE_BIT_SHIFT = 16;
    public static final int MAX_GENRE_IDS = 4;

    private long dbId;
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
    private byte[] poster = new byte[0];

    @Override
    public boolean equals(Object other)
    {
        if (this == other) return true;
        if (other == null || !(other instanceof Movie)) return false;
        Movie o = (Movie) other;
        return dbId == o.dbId &&
                movieId == o.movieId &&
                title.equals(o.title) &&
                releaseDate.getTime() == o.releaseDate.getTime() &&
                posterPath.equals(o.posterPath) &&
                voteAverage == o.voteAverage &&
                overview.equals(o.overview) &&
                Utils.encodeGenreIds(genreIds) == Utils.encodeGenreIds(o.genreIds) &&
                adult == o.adult &&
                originalTitle.equals(o.originalTitle) &&
                originalLanguage.equals(o.originalLanguage) &&
                Arrays.equals(poster, o.poster);
    }

    public Movie()
    {
    }

    public Movie(long dbId, boolean adult, int[] genreIds, int movieId, String originalLanguage, String originalTitle,
                 String overview, byte[] poster, String posterPath, Date releaseDate, String title, float voteAverage)
    {
        this(adult, genreIds, movieId, originalLanguage, originalTitle, overview, poster, posterPath, releaseDate, title, voteAverage);
        this.dbId = dbId;
    }

    public Movie(boolean adult, int[] genreIds, int movieId, String originalLanguage, String originalTitle,
                 String overview, byte[] poster, String posterPath, Date releaseDate, String title, float voteAverage)
    {
        this.adult = adult;
        this.genreIds = genreIds;
        this.movieId = movieId;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.poster = poster;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.voteAverage = voteAverage;
    }

    protected Movie(Parcel in)
    {
        dbId = in.readLong();
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
        poster = new byte[in.readInt()];
        in.readByteArray(poster);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(dbId);
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
        dest.writeInt(poster.length);
        dest.writeByteArray(poster);
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


    public long getDbId()
    {
        return dbId;
    }

    public void setDbId(long dbId)
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
        this.posterPath = posterPath.charAt(0) == '/' ? posterPath.substring(1) : posterPath;
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

    public byte[] getPoster()
    {
        return poster;
    }

    public void setPoster(byte[] poster)
    {
        this.poster = poster;
    }
}
