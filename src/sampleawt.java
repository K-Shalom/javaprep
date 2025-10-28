import java.awt.*;
import java.awt.event.*;

public class sampleawt extends Frame {

    sampleawt()
    {
        Button b= new Button("Click me");
        b.setBounds(80,80,70,30);
        add(b);
        setSize(300,300);
        setTitle("This sample awt");
        setLayout(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        sampleawt f= new sampleawt();
    }

}
