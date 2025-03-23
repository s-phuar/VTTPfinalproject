package VTTPproject.server.model;

import VTTPproject.server.utils.Utils;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class ActivityRatio {
    private double inventoryTurnover;
    private double daysOfInventoryOnHand;
    private double receivablesTurnover;
    private double daysSalesOutstanding;
    private double payablesTurnover;
    private double daysPayablesOutstanding;
    private double fixedAssetTurnoverTTM;
    private double assetTurnoverTTM;

    public double getInventoryTurnover() {return inventoryTurnover;}
    public void setInventoryTurnover(double inventoryTurnover) {this.inventoryTurnover = inventoryTurnover;}
    public double getDaysOfInventoryOnHand() {return daysOfInventoryOnHand;}
    public void setDaysOfInventoryOnHand(double daysOfInventoryOnHand) {this.daysOfInventoryOnHand = daysOfInventoryOnHand;}
    public double getReceivablesTurnover() {return receivablesTurnover;}
    public void setReceivablesTurnover(double receivablesTurnover) {this.receivablesTurnover = receivablesTurnover;}
    public double getDaysSalesOutstanding() {return daysSalesOutstanding;}
    public void setDaysSalesOutstanding(double daysSalesOutstanding) {this.daysSalesOutstanding = daysSalesOutstanding;}
    public double getPayablesTurnover() {return payablesTurnover;}
    public void setPayablesTurnover(double payablesTurnover) {this.payablesTurnover = payablesTurnover;}
    public double getDaysPayablesOutstanding() {return daysPayablesOutstanding;}
    public void setDaysPayablesOutstanding(double daysPayablesOutstanding) {this.daysPayablesOutstanding = daysPayablesOutstanding;}
    public double getFixedAssetTurnoverTTM() {return fixedAssetTurnoverTTM;}
    public void setFixedAssetTurnoverTTM(double fixedAssetTurnoverTTM) {this.fixedAssetTurnoverTTM = fixedAssetTurnoverTTM;}
    public double getAssetTurnoverTTM() {return assetTurnoverTTM;}
    public void setAssetTurnoverTTM(double assetTurnoverTTM) {this.assetTurnoverTTM = assetTurnoverTTM;}
    
    @Override
    public String toString() {
        return "ActivityRatio [inventoryTurnover=" + inventoryTurnover + ", daysOfInventoryOnHand="
                + daysOfInventoryOnHand + ", receivablesTurnover=" + receivablesTurnover + ", daysSalesOutstanding="
                + daysSalesOutstanding + ", payablesTurnover=" + payablesTurnover + ", daysPayablesOutstanding="
                + daysPayablesOutstanding + ", fixedAssetTurnoverTTM=" + fixedAssetTurnoverTTM + ", assetTurnoverTTM="
                + assetTurnoverTTM + "]";
    }

    public static ActivityRatio toActObj(JsonObject keyRatios, JsonObject additionalRatio){
        ActivityRatio ar = new ActivityRatio();
        ar.setInventoryTurnover(Utils.roundValue(keyRatios.getJsonNumber("inventoryTurnover").doubleValue()));
        ar.setDaysOfInventoryOnHand(Utils.roundValue(keyRatios.getJsonNumber("daysOfInventoryOnHand").doubleValue()));
        ar.setReceivablesTurnover(Utils.roundValue(keyRatios.getJsonNumber("receivablesTurnover").doubleValue()));
        ar.setDaysSalesOutstanding(Utils.roundValue(keyRatios.getJsonNumber("daysSalesOutstanding").doubleValue()));
        ar.setPayablesTurnover(Utils.roundValue(keyRatios.getJsonNumber("payablesTurnover").doubleValue()));
        ar.setDaysPayablesOutstanding(Utils.roundValue(keyRatios.getJsonNumber("daysPayablesOutstanding").doubleValue()));
        ar.setFixedAssetTurnoverTTM(Utils.roundValue(additionalRatio.getJsonNumber("fixedAssetTurnoverTTM").doubleValue()));
        ar.setAssetTurnoverTTM(Utils.roundValue(additionalRatio.getJsonNumber("assetTurnoverTTM").doubleValue()));

        return ar;
    }
 
    
    public static JsonObject buildActivityRatioJson(ActivityRatio ar) {
        return Json.createObjectBuilder()
            .add("inventoryTurnover", ar.getInventoryTurnover())
            .add("daysOfInventoryOnHand", ar.getDaysOfInventoryOnHand())
            .add("receivablesTurnover", ar.getReceivablesTurnover())
            .add("daysSalesOutstanding", ar.getDaysSalesOutstanding())
            .add("payablesTurnover", ar.getPayablesTurnover())
            .add("daysPayablesOutstanding", ar.getDaysPayablesOutstanding())
            .add("fixedAssetTurnoverTTM", ar.getFixedAssetTurnoverTTM())
            .add("assetTurnoverTTM", ar.getAssetTurnoverTTM())
            .build();
    }



}
