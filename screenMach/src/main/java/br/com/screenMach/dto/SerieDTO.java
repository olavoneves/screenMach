package br.com.screenMach.dto;

import br.com.screenMach.model.Categoria;

public record SerieDTO(Long id,
                       String titulo,
                       String lancamento,
                       Integer totalTemporadas,
                       Double avaliacao,
                       Categoria genero,
                       String atores,
                       String imagem,
                       String sinopse) { }
