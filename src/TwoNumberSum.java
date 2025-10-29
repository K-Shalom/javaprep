import java.awt.*;
import java.awt.event.*;

public class TwoNumberSum extends Frame implements ActionListener {

    TextField tnum1, tnum2, result;
    Button addButton;

    TwoNumberSum() {
        setSize(300, 250);
        setLayout(null);
        setTitle("ADD TWO NUMBERS");

        Label enum1 = new Label("ENTER NUM 1:");
        enum1.setBounds(30, 40, 100, 30);
        add(enum1);

        tnum1 = new TextField();
        tnum1.setBounds(150, 40, 100, 30);
        add(tnum1);

        Label enum2 = new Label("ENTER NUM 2:");
        enum2.setBounds(30, 80, 100, 30);
        add(enum2);

        tnum2 = new TextField();
        tnum2.setBounds(150, 80, 100, 30);
        add(tnum2);

        Label resLabel = new Label("Result:");
        resLabel.setBounds(30, 120, 100, 30);
        add(resLabel);

        result = new TextField();
        result.setBounds(150, 120, 100, 30);
        result.setEditable(false);
        add(result);

        addButton = new Button("ADD");
        addButton.setBounds(100, 160, 80, 30);
        addButton.addActionListener(this);
        add(addButton);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int n1 = Integer.parseInt(tnum1.getText());
            int n2 = Integer.parseInt(tnum2.getText());
            int sum = n1 + n2;
            result.setText(String.valueOf(sum));
        } catch (Exception ex) {
            result.setText("Invalid Input");
        }
    }

    public static void main(String[] args) {
        new TwoNumberSum();
    }
}
