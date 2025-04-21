package br.com.screenMach.main;

import br.com.screenMach.model.DadosSerie;
import br.com.screenMach.model.DadosTemporadas;
import br.com.screenMach.model.Serie;
import br.com.screenMach.service.ConsumoAPI;
import br.com.screenMach.service.ConverteDados;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados converteDados = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=2d6585";
    private List<DadosSerie> listaDadosSerie = new ArrayList<>();


    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            try {
                var menu = "╔════════════════════════════╗\n║                            ║\n║     Bem-vindo ao           ║\n║        Screen Match        ║\n║                            ║\n║   > 1. Buscar Séries       ║\n║   > 2. Buscar Episódios    ║\n║   > 3. Listar Séries       ║\n║                            ║\n║   > 0. Sair                ║\n║                            ║\n╚════════════════════════════╝\n > ";

                System.out.print(menu);
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        buscarSerie();
                        break;
                    case 2:
                        buscarEpisodioPorSerie();
                        break;
                    case 3:
                        listaSeriesBuscadas();
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção Inválida");
                        break;
                }

            } catch (Exception e) {
                System.err.println("ERRO AO INTERAGIR COM O USUÁRIO!");
                break;
            }
        }
    }

    private void buscarSerie() {
        DadosSerie dadosSerie = getDadosSerie();
        listaDadosSerie.add(dadosSerie);
        System.out.println(dadosSerie);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie = scanner.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dadosSerie = converteDados.obterDados(json, DadosSerie.class);
        return dadosSerie;
    }

    private void buscarEpisodioPorSerie() {
        DadosSerie dadosSerie = getDadosSerie();
        List<DadosTemporadas> listaTemporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            try {
                var json = consumoAPI.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporadas dadosTemporadas = converteDados.obterDados(json, DadosTemporadas.class);
                listaTemporadas.add(dadosTemporadas);
            } catch (Exception e) {
                System.err.println("Erro ao processar temporada " + i + ": " + e.getMessage());
            }
        }
        listaTemporadas.forEach(System.out::println);
    }

    private void listaSeriesBuscadas() {
        List<Serie> listaSerie = new ArrayList<>();
        listaSerie = listaDadosSerie.stream()
                        .map(d -> new Serie(d))
                                .collect(Collectors.toList());
        listaSerie.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}
