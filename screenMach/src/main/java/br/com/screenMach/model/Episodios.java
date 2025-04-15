package br.com.screenMach.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodios {
    private String titulo;
    private Integer temporada;
    private Double avaliacao;
    private LocalDate dataLancamento;

    public Episodios(Integer numberTemporada, DadosEpisodios dadosEpisodio) {
        this.temporada = numberTemporada;
        this.titulo = dadosEpisodio.titulo();
        try {
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
        } catch (NumberFormatException e) {
            this.avaliacao = 0.0;
        }
        try {
            this.dataLancamento = LocalDate.parse(dadosEpisodio.dataLancamento());
        } catch (DateTimeParseException e) {
            this.dataLancamento = null;
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public Episodios setTitulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public Episodios setTemporada(Integer temporada) {
        this.temporada = temporada;
        return this;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public Episodios setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
        return this;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public Episodios setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
        return this;
    }

    @Override
    public String toString() {
        return "Titulo: " + titulo + " | Temporada: " + temporada + " | Avaliação: " + avaliacao + " | Data de lançamento: " + dataLancamento;
    }
}
