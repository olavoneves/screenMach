package br.com.screenMach.service;

import br.com.screenMach.dto.EpisodioDTO;
import br.com.screenMach.dto.SerieDTO;
import br.com.screenMach.model.Categoria;
import br.com.screenMach.model.Serie;
import br.com.screenMach.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> obterSeries() {
        return converteDadosSerie(repository.findAll());
    }

    public List<SerieDTO> obterTop10() {
        return converteDadosSerie(repository.findTop10ByOrderByAvaliacaoDesc());
    }

    public List<SerieDTO> obterLancamentos() {
        return converteDadosSerie(repository.buscarListaEpisodioMaisRecente());
    }

    private List<SerieDTO> converteDadosSerie(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getLancamento(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getImagem(), s.getSinopse()))
                .collect(Collectors.toList());
    }

    public SerieDTO obterSerie(Long id) {
        Optional<Serie> serieBuscada = repository.findById(id);

        if (serieBuscada.isPresent()) {
            Serie s = serieBuscada.get();
            return new SerieDTO(s.getId(), s.getTitulo(), s.getLancamento(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getImagem(), s.getSinopse());
        }
        return null;
    }

    public List<EpisodioDTO> obterTotalTemporadas(Long id) {
        Optional<Serie> serieBuscada = repository.findById(id);

        if (serieBuscada.isPresent()) {
            Serie s = serieBuscada.get();
            return s.getListaEpisodio().stream()
                    .map(e -> new EpisodioDTO(e.getTitulo(), e.getTemporada(), e.getAvaliacao()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDTO> obterTemporada(Long id, int temporada) {
        return repository.buscarTemporada(id, temporada).stream()
                .map(e -> new EpisodioDTO(e.getTitulo(), e.getTemporada(), e.getAvaliacao()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterSeriesPorCategoria(String genero) {
        Categoria categoria = Categoria.fromPortugues(genero);
        return converteDadosSerie(repository.findByGenero(categoria));
    }
}
