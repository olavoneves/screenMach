package br.com.screenMach.controller;

import br.com.screenMach.dto.SerieDTO;
import br.com.screenMach.dto.EpisodioDTO;
import br.com.screenMach.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService service;

    @GetMapping
    public List<SerieDTO> obterSeries() {
        return service.obterSeries();
    }

    @GetMapping("/top10")
    public List<SerieDTO> obterTop10() {
        return service.obterTop10();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> obterLancamentos() {
        return service.obterLancamentos();
    }

    @GetMapping("/{id}")
    public SerieDTO obterSerie(@PathVariable Long id) {
        return service.obterSerie(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTotalTemporadas(@PathVariable Long id){
        return service.obterTotalTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{temporada}")
    public List<EpisodioDTO> obterTemporada(@PathVariable Long id, @PathVariable int temporada) {
        return service.obterTemporada(id, temporada);
    }

    @GetMapping("/categoria/{genero}")
    public List<SerieDTO> obterSeriesPorCategoria(@PathVariable String genero) {
        return service.obterSeriesPorCategoria(genero);
    }
}
