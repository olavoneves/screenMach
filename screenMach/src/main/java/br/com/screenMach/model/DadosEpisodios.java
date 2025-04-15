package br.com.screenMach.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodios(@JsonAlias("Title") String titulo,
                             @JsonAlias("Season") Integer temporada,
                             @JsonAlias("Released") String dataLancamento,
                             @JsonAlias("imdbRating") String avaliacao) {
}
