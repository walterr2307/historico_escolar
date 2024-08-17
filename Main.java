import java.util.Scanner;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        // Declaração de variáveis para controle de fluxo, contagem e armazenamento
        int i, j, num_disciplinas = 0, qtd_alunos = 0, qtd_respostas_v, qtd_respostas_f;
        boolean gabarito_valido = true, zerou[];
        char letras[];
        String disciplinas[], respostas = null, nome_alunos[], formato, gabarito;
        FileWriter arq_disciplina = null;
        Calculadora calculadora = new Calculadora();
        Scanner scanner = new Scanner(System.in);

        // Inicia um bloco try-finally para garantir o fechamento do Scanner
        try {
            // Solicita o número de disciplinas com validação
            while (true) {
                System.out.print("Ponha o numero de disciplinas: ");
                if (scanner.hasNextInt()) {
                    num_disciplinas = scanner.nextInt();
                    if (num_disciplinas > 0)
                        break;
                    else
                        System.out.println("O número de disciplinas deve ser maior que zero.");
                } else {
                    System.out.println("Entrada inválida. Por favor, insira um número inteiro.");
                    scanner.next(); // Limpa o buffer do Scanner
                }
            }

            // Solicita a quantidade de alunos com validação
            while (true) {
                System.out.print("Insira a quantidade de alunos: ");
                if (scanner.hasNextInt()) {
                    qtd_alunos = scanner.nextInt();
                    if (qtd_alunos > 0)
                        break;
                    else
                        System.out.println("A quantidade de alunos deve ser maior que zero.");
                } else {
                    System.out.println("Entrada inválida. Por favor, insira um número inteiro.");
                    scanner.next(); // Limpa o buffer do Scanner
                }
            }
            scanner.nextLine(); // Limpa o buffer do Scanner

            // Inicializa os arrays para armazenar os nomes das disciplinas e dos alunos
            disciplinas = new String[num_disciplinas];
            nome_alunos = new String[qtd_alunos];

            System.out.println();

            // Loop para capturar os nomes dos alunos
            for (i = 0; i < qtd_alunos; i++) {
                System.out.printf("Coloque o nome do %d* aluno: ", i + 1);
                nome_alunos[i] = scanner.nextLine();
                nome_alunos[i] = nome_alunos[i].toUpperCase(); // Converte o nome para maiúsculas
            }

            // Loop para capturar o nome de cada disciplina e o respectivo gabarito
            for (i = 0; i < num_disciplinas; i++) {
                System.out.printf("\nDigite o nome da %d* disciplina: ", i + 1);
                disciplinas[i] = scanner.nextLine();
                disciplinas[i] = disciplinas[i].toUpperCase(); // Converte o nome para maiúsculas

                // Loop para validar o gabarito inserido pelo usuário
                do {
                    System.out.print("Digite o gabarito: ");
                    gabarito = scanner.nextLine();

                    // Verifica se o gabarito tem exatamente 10 caracteres
                    if (gabarito.length() != 10) {
                        System.out.println("O gabarito deve ter exatamente 10 caracteres.");
                        gabarito_valido = false;
                        continue;
                    }

                    gabarito_valido = true; // Assume que o gabarito é válido
                    gabarito = gabarito.trim().toUpperCase(); // Remove espaços e converte para maiúsculas
                    letras = gabarito.toCharArray(); // Converte o gabarito em um array de caracteres
                    qtd_respostas_v = 0;
                    qtd_respostas_f = 0;

                    // Valida cada caractere do gabarito para garantir que seja 'V' ou 'F'
                    for (char letra : letras) {
                        if (letra == 'V')
                            ++qtd_respostas_v;
                        else if (letra == 'F')
                            ++qtd_respostas_f;
                        else {
                            gabarito_valido = false;
                            System.out.println("O gabarito deve conter apenas caracteres 'V' ou 'F'.");
                            break;
                        }
                    }
                } while (!gabarito_valido); // Repete enquanto o gabarito for inválido

                // Bloco try-finally para tratar o arquivo da disciplina e garantir seu
                // fechamento
                try {
                    arq_disciplina = new FileWriter(new File(disciplinas[i] + ".txt"));
                    formato = String.format("%s\tGABARITO%s", gabarito, System.lineSeparator());
                    arq_disciplina.write(formato);
                    zerou = new boolean[qtd_alunos]; // Array para controlar se algum aluno zerou na disciplina

                    System.out.printf("\n==============================\n%s\n\n", disciplinas[i]);

                    j = 0;

                    // Loop para capturar as respostas dos alunos para a disciplina atual
                    while (j < qtd_alunos) {
                        zerou[j] = false; // Inicializa a variável para verificar se o aluno zerou
                        qtd_respostas_v = 0; // Contador de respostas 'V'
                        qtd_respostas_f = 0; // Contador de respostas 'F'

                        // Bloco try-catch para capturar e validar as respostas do aluno
                        try {
                            System.out.printf("Digite a resposta de %s: ", nome_alunos[j]);
                            respostas = scanner.nextLine();

                            respostas = respostas.trim().toUpperCase(); // Remove espaços e converte para maiúsculas
                            letras = respostas.toCharArray(); // Converte as respostas em um array de caracteres

                            // Verifica se as respostas têm exatamente 10 caracteres
                            if (respostas.length() != 10)
                                throw new HistoricoInvalidoException();

                            // Valida cada resposta e conta a quantidade de 'V' e 'F'
                            for (char letra : letras) {
                                if (letra == 'V')
                                    ++qtd_respostas_v;
                                else if (letra == 'F')
                                    ++qtd_respostas_f;
                                else
                                    throw new HistoricoInvalidoException(); // Exceção personalizada para respostas
                                                                            // inválidas
                            }

                            // Verifica se o aluno zerou (respondeu todas as questões com 'V' ou 'F')
                            if (qtd_respostas_v == 10 || qtd_respostas_f == 10)
                                zerou[j] = true;

                            // Formata e escreve as respostas do aluno no arquivo da disciplina
                            formato = String.format("%s\t%s%s", respostas, nome_alunos[j], System.lineSeparator());
                            arq_disciplina.write(formato);

                            ++j; // Incrementa o índice para o próximo aluno
                        } catch (HistoricoInvalidoException e) {
                            // Trata exceções relacionadas a respostas inválidas
                            if (respostas.length() != 10)
                                e.msgQuantidadeInvalida();
                            else
                                e.msgRespostasInvalidas();
                        }
                    }
                } finally {
                    // Garante o fechamento do arquivo da disciplina, tratando possíveis exceções
                    try {
                        if (arq_disciplina != null) {
                            arq_disciplina.close();
                        }
                    } catch (IOException e) {
                        System.out.println("Erro ao fechar o arquivo: " + e.getMessage());
                    }
                }

                calculadora.calcularNotas(disciplinas[i], zerou);
            }

            // Captura qualquer exceção de I/O durante a execução geral do programa
        } catch (IOException e) {
            System.out.println("Erro geral de I/O: " + e.getMessage());
        } finally {
            // Garante o fechamento do Scanner, se necessário
            scanner.close();
        }
    }
}