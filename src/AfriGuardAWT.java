import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * AfriGuardAWT.java
 * AWT-based surveillance dashboard for HealthAlert, OutbreakAlert, and EndemicAlert.
 *
 * Compile: javac AfriGuardAWT.java
 * Run:     java AfriGuardAWT
 *
 * Option C: Dashboard + Input Form
 */
public class AfriGuardAWT {

    // -------------------------
    // Domain classes
    // -------------------------
    public static abstract class HealthAlert {
        protected String countryCode;
        protected String diseaseName;
        protected int casesReported;
        protected String reportDate;

        public HealthAlert(String countryCode, String diseaseName, int casesReported, String reportDate) {
            this.countryCode = countryCode;
            this.diseaseName = diseaseName;
            this.casesReported = casesReported;
            this.reportDate = reportDate;
        }

        public abstract int calculateSeverity();

        public void displayBasicInfo(TextArea output) {
            output.append("Country: " + countryCode + " | Disease: " + diseaseName + " | Cases: " + casesReported + " | Date: " + reportDate + "\n");
        }

        public boolean requiresInternationalResponse() {
            return calculateSeverity() > 70;
        }

        @Override
        public String toString() {
            return String.format("%s - %s (%d) [%s]", countryCode, diseaseName, casesReported, reportDate);
        }
    }

    public static class OutbreakAlert extends HealthAlert {
        private double deathRate; // percent (e.g., 5.5 for 5.5%)

        public OutbreakAlert(String countryCode, String diseaseName, int casesReported, String reportDate, double deathRate) {
            super(countryCode, diseaseName, casesReported, reportDate);
            this.deathRate = deathRate;
        }

        @Override
        public int calculateSeverity() {
            // (casesReported / 10.0) + (deathRate * 20)
            double raw = (casesReported / 10.0) + (deathRate * 20.0);
            return (int) Math.round(raw);
        }

        public String getAlertLevel() {
            int sev = calculateSeverity();
            if (sev > 80) return "CRITICAL";
            if (sev > 50) return "HIGH";
            return "MODERATE";
        }

        public double getDeathRate() {
            return deathRate;
        }

        @Override
        public String toString() {
            return String.format("Outbreak[%s, deathRate=%.2f%%, severity=%d]", super.toString(), deathRate, calculateSeverity());
        }
    }

    public static class EndemicAlert extends HealthAlert {
        private String[] affectedRegions;

        public EndemicAlert(String countryCode, String diseaseName, int casesReported, String reportDate, String[] affectedRegions) {
            super(countryCode, diseaseName, casesReported, reportDate);
            this.affectedRegions = affectedRegions != null ? affectedRegions.clone() : new String[0];
        }

        @Override
        public int calculateSeverity() {
            // Interpreted formula: (casesReported / 100.0) + (affectedRegions.length * 10)
            double raw = (casesReported / 100.0) + (affectedRegions.length * 10.0);
            return (int) Math.round(raw);
        }

        public boolean isWidespread() {
            return affectedRegions.length > 3;
        }

        public String[] getAffectedRegions() {
            return affectedRegions.clone();
        }

        @Override
        public String toString() {
            return String.format("Endemic[%s, regions=%s, severity=%d]", super.toString(), Arrays.toString(affectedRegions), calculateSeverity());
        }
    }

