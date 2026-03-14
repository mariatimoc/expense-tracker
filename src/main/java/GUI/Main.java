package GUI;

public class Main {
    public static void main(String[] args) {
        TransactionView view = new TransactionView();
        new Controller(view);
        view.setVisible(true);
    }
}