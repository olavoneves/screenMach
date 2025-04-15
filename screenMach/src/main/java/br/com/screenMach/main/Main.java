package br.com.screenMach.main;

import br.com.screenMach.model.DadosEpisodios;
import br.com.screenMach.model.DadosSerie;
import br.com.screenMach.model.DadosTemporadas;
import br.com.screenMach.model.Episodios;
import br.com.screenMach.service.ConsumoAPI;
import br.com.screenMach.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

                // Adequando a url de busca
                var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

                // Converter os dados para os dados da serie
                DadosSerie dadosSerie = converteDados.obterDados(json, DadosSerie.class);
                System.out.println(dadosSerie);

                // Lista de temporadas
                List<DadosTemporadas> listaTemporadas = new ArrayList<>();

                // Incrementar o total de temporadas correto na url e adicionar os dados convertidos na lista
                for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
                    try {
                        json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
                        DadosTemporadas dadosTemporadas = converteDados.obterDados(json, DadosTemporadas.class);
                        listaTemporadas.add(dadosTemporadas);
                    } catch (Exception e) {
                        System.err.println("Erro ao processar temporada " + i + ": " + e.getMessage());
                    }
                }
                // Imprimir cada lista de temporadas separadas
                listaTemporadas.forEach(System.out::println);

                // Imprimir os episodios e os dados deles de cada temporada, em suas respectivas temporadas
                listaTemporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

                // Pegar a lista de todos episodios e retornar os nomes dos episodios
                List<DadosEpisodios> listaEpisodios = listaTemporadas.stream()
                                .flatMap(t -> t.episodios().stream())
                                        .collect(Collectors.toList());

                // Exibir os top xxx episodios determinados
                System.out.println("\n******************* \n\nTop 10 Episodios de " + nomeSerie + "\n");
                listaEpisodios.stream()
                        .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                            .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
                            .peek(e -> System.out.println("Ordenação: " + e))
                                .limit(10)
                                .peek(e -> System.out.println("Limite: " + e))
                                    .map(e -> e.titulo().toUpperCase())
                                    .peek(e -> System.out.println("Mapeamento: " + e))
                                        .forEach(System.out::println);
                System.out.println("\n*******************");

                // Pegar todas as temporadas e retornar separadamente os episodios e caracteristicas dele
                List<Episodios> episodios = listaTemporadas.stream()
                        .flatMap(t -> t.episodios().stream()
                            .map(d -> new Episodios(t.number(), d)))
                                .collect(Collectors.toList());
                episodios.forEach(System.out::println);

                System.out.print("Digite um trecho do titulo do episodio que está procurando: ");
                var trechoTitulo = scanner.nextLine();

                // Buscar o episodio que o usuario especificou
                Optional<Episodios> episodioBuscado = episodios.stream()
                        .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                            .findFirst();
                if (episodioBuscado.isPresent()) {
                    System.out.println("Episodio encontrado!");
                    System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
                } else {
                    System.out.println("Episodio não encontrado!");
                }

                System.out.print("A partir de que ano você deseja ver os episodios? ");
                var ano = scanner.nextInt();
                scanner.nextLine();

                // Formata data para modelo brasileiro e mostrar os episodios a partir da data que o usuario digitou
                LocalDate dataBusca = LocalDate.of(ano, 1, 1);
                DateTimeFormatter formataData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                episodios.stream()
                                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                                        .forEach(e -> System.out.println(
                                                "Episodio: " + e.getTitulo() + " | Temporada: " + e.getTemporada() + " | Data de lançamento: " + e.getDataLancamento().format(formataData)
                                        ));

                // Tratando estatísticas de avaliação de temporadas que não vem na OMDb
                Map<Integer, Double> avaliacoesTemporadas = episodios.stream()
                        .filter(e -> e.getAvaliacao() > 0.0)
                                .collect(Collectors.groupingBy(Episodios::getTemporada, Collectors.averagingDouble(Episodios::getAvaliacao)));
                System.out.println(avaliacoesTemporadas);

                //
                DoubleSummaryStatistics est = episodios.stream()
                        .filter(e -> e.getAvaliacao() > 0.0)
                                .collect(Collectors.summarizingDouble(Episodios::getAvaliacao));
                System.out.println("Quantidade: " + est.getCount() + " | Média: " + est.getAverage() + " | Pior episódio: " + est.getMin() + " | Melhor episódio: " + est.getMax());

                // Dar continuidade no programa
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
