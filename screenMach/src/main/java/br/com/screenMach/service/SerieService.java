package br.com.screenMach.service;

import br.com.screenMach.dto.SerieDTO;
import br.com.screenMach.model.Serie;
import br.com.screenMach.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return repository.buscarPorLancamentos("2024");
    }

    private List<SerieDTO> converteDadosSerie(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getLancamento(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getImagem(), s.getSinopse()))
                .collect(Collectors.toList());
    }
}
