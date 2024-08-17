import java.io.*;
import java.util.ArrayList;

public class Calculadora {
    private float media; // Variável para armazenar a média das notas dos alunos

    public void calcularNotas(String disciplina, boolean zerou[]) {
        String aux[], respostas_gabarito = null, linha, str_respostas_alunos[];
        BufferedReader gabarito = null;
        FileWriter ordem_alfabetica = null;
        FileWriter ordem_notas = null;
        ArrayList<Float> notas_alunos = new ArrayList<Float>(); // Lista para armazenar as notas dos alunos
        ArrayList<String> nomes_alunos = new ArrayList<String>(); // Lista para armazenar os nomes dos alunos
        ArrayList<String> respostas_alunos = new ArrayList<String>(); // Lista para armazenar as respostas dos alunos

        media = 0f; // Inicializa a média

        try {
            // Abre o arquivo da disciplina para leitura
            gabarito = new BufferedReader(new FileReader(disciplina + ".txt"));

            // Abre os arquivos para escrever os resultados em ordem alfabética e ordem de
            // notas
            ordem_alfabetica = new FileWriter(new File(disciplina + "_ORDEM_ALFABETICA.txt"));
            ordem_notas = new FileWriter(new File(disciplina + "_ORDEM_NOTAS.txt"));

            // Lê a primeira linha do arquivo, que contém o gabarito das respostas
            while ((linha = gabarito.readLine()) != null) {
                aux = linha.split("\t"); // Divide a linha usando tab como delimitador
                respostas_gabarito = aux[0]; // Obtém o gabarito
                break; // Sai do loop após ler a primeira linha, que é o gabarito
            }

            // Lê as respostas dos alunos e seus nomes, linha por linha
            while ((linha = gabarito.readLine()) != null) {
                aux = linha.split("\t"); // Divide a linha em resposta e nome do aluno usando tab como delimitador
                respostas_alunos.add(aux[0]); // Adiciona a resposta do aluno à lista
                nomes_alunos.add(aux[1]); // Adiciona o nome do aluno à lista
            }

            // Converte a lista de respostas dos alunos para um array de strings
            str_respostas_alunos = new String[respostas_alunos.size()];
            for (int i = 0; i < respostas_alunos.size(); i++)
                str_respostas_alunos[i] = respostas_alunos.get(i); // Preenche o array com as respostas dos alunos

            // Calcula as notas dos alunos comparando suas respostas com o gabarito
            for (int i = 0; i < respostas_alunos.size(); i++) {
                float nota = 0f; // Inicializa a nota do aluno
                char letras[] = str_respostas_alunos[i].toCharArray(); // Converte as respostas do aluno em um array de
                                                                       // caracteres
                char letras_gabarito[] = respostas_gabarito.toCharArray(); // Converte o gabarito em um array de
                                                                           // caracteres

                // Compara cada resposta do aluno com o gabarito
                if (!zerou[i]) { // Verifica se o aluno não zerou a disciplina
                    for (int j = 0; j < 10; j++) {
                        if (letras[j] == letras_gabarito[j])
                            nota += 1f; // Incrementa a nota se a resposta estiver correta
                    }
                }

                // Adiciona a nota do aluno à lista de notas
                notas_alunos.add(nota);
            }

            // Calcula a média das notas dos alunos
            for (int i = 0; i < notas_alunos.size(); i++)
                media += notas_alunos.get(i); // Soma todas as notas

            media /= notas_alunos.size(); // Divide pela quantidade de alunos para obter a média

            // Ordena os alunos alfabeticamente e escreve no arquivo
            ordenarAlfabeticamente(nomes_alunos, notas_alunos, ordem_alfabetica);

            // Ordena os alunos por nota e escreve no arquivo
            ordenarPorNota(nomes_alunos, notas_alunos, ordem_notas);

        } catch (FileNotFoundException e) {
            System.err.println("Erro: Arquivo não encontrado - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro de I/O - " + e.getMessage());
        } finally {
            // Fecha todos os recursos para evitar vazamentos de memória
            try {
                if (gabarito != null) {
                    gabarito.close(); // Fecha o arquivo de leitura do gabarito
                }

                if (ordem_alfabetica != null) {
                    ordem_alfabetica.close(); // Fecha o arquivo de escrita de notas em ordem alfabética
                }

                if (ordem_notas != null) {
                    ordem_notas.close(); // Fecha o arquivo de escrita de notas em ordem de desempenho
                }

            } catch (IOException e) {
                System.err.println("Erro ao fechar os arquivos - " + e.getMessage());
            }
        }
    }

    // Método para ordenar os alunos alfabeticamente e escrever no arquivo
    private void ordenarAlfabeticamente(ArrayList<String> nomes, ArrayList<Float> notas, FileWriter ordem_alfabetica) {
        int i, tam = nomes.size();
        float copia_nota;
        boolean troca = false;
        String copia, formato;

        do {
            troca = false; // Flag para verificar se houve troca de elementos

            // Ordena os alunos em ordem alfabética usando o método Bubble Sort
            for (i = 0; i < tam - 1; i++) {
                if (nomes.get(i).compareTo(nomes.get(i + 1)) > 0) {
                    // Troca os nomes
                    copia = nomes.get(i);
                    nomes.set(i, nomes.get(i + 1));
                    nomes.set(i + 1, copia);

                    // Troca as notas correspondentes
                    copia_nota = notas.get(i);
                    notas.set(i, notas.get(i + 1));
                    notas.set(i + 1, copia_nota);

                    troca = true; // Indica que houve troca
                }
            }
        } while (troca); // Repete enquanto houver trocas

        try {
            // Escreve os resultados em ordem alfabética no arquivo
            for (i = 0; i < tam; i++) {
                formato = String.format("NOME: %s\tNota: %.1f%n", nomes.get(i), notas.get(i));
                ordem_alfabetica.write(formato);
            }

            // Escreve a média da turma no final do arquivo
            formato = String.format("%sMEDIA DA TURMA: %.1f", System.lineSeparator(), media);
            ordem_alfabetica.write(formato);
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo ordem_alfabetica - " + e.getMessage());
        }
    }

    // Método para ordenar os alunos por nota e escrever no arquivo
    private void ordenarPorNota(ArrayList<String> nomes, ArrayList<Float> notas, FileWriter ordem_notas) {
        int i, tam = nomes.size();
        float copia_nota;
        boolean troca = false;
        String copia, formato;

        do {
            troca = false; // Flag para verificar se houve troca de elementos

            // Ordena os alunos por nota em ordem decrescente usando o método Bubble Sort
            for (i = tam - 1; i > 0; i--) {
                if (notas.get(i) < notas.get(i - 1)) {
                    // Troca os nomes
                    copia = nomes.get(i);
                    nomes.set(i, nomes.get(i - 1));
                    nomes.set(i - 1, copia);

                    // Troca as notas correspondentes
                    copia_nota = notas.get(i);
                    notas.set(i, notas.get(i - 1));
                    notas.set(i - 1, copia_nota);

                    troca = true; // Indica que houve troca
                }
            }
        } while (troca); // Repete enquanto houver trocas

        try {
            // Escreve os resultados por nota no arquivo
            for (i = 0; i < tam; i++) {
                formato = String.format("NOME: %s\tNota: %.1f%n", nomes.get(i), notas.get(i));
                ordem_notas.write(formato);
            }

            // Escreve a média da turma no final do arquivo
            formato = String.format("%sMEDIA DA TURMA: %.1f", System.lineSeparator(), media);
            ordem_notas.write(formato);
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo ordem_nota - " + e.getMessage());
        }
    }

}