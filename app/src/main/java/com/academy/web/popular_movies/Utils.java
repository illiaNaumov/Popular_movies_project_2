package com.academy.web.popular_movies;

/**
 * Created by ilyua on 25.04.2016.
 */
public class Utils {

    public static String parsePopularity(String popularity){
        return popularity.substring(0, popularity.indexOf('/'));
    }
}
