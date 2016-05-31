package com.jueggs.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder(builderClassName = "Builder")
public class Review implements Parcelable
{
    private String id;
    private String author;
    private String content;
    private String url;

    public static final Creator<Review> CREATOR = new Creator<Review>()
    {
        @Override
        public Review createFromParcel(Parcel in)
        {
            return builder()
                    .id(in.readString())
                    .author(in.readString())
                    .content(in.readString())
                    .url(in.readString()).build();
        }

        @Override
        public Review[] newArray(int size)
        {
            return new Review[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }
}
