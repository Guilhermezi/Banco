package controle;

public class Main {
    @SuppressWarnings("unchecked") // avisa o compilador para ignorar alertas deste bloco
    public static void main(String[] args) { // ponto de entrada do programa
        Login log = new Login();
        log.setVisible(true); // exibe a janela na tela
    }
}
