package com.example.starsgallery.beans;

public class Star {
    private int id;
    private String name;
    private String img;
    private float rating;
    private String description;
    private String filmography;
    private static int counter = 0;

    public Star(String name, String img, float rating, String description, String filmography) {
        this.id = ++counter;
        this.name = name;
        this.img = img;
        this.rating = rating;
        this.description = description;
        this.filmography = filmography;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getImg() { return img; }
    public float getStar() { return rating; }
    public String getDescription() { return description; }
    public String getFilmography() { return filmography; }

    public void setName(String name) { this.name = name; }
    public void setImg(String img) { this.img = img; }
    public void setStar(float rating) { this.rating = rating; }
    public void setDescription(String description) { this.description = description; }
    public void setFilmography(String filmography) { this.filmography = filmography; }
}
