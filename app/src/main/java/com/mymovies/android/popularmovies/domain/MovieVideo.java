package com.mymovies.android.popularmovies.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.mymovies.android.popularmovies.utils.StringConstants;

/**
 * Created by makrandsumant on 15/01/18.
 */

public class MovieVideo implements Parcelable {
    private int movieId;
    private String key;
    private String site;
    private String type;
    private String name;

    public MovieVideo() {

    }
    protected MovieVideo(Parcel in) {
        movieId = in.readInt();
        key = in.readString();
        site = in.readString();
        type = in.readString();
        name = in.readString();
    }

    public static final Creator<MovieVideo> CREATOR = new Creator<MovieVideo>() {
        @Override
        public MovieVideo createFromParcel(Parcel in) {
            return new MovieVideo(in);
        }

        @Override
        public MovieVideo[] newArray(int size) {
            return new MovieVideo[size];
        }
    };

    @Override
    public String toString() {
        return "MovieVideo{" +
                "movieId=" + movieId +
                ", key='" + key + '\'' +
                ", site='" + site + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(movieId);
        parcel.writeString(key);
        parcel.writeString(site);
        parcel.writeString(type);
        parcel.writeString(name);
    }
}
