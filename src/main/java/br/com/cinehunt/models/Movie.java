package br.com.cinehunt.models;

import br.com.cinehunt.util.EntertainmentData;

import java.util.Collections;

public class Movie extends Entertainment{
    private final int durationInMinutes;

    public Movie(EntertainmentData data) {
        this.durationInMinutes = !data.runtime().isEmpty() ? Integer.parseInt(data.runtime()) : 0;
        this.setName(data.title() != null && !data.title().isEmpty() ? data.title() : "Sem informação!");
        this.setGenre(data.genres() != null && !data.genres().isEmpty() ? data.genres() : Collections.singletonList("Sem informação!"));
        this.setReleaseDate(data.release_date() != null && !data.release_date().isEmpty() ? data.release_date() : "Sem informação!");
        this.setSynopsis(data.overview() != null && !data.overview().isEmpty() ? data.overview() : "Sem informação!");
        this.setCoverImage(data.poster_path() != null &&  !data.poster_path().isEmpty() ? "https://image.tmdb.org/t/p/w500" + data.poster_path() : "Sem informação!");
        this.setRating(data.vote_average() != null &&  !data.vote_average().isEmpty() ? Double.parseDouble(data.vote_average()) : 0.0);
        this.setType(data.type());
    }

    @Override
    public String toString() {
        return """
                Nome: %s
                Genero: %s
                Duração: %d min
                Ano de lançamento: %s
                Sinopse: %s
                Capa: %s
                Avaliação: %f
                Tipo: %s
                """.formatted(this.getName(), this.getGenre(), this.durationInMinutes, this.getReleaseDate(), this.getSynopsis(),  this.getCoverImage(), this.getRating(), this.getType());
    }
}
