import java.awt.*;
import java.awt.event.*;

public class MyShapesAWT extends Frame {
    public MyShapesAWT() {
        // keep the OS window title minimal; we'll draw a custom title area inside the frame
        super("MyShapesAWT");
        setSize(800, 520);
        setBackground(Color.WHITE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // coordinates for the custom content window drawn inside the frame
        int x = 200, y = 70, w = 440, h = 340;

        Color brown = new Color(165, 90, 13);

        // outer border
        g2.setColor(brown);
        g2.setStroke(new BasicStroke(3f));
        g2.drawRect(x, y, w, h);

        // draw title area (a white area with brown border at the top)
        int titleH = 40;
        g2.setColor(new Color(255, 255, 255));
        g2.fillRect(x + 2, y + 2, w - 4, titleH - 4);
        g2.setColor(brown);
        g2.drawLine(x, y + titleH, x + w, y + titleH);

        // draw "My shapes" text in the title area
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.setColor(brown);
        g2.drawString("My shapes", x + 10, y + 26);

        // draw red X box on the right side of the title
        int boxW = 46, boxH = titleH - 8;
        int boxX = x + w - boxW - 4, boxY = y + 4;
        g2.setColor(Color.RED);
        g2.fillRect(boxX, boxY, boxW, boxH);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.drawString("X", boxX + 15, boxY + 23);

    // inner drawing area (content) - center for the smiley
    int contentY = y + titleH + 10;
        int contentH = h - titleH - 20;

        // smiley face circle
        int faceRadius = 110;
        int cx = x + w/2;
        int cy = contentY + contentH/2;

        g2.setStroke(new BasicStroke(3f));
        g2.setColor(brown);
        g2.drawOval(cx - faceRadius, cy - faceRadius, faceRadius * 2, faceRadius * 2);

        // eyes - filled brown circles
        g2.setColor(new Color(150, 75, 0));
        int eyeRadius = 14;
        int eyeOffsetX = 45;
        int eyeOffsetY = -25;
        g2.fillOval(cx - eyeOffsetX - eyeRadius, cy + eyeOffsetY - eyeRadius, eyeRadius*2, eyeRadius*2);
        g2.fillOval(cx + eyeOffsetX - eyeRadius, cy + eyeOffsetY - eyeRadius, eyeRadius*2, eyeRadius*2);

        // smile - arc
        g2.setColor(new Color(230,120,20));
        g2.setStroke(new BasicStroke(4f));
        int smileW = 120;
        int smileH = 80;
        g2.drawArc(cx - smileW/2, cy - smileH/4, smileW, smileH, 200, 140);
    }

    public static void main(String[] args) {
        // ensure AWT event thread creates UI
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MyShapesAWT();
            }
        });
    }
}
