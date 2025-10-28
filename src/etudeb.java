import java.awt.*;
import java.awt.event.*;

public class etudeb extends Frame {
    etudeb(){
        setSize(300,300);
        setLayout(null);


        Label label= new Label("controls");
        label.setBounds(30,30,90,30);
        add(label);

        Label cll= new Label("Choose class:");
        cll.setBounds(30,50,90,30);
        add(cll);
        Choice choice= new Choice();
        choice.add("Group 1");
        choice.add("Group 2");
        choice.add("Group 3");
        choice.setBounds(30,80,90,30);
        add(choice);

        Label radio = new Label("Gender");
        radio.setBounds(30,100,90,30);
        add(radio);

        CheckboxGroup gender= new CheckboxGroup();
        Checkbox male = new Checkbox("Male",gender,true);
        Checkbox female= new Checkbox("female",gender,false);
        male.setBounds(34,125,60,30);
        female.setBounds(34,145,60,30);
        add(male);
        add(female);





        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    public static void main(String[] args) {
        new etudeb();
    }
}
