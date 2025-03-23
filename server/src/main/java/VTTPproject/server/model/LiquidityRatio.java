package VTTPproject.server.model;

import VTTPproject.server.utils.Utils;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class LiquidityRatio {
    private double currentRatioTTM;
    private double quickRatioTTM;
    private double cashRatioTTM;
    private double cashConversionCycleTTM;

    public double getCurrentRatioTTM() {return currentRatioTTM;}
    public void setCurrentRatioTTM(double currentRatioTTM) {this.currentRatioTTM = currentRatioTTM;}
    public double getQuickRatioTTM() {return quickRatioTTM;}
    public void setQuickRatioTTM(double quickRatioTTM) {this.quickRatioTTM = quickRatioTTM;}
    public double getCashRatioTTM() {return cashRatioTTM;}
    public void setCashRatioTTM(double cashRatioTTM) {this.cashRatioTTM = cashRatioTTM;}
    public double getCashConversionCycleTTM() {return cashConversionCycleTTM;}
    public void setCashConversionCycleTTM(double cashConversionCycleTTM) {this.cashConversionCycleTTM = cashConversionCycleTTM;}

    @Override
    public String toString() {
        return "LiquidityRatio [currentRatioTTM=" + currentRatioTTM + ", quickRatioTTM=" + quickRatioTTM
                + ", cashRatioTTM=" + cashRatioTTM + ", cashConversionCycleTTM=" + cashConversionCycleTTM + "]";
    }

    public static LiquidityRatio toLiquidObj(JsonObject additionalRatio){
        LiquidityRatio lr = new LiquidityRatio();
        lr.setCurrentRatioTTM(Utils.roundValue(additionalRatio.getJsonNumber("currentRatioTTM").doubleValue()));
        lr.setQuickRatioTTM(Utils.roundValue(additionalRatio.getJsonNumber("quickRatioTTM").doubleValue()));
        lr.setCashRatioTTM(Utils.roundValue(additionalRatio.getJsonNumber("cashRatioTTM").doubleValue()));
        lr.setCashConversionCycleTTM(Utils.roundValue(additionalRatio.getJsonNumber("cashConversionCycleTTM").doubleValue()));

        return lr;
    }

    public static JsonObject buildLiquidityRatioJson(LiquidityRatio lr) {
        return Json.createObjectBuilder()
            .add("currentRatioTTM", lr.getCurrentRatioTTM())
            .add("quickRatioTTM", lr.getQuickRatioTTM())
            .add("cashRatioTTM", lr.getCashRatioTTM())
            .add("cashConversionCycleTTM", lr.getCashConversionCycleTTM())
            .build();
    }
    



}
