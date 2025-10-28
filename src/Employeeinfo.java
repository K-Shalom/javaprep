import java.awt.*;
import java.awt.event.*;

public class Employeeinfo extends Frame {
    Employeeinfo(){
        Label l = new Label("Enter Employee Name");
        TextField t= new TextField();
        Button b= new Button("Submit");
        l.setBounds(30,40,150,30);
        t.setBounds(30,70,140,30);
        b.setBounds(180,70,50,30);

        add(l);
        add(b);
        add(t);

        setSize(400,300);
        setTitle("Employe Form");
        setLayout(null);
        setVisible(true);
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        Employeeinfo g= new Employeeinfo();
    }
}
