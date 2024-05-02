import com.google.gson.Gson;

import java.io.IOException;
import java.lang.invoke.SwitchPoint;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final String API_KEY = "c271f2226329a895cf80168b";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean continuar = true;
        while (continuar) {
            System.out.println("""
            Seja bem-vindo/a ao conversor de moedas);
            1) Dólar =>> Peso argentino);
            2) Peso argentino =>> Dólar);
            3) Dólar =>> Real brasileiro);
            4) Real brasileiro =>> Dólar);
            5) Dólar =>> Peso colombiano);
            6) Peso colombiano =>> Dólar);
            7) Sair);
            \nEscolha uma opção válida:);
            ***********************************************
            """);

            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    converterMoeda("USD", "ARS");
                    break;
                case 2:
                    converterMoeda("ARS", "USD");
                    break;
                case 3:
                    converterMoeda("USD", "BRL");
                    break;
                case 4:
                    converterMoeda("BRL", "USD");
                    break;
                case 5:
                    converterMoeda("USD", "COP");
                    break;
                case 6:
                    converterMoeda("COP", "USD");
                    break;
                case 7:
                    continuar = false;
                    System.out.println("Adeus!");
                    break;
                default:
                    System.out.println("Opção inválida. Por favor, escolha uma opção de 1 a 7.");
            }
        }
    }

    private static void converterMoeda(String from, String to) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nDigite o valor que deseja converter:");
        double valueInput = scanner.nextDouble();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + from))
                .build();

        Gson gson = new Gson();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {

                ExchangeRateData data = gson.fromJson(response.body(), ExchangeRateData.class);
                double tax = data.getConversion_rates().get(to);
                double convertedValue = valueInput * tax;

                String responseResult = String.format("\n%.2f %s corresponde ao valor final de %.2f %s\n", valueInput, from, convertedValue, to);
                System.out.println(responseResult);
            } else {
                System.out.println("Erro ao obter dados da taxa de câmbio.");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao fazer request no client");
        }
    }

}