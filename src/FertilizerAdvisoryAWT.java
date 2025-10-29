import java.awt.*;
import java.awt.event.*;

public class FertilizerAdvisoryAWT extends Frame implements ActionListener {

    TextField tFarmer, tDistrict, tCrop, tN, tP, tK;
    TextArea output;
    Button submit;

    public FertilizerAdvisoryAWT() {
        setLayout(null);

        // Labels & TextFields
        addLabel("Farmer ID:", 40, 50); tFarmer = addTextField(160, 50);
        addLabel("District:", 40, 90); tDistrict = addTextField(160, 90);
        addLabel("Crop Type:", 40, 130); tCrop = addTextField(160, 130);
        addLabel("Nitrogen (ppm):", 40, 170); tN = addTextField(160, 170);
        addLabel("Phosphorus (ppm):", 40, 210); tP = addTextField(160, 210);
        addLabel("Potassium (ppm):", 40, 250); tK = addTextField(160, 250);

        submit = new Button("Submit");
        submit.setBounds(120, 290, 80, 30);
        submit.addActionListener(this);
        add(submit);

        output = new TextArea();
        output.setBounds(50, 340, 260, 150);
        output.setEditable(false);
        add(output);

        setTitle("IhindukaConnect  Fertilizer Advisory");
        setSize(400, 520);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dispose(); }
        });
    }

    private void addLabel(String text, int x, int y) {
        Label l = new Label(text);
        l.setBounds(x, y, 120, 25);
        add(l);
    }

    private TextField addTextField(int x, int y) {
        TextField tf = new TextField();
        tf.setBounds(x, y, 150, 25);
        add(tf);
        return tf;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String farmer = tFarmer.getText().trim();
            String district = tDistrict.getText().trim();
            String crop = tCrop.getText().trim();
            double n = Double.parseDouble(tN.getText().trim());
            double p = Double.parseDouble(tP.getText().trim());
            double k = Double.parseDouble(tK.getText().trim());

            SoilAnalysis s = new SoilAnalysis(farmer, district, n, p, k, crop);
            String rec = s.calculateFertilizerNeeded();

            output.setText(
                    "Farmer: " + farmer +
                            "\nDistrict: " + district +
                            "\nCrop: " + crop +
                            "\n\nRecommendation:\n" + rec
            );

        } catch (NumberFormatException ex) {
            output.setText("ERROR: Please enter valid numeric values for N, P, K.");
        } catch (IllegalArgumentException ex) {
            output.setText("ERROR: " + ex.getMessage());
        } catch (Exception ex) {
            output.setText("Unexpected error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new FertilizerAdvisoryAWT();
    }
}
