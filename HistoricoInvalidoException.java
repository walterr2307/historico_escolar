public class HistoricoInvalidoException extends Exception {
    public void mostrarMsgQtdInvalida() {
        System.out.println("Precisa inserir 10 itens!");
    }

    public void mostrarMsgItensIguais() {
        System.out.println("Todos os itens não podem ser iguais!");
    }

    public void mostrarMsgItemInvalido() {
        System.out.println("Os itens devem ser, somente, V ou F!");
    }
}