    // -------------------------
    // Surveillance Dashboard logic (non-GUI)
    // -------------------------
    public static class SurveillanceDashboard {
        /**
         * Processes an array of HealthAlert objects, using polymorphism.
         * Handles NullPointerException and ArrayIndexOutOfBoundsException when iterating.
         * Displays information to given TextArea.
         */
        public void processAlerts(HealthAlert[] alerts, TextArea output) {
            if (alerts == null) {
                output.append("No alerts to process (alerts array is null).\n");
                return;
            }

            output.append("=== START SURVEILLANCE DASHBOARD ===\n");
            HealthAlert highest = null;
            int highestSeverity = Integer.MIN_VALUE;

            for (int i = 0; i < alerts.length; i++) {
                try {
                    HealthAlert alert = alerts[i]; // may be null
                    if (alert == null) {
                        output.append(String.format("Alert at index %d is null — skipping (null handling test).\n", i));
                        continue;
                    }

                    // Polymorphic calls
                    alert.displayBasicInfo(output);
                    int severity = alert.calculateSeverity();
                    output.append(" -> Calculated severity: " + severity + "\n");

                    if (alert.requiresInternationalResponse()) {
                        output.append(" -> ACTION: Requires INTERNATIONAL RESPONSE!\n");
                    } else {
                        output.append(" -> Local response likely sufficient.\n");
                    }

                    // Subclass-specific info
                    if (alert instanceof OutbreakAlert) {
                        OutbreakAlert oa = (OutbreakAlert) alert;
                        output.append(String.format("    [Outbreak] Death rate = %.2f%% | Level = %s\n", oa.getDeathRate(), oa.getAlertLevel()));
                    } else if (alert instanceof EndemicAlert) {
                        EndemicAlert ea = (EndemicAlert) alert;
                        output.append(String.format("    [Endemic] Regions affected = %s | Widespread = %b\n", Arrays.toString(ea.getAffectedRegions()), ea.isWidespread()));
                    }

                    // Track highest severity
                    if (severity > highestSeverity) {
                        highestSeverity = severity;
                        highest = alert;
                    }

                } catch (NullPointerException npe) {
                    output.append("Encountered NullPointerException while processing index " + i + ": " + npe.getMessage() + "\n");
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    output.append("Array index error while processing alerts: " + aioobe.getMessage() + "\n");
                } catch (Exception ex) {
                    output.append("Unexpected error processing alert at index " + i + ": " + ex.getMessage() + "\n");
                }

                output.append("-------------------------------------------------\n");
            }

            output.append("=== SUMMARY ===\n");
            if (highest != null) {
                output.append("Highest severity alert:\n");
                highest.displayBasicInfo(output);
                output.append("Severity: " + highest.calculateSeverity() + "\n");
                output.append("Full details: " + highest.toString() + "\n");
            } else {
                output.append("No valid alerts processed.\n");
            }

            output.append("=== END SURVEILLANCE DASHBOARD ===\n\n");
        }

        /**
         * Parse comma-separated report string to produce OutbreakAlert or EndemicAlert.
         * Expected: "COUNTRY,DISEASE,CASES,DEATHS,REGIONS"
         * If DEATHS present & parseable -> OutbreakAlert (deathRate calculated)
         * Else if REGIONS present -> EndemicAlert
         * Returns null on parsing failure.
         */
        public HealthAlert parseReportData(String reportData, TextArea output) {
            if (reportData == null) return null;

            try {
                String[] parts = reportData.split(",", -1);
                for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();

                if (parts.length < 3) {
                    output.append("Parsing failed: insufficient fields in \"" + reportData + "\"\n");
                    return null;
                }

                String country = parts[0];
                String disease = parts[1];
                int cases = Integer.parseInt(parts[2]);
                String reportDate = java.time.LocalDate.now().toString();

                // If deaths field present and numeric -> OutbreakAlert
                if (parts.length >= 4 && !parts[3].isEmpty()) {
                    try {
                        int deaths = Integer.parseInt(parts[3]);
                        double deathRate = cases > 0 ? (deaths * 100.0) / cases : 0.0;
                        return new OutbreakAlert(country, disease, cases, reportDate, deathRate);
                    } catch (NumberFormatException nfeDeaths) {
                        // Not numeric -> fall through to check regions
                    }
                }

                // Regions (could be parts[4] or parts[3] if deaths empty)
                String regionsRaw = "";
                if (parts.length >= 5) regionsRaw = parts[4];
                else if (parts.length >= 4 && parts[3] != null && parts[3].trim().length() > 0 && !isNumeric(parts[3])) {
                    // If deaths slot non-numeric, assume it's regions
                    regionsRaw = parts[3];
                }

                if (!regionsRaw.isEmpty()) {
                    String[] regions = regionsRaw.split("\\s+|;|\\||,");
                    regions = Arrays.stream(regions).map(String::trim).filter(s -> !s.isEmpty()).toArray(String[]::new);
                    return new EndemicAlert(country, disease, cases, reportDate, regions);
                }

                output.append("Parsing ambiguous (no deaths numeric and no regions) for: \"" + reportData + "\"\n");
                return null;

            } catch (NumberFormatException nfe) {
                output.append("Number format error while parsing \"" + reportData + "\": " + nfe.getMessage() + "\n");
                return null;
            } catch (Exception ex) {
                output.append("Unexpected parse error for \"" + reportData + "\": " + ex.getMessage() + "\n");
                return null;
            }
        }

