import java.io.*;
import java.util.ArrayList;

public class Calculadora {
    public void imprimir(String disciplina) {
        int tam, num_itens_v, num_itens_f;
        float media = 0f, nota_alunos[];
        char letras_aluno[], letras_gabarito[];
        String linha, gabarito, dados[], str_resposta_alunos[];
        ArrayList<String> resposta_alunos = new ArrayList<String>(), nome_alunos = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader(disciplina + ".txt"))) {
            while ((gabarito = br.readLine()) != null)
                break;

            while ((linha = br.readLine()) != null) {
                dados = linha.split("\t");
                resposta_alunos.add(dados[0]);
                nome_alunos.add(dados[1]);
            }

            tam = resposta_alunos.size();
            letras_gabarito = gabarito.toCharArray();
            str_resposta_alunos = new String[tam];
            nota_alunos = new float[tam];

            for (int i = 0; i < tam; i++)
                str_resposta_alunos[i] = resposta_alunos.get(i);

            for (int i = 0; i < tam; i++) {
                nota_alunos[i] = 0f;
                num_itens_v = 0;
                num_itens_f = 0;
                letras_aluno = str_resposta_alunos[i].toCharArray();

                for (int j = 0; j < 10; j++) {
                    if (letras_aluno[j] == letras_gabarito[j])
                        ++nota_alunos[i];
                    if (letras_aluno[j] == 'V')
                        ++num_itens_v;
                    if (letras_aluno[j] == 'F')
                        ++num_itens_f;
                }

                if (num_itens_v == 10 || num_itens_f == 10)
                    nota_alunos[i] = 0f;

                media += nota_alunos[i];
            }

            media /= tam;

            ordenarPorNome(disciplina, resposta_alunos, nome_alunos, nota_alunos, media);
            ordenarPorNota(disciplina, resposta_alunos, nome_alunos, nota_alunos, media);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lerArquivo(String caminho) {
        String linha;

        // Lê e imprime o conteúdo do arquivo criado
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            while ((linha = br.readLine()) != null) {
                System.out.println(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ordenarPorNome(String disciplina, ArrayList<String> respostas, ArrayList<String> nomes, float notas[],
            float media) {
        float copia_nota;
        boolean troca;
        String copia, formato;

        try (FileWriter arq = new FileWriter(new File(disciplina + "_ORDEM_NOME.txt"))) {
            do {
                troca = false;
                // Algoritmo de ordenação Bubble Sort por nome
                for (int i = 0; i < nomes.size() - 1; i++) {
                    if (nomes.get(i).compareTo(nomes.get(i + 1)) > 0) {
                        // Troca os nomes
                        copia = nomes.get(i);
                        nomes.set(i, nomes.get(i + 1));
                        nomes.set(i + 1, copia);

                        // Troca as respostas correspondentes
                        copia = respostas.get(i);
                        respostas.set(i, respostas.get(i + 1));
                        respostas.set(i + 1, copia);

                        // Troca as notas correspondentes
                        copia_nota = notas[i];
                        notas[i] = notas[i + 1];
                        notas[i + 1] = copia_nota;

                        troca = true;
                    }
                }
            } while (troca);

            // Escreve os dados ordenados no arquivo
            for (int i = 0; i < nomes.size(); i++) {
                formato = String.format("%s\t%s\t%.1f%s", respostas.get(i), nomes.get(i), notas[i],
                        System.lineSeparator());
                arq.write(formato);
            }

            // Escreve a média no final do arquivo
            formato = String.format("%sMEDIA: %.1f", System.lineSeparator(), media);
            arq.write(formato);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ordenarPorNota(String disciplina, ArrayList<String> respostas, ArrayList<String> nomes, float notas[],
            float media) {
        float copia_nota;
        boolean troca;
        String copia, formato;

        try (FileWriter arq = new FileWriter(new File(disciplina + "_ORDEM_NOTA.txt"))) {
            do {
                troca = false;
                // Algoritmo de ordenação Bubble Sort por nota
                for (int i = nomes.size() - 1; i > 0; i--) {
                    if (notas[i] < notas[i - 1]) {
                        // Troca as notas
                        copia_nota = notas[i];
                        notas[i] = notas[i - 1];
                        notas[i - 1] = copia_nota;

                        // Troca as respostas correspondentes
                        copia = respostas.get(i);
                        respostas.set(i, respostas.get(i - 1));
                        respostas.set(i - 1, copia);

                        // Troca os nomes correspondentes
                        copia = nomes.get(i);
                        nomes.set(i, nomes.get(i - 1));
                        nomes.set(i - 1, copia);

                        troca = true;
                    }
                }
            } while (troca);

            // Escreve os dados ordenados no arquivo
            for (int i = 0; i < nomes.size(); i++) {
                formato = String.format("%s\t%s\t%.1f%s", respostas.get(i), nomes.get(i), notas[i],
                        System.lineSeparator());
                arq.write(formato);
            }

            // Escreve a média no final do arquivo
            formato = String.format("%sMEDIA: %.1f", System.lineSeparator(), media);
            arq.write(formato);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}