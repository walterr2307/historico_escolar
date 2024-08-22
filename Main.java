import java.io.FileWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Declaração das variáveis necessárias
        int qtd_disciplinas, qtd_alunos;
        String gabarito, disciplinas[], nomes_alunos[];
        FileWriter arq_disciplina, arq_gabarito;
        Arquivos arquivos = new Arquivos(); // Instância da classe Arquivos para manipulação de arquivos
        Funcoes funcoes = new Funcoes(); // Instância da classe Funcoes para operações auxiliares
        Scanner scanner = new Scanner(System.in); // Scanner para leitura de entradas do usuário

        // Loop para garantir que o usuário digite um número inteiro para a quantidade
        // de disciplinas
        while (true) {
            try {
                System.out.print("Digite a quantidade de disciplinas: ");
                qtd_disciplinas = scanner.nextInt();
                break; // Sai do loop se a entrada for válida
            } catch (InputMismatchException e) {
                System.out.println("Por favor, digite um número inteiro válido.");
                scanner.nextLine(); // Limpa o buffer de entrada
            }
        }

        // Loop para garantir que o usuário digite um número inteiro para a quantidade
        // de alunos
        while (true) {
            try {
                System.out.print("Digite a quantidade de alunos: ");
                qtd_alunos = scanner.nextInt();
                break; // Sai do loop se a entrada for válida
            } catch (InputMismatchException e) {
                System.out.println("Por favor, digite um número inteiro válido.");
                scanner.nextLine(); // Limpa o buffer de entrada
            }
        }

        // Inicializa o array de disciplinas e obtém os nomes dos alunos
        disciplinas = new String[qtd_disciplinas];
        nomes_alunos = funcoes.getNomes(qtd_alunos, scanner);

        // Apaga o arquivo de gabaritos anterior (se existir) e abre um novo para
        // escrita
        arquivos.apagarArquivo("GABARITOS.txt");
        arq_gabarito = arquivos.getArquivoGabarito();

        // Loop para cada disciplina
        for (int i = 0; i < qtd_disciplinas; i++) {
            // Solicita o nome da disciplina e o armazena em letras maiúsculas
            System.out.printf("\nColoque o nome da %d* disciplina: ", i + 1);
            disciplinas[i] = scanner.nextLine().trim().toUpperCase();

            // Obtém o gabarito da disciplina
            gabarito = funcoes.getGabarito(scanner);

            // Apaga o arquivo de respostas da disciplina anterior (se existir) e abre um
            // novo para escrita
            arquivos.apagarArquivo(disciplinas[i] + ".txt");
            arq_disciplina = arquivos.getArquivoDisciplina(disciplinas[i]);

            // Registra o gabarito no arquivo geral de gabaritos
            arquivos.registrarGabarito(arq_gabarito, gabarito, disciplinas[i]);

            // Loop para cada aluno
            for (int j = 0; j < qtd_alunos; j++) {
                // Registra a resposta do aluno para a disciplina no arquivo da disciplina
                arquivos.registrarResposta(arq_disciplina, nomes_alunos[j], scanner);
            }

            // Fecha o arquivo da disciplina e imprime seu conteúdo
            arquivos.fecharArquivo(arq_disciplina);
            arquivos.imprimirArquivo(disciplinas[i] + ".txt");
        }

        // Fecha o arquivo de gabaritos e imprime seu conteúdo
        arquivos.fecharArquivo(arq_gabarito);
        arquivos.imprimirArquivo("GABARITOS.txt");

        // Chama o método para exibir o menu de disciplinas e realizar operações com os
        // dados coletados
        funcoes.mostrarMenuDisciplinas(disciplinas, qtd_disciplinas, qtd_alunos, scanner, arquivos);

        // Fecha o scanner para liberar recursos
        scanner.close();
    }
}
