package br.com.screenMach;

import br.com.screenMach.model.DadosEpisodios;
import br.com.screenMach.model.DadosSerie;
import br.com.screenMach.model.DadosTemporadas;
import br.com.screenMach.service.ConsumoAPI;
import br.com.screenMach.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenMachApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ScreenMachApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoAPI = new ConsumoAPI();

		ConverteDados converteDados = new ConverteDados();

		var serie = "game+of+thrones";
		var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=" + serie + "&apikey=2d6585");
		DadosSerie dadosSerie = converteDados.obterDados(json, DadosSerie.class);
		System.out.println(dadosSerie);

		var episode = serie + "&season=" + 1 + "&episode=" + 1;
		json = consumoAPI.obterDados("https://www.omdbapi.com/?t=" + episode + "&apikey=2d6585");
		DadosEpisodios dadosEpisodios = converteDados.obterDados(json, DadosEpisodios.class);
		System.out.println(dadosEpisodios);

		List<DadosTemporadas> listaTemporadas = new ArrayList<>();

		for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
			json = consumoAPI.obterDados("https://www.omdbapi.com/?t=game+of+thrones&season=" + i + "&apikey=2d6585");
			DadosTemporadas dadosTemporadas = converteDados.obterDados(json, DadosTemporadas.class);
			if (dadosTemporadas != null) {
				listaTemporadas.add(dadosTemporadas);
			}
		}
		listaTemporadas.forEach(System.out::println);

	}
}
