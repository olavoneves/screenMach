package br.com.screenMach.main;

import br.com.screenMach.model.DadosSerie;
import br.com.screenMach.model.DadosTemporadas;
import br.com.screenMach.model.Episodio;
import br.com.screenMach.model.Serie;
import br.com.screenMach.repository.SerieRepository;
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
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private List<Serie> listaSerie = new ArrayList<>();

    private SerieRepository repository;
    public Main(SerieRepository repository) {
        this.repository = repository;
    }

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
        // dadosSeries.add(dadosSerie);
        Serie serie = new Serie(dadosSerie);
        repository.save(serie);
        System.out.println(dadosSerie);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie = scanner.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        return converteDados.obterDados(json, DadosSerie.class);
    }

    private void buscarEpisodioPorSerie() {
        listaSeriesBuscadas();
        System.out.print("Escolha uma série pelo nome: ");
        var nomeSerie = scanner.nextLine();

        Optional<Serie> firstSerie = listaSerie.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
                .findFirst();

        if (firstSerie.isPresent()) {
            var serieEncontrada = firstSerie.get();
            List<DadosTemporadas> listaTemporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                try {
                    var json = consumoAPI.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                    DadosTemporadas dadosTemporadas = converteDados.obterDados(json, DadosTemporadas.class);
                    listaTemporadas.add(dadosTemporadas);
                } catch (Exception e) {
                    System.err.println("Erro ao processar temporada " + i + ": " + e.getMessage());
                }
            }
            listaTemporadas.forEach(System.out::println);

            List<Episodio> episodios = listaTemporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.number(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setListaEpisodio(episodios);
            repository.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void listaSeriesBuscadas() {
        listaSerie = repository.findAll();
        listaSerie.stream()
                .sorted(Comparator.comparing(Serie::getGenero, Comparator.nullsFirst(Comparator.naturalOrder())))
                .forEach(System.out::println);
    }
}
