package com.example.finalprojectbase;

public class Car {
    private String make;
    private String model;
    private int year;
    private String color;
    private boolean used;

    public Car(){};

    public Car(String make, String model, int year, String color, boolean used){
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.used = used;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    @Override
    public String toString() {
        return "This is a " + color + " " + year + " " + make +  " " + model + " " + used;
    }
}
