public class HistoricoInvalidoException extends Exception {

    public void msgQuantidadeInvalida() {
        System.out.println("Coloque 10 respostas!");
    }

    public void msgRespostasInvalidas() {
        System.out.println("Coloque somente V ou F!");
    }
}
