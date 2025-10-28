import java.awt.*;
import java.awt.event.*;

public class AllControlsDemo extends Frame {
    public AllControlsDemo() {
        setTitle("AWT Controls Demo");
        setSize(600, 600);
        setLayout(new FlowLayout());

        // 1. Label
        Label label = new Label("This is a Label");
        add(label);

        // 2. Button
        Button button = new Button("Click Me");
        add(button);

        // 3. Checkbox
        Checkbox checkbox1 = new Checkbox("Option 1");
        Checkbox checkbox2 = new Checkbox("Option 2", true);
        add(checkbox1);
        add(checkbox2);

        // 4. CheckboxGroup
        CheckboxGroup group = new CheckboxGroup();
        Checkbox cbg1 = new Checkbox("Group 1", group, false);
        Checkbox cbg2 = new Checkbox("Group 2", group, true);
        add(cbg1);
        add(cbg2);

        // 5. List
        List list = new List(3, false); // 3 visible rows, multiple selection false
        list.add("Item 1");
        list.add("Item 2");
        list.add("Item 3");
        add(list);

        // 6. TextField
        TextField tf = new TextField("Single line text", 20);
        add(tf);

        // 7. TextArea
        TextArea ta = new TextArea("This is a TextArea.\nMultiple lines allowed.", 5, 20);
        add(ta);

        // 8. Choice
        Choice choice = new Choice();
        choice.add("Choice 1");
        choice.add("Choice 2");
        choice.add("Choice 3");
        add(choice);

        // 9. Canvas
        Canvas canvas = new Canvas() {
            public void paint(Graphics g) {
                g.setColor(Color.RED);
                g.fillRect(10, 10, 100, 50);
            }
        };
        canvas.setSize(120, 70);
        canvas.setBackground(Color.LIGHT_GRAY);
        add(canvas);

        // 10. Image
        Image img = Toolkit.getDefaultToolkit().getImage("example.png"); // Replace with your image path
        Canvas imgCanvas = new Canvas() {
            public void paint(Graphics g) {
                g.drawImage(img, 0, 0, 100, 100, this);
            }
        };
        imgCanvas.setSize(100, 100);
        add(imgCanvas);

        // 11. Scrollbar
        Scrollbar scrollbar = new Scrollbar(Scrollbar.HORIZONTAL, 50, 10, 0, 100);
        add(scrollbar);

        // 12. Dialog
        Button showDialogBtn = new Button("Show Dialog");
        add(showDialogBtn);
        showDialogBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Dialog dialog = new Dialog(AllControlsDemo.this, "Dialog Example", true);
                dialog.setLayout(new FlowLayout());
                dialog.setSize(200, 100);
                dialog.add(new Label("This is a Dialog"));
                Button ok = new Button("OK");
                ok.addActionListener(ev -> dialog.dispose());
                dialog.add(ok);
                dialog.setVisible(true);
            }
        });

        // 13. FileDialog
        Button fileDialogBtn = new Button("Open File Dialog");
        add(fileDialogBtn);
        fileDialogBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(AllControlsDemo.this, "Select a file", FileDialog.LOAD);
                fd.setVisible(true);
                System.out.println("Selected file to add: " + fd.getFile());
            }
        });

        // Close window
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new AllControlsDemo();
    }
}
