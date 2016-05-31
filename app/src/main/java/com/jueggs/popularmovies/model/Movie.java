package com.jueggs.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import lombok.Data;
import lombok.experimental.Builder;

import java.text.ParseException;
import java.util.Date;

import static com.jueggs.popularmovies.util.DateTimeUtils.*;

@Data
@Builder(builderClassName = "Builder")
public class Movie implements Parcelable
{
    public static final String TAG = Movie.class.getSimpleName();
    public static final int ENCODE_BIT_SHIFT = 16;
    public static final int MAX_GENRE_IDS = 4;

    private long dbId;
    private String posterPath;
    private boolean adult;
    private String overview;
    private String releaseDate;
    private int[] genreIds;
    private int id;
    private String originalTitle;
    private String originalLanguage;
    private String title;
    private String backdropPath;
    private float popularity;
    private int voteCount;
    private boolean video;
    private float voteAverage;
    private byte[] poster = new byte[0];

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        if (poster == null)
            poster = new byte[0];

        dest.writeLong(dbId);
        dest.writeString(posterPath);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeIntArray(genreIds);
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeFloat(popularity);
        dest.writeInt(voteCount);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeFloat(voteAverage);
        dest.writeInt(poster.length);
        dest.writeByteArray(poster);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>()
    {
        @Override
        public Movie createFromParcel(Parcel in)
        {
            Movie.Builder builder = builder()
                    .dbId(in.readLong())
                    .posterPath(in.readString())
                    .adult(in.readByte() != 0)
                    .overview(in.readString())
                    .releaseDate(in.readString())
                    .genreIds(in.createIntArray())
                    .id(in.readInt())
                    .originalTitle(in.readString())
                    .originalLanguage(in.readString())
                    .title(in.readString())
                    .backdropPath(in.readString())
                    .popularity(in.readFloat())
                    .voteCount(in.readInt())
                    .video(in.readByte() != 0)
                    .voteAverage(in.readFloat());

            byte[] poster = new byte[in.readInt()];
            in.readByteArray(poster);
            builder.poster(poster);

            return builder.build();
        }

        @Override
        public Movie[] newArray(int size)
        {
            return new Movie[size];
        }
    };

    public Date getReleaseDateAsDate()
    {
        try
        {
            return RELEASE_DATE_FORMATER.parse(this.releaseDate);
        }
        catch (ParseException e)
        {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }
}
