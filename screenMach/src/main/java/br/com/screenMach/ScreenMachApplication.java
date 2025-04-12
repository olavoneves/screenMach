package br.com.screenMach;

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

		var serie = "game+of+thrones";
		var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=" + serie + "&apikey=2d6585");
		System.out.println(json);

		ConverteDados converteDados = new ConverteDados();

		DadosSerie dadosSerie = converteDados.obterDados(json, DadosSerie.class);
		System.out.println(dadosSerie);
	}
}
