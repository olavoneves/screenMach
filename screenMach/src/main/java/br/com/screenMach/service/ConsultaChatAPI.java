package br.com.screenMach.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import java.util.List;

public class ConsultaChatAPI {
    public static String obterTraducao(String texto) {

        OpenAiService service = new OpenAiService(System.getenv("OPENAI KEY"));

        ChatMessage mensagem = new ChatMessage("olavoneves", "Traduza para português: " + texto);


        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(mensagem))
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        String resposta = service.createChatCompletion(request)
                .getChoices().get(0)  // Pega a primeira resposta
                .getMessage().getContent();

        return resposta.trim();
    }
}
