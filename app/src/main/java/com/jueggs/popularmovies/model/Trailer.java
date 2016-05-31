package com.jueggs.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder(builderClassName = "Builder")
public class Trailer implements Parcelable
{
    private String id;
    @SerializedName("iso_639_1")
    private String iso6391;
    @SerializedName("iso_3166_1")
    private String iso31661;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>()
    {
        @Override
        public Trailer createFromParcel(Parcel in)
        {
            return builder()
                    .id(in.readString())
                    .iso6391(in.readString())
                    .iso31661(in.readString())
                    .key(in.readString())
                    .name(in.readString())
                    .site(in.readString())
                    .size(in.readInt())
                    .type(in.readString()).build();
        }

        @Override
        public Trailer[] newArray(int size)
        {
            return new Trailer[size];
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
        dest.writeString(iso6391);
        dest.writeString(iso31661);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeInt(size);
        dest.writeString(type);
    }
}
