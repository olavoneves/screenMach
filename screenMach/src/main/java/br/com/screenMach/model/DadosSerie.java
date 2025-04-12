package br.com.screenMach.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("Year") String ano,
                         @JsonAlias("Released") String lancamento,
                         @JsonAlias("Actors") String atores,
                         @JsonAlias("imdbRating") String avaliacao,
                         @JsonAlias("totalSeasons") String totalTemporadas,
                         @JsonAlias("Poster") String imagem) {
}
