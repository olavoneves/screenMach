package br.com.screenMach.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nomeDaSerie", unique = true)
    private String titulo;

    private String lancamento;
    private Integer totalTemporadas;
    private Double avaliacao;

    @Enumerated(EnumType.STRING)
    private Categoria genero;

    private String atores;
    private String imagem;
    private String sinopse;

    @OneToMany(mappedBy = "serie", cascade =CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> listaEpisodio = new ArrayList<>();

    public Serie() {

    }

    public Serie(DadosSerie dadosSerie) {
        this.titulo = dadosSerie.titulo();
        this.lancamento = dadosSerie.lancamento();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.parseDouble(dadosSerie.avaliacao())).orElse(0);
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.atores = dadosSerie.atores();
        this.imagem = dadosSerie.imagem();
        // this.sinopse = ConsultaChatAPI.obterTraducao(dadosSerie.sinopse()).trim();
    }

    public Long getId() {
        return id;
    }

    public Serie setId(Long id) {
        this.id = id;
        return this;
    }

    public List<Episodio> getListaEpisodio() {
        return listaEpisodio;
    }

    public Serie setListaEpisodio(List<Episodio> listaEpisodio) {
        listaEpisodio.forEach(e -> e.setSerie(this));
        this.listaEpisodio = listaEpisodio;
        return this;
    }

    public String getTitulo() {
        return titulo;
    }

    public Serie setTitulo(String titulo) {
        this.titulo = titulo;
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
        return
                "Genero= " + genero  +
                ", Titulo= " + titulo  +
                ", lancamento= " + lancamento  +
                ", Total Temporadas= " + totalTemporadas  +
                ", Avaliacao= " + avaliacao  +
                ", Atores= " + atores  +
                ", Imagem= " + imagem  +
                ", Sinopse= " + sinopse +
                ", Episodios= " + listaEpisodio;
    }
}
