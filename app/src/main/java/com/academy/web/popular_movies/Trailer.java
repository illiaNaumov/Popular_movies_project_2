package com.academy.web.popular_movies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ilyua on 30.04.2016.
 */
public class Trailer implements Parcelable {
    public String trailerName = "";
    public String key = "";

    public Trailer() {
    }

    protected Trailer(Parcel in) {
        trailerName = in.readString();
        key = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trailerName);
        dest.writeString(key);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
