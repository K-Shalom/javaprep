public class FertilizerAdvisorySystem {

    public static void processSamples(SoilAnalysis[] samples) {
        int balancedCount = 0;
        int deficientCount = 0;

        for (SoilAnalysis sample : samples) {
            try {
                String recommendation = sample.calculateFertilizerNeeded();
                System.out.println("\nFarmer ID: " + sample.getFarmerId());
                System.out.println("District: " + sample.getDistrictName());
                System.out.println("Crop Type: " + sample.getCropType());
                System.out.println("Recommendation: " + recommendation);

                if (sample.isBalanced()) balancedCount++;
                else deficientCount++;

            } catch (IllegalArgumentException e) {
                System.out.println("\nError for Farmer ID " + sample.getFarmerId() + ": " + e.getMessage());
            }
        }

        System.out.println("\n--- SUMMARY REPORT ---");
        System.out.println("Total Balanced Samples: " + balancedCount);
        System.out.println("Total Deficient Samples: " + deficientCount);
    }


    public static void main(String[] args) {
        SoilAnalysis[] samples = {
                new SoilAnalysis("F001", "Kirehe", 45, 60, 80, "Maize"),      // Balanced
                new SoilAnalysis("F002", "Bugesera", 10, 55, 70, "Rice"),     // Deficient (N low)
                new SoilAnalysis("F003", "Nyagatare", 120, 65, 90, "Beans"),  // Excess (N high)
                new SoilAnalysis("F004", "Gatsibo", -5, 40, 50, "Maize"),     // Invalid negative
                new SoilAnalysis("F005", "Karongi", 18, 15, 10, "Wheat")      // Deficient multiple
        };

        processSamples(samples);
    }
}
