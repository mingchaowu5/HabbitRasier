package com.example.liu.habbitrasier;

public class Pet {
    private String petName;
    private String healthy;
    private String food;


    public Pet(String healthy, String food) {
        this.petName = "pet";
        this.healthy = healthy;
        this.food = food;
    }

    public Pet(String name, String healthy, String food) {
        this.petName = name;
        this.healthy = healthy;
        this.food = food;
    }

    public String getPetName() {
        return petName;
    }

    public void setName(String name) {
        this.petName = name;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getHealthy() {
        return healthy;
    }

    public void setHealthy(String healthy) {
        this.healthy = healthy;
    }
}
