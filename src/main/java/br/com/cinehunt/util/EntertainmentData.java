package br.com.cinehunt.util;

import java.util.List;

public record EntertainmentData(String title, String name, List<String> genres, String release_date, String overview, String type, String runtime, String number_of_episodes, String number_of_seasons, String poster_path, String vote_average) {
}
