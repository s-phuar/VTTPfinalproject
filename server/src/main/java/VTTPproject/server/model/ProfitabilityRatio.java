package VTTPproject.server.model;

import VTTPproject.server.utils.Utils;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class ProfitabilityRatio {
    private double grossProfitMarginTTM;
    private double operatingProfitMarginTTM;
    private double pretaxProfitMarginTTM;
    private double netProfitMarginTTM;
    private double returnOnAssetsTTM;
    private double returnOnEquityTTM;
    private double returnOnCapitalEmployedTTM;
    private double companyEquityMultiplierTTM; //financial leverage

    public double getGrossProfitMarginTTM() {return grossProfitMarginTTM;}
    public void setGrossProfitMarginTTM(double grossProfitMarginTTM) {this.grossProfitMarginTTM = grossProfitMarginTTM;}
    public double getOperatingProfitMarginTTM() {return operatingProfitMarginTTM;}
    public void setOperatingProfitMarginTTM(double operatingProfitMarginTTM) {this.operatingProfitMarginTTM = operatingProfitMarginTTM;}
    public double getPretaxProfitMarginTTM() {return pretaxProfitMarginTTM;}
    public void setPretaxProfitMarginTTM(double pretaxProfitMarginTTM) {this.pretaxProfitMarginTTM = pretaxProfitMarginTTM;}
    public double getNetProfitMarginTTM() {return netProfitMarginTTM;}
    public void setNetProfitMarginTTM(double netProfitMarginTTM) {this.netProfitMarginTTM = netProfitMarginTTM;}
    public double getReturnOnAssetsTTM() {return returnOnAssetsTTM;}
    public void setReturnOnAssetsTTM(double returnOnAssetsTTM) {this.returnOnAssetsTTM = returnOnAssetsTTM;}
    public double getReturnOnEquityTTM() {return returnOnEquityTTM;}
    public void setReturnOnEquityTTM(double returnOnEquityTTM) {this.returnOnEquityTTM = returnOnEquityTTM;}
    public double getReturnOnCapitalEmployedTTM() {return returnOnCapitalEmployedTTM;}
    public void setReturnOnCapitalEmployedTTM(double returnOnCapitalEmployedTTM) {this.returnOnCapitalEmployedTTM = returnOnCapitalEmployedTTM;}
    public double getCompanyEquityMultiplierTTM() {return companyEquityMultiplierTTM;}
    public void setCompanyEquityMultiplierTTM(double companyEquityMultiplierTTM) {this.companyEquityMultiplierTTM = companyEquityMultiplierTTM;}

    @Override
    public String toString() {
        return "ProfitabilityRatio [grossProfitMarginTTM=" + grossProfitMarginTTM + ", operatingProfitMarginTTM="
                + operatingProfitMarginTTM + ", pretaxProfitMarginTTM=" + pretaxProfitMarginTTM
                + ", netProfitMarginTTM=" + netProfitMarginTTM + ", returnOnAssetsTTM=" + returnOnAssetsTTM
                + ", returnOnEquityTTM=" + returnOnEquityTTM + ", returnOnCapitalEmployedTTM="
                + returnOnCapitalEmployedTTM + ", companyEquityMultiplierTTM=" + companyEquityMultiplierTTM + "]";
    }

    public static ProfitabilityRatio toProfObj(JsonObject additionalRatio){
        ProfitabilityRatio pr = new ProfitabilityRatio();
        pr.setGrossProfitMarginTTM(Utils.roundValue(additionalRatio.getJsonNumber("grossProfitMarginTTM").doubleValue()));
        pr.setOperatingProfitMarginTTM(Utils.roundValue(additionalRatio.getJsonNumber("operatingProfitMarginTTM").doubleValue()));
        pr.setPretaxProfitMarginTTM(Utils.roundValue(additionalRatio.getJsonNumber("pretaxProfitMarginTTM").doubleValue()));
        pr.setNetProfitMarginTTM(Utils.roundValue(additionalRatio.getJsonNumber("netProfitMarginTTM").doubleValue()));
        pr.setReturnOnAssetsTTM(Utils.roundValue(additionalRatio.getJsonNumber("returnOnAssetsTTM").doubleValue()));
        pr.setReturnOnEquityTTM(Utils.roundValue(additionalRatio.getJsonNumber("returnOnEquityTTM").doubleValue()));
        pr.setReturnOnCapitalEmployedTTM(Utils.roundValue(additionalRatio.getJsonNumber("returnOnCapitalEmployedTTM").doubleValue()));
        pr.setCompanyEquityMultiplierTTM(Utils.roundValue(additionalRatio.getJsonNumber("companyEquityMultiplierTTM").doubleValue()));

        return pr;
    }


    public static JsonObject buildProfitabilityRatioJson(ProfitabilityRatio pr) {
        return Json.createObjectBuilder()
            .add("grossProfitMarginTTM", pr.getGrossProfitMarginTTM())
            .add("operatingProfitMarginTTM", pr.getOperatingProfitMarginTTM())
            .add("pretaxProfitMarginTTM", pr.getPretaxProfitMarginTTM())
            .add("netProfitMarginTTM", pr.getNetProfitMarginTTM())
            .add("returnOnAssetsTTM", pr.getReturnOnAssetsTTM())
            .add("returnOnEquityTTM", pr.getReturnOnEquityTTM())
            .add("returnOnCapitalEmployedTTM", pr.getReturnOnCapitalEmployedTTM())
            .add("companyEquityMultiplierTTM", pr.getCompanyEquityMultiplierTTM())
            .build();
    }

    
}
