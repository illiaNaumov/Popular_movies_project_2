package com.academy.web.popular_movies;

import android.os.Parcel;
import android.os.Parcelable;

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
        movieID = in.readLong();
        movieTitle = in.readString();
        posterImageLink = in.readString();
        plotSynopsis = in.readString();
        userRating = in.readDouble();
        releaseDate = in.readString();
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(movieID);
        parcel.writeString(movieTitle);
        parcel.writeString(posterImageLink);
        parcel.writeString(plotSynopsis);
        parcel.writeDouble(userRating);
        parcel.writeString(releaseDate);
    }
}
