import java.util.InputMismatchException;
import java.util.Scanner;

public class Funcoes {

    // Método para obter os nomes dos alunos, solicitando ao usuário
    public String[] getNomes(int qtd_alunos, Scanner scanner) {
        String nome_alunos[] = new String[qtd_alunos];

        System.out.println();
        scanner.nextLine(); // Limpa o buffer do scanner

        for (int i = 0; i < qtd_alunos; i++) {
            // Solicita o nome de cada aluno e o armazena no array, convertendo para
            // maiúsculas
            System.out.printf("Ponha o nome do %d* aluno: ", i + 1);
            nome_alunos[i] = scanner.nextLine().trim().toUpperCase();
        }

        System.out.println();
        return nome_alunos; // Retorna o array com os nomes dos alunos
    }

    // Método para obter o gabarito de respostas (sequência de 'V' ou 'F')
    public String getGabarito(Scanner scanner) {
        int num_itens_v, num_itens_f;
        char letras[];
        String gabarito = null;

        while (true) {
            num_itens_v = 0;
            num_itens_f = 0;

            try {
                // Solicita o gabarito do usuário e converte para maiúsculas
                System.out.print("Coloque o gabarito: ");
                gabarito = scanner.nextLine().trim().toUpperCase();
                letras = gabarito.toCharArray();

                // Verifica se o gabarito tem exatamente 10 caracteres
                if (gabarito.length() != 10)
                    throw new HistoricoInvalidoException();

                // Conta quantos 'V' e 'F' existem e valida o gabarito
                for (char letra : letras) {
                    if (letra == 'V')
                        ++num_itens_v;
                    else if (letra == 'F')
                        ++num_itens_f;
                    else
                        throw new HistoricoInvalidoException(); // Exceção se houver caracter inválido
                }

                // Gabaritos com todos os itens iguais são inválidos
                if (num_itens_v == 10 || num_itens_f == 10)
                    throw new HistoricoInvalidoException();

                break; // Sai do loop se o gabarito for válido
            } catch (HistoricoInvalidoException e) {
                // Exibe mensagens de erro dependendo do tipo de problema no gabarito
                if (gabarito.length() != 10)
                    e.mostrarMsgQtdInvalida();
                else if (num_itens_v == 10 || num_itens_f == 10)
                    e.mostrarMsgItensIguais();
                else
                    e.mostrarMsgItemInvalido();
            }
        }

        return gabarito; // Retorna o gabarito válido
    }

    // Método privado para calcular a média das notas dos alunos
    private float calcularMedia(String gabarito, String respostas[], float notas[], int qtd_alunos) {
        int num_itens_v, num_itens_f;
        float media = 0f;
        char letras_gabarito[], letras_resposta[];

        letras_gabarito = gabarito.toCharArray();

        // Inicializa as notas dos alunos com 0
        for (int i = 0; i < qtd_alunos; i++)
            notas[i] = 0f;

        // Calcula a nota de cada aluno comparando a resposta com o gabarito
        for (int i = 0; i < qtd_alunos; i++) {
            num_itens_v = 0;
            num_itens_f = 0;
            letras_resposta = respostas[i].toCharArray();

            for (int j = 0; j < 10; j++) {
                if (letras_gabarito[j] == letras_resposta[j])
                    ++notas[i]; // Incrementa nota para cada resposta correta
                if (letras_resposta[j] == 'V')
                    ++num_itens_v;
                if (letras_resposta[j] == 'F')
                    ++num_itens_f;
            }

            // Zera a nota se todas as respostas forem iguais (todas 'V' ou todas 'F')
            if (num_itens_v == 10 || num_itens_f == 10)
                notas[i] = 0f;

            media += notas[i]; // Soma as notas para calcular a média
        }

        media /= qtd_alunos; // Calcula a média
        return media; // Retorna a média
    }

