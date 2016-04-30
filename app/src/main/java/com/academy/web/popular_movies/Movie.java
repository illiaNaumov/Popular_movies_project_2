package com.academy.web.popular_movies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by user on 16.02.2016.
 */
public class Movie implements Parcelable{
    public String movieTitle;
    public String posterImageLink;
    public String plotSynopsis;
    public double userRating;
    public String releaseDate;
    public long movieID;

    public Movie() {
    }

    public Movie(long movieID, String movieTitle, String posterImageLink, String plotSynopsis, double userRating, String releaseDate) {
        this.movieID = movieID;
        this.movieTitle = movieTitle;
        this.posterImageLink = posterImageLink;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    protected Movie(Parcel in) {
        movieTitle = in.readString();
        posterImageLink = in.readString();
        plotSynopsis = in.readString();
        userRating = in.readDouble();
        releaseDate = in.readString();
        movieID = in.readLong();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieTitle);
        dest.writeString(posterImageLink);
        dest.writeString(plotSynopsis);
        dest.writeDouble(userRating);
        dest.writeString(releaseDate);
        dest.writeLong(movieID);
    }
}
