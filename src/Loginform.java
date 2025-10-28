import java.awt.*;
import java.awt.event.*;

public class Loginform extends Frame {
    Loginform(){
        Label l= new Label("Login Form");
        Label uname=new Label("Enter Username:");
        TextField tname= new TextField();

        Label pass= new Label("Enter Password");
        TextField tpass = new TextField();
        Button b=new Button("Login");

        l.setBounds(50,30,140,30);
        uname.setBounds(30,60,100,30);
        tname.setBounds(140,60,150,30);
        pass.setBounds(30,100,130,30);
        tpass.setBounds(140,100,150,30);
        b.setBounds(140,140,70,30);

        add(l);
        add(uname);
        add(tpass);
        add(pass);
        add(tname);
        add(b);

        setSize(340,230);
        setTitle("Login Form");
        setLayout(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        new Loginform();
    }
}
