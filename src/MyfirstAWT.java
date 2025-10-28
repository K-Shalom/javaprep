import java.awt.*;
import java.awt.event.*;

public class MyfirstAWT extends Frame{

    MyfirstAWT(){
        Label l=new Label("My firt awt");
        l.setBounds(80,80,80,30);

        Button b= new Button("click me");
        b.setBounds(100,50,80,30);

        add(l);
        add(b);

        setSize(300,300);

        setLayout(null);
        setVisible(true);
        // Gufunga window neza
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    // Main method
    public static void main(String[] args) {
        new MyfirstAWT();
    }
}