    // Método privado para ordenar os nomes dos alunos em ordem alfabética
    private void ordenarNomes(String respostas[], String alunos[], float notas[], int qtd_alunos) {
        boolean troca;
        float copia_nota;
        String copia;

        do {
            troca = false;

            // Algoritmo de ordenação Bubble Sort para ordenar os alunos
            for (int i = 0; i < qtd_alunos - 1; i++) {
                if (alunos[i].compareTo(alunos[i + 1]) > 0) {
                    // Troca de posições entre alunos e suas respectivas respostas e notas
                    copia = respostas[i];
                    respostas[i] = respostas[i + 1];
                    respostas[i + 1] = copia;

                    copia = alunos[i];
                    alunos[i] = alunos[i + 1];
                    alunos[i + 1] = copia;

                    copia_nota = notas[i];
                    notas[i] = notas[i + 1];
                    notas[i + 1] = copia_nota;

                    troca = true; // Marca que houve troca, então o loop continua
                }
            }
        } while (troca); // Repete até que não haja mais trocas necessárias
    }

    // Método privado para ordenar as notas dos alunos em ordem decrescente
    private void ordenarNotas(String respostas[], String alunos[], float notas[], int qtd_alunos) {
        boolean troca;
        float copia_nota;
        String copia;

        do {
            troca = false;

            // Algoritmo de ordenação Bubble Sort para ordenar as notas
            for (int i = qtd_alunos - 1; i > 0; i--) {
                if (notas[i - 1] < notas[i]) {
                    // Troca de posições entre alunos, respostas e notas para ordenar
                    copia = respostas[i];
                    respostas[i] = respostas[i - 1];
                    respostas[i - 1] = copia;

                    copia = alunos[i];
                    alunos[i] = alunos[i - 1];
                    alunos[i - 1] = copia;

                    copia_nota = notas[i];
                    notas[i] = notas[i - 1];
                    notas[i - 1] = copia_nota;

                    troca = true; // Marca que houve troca, então o loop continua
                }
            }
        } while (troca); // Repete até que não haja mais trocas necessárias
    }

    // Método para mostrar o menu de disciplinas e executar operações relacionadas
    public void mostrarMenuDisciplinas(String disciplinas[], int qtd_disciplinas, int qtd_alunos, Scanner scanner,
            Arquivos arquivos) {
        int opcao_disciplina;
        float media, notas[] = new float[qtd_alunos];
        String disciplina, gabarito, alunos[] = new String[qtd_alunos], respostas[] = new String[qtd_alunos];

        while (true) {
            System.out.print("\n========== DISCIPLINAS ==========");

            // Exibe a lista de disciplinas disponíveis
            for (int i = 0; i < qtd_disciplinas; i++)
                System.out.printf("\n%d - %s", i + 1, disciplinas[i]);

            System.out.println("\n0 - SAIR");

            // Solicita ao usuário que selecione uma disciplina por meio de seu índice.
            // O loop continua até que seja fornecido um número inteiro.
            while (true) {
                try {
                    System.out.print("\nSelecione o indice da disciplina desejada: ");
                    opcao_disciplina = scanner.nextInt() - 1;
                    break; // Sai do loop se a entrada for válida
                } catch (InputMismatchException e) {
                    System.out.println("Por favor, digite um número inteiro válido.");
                    scanner.nextLine(); // Limpa o buffer de entrada
                }
            }

            // Valida a escolha da disciplina
            if (opcao_disciplina < -1 || opcao_disciplina >= qtd_disciplinas)
                continue; // Repete se a escolha for inválida
            if (opcao_disciplina == -1)
                break; // Sai do loop se a opção for sair (0)

            disciplina = disciplinas[opcao_disciplina];

            // Descobre o gabarito da disciplina selecionada
            gabarito = arquivos.descobrirGabarito(disciplina);
            // Procura as respostas dos alunos para a disciplina selecionada
            arquivos.procurarRespostasNomes(disciplina, respostas, alunos, qtd_alunos);

            // Calcula a média das notas dos alunos
            media = calcularMedia(gabarito, respostas, notas, qtd_alunos);

            // Ordena os nomes dos alunos e salva em um arquivo
            ordenarNomes(respostas, alunos, notas, qtd_alunos);
            arquivos.escreverArquivo(disciplina + "_ORDEM_NOME.txt", respostas, alunos, notas, qtd_alunos, media);
            arquivos.imprimirArquivo(disciplina + "_ORDEM_NOME.txt");

            // Ordena as notas dos alunos e salva em um arquivo
            ordenarNotas(respostas, alunos, notas, qtd_alunos);
            arquivos.escreverArquivo(disciplina + "_ORDEM_NOTA.txt", respostas, alunos, notas, qtd_alunos, media);
            arquivos.imprimirArquivo(disciplina + "_ORDEM_NOTA.txt");
        }
    }
}