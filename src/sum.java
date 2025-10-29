import java.awt.*;
import java.awt.event.*;


public class sum extends Frame implements ActionListener {

    TextField tnum1,tnum2,result;
    Button addbutton;

    sum(){
        setTitle("Summ of two numbers");
        setSize(300,300);
        setLayout(null);

        Label num1=new Label("Enter first num");
        num1.setBounds(30,80,100,30);
        add(num1);
        Label num2=new Label ("Enter last num");
        num2.setBounds(30,110,100,30);
        add(num2);

        Label result1= new Label ("result");
        result1.setBounds(30,150,100,30);
        add(result1);


        tnum1= new TextField();
        tnum1.setBounds(140,80,90,30);
        add(tnum1);
        tnum2= new TextField();
        tnum2.setBounds(140,110,90,30);
        add(tnum2);

        result = new TextField();
        result.setBounds(140,150,90,30);
        result.setEditable(false);
        add(result);

        Button addbutton = new Button("Add+");
        addbutton.setBounds(140,200,60,30);
        addbutton.addActionListener(this);
        add(addbutton);


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            double n1= Integer.parseInt(tnum1.getText());
            double n2= Integer.parseInt(tnum2.getText());
            double sum=n1+n2;
            result.setText(String.valueOf(sum));
        }
        catch (Exception ex){
            result.setText("Invalid");
        }
    }

    public static void main(String[] args) {
        new sum();
    }
}
