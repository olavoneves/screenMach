package br.com.screenMach.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodios")
public class Episodio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private Integer temporada;
    private Double avaliacao;
    private LocalDate dataLancamento;
    @ManyToOne
    private Serie serie;

    public Episodio() {
    }

    public Episodio(Integer numberTemporada, DadosEpisodios dadosEpisodio) {
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

    public Long getId() {
        return id;
    }

    public Episodio setId(Long id) {
        this.id = id;
        return this;
    }

    public Serie getSerie() {
        return serie;
    }

    public Episodio setSerie(Serie serie) {
        this.serie = serie;
        return this;
    }

    public String getTitulo() {
        return titulo;
    }

    public Episodio setTitulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public Episodio setTemporada(Integer temporada) {
        this.temporada = temporada;
        return this;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public Episodio setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
        return this;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public Episodio setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
        return this;
    }

    @Override
    public String toString() {
        return "Episodio: " + titulo + " | Temporada: " + temporada + " | Avaliação: " + avaliacao + " | Data de lançamento: " + dataLancamento;
    }
}
