import java.awt.*;
import java.awt.event.*;

public class Smileface extends Frame{
    Smileface(){
        setTitle("Face smile");
        setSize(300,300);
        setLayout(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                dispose();
            }
        });
    }
    public void paint(Graphics g){
        g.setColor(Color.yellow);
        g.fillOval(60,60,100,100);
        g.setColor(Color.gray);
        g.fillOval(76,85,20,20);

        g.setColor(Color.gray);
        g.fillOval(120,85,20,20);

        g.setColor(Color.black);
        g.drawArc(87,100,50,40,0,-190);


    }

    public static void main(String[] args) {
       new Smileface().setVisible(true);
    }
}
