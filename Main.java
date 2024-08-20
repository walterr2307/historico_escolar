import java.util.Scanner;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        // Declaração de variáveis
        int i, j, qtd_disciplinas = 0, qtd_alunos = 0, qtd_linhas = 0, num_itens_v, num_itens_f, opcao_arq;
        boolean entrada_valida, disciplina_registrada;
        char letras[];
        String disciplinas[], nome_alunos[], formato, gabarito = null, resposta_aluno = null, linha;
        FileWriter arq_disciplina = null;
        Calculadora calculadora = new Calculadora();

        // Scanner para entrada do usuário
        try (Scanner scanner = new Scanner(System.in)) {
            // Loop para validar a entrada do número de disciplinas
            do {
                System.out.print("Insira a quantidade de disciplinas: ");
                if (scanner.hasNextInt()) {
                    qtd_disciplinas = scanner.nextInt(); // Lê e armazena a quantidade de disciplinas
                    entrada_valida = true; // Marca a entrada como válida
                } else {
                    System.out.println("Entrada inválida. Por favor, insira um número inteiro.");
                    entrada_valida = false; // Marca a entrada como inválida
                    scanner.next(); // Limpa a entrada inválida
                }
            } while (!entrada_valida); // Repete até que a entrada seja válida

            // Loop para validar a entrada do número de alunos
            do {
                System.out.print("Insira a quantidade de alunos: ");
                if (scanner.hasNextInt()) {
                    qtd_alunos = scanner.nextInt(); // Lê e armazena a quantidade de alunos
                    entrada_valida = true; // Marca a entrada como válida
                } else {
                    System.out.println("Entrada inválida. Por favor, insira um número inteiro.");
                    entrada_valida = false; // Marca a entrada como inválida
                    scanner.next(); // Limpa a entrada inválida
                }
            } while (!entrada_valida); // Repete até que a entrada seja válida

            scanner.nextLine(); // Limpa o buffer de nova linha

            // Inicializa os arrays de disciplinas e nomes de alunos
            disciplinas = new String[qtd_disciplinas];
            nome_alunos = new String[qtd_alunos];

            // Loop para obter os nomes dos alunos
            for (i = 0; i < qtd_alunos; i++) {
                System.out.printf("Digite o nome do %dº aluno: ", i + 1);
                nome_alunos[i] = scanner.nextLine().toUpperCase(); // Armazena o nome do aluno em maiúsculas
            }

            // Loop para obter o nome das disciplinas e gabarito correspondente
            for (i = 0; i < qtd_disciplinas; i++) {
                System.out.printf("\nPonha o nome da %dª disciplina: ", i + 1);
                disciplinas[i] = scanner.nextLine().toUpperCase(); // Armazena o nome da disciplina em maiúsculas
                formato = String.format(disciplinas[i] + ".txt"); // Formata o nome do arquivo da disciplina

                // Tenta criar o arquivo da disciplina
                try {
                    arq_disciplina = new FileWriter(new File(formato));

                    // Loop para garantir que o gabarito inserido seja válido
                    while (true) {
                        num_itens_v = 0;
                        num_itens_f = 0;

                        try {
                            System.out.print("Coloque o gabarito: ");
                            gabarito = scanner.nextLine().toUpperCase(); // Lê o gabarito em maiúsculas
                            letras = gabarito.toCharArray(); // Converte o gabarito em array de caracteres

                            // Verifica se o gabarito tem 10 itens
                            if (gabarito.length() != 10)
                                throw new HistoricoInvalidoException(); // Lança uma exceção se estiver incorreto

                            // Conta os itens 'V' e 'F' no gabarito
                            for (char letra : letras) {
                                if (letra == 'V')
                                    ++num_itens_v;
                                else if (letra == 'F')
                                    ++num_itens_f;
                                else
                                    throw new HistoricoInvalidoException(); // Lança uma exceção se houver letras
                                                                            // inválidas
                            }

                            // Lança uma exceção se todos os itens forem iguais
                            if (num_itens_v == 10 || num_itens_f == 10)
                                throw new HistoricoInvalidoException();

                            break; // Sai do loop se o gabarito for válido
                        } catch (HistoricoInvalidoException e) {
                            // Exibe mensagens de erro apropriadas
                            if (gabarito.length() != 10)
                                e.mostrarMsgQtdInvalida();
                            else if (num_itens_v == 10 || num_itens_f == 10)
                                e.mostrarMsgItensIguais();
                            else
                                e.mostrarMsgItemInvalido();
                        }
                    }

                    System.out.printf("\n========== %s ==========\n", disciplinas[i]);

                    // Formata e escreve o gabarito no arquivo
                    formato = String.format("%s\tGABARITO%s", gabarito, System.lineSeparator());
                    arq_disciplina.write(formato);

                    j = 0;

                    // Loop para obter e validar as respostas dos alunos
                    while (j < qtd_alunos) {
                        try {
                            System.out.printf("Insira a resposta do aluno %s: ", nome_alunos[j]);
                            resposta_aluno = scanner.nextLine().toUpperCase(); // Lê a resposta do aluno em maiúsculas
                            letras = resposta_aluno.toCharArray(); // Converte a resposta em array de caracteres

                            // Verifica se a resposta tem 10 itens
                            if (resposta_aluno.length() != 10)
                                throw new HistoricoInvalidoException();

                            // Verifica se todos os caracteres são válidos
                            for (char letra : letras) {
                                if (letra != 'V' && letra != 'F')
                                    throw new HistoricoInvalidoException(); // Lança uma exceção se houver letras
                                                                            // inválidas
                            }

                            // Formata e escreve a resposta no arquivo
                            formato = String.format("%s\t%s%s", resposta_aluno, nome_alunos[j], System.lineSeparator());
                            arq_disciplina.write(formato);

                            ++j; // Passa para o próximo aluno
                        } catch (HistoricoInvalidoException e) {
                            // Exibe mensagens de erro apropriadas
                            if (resposta_aluno.length() != 10)
                                e.mostrarMsgQtdInvalida();
                            else
                                e.mostrarMsgItemInvalido();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace(); // Exibe a pilha de chamadas se houver erro de I/O
                } finally {
                    try {
                        if (arq_disciplina != null) {
                            arq_disciplina.close(); // Garante que o arquivo seja fechado

                            System.out.println();

                            // Lê e exibe o conteúdo do arquivo da disciplina
                            try (BufferedReader br = new BufferedReader(new FileReader(disciplinas[i] + ".txt"))) {
                                while ((linha = br.readLine()) != null) {
                                    System.out.println(linha); // Imprime cada linha do arquivo
                                }
                            } catch (IOException e) {
                                e.printStackTrace(); // Trata exceções de I/O
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace(); // Exibe a pilha de chamadas se houver erro ao fechar o arquivo
                    }
                }

                // Tenta salvar as disciplinas registradas
                try (FileWriter disciplinas_salvas = new FileWriter(new File("DISCIPLINAS.txt"), true)) {
                    try (BufferedReader br = new BufferedReader(new FileReader(disciplinas[i] + ".txt"))) {
                        disciplina_registrada = false;
                        qtd_linhas = 0;

                        while ((linha = br.readLine()) != null) {
                            if (linha.equals(disciplinas[i]))
                                disciplina_registrada = true; // Verifica se a disciplina já foi registrada

                            ++qtd_linhas; // Conta o número de linhas no arquivo
                        }

                        if (!disciplina_registrada)
                            disciplinas_salvas.write(disciplinas[i] + System.lineSeparator()); // Escreve a disciplina
                                                                                               // no arquivo
                    } catch (IOException e) {
                        e.printStackTrace(); // Trata exceções de I/O durante a leitura
                    }
                } catch (IOException e) {
                    e.printStackTrace(); // Trata exceções de I/O durante a escrita
                }
            }

            try (BufferedReader br = new BufferedReader(new FileReader("DISCIPLINAS.txt"))) {
                qtd_linhas = 0;

                while ((linha = br.readLine()) != null)
                    ++qtd_linhas;

                // Recarrega as disciplinas a partir do arquivo
                qtd_disciplinas = qtd_linhas;
                disciplinas = new String[qtd_disciplinas];

                // Reinicia o BufferedReader para ler desde o início
                br.close();
            } catch (IOException e) {
                // Trata exceções de I/O
                e.printStackTrace();
            }

            try (BufferedReader br = new BufferedReader(new FileReader("DISCIPLINAS.txt"))) {
                j = 0;

                while ((linha = br.readLine()) != null) {
                    disciplinas[j] = linha; // Armazena cada linha no array de disciplinas
                    ++j;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Loop para exibir as disciplinas e calcular notas
            while (true) {
                System.out.print("\n========== DISCIPLINAS ==========");

                for (i = 0; i < qtd_disciplinas; i++)
                    System.out.printf("\n%d - %s", i + 1, disciplinas[i]); // Exibe cada disciplina

                System.out.print("\n0 - SAIR");

                System.out.print("\n\nEscolha uma para calcular suas notas: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Entrada inválida. Por favor, insira um número inteiro.");
                    scanner.next(); // Limpa a entrada inválida
                }
                opcao_arq = scanner.nextInt() - 1; // Lê a opção selecionada

                if (opcao_arq < -1 || opcao_arq >= qtd_disciplinas)
                    continue; // Verifica se a opção é válida
                if (opcao_arq == -1)
                    break; // Sai do loop se a opção for "sair"

                calculadora.imprimir(disciplinas[opcao_arq]);

                System.out.println("\nORDENADOS POR NOME: ");
                formato = String.format("%s_ORDEM_NOME.txt", disciplinas[opcao_arq]);
                calculadora.lerArquivo(formato);

                System.out.println("\nORDENADOS POR NOTA: ");
                formato = String.format("%s_ORDEM_NOTA.txt", disciplinas[opcao_arq]);
                calculadora.lerArquivo(formato);
            }
        }
    }
}