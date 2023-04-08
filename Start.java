import javax.swing.JFrame;



public class Start extends  JFrame {
    Start() {
        add(new TitleGame());
        setTitle("Snake Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
      
    }

    public static void main(String[] args) {
        new Start();
    }
}
