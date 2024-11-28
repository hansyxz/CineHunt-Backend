package br.com.cinehunt.models;

import com.google.gson.JsonObject;

import java.util.Comparator;
import java.util.List;

public class Entertainment implements Comparator<JsonObject> {
    private String name;
    private List<String> genre;
    private String releaseDate;
    private String synopsis;
    private double rating;
    private String type;
    private String coverImage;
    private boolean favorite;

    public void setName(String name) {
        this.name = name;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public List<String> getGenre() {
        return genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public double getRating() {
        return rating;
    }

    public String getType() {
        return type;
    }

    @Override
    public int compare(JsonObject media, JsonObject otherMedia) {
        return otherMedia.get("release_year").getAsInt() - media.get("release_year").getAsInt();
    }
}
