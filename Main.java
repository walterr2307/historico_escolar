import java.util.Scanner;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        int i, j, num_disciplinas, qtd_alunos, qtd_respostas_v, qtd_respostas_f;
        boolean zerou[];
        char letras[];
        String disciplinas[], respostas = null, nome_alunos[], formato;
        FileWriter arq_disciplina = null;

        try (Scanner scanner = new Scanner(System.in)) {

            // Solicita ao usuário o número de disciplinas e lê a entrada como um inteiro
            System.out.print("Ponha o numero de disciplinas: ");
            num_disciplinas = scanner.nextInt();

            // Solicita ao usuário a quantidade de alunos e lê a entrada como um inteiro
            System.out.print("Insira a quantidade de alunos: ");
            qtd_alunos = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer do teclado para evitar problemas na leitura posterior

            disciplinas = new String[num_disciplinas];
            nome_alunos = new String[qtd_alunos];

            System.out.println();

            // Lê e armazena o nome de cada aluno
            for (i = 0; i < qtd_alunos; i++) {
                System.out.printf("Coloque o nome do %d* aluno: ", i + 1);
                nome_alunos[i] = scanner.nextLine();
                nome_alunos[i] = nome_alunos[i].toUpperCase();
            }

            // Solicita ao usuário o nome de cada disciplina e processa as respostas dos
            // alunos
            for (i = 0; i < num_disciplinas; i++) {
                System.out.printf("\nDigite o nome da %d* disciplina: ", i + 1);
                disciplinas[i] = scanner.nextLine(); // Lê o nome da disciplina
                disciplinas[i] = disciplinas[i].toUpperCase();

                try {
                    // Tenta criar e abrir o arquivo para escrita com o nome da disciplina
                    arq_disciplina = new FileWriter(new File(disciplinas[i] + ".txt"));

                    // Inicializa o array de booleanos para rastrear se um aluno "zerou" o teste
                    zerou = new boolean[qtd_alunos];

                    System.out.printf("\n==============================\n%s\n\n", disciplinas[i]);

                    j = 0;

                    // Loop para processar as respostas de cada aluno
                    while (j < qtd_alunos) {
                        zerou[j] = false; // Define que o aluno não zerou o teste por padrão
                        qtd_respostas_v = 0; // Inicializa o contador de respostas 'V'
                        qtd_respostas_f = 0; // Inicializa o contador de respostas 'F'

                        try {
                            // Solicita e lê a resposta do aluno
                            System.out.printf("Digite a resposta de %s: ", nome_alunos[j]);
                            respostas = scanner.nextLine();

                            // Converte a resposta em letras maiúsculas e a transforma em um array de
                            // caracteres
                            respostas = respostas.trim().toUpperCase();
                            letras = respostas.toCharArray();

                            // Verifica se a resposta tem exatamente 10 caracteres
                            if (respostas.length() != 10)
                                throw new HistoricoInvalidoException(); // Lança uma exceção se o tamanho for inválido

                            // Loop para contar o número de respostas 'V' e 'F' e verificar caracteres
                            // inválidos
                            for (char letra : letras) {
                                if (letra == 'V')
                                    ++qtd_respostas_v; // Incrementa o contador de respostas 'V'
                                else if (letra == 'F')
                                    ++qtd_respostas_f; // Incrementa o contador de respostas 'F'
                                else
                                    throw new HistoricoInvalidoException(); // Lança uma exceção se o caractere não for
                                                                            // 'V' ou 'F'
                            }

                            // Verifica se o aluno "zerou" o teste (respondeu todas as questões com 'V' ou
                            // 'F')
                            if (qtd_respostas_v == 10 || qtd_respostas_f == 10)
                                zerou[j] = true;

                            // Formata e escreve os dados no arquivo
                            formato = String.format("%s\t%s%n", respostas, nome_alunos[j]);
                            arq_disciplina.write(formato);

                            ++j; // Incrementa o índice do aluno para o próximo ciclo
                        } catch (HistoricoInvalidoException e) {
                            // Trata exceções lançadas para resposta inválida
                            if (respostas.length() != 10)
                                e.msgQuantidadeInvalida(); // Mensagem de erro para tamanho inválido
                            else
                                e.msgRespostasInvalidas(); // Mensagem de erro para caracteres inválidos
                        }
                    }
                } catch (IOException e) {
                    // Trata exceções lançadas ao criar ou manipular o arquivo
                    System.out.println("Erro ao criar ou manipular o arquivo: " + e.getMessage());
                } finally {
                    // Garante que o FileWriter seja fechado
                    try {
                        if (arq_disciplina != null) {
                            arq_disciplina.close(); // Fecha o arquivo após a escrita
                        }
                    } catch (IOException e) {
                        System.out.println("Erro ao fechar o arquivo: " + e.getMessage());
                    }
                }
            }

            // Fecha o scanner após o uso
            scanner.close();
        }
    }
}