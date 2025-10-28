// AfriGuardSurveillance.java
// Compile: javac AfriGuardSurveillance.java
// Run: java AfriGuardSurveillance

import java.util.Arrays;

abstract class HealthAlert {
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

    // abstracts severity calculation
    public abstract int calculateSeverity();

    // concrete: display basic info
    public void displayBasicInfo() {
        System.out.println("Country: " + countryCode + " | Disease: " + diseaseName + " | Cases: " + casesReported + " | Date: " + reportDate);
    }

    // concrete: requires international response if severity > 70
    public boolean requiresInternationalResponse() {
        return calculateSeverity() > 70;
    }
}

class OutbreakAlert extends HealthAlert {
    private double deathRate; // expressed as percentage (e.g., 5.5 for 5.5%)

    public OutbreakAlert(String countryCode, String diseaseName, int casesReported, String reportDate, double deathRate) {
        super(countryCode, diseaseName, casesReported, reportDate);
        this.deathRate = deathRate;
    }

    @Override
    public int calculateSeverity() {
        // formula given: (casesReported/10.0) + (deathRate * 20)
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
        return "OutbreakAlert{" +
                "country='" + countryCode + '\'' +
                ", disease='" + diseaseName + '\'' +
                ", cases=" + casesReported +
                ", date=" + reportDate +
                ", deathRate=" + deathRate +
                ", severity=" + calculateSeverity() +
                ", level=" + getAlertLevel() +
                '}';
    }
}

class EndemicAlert extends HealthAlert {
    private String[] affectedRegions;

    public EndemicAlert(String countryCode, String diseaseName, int casesReported, String reportDate, String[] affectedRegions) {
        super(countryCode, diseaseName, casesReported, reportDate);
        this.affectedRegions = affectedRegions != null ? affectedRegions.clone() : new String[0];
    }

    @Override
    public int calculateSeverity() {
        // The prompt's formula was garbled. Reasonable interpretation:
        // severity = (casesReported / 100.0) + (affectedRegions.length * 10)
        // Then convert to int.
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
        return "EndemicAlert{" +
                "country='" + countryCode + '\'' +
                ", disease='" + diseaseName + '\'' +
                ", cases=" + casesReported +
                ", date=" + reportDate +
                ", regions=" + Arrays.toString(affectedRegions) +
                ", severity=" + calculateSeverity() +
                ", widespread=" + isWidespread() +
                '}';
    }
}

class SurveillanceDashboard {

    // Process array of alerts polymorphically with exception handling
    public void processAlerts(HealthAlert[] alerts) {
        if (alerts == null) {
            System.out.println("No alerts to process (alerts array is null).");
            return;
        }

        HealthAlert highest = null;
        int highestSeverity = Integer.MIN_VALUE;

        System.out.println("=== START SURVEILLANCE DASHBOARD ===");

        for (int i = 0; i < alerts.length; i++) {
            try {
                // Attempt to access element (may be null)
                HealthAlert alert = alerts[i]; // could be null -> handle below
                if (alert == null) {
                    // Trigger aside message rather than throwing NPE
                    System.out.println("Alert at index " + i + " is null — skipping (test of null handling).");
                    continue;
                }

                // Polymorphic calls
                alert.displayBasicInfo();
                int severity = alert.calculateSeverity();
                System.out.println(" -> Calculated severity: " + severity);

                if (alert.requiresInternationalResponse()) {
                    System.out.println(" -> ACTION: Requires INTERNATIONAL RESPONSE!");
                } else {
                    System.out.println(" -> Local response likely sufficient.");
                }

                // Additional subclass-specific info with instanceof
                if (alert instanceof OutbreakAlert) {
                    OutbreakAlert oa = (OutbreakAlert) alert;
                    System.out.println("    [Outbreak] Death rate (%) = " + oa.getDeathRate() + " | Alert Level = " + oa.getAlertLevel());
                } else if (alert instanceof EndemicAlert) {
                    EndemicAlert ea = (EndemicAlert) alert;
                    System.out.println("    [Endemic] Regions affected = " + Arrays.toString(ea.getAffectedRegions()) + " | Widespread = " + ea.isWidespread());
                }

                // Track highest severity
                if (severity > highestSeverity) {
                    highestSeverity = severity;
                    highest = alert;
                }

            } catch (NullPointerException npe) {
                // Defensive - though we handled nulls above, catch unexpected NPE
                System.out.println("Encountered a NullPointerException while processing index " + i + ": " + npe.getMessage());
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                // Defensive - not expected during standard loop, but handled per spec
                System.out.println("Array index error while processing alerts: " + aioobe.getMessage());
            } catch (Exception ex) {
                // Catch-all so dashboard doesn't crash on unexpected problems
                System.out.println("Unexpected error processing alert at index " + i + ": " + ex.getMessage());
            }

            System.out.println("-------------------------------------------------");
        }

        System.out.println("=== SUMMARY ===");
        if (highest != null) {
            System.out.println("Highest severity alert:");
            highest.displayBasicInfo();
            System.out.println("Severity: " + highest.calculateSeverity());
            System.out.println("Full details: " + highest.toString());
        } else {
            System.out.println("No valid alerts processed.");
        }

        System.out.println("=== END SURVEILLANCE DASHBOARD ===");
    }

