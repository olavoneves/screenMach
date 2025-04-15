package br.com.screenMach.main;

import br.com.screenMach.model.DadosEpisodios;
import br.com.screenMach.model.DadosSerie;
import br.com.screenMach.model.DadosTemporadas;
import br.com.screenMach.model.Episodios;
import br.com.screenMach.service.ConsumoAPI;
import br.com.screenMach.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados converteDados = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=2d6585";


    public void exibeMenu() {
        String confere = "S";
        while (confere.equals("S")) {
            try {
                System.out.print("Digite o nome da série para busca: ");
                var nomeSerie = scanner.nextLine();

                var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
                DadosSerie dadosSerie = converteDados.obterDados(json, DadosSerie.class);
                System.out.println(dadosSerie);

                List<DadosTemporadas> listaTemporadas = new ArrayList<>();

                for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
                    try {
                        json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
                        DadosTemporadas dadosTemporadas = converteDados.obterDados(json, DadosTemporadas.class);
                        listaTemporadas.add(dadosTemporadas);
                    } catch (Exception e) {
                        System.err.println("Erro ao processar temporada " + i + ": " + e.getMessage());
                    }
                }
                listaTemporadas.forEach(System.out::println);

                listaTemporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

                List<DadosEpisodios> listaEpisodios = listaTemporadas.stream()
                                .flatMap(t -> t.episodios().stream())
                                        .collect(Collectors.toList());

                System.out.println("\n******************* \n\nTop 05 Episodios de " + nomeSerie + "\n");
                listaEpisodios.stream()
                        .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                            .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
                                .limit(5)
                                    .forEach(System.out::println);
                System.out.println("\n*******************");

                List<Episodios> episodios = listaTemporadas.stream()
                        .flatMap(t -> t.episodios().stream()
                                .map(d -> new Episodios(t.number(), d))).collect(Collectors.toList());

                episodios.forEach(System.out::println);

                System.out.println("Gostaria de digitar outra serie? [S/N]");
                confere = scanner.nextLine().toUpperCase();
                if (confere.equals("N")) {
                    System.out.println("Obrigado por interagir com a gente!");
                    System.out.println(nomeSerie + " é muito boa!");
                    break;
                }
            } catch (Exception e) {
                System.err.println("ERRO AO INTERAGIR COM O USUÁRIO!");
                System.out.println("Gostaria de digitar outra serie? [S/N]");
                confere = scanner.nextLine().toUpperCase();
                if (confere.equals("N")) {
                    System.out.println("Obrigado por interagir com a gente!");
                    break;
                }
            }
        }
    }
}
