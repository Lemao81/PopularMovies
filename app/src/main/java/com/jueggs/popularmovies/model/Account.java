package com.jueggs.popularmovies.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Account
{
    private Avatar avatar;
    private Integer id;
    @SerializedName("iso_639_1")
    private String iso6391;
    @SerializedName("iso_3166_1")
    private String iso31661;
    private Object name;
    private Boolean includeAdult;
    private String username;

    @Data
    public static class Avatar
    {
        private Gravatar gravatar;

        @Data
        public static class Gravatar
        {
            private String hash;
        }
    }
}