        private boolean isNumeric(String s) {
            if (s == null) return false;
            try {
                Integer.parseInt(s.trim());
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

    // -------------------------
    // AWT GUI
    // -------------------------
    public static class DashboardFrame extends Frame implements ActionListener, WindowListener {
        private final TextField tfCountry = new TextField(6);
        private final TextField tfDisease = new TextField(12);
        private final TextField tfCases = new TextField(6);
        private final TextField tfDeaths = new TextField(6);
        private final TextField tfRegions = new TextField(20);
        private final TextField tfDate = new TextField(10);

        private final Button btnAdd = new Button("Add Alert");
        private final Button btnParse = new Button("Parse Report");
        private final Button btnProcess = new Button("Process Alerts");
        private final Button btnDemo = new Button("Run Demo (seed & process)");
        private final Button btnClear = new Button("Clear Output");

        private final List alertList = new List();
        private final TextArea outputArea = new TextArea("", 18, 80, TextArea.SCROLLBARS_VERTICAL_ONLY);

        private final ArrayList<HealthAlert> alerts = new ArrayList<>();
        private final SurveillanceDashboard dashboardLogic = new SurveillanceDashboard();

        public DashboardFrame() {
            super("AfriGuard - AWT Surveillance Dashboard (Option C)");
            setLayout(new BorderLayout(6, 6));
            addWindowListener(this);

            // Top: input form
            Panel top = new Panel(new FlowLayout(FlowLayout.LEFT, 6, 6));
            top.add(new Label("Country:")); top.add(tfCountry);
            top.add(new Label("Disease:")); top.add(tfDisease);
            top.add(new Label("Cases:")); top.add(tfCases);
            top.add(new Label("Deaths:")); top.add(tfDeaths);
            top.add(new Label("Regions (space-separated):")); top.add(tfRegions);
            top.add(new Label("Date:")); top.add(tfDate);
            add(top, BorderLayout.NORTH);

            // Left: alert list & buttons
            Panel left = new Panel(new BorderLayout(6,6));
            left.add(new Label("Alerts (click to view):"), BorderLayout.NORTH);
            left.add(alertList, BorderLayout.CENTER);

            Panel leftButtons = new Panel(new GridLayout(5,1,4,4));
            leftButtons.add(btnAdd); leftButtons.add(btnParse); leftButtons.add(btnProcess); leftButtons.add(btnDemo); leftButtons.add(btnClear);
            left.add(leftButtons, BorderLayout.SOUTH);
            add(left, BorderLayout.WEST);

            // Center: output area
            Panel center = new Panel(new BorderLayout(6,6));
            center.add(new Label("Output / Logs:"), BorderLayout.NORTH);
            center.add(outputArea, BorderLayout.CENTER);
            add(center, BorderLayout.CENTER);

            // add listeners
            btnAdd.addActionListener(this);
            btnParse.addActionListener(this);
            btnProcess.addActionListener(this);
            btnDemo.addActionListener(this);
            btnClear.addActionListener(this);
            alertList.addActionListener(this);

            // initial size and show
            setSize(1100, 600);
            setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            if (src == btnAdd) {
                addAlertFromForm();
            } else if (src == btnParse) {
                parseReportFromForm();
            } else if (src == btnProcess) {
                processAlertsCurrent();
            } else if (src == btnDemo) {
                seedDemoAlertsAndProcess();
            } else if (src == btnClear) {
                outputArea.setText("");
            } else if (src == alertList) {
                int idx = alertList.getSelectedIndex();
                if (idx >= 0 && idx < alerts.size()) {
                    HealthAlert ha = alerts.get(idx);
                    outputArea.append("Selected: " + (ha == null ? "NULL" : ha.toString()) + "\n");
                }
            }
        }

        // Adds an alert constructed from input fields
        private void addAlertFromForm() {
            String c = tfCountry.getText().trim();
            String d = tfDisease.getText().trim();
            String casesStr = tfCases.getText().trim();
            String deathsStr = tfDeaths.getText().trim();
            String regionsStr = tfRegions.getText().trim();
            String date = tfDate.getText().trim();
            if (date.isEmpty()) date = java.time.LocalDate.now().toString();

            if (c.isEmpty() || d.isEmpty() || casesStr.isEmpty()) {
                showDialog("Please fill at least Country, Disease and Cases fields.");
                return;
            }

            try {
                int cases = Integer.parseInt(casesStr);
                if (!deathsStr.isEmpty()) {
                    // create OutbreakAlert
                    int deaths = Integer.parseInt(deathsStr);
                    double deathRate = cases > 0 ? (deaths * 100.0) / cases : 0.0;
                    OutbreakAlert oa = new OutbreakAlert(c, d, cases, date, deathRate);
                    alerts.add(oa);
                    alertList.add(oa.toString());
                    outputArea.append("Added OutbreakAlert: " + oa.toString() + "\n");
                } else if (!regionsStr.isEmpty()) {
                    // create EndemicAlert
                    String[] regions = regionsStr.split("\\s+");
                    EndemicAlert ea = new EndemicAlert(c, d, cases, date, regions);
                    alerts.add(ea);
                    alertList.add(ea.toString());
                    outputArea.append("Added EndemicAlert: " + ea.toString() + "\n");
                } else {
                    // If neither deaths nor regions, default to Endemic with zero regions (ambiguous)
                    EndemicAlert ea = new EndemicAlert(c, d, cases, date, new String[]{});
                    alerts.add(ea);
                    alertList.add(ea.toString());
                    outputArea.append("Added EndemicAlert (no regions): " + ea.toString() + "\n");
                }

                // clear input fields (except date)
                tfCountry.setText(""); tfDisease.setText(""); tfCases.setText(""); tfDeaths.setText(""); tfRegions.setText("");
            } catch (NumberFormatException nfe) {
                showDialog("Number format error: " + nfe.getMessage());
            } catch (Exception ex) {
                showDialog("Unexpected error adding alert: " + ex.getMessage());
            }
        }

        // Parse using parseReportData and add to list (report string typed in Disease field for convenience)
        private void parseReportFromForm() {
            // We'll read from disease field as the report string (short-cut)
            String report = tfDisease.getText().trim();
            if (report.isEmpty()) {
                showDialog("Please paste the report string to parse into the 'Disease' field (for quick test). Format:\nCOUNTRY,DISEASE,CASES,DEATHS,REGIONS");
                return;
            }

            HealthAlert parsed = dashboardLogic.parseReportData(report, outputArea);
            if (parsed != null) {
                alerts.add(parsed);
                alertList.add(parsed.toString());
                outputArea.append("Parsed and added alert: " + parsed.toString() + "\n");
            } else {
                outputArea.append("Parsing returned null for: " + report + "\n");
            }
        }

        // Convert alerts to array (intentionally insert null to test handling) then call processAlerts
        private void processAlertsCurrent() {
            // build array, include a null if the list doesn't have one (to test handling)
            HealthAlert[] arr = new HealthAlert[alerts.size() + 1]; // add extra slot for null test
            for (int i = 0; i < alerts.size(); i++) arr[i] = alerts.get(i);
            arr[arr.length - 1] = null; // intentional null element
            outputArea.append("Processing " + arr.length + " alerts (includes intentional null) ...\n");
            dashboardLogic.processAlerts(arr, outputArea);
        }

        // Seed with at least 6 alerts (mix), include null, and process
        private void seedDemoAlertsAndProcess() {
            alerts.clear();
            alertList.removeAll();

            // 1) OutbreakAlert
            alerts.add(new OutbreakAlert("NG", "LASSA_FEVER", 450, "2025-10-27", 5.1));
            alertList.add(alerts.get(alerts.size()-1).toString());

            // 2) EndemicAlert (widespread)
            alerts.add(new EndemicAlert("KE", "MALARIA", 12000, "2025-10-26", new String[]{"Nairobi","Mombasa","Kisumu","Nakuru"}));
            alertList.add(alerts.get(alerts.size()-1).toString());

            // 3) Outbreak requiring international response
            alerts.add(new OutbreakAlert("ET", "VHF", 2000, "2025-10-25", 3.5));
            alertList.add(alerts.get(alerts.size()-1).toString());

            // 4) EndemicAlert
            alerts.add(new EndemicAlert("UG", "CHOLERA", 800, "2025-10-20", new String[]{"Central","East"}));
            alertList.add(alerts.get(alerts.size()-1).toString());

            // 5) Another Outbreak
            alerts.add(new OutbreakAlert("ZA", "MEASLES", 300, "2025-10-23", 1.2));
            alertList.add(alerts.get(alerts.size()-1).toString());

            // 6) EndemicAlert
            alerts.add(new EndemicAlert("GH", "HEPATITIS", 2500, "2025-10-22", new String[]{"Accra","Kumasi","Tamale"}));
            alertList.add(alerts.get(alerts.size()-1).toString());

            // Add an explicit null into the ArrayList to test null handling (this will also appear in process array)
            alerts.add(null);
            alertList.add("NULL (intentional)");

            outputArea.append("Demo alerts seeded (" + alerts.size() + " items, including null). Now processing...\n");
            HealthAlert[] arr = alerts.toArray(new HealthAlert[0]);
            dashboardLogic.processAlerts(arr, outputArea);
        }

        private void showDialog(String msg) {
            Dialog dlg = new Dialog(this, "Message", true);
            dlg.setLayout(new BorderLayout(6,6));
            TextArea ta = new TextArea(msg, 6, 40, TextArea.SCROLLBARS_NONE);
            ta.setEditable(false);
            dlg.add(ta, BorderLayout.CENTER);
            Button ok = new Button("OK");
            ok.addActionListener(ev -> dlg.dispose());
            Panel p = new Panel();
            p.add(ok);
            dlg.add(p, BorderLayout.SOUTH);
            dlg.setSize(420, 180);
            dlg.setLocationRelativeTo(this);
            dlg.setVisible(true);
        }

        // WindowListener
        public void windowOpened(WindowEvent e) {}
        public void windowClosing(WindowEvent e) { dispose(); }
        public void windowClosed(WindowEvent e) { System.exit(0); }
        public void windowIconified(WindowEvent e) {}
        public void windowDeiconified(WindowEvent e) {}
        public void windowActivated(WindowEvent e) {}
        public void windowDeactivated(WindowEvent e) {}
    }

    // -------------------------
    // Main
    // -------------------------
    public static void main(String[] args) {
        // Run GUI on AWT event thread
        EventQueue.invokeLater(() -> {
            try {
                new DashboardFrame();
            } catch (Exception ex) {
                System.err.println("Failed to start AfriGuard AWT GUI: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
}