    // Parse comma-separated report data and return appropriate HealthAlert object or null on failure
    // Expected format: "COUNTRY,Disease,CASES,DEATHS,REGION1 REGION2 REGION3"
    // NOTES:
    //  - If DEATHS field present and parseable -> produce OutbreakAlert
    //  - Else if REGIONS present -> produce EndemicAlert
    //  - reportDate is set to current placeholder (could be extended)
    public HealthAlert parseReportData(String reportData) {
        if (reportData == null) return null;

        try {
            // Split by comma first
            String[] parts = reportData.split(",", -1); // keep trailing empty fields
            // Trim parts
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].trim();
            }

            if (parts.length < 3) {
                // not enough fields
                System.out.println("Parsing failed: insufficient fields in \"" + reportData + "\"");
                return null;
            }

            String country = parts[0];
            String disease = parts[1];
            int cases = Integer.parseInt(parts[2]);

            String reportDate = java.time.LocalDate.now().toString(); // simple date

            // If there's a deaths field and it looks numeric -> OutbreakAlert
            if (parts.length >= 4 && !parts[3].isEmpty()) {
                try {
                    int deaths = Integer.parseInt(parts[3]);
                    double deathRatePercent = 0.0;
                    if (cases > 0) {
                        deathRatePercent = (deaths * 100.0) / cases; // percent
                    }
                    return new OutbreakAlert(country, disease, cases, reportDate, deathRatePercent);
                } catch (NumberFormatException nfeDeaths) {
                    // Not numeric deaths -> maybe it's actually regions string (user misordered). fall through.
                }
            }

            // If there is a regions field (4th or 5th), build EndemicAlert
            if (parts.length >= 5) {
                // parts[4] expected to be region list, maybe space-separated
                String regionsRaw = parts[4];
                if (!regionsRaw.isEmpty()) {
                    // Split by whitespace, semicolon, or pipe if user used different separators
                    String[] regions = regionsRaw.split("\\s+|;|\\||,");
                    // clean up blanks
                    regions = Arrays.stream(regions).map(String::trim).filter(s -> !s.isEmpty()).toArray(String[]::new);
                    return new EndemicAlert(country, disease, cases, reportDate, regions);
                }
            }

            // If we reach here, no deaths numeric and no regions: ambiguous.
            System.out.println("Parsing result ambiguous (no deaths numeric and no regions) for: \"" + reportData + "\"");
            return null;

        } catch (NumberFormatException nfe) {
            System.out.println("Number format error while parsing \"" + reportData + "\": " + nfe.getMessage());
            return null;
        } catch (Exception ex) {
            System.out.println("Unexpected parse error for \"" + reportData + "\": " + ex.getMessage());
            return null;
        }
    }
}

