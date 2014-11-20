package com.kekwanu.androidstorageexample;

import java.io.Serializable;

/**
 * Created by onwuneme on 11/19/14.
 */
public class Car implements Serializable {
    private String make;
    private String model;
    private String year;

    private static final long serialVersionUID = 45L;

    public Car(String make, String model, String year){

        this.make = make;
        this.model = model;
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public String getModel(){
        return model;
    }

    public String getYear() {
        return year;
    }
}
