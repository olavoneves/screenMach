package br.com.screenMach;

import br.com.screenMach.model.DadosEpisodios;
import br.com.screenMach.model.DadosSerie;
import br.com.screenMach.service.ConsumoAPI;
import br.com.screenMach.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

		var temporada = 3;
		var numeroEpisodio = 6;
		var episode = serie + "&season=" + temporada + "&episode=" + numeroEpisodio;
		json = consumoAPI.obterDados("https://www.omdbapi.com/?t=" + episode + "&apikey=2d6585");
		DadosEpisodios dadosEpisodios = converteDados.obterDados(json, DadosEpisodios.class);
		System.out.println(dadosEpisodios);
	}
}