public class AfriGuardSurveillance {
    public static void main(String[] args) {
        SurveillanceDashboard dashboard = new SurveillanceDashboard();

        // Create sample alerts (mix of Outbreak and Endemic). At least 6 objects including a null.
        HealthAlert[] alerts = new HealthAlert[7];

        // 1) OutbreakAlert (moderate)
        alerts[0] = new OutbreakAlert("NG", "LASSA_FEVER", 450, "2025-10-27", 5.1); // deathRate=5.1%

        // 2) EndemicAlert (several regions)
        alerts[1] = new EndemicAlert("KE", "MALARIA", 12000, "2025-10-26", new String[]{"Nairobi", "Mombasa", "Kisumu", "Nakuru"}); // widespread

        // 3) OutbreakAlert that should require international response (high severity)
        // Make severity large: large cases OR high deathRate: e.g., cases=2000, deathRate=3.5 -> severity = (2000/10)=200 + (3.5*20)=70 => 270
        alerts[2] = new OutbreakAlert("ET", "VIRAL_HEMORRHAGIC_FEVER", 2000, "2025-10-25", 3.5);

        // 4) EndemicAlert (few regions)
        alerts[3] = new EndemicAlert("UG", "CHOLERA", 800, "2025-10-20", new String[]{"Central", "East"});

        // 5) Null element to test exception handling
        alerts[4] = null;

        // 6) Another OutbreakAlert (different country/disease)
        alerts[5] = new OutbreakAlert("ZA", "MEASLES", 300, "2025-10-23", 1.2);

        // 7) EndemicAlert
        alerts[6] = new EndemicAlert("GH", "HEPATITIS", 2500, "2025-10-22", new String[]{"Accra", "Kumasi", "Tamale"});

        // Demonstrate parseReportData with valid and invalid examples
        String validReport = "NG,LASSA_FEVER,450,23,";
        String validReport2 = "TZ,BRUCELLOSIS,900,,North South East"; // deaths empty but regions present -> endemic
        String invalidReport = "XX,UNKNOWN,not_a_number,10,North";

        System.out.println("\n=== PARSE REPORT DATA TESTS ===");
        System.out.println("Parsing valid: \"" + validReport + "\"");
        HealthAlert parsed1 = dashboard.parseReportData(validReport);
        if (parsed1 != null) {
            System.out.println("Parsed into: " + parsed1.toString());
        } else {
            System.out.println("Parsing returned null for validReport (unexpected).");
        }

        System.out.println("\nParsing valid (regions): \"" + validReport2 + "\"");
        HealthAlert parsed2 = dashboard.parseReportData(validReport2);
        if (parsed2 != null) {
            System.out.println("Parsed into: " + parsed2.toString());
        } else {
            System.out.println("Parsing returned null for validReport2 (unexpected).");
        }

        System.out.println("\nParsing invalid: \"" + invalidReport + "\"");
        HealthAlert parsedInvalid = dashboard.parseReportData(invalidReport);
        if (parsedInvalid == null) {
            System.out.println("Parsing failed as expected for invalid report.");
        } else {
            System.out.println("Parsing unexpectedly succeeded: " + parsedInvalid.toString());
        }

        // Optionally add parsed alerts into the alerts array if desired (demonstrating polymorphism)
        // For demonstration, replace the null slot with parsed1 if not null
        if (parsed1 != null) {
            alerts[4] = parsed1; // overwrite the null to show parse integration
        }

        // Now call processAlerts to generate the surveillance dashboard
        System.out.println("\n\n=== RUNNING PROCESS ALERTS ===\n");
        dashboard.processAlerts(alerts);
    }
}
