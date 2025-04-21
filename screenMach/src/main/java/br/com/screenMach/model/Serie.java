package br.com.screenMach.model;

import java.util.OptionalDouble;
import java.util.OptionalInt;

public class Serie {
    private String titulo;
    private Integer ano;
    private String lancamento;
    private Integer totalTemporadas;
    private Double avaliacao;
    private Categoria genero;
    private String atores;
    private String imagem;
    private String sinopse;

    public Serie(DadosSerie dadosSerie) {
        this.titulo = dadosSerie.titulo();
        this.ano = OptionalInt.of(Integer.parseInt(dadosSerie.ano())).orElse(0);
        this.lancamento = dadosSerie.lancamento();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.parseDouble(dadosSerie.avaliacao())).orElse(0);
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.atores = dadosSerie.atores();
        this.imagem = dadosSerie.imagem();
        this.sinopse = dadosSerie.sinopse();
    }

    public String getTitulo() {
        return titulo;
    }

    public Serie setTitulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public Integer getAno() {
        return ano;
    }

    public Serie setAno(Integer ano) {
        this.ano = ano;
        return this;
    }

    public String getLancamento() {
        return lancamento;
    }

    public Serie setLancamento(String lancamento) {
        this.lancamento = lancamento;
        return this;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public Serie setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
        return this;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public Serie setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
        return this;
    }

    public Categoria getGenero() {
        return genero;
    }

    public Serie setGenero(Categoria genero) {
        this.genero = genero;
        return this;
    }

    public String getAtores() {
        return atores;
    }

    public Serie setAtores(String atores) {
        this.atores = atores;
        return this;
    }

    public String getImagem() {
        return imagem;
    }

    public Serie setImagem(String imagem) {
        this.imagem = imagem;
        return this;
    }

    public String getSinopse() {
        return sinopse;
    }

    public Serie setSinopse(String sinopse) {
        this.sinopse = sinopse;
        return this;
    }

    @Override
    public String toString() {
        return  "Genero= " + genero  +
                ", Titulo= " + titulo  +
                ", Ano= " + ano + ", lancamento='" + lancamento  +
                ", Total Temporadas= " + totalTemporadas  +
                ", Avaliacao= " + avaliacao  +
                ", Atores= " + atores  +
                ", Imagem= " + imagem  +
                ", Sinopse= " + sinopse ;
    }
}
