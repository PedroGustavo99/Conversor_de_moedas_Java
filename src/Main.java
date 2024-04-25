import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {

    private static final String API_KEY = "6699e1cb713f49d863fd5454";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                //opções de moedas disponiveis
                System.out.println("Bem-vindo ao Conversor de Moeda!\n");
                System.out.println("Escolha a moeda de origem:");
                System.out.println("1. USD - Dólar Americano");
                System.out.println("2. EUR - Euro");
                System.out.println("3. GBP - Libra Esterlina");
                System.out.println("4. JPY - Iene Japonês");
                System.out.println("5. AUD - Dólar Australiano");
                System.out.println("6. BRL - Real Brasileiro");
                System.out.println("0. Sair");
                System.out.print("Digite o número da moeda de origem:");

                int opcao = scanner.nextInt();
                if (opcao == 0) {
                    System.out.println("Saindo...");
                    break;
                } else if (opcao <1 || opcao >6) {
                    System.out.println("Opção inválida ! Por favor, digite um número entre 1 e 6.\n");
                    continue;
                }

                String moedaOrigem = getMoeda(opcao);

                // Obtém a moeda de destino
                System.out.println("\nEscolha a moeda de destino:");
                System.out.println("1. USD - Dólar Americano");
                System.out.println("2. EUR - Euro");
                System.out.println("3. GBP - Libra Esterlina");
                System.out.println("4. JPY - Iene Japonês");
                System.out.println("5. AUD - Dólar Australiano");
                System.out.println("6. BRL - Real Brasileiro");
                System.out.println("0. Sair");
                System.out.print("Digite o número da moeda de destino: ");
                opcao = scanner.nextInt();
                if (opcao == 0) {
                    System.out.println("Saindo...");
                    break;
                }

                String moedaDestino = getMoeda(opcao);

                // Obtém a quantidade a ser convertida
                System.out.print("Digite a quantidade a ser convertida: ");
                double quantidade = scanner.nextDouble();

                // Obtém a taxa de câmbio usando a API
                double taxaDeCambio = getTaxaDeCambio(moedaOrigem, moedaDestino);

                // Calcula o valor convertido
                double valorConvertido = quantidade * taxaDeCambio;

                // Exibe o resultado
                System.out.println(quantidade + " " + moedaOrigem + " equivalem a " + valorConvertido + " " + moedaDestino + "\n");

                // Verifica se a pessoa deseja realizar uma nova consulta
                System.out.println("Deseja realizar uma nova consulta ?");
                char novaConsulta = scanner.next().charAt(0);

                if (novaConsulta == 's' || novaConsulta == 'S') {
                    continue;
                } else {
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String getMoeda(int opcao) {
        switch (opcao) {
            case 1:
                return "USD";
            case 2:
                return "EUR";
            case 3:
                return "GBP";
            case 4:
                return "JPY";
            case 5:
                return "AUD";
            case 6:
                return "BRL";
            default:
                throw new IllegalArgumentException("Opção inválida.");
        }
    }

    private static double getTaxaDeCambio(String moedaOrigem, String moedaDestino) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/" + moedaOrigem))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();

        // Extrair a taxa de câmbio do corpo da resposta
        String searchStr = "\"" + moedaDestino + "\":";
        int startIndex = responseBody.indexOf(searchStr) + searchStr.length();
        int endIndex = responseBody.indexOf(",", startIndex);
        String taxaStr = responseBody.substring(startIndex, endIndex);

        return Double.parseDouble(taxaStr);
    }
}
