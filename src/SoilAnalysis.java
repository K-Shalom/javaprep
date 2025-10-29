public class SoilAnalysis {
    private String farmerId;
    private String districtName;
    private double nitrogenLevel;
    private double phosphorusLevel;
    private double potassiumLevel;
    private String cropType;

    public SoilAnalysis(String farmerId, String districtName,
                        double nitrogenLevel, double phosphorusLevel,
                        double potassiumLevel, String cropType) {
        this.farmerId = farmerId;
        this.districtName = districtName;
        this.nitrogenLevel = nitrogenLevel;
        this.phosphorusLevel = phosphorusLevel;
        this.potassiumLevel = potassiumLevel;
        this.cropType = cropType;
    }

    public String getFarmerId() { return farmerId; }
    public String getDistrictName() { return districtName; }
    public String getCropType() { return cropType; }

    public String calculateFertilizerNeeded() {
        if (nitrogenLevel <= 0 || phosphorusLevel <= 0 || potassiumLevel <= 0) {
            throw new IllegalArgumentException("Invalid nutrient reading (must be > 0)");
        }

        boolean balanced = true;
        StringBuilder deficient = new StringBuilder();
        StringBuilder excess = new StringBuilder();

        if (nitrogenLevel < 20) { deficient.append("Nitrogen "); balanced = false; }
        if (phosphorusLevel < 20) { deficient.append("Phosphorus "); balanced = false; }
        if (potassiumLevel < 20) { deficient.append("Potassium "); balanced = false; }

        if (nitrogenLevel > 100) { excess.append("Nitrogen "); balanced = false; }
        if (phosphorusLevel > 100) { excess.append("Phosphorus "); balanced = false; }
        if (potassiumLevel > 100) { excess.append("Potassium "); balanced = false; }

        if (balanced) return " OPTIMAL - Maintenance fertilizer only";
        if (deficient.length() > 0) return " DEFICIENT - High application needed for: " + deficient.toString().trim();
        if (excess.length() > 0) return " EXCESS - Reduce application for: " + excess.toString().trim();

        return "Normal";
    }
}
