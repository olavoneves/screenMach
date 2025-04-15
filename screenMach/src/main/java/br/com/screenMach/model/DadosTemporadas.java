package br.com.screenMach.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosTemporadas(@JsonAlias("Title") String titulo,
                              @JsonAlias("Season") Integer number,
                              @JsonAlias("totalSeasons") Integer totalTemporadas,
                              @JsonAlias("Episodes") List<DadosEpisodios> episodios) {
}
