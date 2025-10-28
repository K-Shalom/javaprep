import java.awt.*;
import javax.swing.*;

public class GraphicEx extends JPanel {

    public void paintComponent(Graphics g) {
        g.drawLine(3, 3, 303, 303);
        g.drawRect(3, 3, 303, 303);
        g.drawOval(100, 100, 100, 100);
        g.drawRoundRect(20, 20, 250, 250, 5, 25);
    }
    public static void main(String arg[]) {
        GraphicEx gp = new GraphicEx();
        JFrame sh = new JFrame();
        sh.add(gp);
        sh.setSize(new Dimension(320, 320));
        sh.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sh.setVisible(true);
        sh.setTitle("My Shapes");
    }
}
