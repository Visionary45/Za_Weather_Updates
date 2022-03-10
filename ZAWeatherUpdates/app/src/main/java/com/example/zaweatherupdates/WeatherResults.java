package com.example.zaweatherupdates;

import com.google.gson.annotations.SerializedName;

public class WeatherResults {
    private String Day;
    private double Temp;
    private String Icon;
    private String Description;

    public WeatherResults( String day, double temp, String icon, String description) {
        this.Day = day;
        this.Temp = temp;
        this.Icon = icon;
        this.Description = description;
    }



    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public double getTemp() {
        return Temp;
    }

    public void setTemp(double temp) {
        Temp = temp;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
