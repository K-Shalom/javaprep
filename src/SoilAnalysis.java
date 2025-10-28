public class SoilAnalysis {

    private String farmerId;
    private String districtName;
    private double nitrogenLevel;
    private double phosphorusLevel;
    private double potassiumLevel;
    private String cropType;

    public SoilAnalysis(String farmerId, String districtName, double nitrogenLevel, double phosphorusLevel, double potassiumLevel, String cropType) {
        this.farmerId = farmerId;
        this.districtName = districtName;
        this.nitrogenLevel = nitrogenLevel;
        this.phosphorusLevel = phosphorusLevel;
        this.potassiumLevel = potassiumLevel;
        this.cropType = cropType;
    }

    // Getters
    public String getFarmerId() { return farmerId; }
    public String getDistrictName() { return districtName; }
    public double getNitrogenLevel() { return nitrogenLevel; }
    public double getPhosphorusLevel() { return phosphorusLevel; }
    public double getPotassiumLevel() { return potassiumLevel; }
    public String getCropType() { return cropType; }

    // Check if nutrients are balanced
    public boolean isBalanced() {
        return (nitrogenLevel >= 20 && nitrogenLevel <= 100) &&
                (phosphorusLevel >= 20 && phosphorusLevel <= 100) &&
                (potassiumLevel >= 20 && potassiumLevel <= 100);
    }

    // Fertilizer Recommendation
    public String calculateFertilizerNeeded() {
        // Validate readings
        if (nitrogenLevel <= 0 || phosphorusLevel <= 0 || potassiumLevel <= 0) {
            throw new IllegalArgumentException("Invalid nutrient reading");
        }

        String deficient = "";
        String excess = "";

        if (nitrogenLevel < 20) deficient += "Nitrogen ";
        if (phosphorusLevel < 20) deficient += "Phosphorus ";
        if (potassiumLevel < 20) deficient += "Potassium ";

        if (nitrogenLevel > 100) excess += "Nitrogen ";
        if (phosphorusLevel > 100) excess += "Phosphorus ";
        if (potassiumLevel > 100) excess += "Potassium ";

        if (isBalanced()) {
            return "OPTIMAL - Maintenance fertilizer only";
        } else if (!deficient.isEmpty()) {
            return "DEFICIENT - High application needed for " + deficient.trim();
        } else if (!excess.isEmpty()) {
            return "EXCESS - Reduce " + excess.trim() + " application";
        }

        return "No specific recommendation";
    }
}
