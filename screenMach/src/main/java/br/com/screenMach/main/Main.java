package br.com.screenMach.main;

import br.com.screenMach.model.*;
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
    private Optional<Serie> serieBuscada;

    private SerieRepository repository;
    public Main(SerieRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            try {
                var menu = "╔════════════════════════════╗\n║                            ║\n║     Bem-vindo ao           ║\n║        Screen Match        ║\n║                            ║\n║   > 1. Buscar Séries       ║\n║   > 2. Buscar Episódios    ║\n║   > 3. Listar Séries       ║\n║   > 4. Buscar por Título   ║\n║   > 5. Buscar por Atores   ║\n║   > 6. Top 10 séries       ║\n║   > 7. Buscar Categoria    ║\n║   > 8. Buscar Categoria    ║\n║   > 9. Episodio por nome   ║\n║   > 10.Top 10 episódios    ║\n║                            ║\n║   > 0. Sair                ║\n║                            ║\n╚════════════════════════════╝\n > ";

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
                    case 4:
                        buscarSeriePorTitulo();
                        break;
                    case 5:
                        buscarSeriePorAtores();
                        break;
                    case 6:
                        buscarTopDezSeries();
                        break;
                    case 7:
                        buscarSeriesPorCategoria();
                        break;
                    case 8:
                        buscarSeriesPorMaxTemporadas();
                        break;
                    case 9:
                        buscarEpisodioPorNome();
                        break;
                    case 10:
                        buscarTopDezEpisodios();
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

        Optional<Serie> firstSerie = repository.findByTituloContainingIgnoreCase(nomeSerie);

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

    private void buscarSeriePorTitulo() {
        System.out.print("Escolha uma série pelo nome: ");
        var nomeSerie = scanner.nextLine();
        serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("*****************************************" +
                    "\nDados da Série: " +
                    serieBuscada.get());
        } else {
            System.out.println("*****************************************" +
                    "\nSérie não encontrada!");
        }
    }

    private void buscarSeriePorAtores() {
        System.out.print("Escolha uma série pelo nome do ator: ");
        var nomeAtor = scanner.nextLine();

        System.out.print("Você gostaria das séries com avaliações maiores que 8.5 ? [S/N]: ");
        var avaliacaoSerie = scanner.nextLine().toUpperCase();

        if (avaliacaoSerie.equals("S")) {
            List<Serie> serieBuscada = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, 8.5);
            System.out.println("*****************************************\n");
            System.out.println("Melhores séries que " + nomeAtor + " trabalhou: ");
            serieBuscada.forEach(s -> System.out.println("Série: " + s.getTitulo() + ", avaliação: " + s.getAvaliacao()));
        } else {
            List<Serie> serieBuscada = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, 0.0);
            System.out.println("*****************************************\n");
            System.out.println("Séries que " + nomeAtor + " trabalhou: ");
            serieBuscada.forEach(s -> System.out.println("Série: " + s.getTitulo() + ", avaliação: " + s.getAvaliacao()));
        }
    }

    private void buscarTopDezSeries() {
        List<Serie> topSeries = repository.findTop10ByOrderByAvaliacaoDesc();
        System.out.println("*****************************************");
        System.out.println("*             Top 10 Séries             *");
        System.out.println("*****************************************");
        topSeries.forEach(s -> System.out.println("Série: " + s.getTitulo() + ", avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriesPorCategoria() {
        System.out.print("Deseja buscar séries a partir de qual categoria/gênero: ");
        var categoriaSerie = scanner.nextLine();
        Categoria categoria = Categoria.fromPortugues(categoriaSerie);
        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);
        System.out.println("*****************************************");
        System.out.println("Séries da categoria: " + categoriaSerie);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void buscarSeriesPorMaxTemporadas() {
        System.out.print("Digite a quantidade maxima de temporadas que você gostaria de assistir: ");
        var totalTemporadas = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Você gostaria das séries com avaliações maiores que 8.5 ? [S/N]: ");
        var avaliacaoSerie = scanner.nextLine().toUpperCase();

        if (avaliacaoSerie.equals("S")) {
            List<Serie> seriesMaxTemporadas = repository.seriesPorTemporadaEAvaliacao(totalTemporadas, 8.5);
            System.out.println("*********************************************************************\n");
            System.out.println("Séries encontradas com " + totalTemporadas + " ou menos temporadas.");
            seriesMaxTemporadas.forEach(s -> System.out.println("Série: " + s.getTitulo() + ", temporadas: " + s.getTotalTemporadas() + ", avaliação: " + s.getAvaliacao() + ", gênero: " + s.getGenero()));
        } else {
            List<Serie> seriesMaxTemporadas = repository.seriesPorTemporadaEAvaliacao(totalTemporadas, 0.0);
            System.out.println("*********************************************************************\n");
            System.out.println("Séries encontradas com " + totalTemporadas + " ou menos temporadas.");
            seriesMaxTemporadas.forEach(s -> System.out.println("Série: " + s.getTitulo() + ", temporadas: " + s.getTotalTemporadas() + ", avaliação: " + s.getAvaliacao() + ", gênero: " + s.getGenero()));
        }
    }

    private void buscarEpisodioPorNome() {
        System.out.print("Digite um trecho do nome do episodio: ");
        var nomeEpisodio = scanner.nextLine();

        List<Episodio> listaEpisodiosBuscados = repository.episodiosPorNome(nomeEpisodio);
        listaEpisodiosBuscados.forEach(e ->
                System.out.printf("Série: %s Temporada: %s, Episódio: %s\n", e.getSerie().getTitulo(), e.getTemporada(), e.getTitulo()));
    }

    private void buscarTopDezEpisodios() {
        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repository.topEpisodios(serie);

            System.out.println("*****************************************");
            System.out.println("*            Top 10 Episódios           *");
            System.out.println("*****************************************");
            topEpisodios.forEach(e ->
                    System.out.printf("Série: %s Temporada: %s, Episódio: %s, Avaliação: %s\n", e.getSerie().getTitulo(), e.getTemporada(), e.getTitulo(), e.getAvaliacao()));
        }
    }
}
