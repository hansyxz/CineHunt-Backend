package br.com.cinehunt.models;

import br.com.cinehunt.util.EntertainmentData;

import java.util.Collections;

public class Series extends Entertainment{
    private final int numberOfSeasons;
    private final int numberOfEpisodes;
    private final int averageEpisodesPerSeason;

    public Series(EntertainmentData data) {
        this.numberOfSeasons = !data.number_of_seasons().isEmpty() ? Integer.parseInt(data.number_of_seasons()) : 0;
        this.numberOfEpisodes = !data.number_of_episodes().isEmpty() ? Integer.parseInt(data.number_of_episodes()) : 0;
        this.averageEpisodesPerSeason = !(numberOfSeasons == 0) ? (int) Math.round((double) numberOfEpisodes / numberOfSeasons) : 0;
        this.setName(data.name() != null && !data.name().isEmpty() ? data.name() : "Sem informação!");
        this.setGenre(data.genres() != null && !data.genres().isEmpty() ? data.genres() : Collections.singletonList("Sem informação!"));
        this.setReleaseDate(data.release_date() != null && !data.release_date().isEmpty() ? data.release_date() : "Sem informação!");
        this.setSynopsis(data.overview() != null && !data.overview().isEmpty() ? data.overview() : "Sem informação!");
        this.setCoverImage("https://image.tmdb.org/t/p/w500" + data.poster_path());
        this.setRating(data.vote_average() != null &&  !data.vote_average().isEmpty() ? Double.parseDouble(data.vote_average()) : 0.0);
        this.setType(data.type());
    }

    @Override
    public String toString() {
        return """
                Nome: %s
                Genero: %s
                Ano de lançamento: %s
                Quantidade de episdios: %d
                Quantidade de temporadas: %d
                Média de episodios por temporada: %d
                Sinopse: %s
                Capa: %s
                Avaliação: %f
                Tipo: %s
                """.formatted(this.getName(), this.getGenre(), this.getReleaseDate(), this.numberOfEpisodes, this.numberOfSeasons, this.averageEpisodesPerSeason, this.getSynopsis(), this.getCoverImage(), this.getRating(), this.getType());
    }
}
