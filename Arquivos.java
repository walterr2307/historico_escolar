import java.io.*;
import java.util.Scanner;

public class Arquivos {

    // Método para registrar um gabarito no arquivo de gabaritos
    public void registrarGabarito(FileWriter arq_gabarito, String gabarito, String disciplina) {
        try {
            // Escreve o gabarito e a disciplina no arquivo, separados por uma tabulação
            arq_gabarito.write(gabarito + "\t" + disciplina + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace(); // Imprime a exceção em caso de erro
        }
    }

    // Método para abrir o arquivo de gabaritos, criando-o se não existir
    public FileWriter getArquivoGabarito() {
        FileWriter arq = null;

        try {
            // Abre o arquivo "GABARITOS.txt" em modo de adição (append)
            arq = new FileWriter(new File("GABARITOS.txt"), true);
        } catch (IOException e) {
            e.printStackTrace(); // Imprime a exceção em caso de erro
        }

        return arq; // Retorna o FileWriter para o arquivo de gabaritos
    }

    // Método para apagar o conteúdo de um arquivo (ou criar um novo vazio)
    public void apagarArquivo(String caminho) {
        try (FileWriter arq = new FileWriter(new File(caminho))) {
            arq.close(); // Fecha o arquivo após abri-lo (efetivamente apagando seu conteúdo)
        } catch (IOException e) {
            e.printStackTrace(); // Imprime a exceção em caso de erro
        }
    }

    // Método para abrir (ou criar) um arquivo específico de disciplina
    public FileWriter getArquivoDisciplina(String disciplina) {
        FileWriter arq = null;

        System.out.printf("\n\n========== %s ==========\n", disciplina); // Exibe o nome da disciplina

        try {
            // Abre o arquivo da disciplina em modo de adição (append)
            arq = new FileWriter(new File(disciplina + ".txt"), true);
        } catch (IOException e) {
            e.printStackTrace(); // Imprime a exceção em caso de erro
        }

        return arq; // Retorna o FileWriter para o arquivo da disciplina
    }

    // Método para registrar a resposta de um aluno em um arquivo de disciplina
    public void registrarResposta(FileWriter arq_disciplina, String nome, Scanner scanner) {
        char letras[];
        String resposta = null;

        while (true) { // Laço para capturar a resposta até que seja válida
            try {
                System.out.printf("Coloque a resposta de %s: ", nome);
                resposta = scanner.nextLine().trim().toUpperCase(); // Captura e formata a resposta
                letras = resposta.toCharArray(); // Converte a resposta para um array de caracteres

                if (resposta.length() != 10)
                    throw new HistoricoInvalidoException(); // Verifica se a resposta tem 10 caracteres

                for (char letra : letras) {
                    if (letra != 'V' && letra != 'F')
                        throw new HistoricoInvalidoException(); // Verifica se os caracteres são 'V' ou 'F'
                }

                try {
                    // Escreve a resposta e o nome do aluno no arquivo
                    arq_disciplina.write(resposta + "\t" + nome + System.lineSeparator());
                } catch (IOException e) {
                    e.printStackTrace(); // Imprime a exceção em caso de erro
                }

                break; // Sai do laço se a resposta for válida
            } catch (HistoricoInvalidoException e) {
                if (resposta.length() != 10)
                    e.mostrarMsgQtdInvalida(); // Mensagem para quantidade inválida de respostas
                else
                    e.mostrarMsgItemInvalido(); // Mensagem para item inválido na resposta
            }
        }
    }

    // Método para descobrir o gabarito de uma disciplina no arquivo de gabaritos
    public String descobrirGabarito(String disciplina) {
        String linha, gabarito = null, dados_arq[];

        try (BufferedReader arq = new BufferedReader(new FileReader("GABARITOS.txt"))) {
            // Lê o arquivo linha por linha
            while ((linha = arq.readLine()) != null) {
                dados_arq = linha.split("\t"); // Separa a linha em gabarito e disciplina

                if (dados_arq[1].equals(disciplina)) { // Verifica se a disciplina coincide
                    gabarito = dados_arq[0]; // Armazena o gabarito correspondente
                    break; // Sai do laço ao encontrar o gabarito
                }
            }

            arq.close(); // Fecha o arquivo após a leitura
        } catch (IOException e) {
            e.printStackTrace(); // Imprime a exceção em caso de erro
        }

        return gabarito; // Retorna o gabarito encontrado
    }

    // Método para procurar respostas e nomes de alunos em um arquivo de disciplina
    public void procurarRespostasNomes(String disciplina, String respostas[], String alunos[], int qtd_alunos) {
        int i = 0;
        String linha, dados_arq[];

        try (BufferedReader arq = new BufferedReader(new FileReader(disciplina + ".txt"))) {
            // Lê o arquivo linha por linha
            while ((linha = arq.readLine()) != null) {
                dados_arq = linha.split("\t"); // Separa a linha em resposta e nome do aluno
                respostas[i] = dados_arq[0]; // Armazena a resposta no array
                alunos[i] = dados_arq[1]; // Armazena o nome do aluno no array
                ++i; // Incrementa o índice
            }

            arq.close(); // Fecha o arquivo após a leitura
        } catch (IOException e) {
            e.printStackTrace(); // Imprime a exceção em caso de erro
        }
    }

    // Método para escrever um arquivo com respostas, nomes e notas dos alunos
    public void escreverArquivo(String caminho, String respostas[], String alunos[], float notas[], int qtd_alunos,
            float media) {
        String formato;

        try (FileWriter arq = new FileWriter(new File(caminho))) {

            for (int i = 0; i < qtd_alunos; i++) {
                // Formata a linha com resposta, nome e nota do aluno
                formato = String.format("%s\t%s\t%.1f", respostas[i], alunos[i], notas[i]);
                arq.write(formato + System.lineSeparator()); // Escreve a linha no arquivo
            }

            // Escreve a média das notas ao final do arquivo
            formato = String.format("MEDIA: %.1f", media);
            arq.write(System.lineSeparator() + formato);
            arq.close(); // Fecha o arquivo após a escrita
        } catch (IOException e) {
            e.printStackTrace(); // Imprime a exceção em caso de erro
        }
    }

    // Método para imprimir o conteúdo de um arquivo no console
    public void imprimirArquivo(String caminho) {
        String linha;

        try (BufferedReader arq = new BufferedReader(new FileReader(caminho))) {
            System.out.printf("\n========== %s ==========\n", caminho); // Exibe o nome do arquivo

            // Lê e imprime o arquivo linha por linha
            while ((linha = arq.readLine()) != null)
                System.out.println(linha);

            arq.close(); // Fecha o arquivo após a leitura
        } catch (IOException e) {
            e.printStackTrace(); // Imprime a exceção em caso de erro
        }
    }

    // Método para fechar um FileWriter
    public void fecharArquivo(FileWriter arq) {
        try {
            arq.close(); // Fecha o arquivo
        } catch (IOException e) {
            e.printStackTrace(); // Imprime a exceção em caso de erro
        }
    }
}