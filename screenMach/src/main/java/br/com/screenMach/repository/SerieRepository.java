package br.com.screenMach.repository;

import br.com.screenMach.model.Categoria;
import br.com.screenMach.model.Episodio;
import br.com.screenMach.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeDaSerie);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeSerie, double avaliacaoSerie);

    List<Serie> findTop10ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    @Query("select s from Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEAvaliacao(int totalTemporadas, double avaliacao);

    @Query("SELECT e FROM Serie s JOIN s.listaEpisodio e WHERE e.titulo ILIKE %:nomeEpisodio%")
    List<Episodio> episodiosPorNome(String nomeEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.listaEpisodio e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 10")
    List<Episodio> topEpisodios(Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.listaEpisodio e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);
}
