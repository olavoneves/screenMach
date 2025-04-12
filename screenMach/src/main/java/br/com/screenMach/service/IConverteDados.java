package br.com.screenMach.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
