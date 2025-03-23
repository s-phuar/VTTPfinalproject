package VTTPproject.server.model;

import VTTPproject.server.utils.Utils;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class ValuationRatio {
    private double peRatioTTM;
    private double pegRatioTTM;
    private double priceToBookRatioTTM;
    private double priceToSalesRatioTTM;
    private double priceEarningsRatioTTM;
    private double priceFairValueTTM;
    private double payoutRatioTTM;
    private double retentionRate; //1 - payout ratio
    private double sustainableGrowthRate; // retenetion * ROE
    
    public double getPeRatioTTM() {return peRatioTTM;}
    public void setPeRatioTTM(double peRatioTTM) {this.peRatioTTM = peRatioTTM;}
    public double getPegRatioTTM() {return pegRatioTTM;}
    public void setPegRatioTTM(double pegRatioTTM) {this.pegRatioTTM = pegRatioTTM;}
    public double getPriceToBookRatioTTM() {return priceToBookRatioTTM;}
    public void setPriceToBookRatioTTM(double priceToBookRatioTTM) {this.priceToBookRatioTTM = priceToBookRatioTTM;}
    public double getPriceToSalesRatioTTM() {return priceToSalesRatioTTM;}
    public void setPriceToSalesRatioTTM(double priceToSalesRatioTTM) {this.priceToSalesRatioTTM = priceToSalesRatioTTM;}
    public double getPriceEarningsRatioTTM() {return priceEarningsRatioTTM;}
    public void setPriceEarningsRatioTTM(double priceEarningsRatioTTM) {this.priceEarningsRatioTTM = priceEarningsRatioTTM;}
    public double getPriceFairValueTTM() {return priceFairValueTTM;}
    public void setPriceFairValueTTM(double priceFairValueTTM) {this.priceFairValueTTM = priceFairValueTTM;}
    public double getPayoutRatioTTM() {return payoutRatioTTM;}
    public void setPayoutRatioTTM(double payoutRatioTTM) {this.payoutRatioTTM = payoutRatioTTM;}
    public double getRetentionRate() {return retentionRate;}
    public void setRetentionRate(double retentionRate) {this.retentionRate = retentionRate;}
    public double getSustainableGrowthRate() {return sustainableGrowthRate;}
    public void setSustainableGrowthRate(double sustainableGrowthRate) {this.sustainableGrowthRate = sustainableGrowthRate;}
    
    @Override
    public String toString() {
        return "ValuationRatio [peRatioTTM=" + peRatioTTM + ", pegRatioTTM=" + pegRatioTTM + ", priceToBookRatioTTM="
                + priceToBookRatioTTM + ", priceToSalesRatioTTM=" + priceToSalesRatioTTM + ", priceEarningsRatioTTM="
                + priceEarningsRatioTTM + ", priceFairValueTTM=" + priceFairValueTTM + ", payoutRatioTTM="
                + payoutRatioTTM + ", retentionRate=" + retentionRate + ", sustainableGrowthRate="
                + sustainableGrowthRate + "]";
    }

    public static ValuationRatio toValObj(JsonObject additionalRatio){
        ValuationRatio vr = new ValuationRatio();
        vr.setPeRatioTTM(Utils.roundValue(additionalRatio.getJsonNumber("peRatioTTM").doubleValue()));
        vr.setPegRatioTTM(Utils.roundValue(additionalRatio.getJsonNumber("pegRatioTTM").doubleValue()));
        vr.setPriceToBookRatioTTM(Utils.roundValue(additionalRatio.getJsonNumber("priceToBookRatioTTM").doubleValue()));
        vr.setPriceToSalesRatioTTM(Utils.roundValue(additionalRatio.getJsonNumber("priceToSalesRatioTTM").doubleValue()));
        vr.setPriceEarningsRatioTTM(Utils.roundValue(additionalRatio.getJsonNumber("priceEarningsRatioTTM").doubleValue()));
        vr.setPriceFairValueTTM(Utils.roundValue(additionalRatio.getJsonNumber("priceFairValueTTM").doubleValue()));
        vr.setPayoutRatioTTM(Utils.roundValue(additionalRatio.getJsonNumber("payoutRatioTTM").doubleValue()));
        vr.setRetentionRate(1 - vr.getPayoutRatioTTM());

        return vr;
    }


    public static JsonObject buildValuationRatioJson(ValuationRatio vr) {
        return Json.createObjectBuilder()
            .add("peRatioTTM", vr.getPeRatioTTM())
            .add("pegRatioTTM", vr.getPegRatioTTM())
            .add("priceToBookRatioTTM", vr.getPriceToBookRatioTTM())
            .add("priceToSalesRatioTTM", vr.getPriceToSalesRatioTTM())
            .add("priceEarningsRatioTTM", vr.getPriceEarningsRatioTTM())
            .add("priceFairValueTTM", vr.getPriceFairValueTTM())
            .add("payoutRatioTTM", vr.getPayoutRatioTTM())
            .add("retentionRate", vr.getRetentionRate())
            .add("sustainableGrowthRate", vr.getSustainableGrowthRate())
            .build();
    }


    
}
