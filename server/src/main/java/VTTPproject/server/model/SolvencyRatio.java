package VTTPproject.server.model;

import VTTPproject.server.utils.Utils;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class SolvencyRatio {
    private double debtRatioTTM; // debt/total capital
    private double debtEquityRatioTTM; // debt/equity
    private double interestCoverageTTM;
    private double cashFlowCoverageRatiosTTM;
    private double shortTermCoverageRatiosTTM;
    private double capitalExpenditureCoverageRatioTTM;
    private double dividendPaidAndCapexCoverageRatioTTM;

    public double getDebtRatioTTM() {return debtRatioTTM;}
    public void setDebtRatioTTM(double debtRatioTTM) {this.debtRatioTTM = debtRatioTTM;}
    public double getDebtEquityRatioTTM() {return debtEquityRatioTTM;}
    public void setDebtEquityRatioTTM(double debtEquityRatioTTM) {this.debtEquityRatioTTM = debtEquityRatioTTM;}
    public double getInterestCoverageTTM() {return interestCoverageTTM;}
    public void setInterestCoverageTTM(double interestCoverageTTM) {this.interestCoverageTTM = interestCoverageTTM;}
    public double getCashFlowCoverageRatiosTTM() {return cashFlowCoverageRatiosTTM;}
    public void setCashFlowCoverageRatiosTTM(double cashFlowCoverageRatiosTTM) {this.cashFlowCoverageRatiosTTM = cashFlowCoverageRatiosTTM;}
    public double getShortTermCoverageRatiosTTM() {return shortTermCoverageRatiosTTM;}
    public void setShortTermCoverageRatiosTTM(double shortTermCoverageRatiosTTM) {this.shortTermCoverageRatiosTTM = shortTermCoverageRatiosTTM;}
    public double getCapitalExpenditureCoverageRatioTTM() {return capitalExpenditureCoverageRatioTTM;}
    public void setCapitalExpenditureCoverageRatioTTM(double capitalExpenditureCoverageRatioTTM) {this.capitalExpenditureCoverageRatioTTM = capitalExpenditureCoverageRatioTTM;}
    public double getDividendPaidAndCapexCoverageRatioTTM() {return dividendPaidAndCapexCoverageRatioTTM;}
    public void setDividendPaidAndCapexCoverageRatioTTM(double dividendPaidAndCapexCoverageRatioTTM) {this.dividendPaidAndCapexCoverageRatioTTM = dividendPaidAndCapexCoverageRatioTTM;}
    
    @Override
    public String toString() {
        return "SolvencyRatio [debtRatioTTM=" + debtRatioTTM + ", debtEquityRatioTTM=" + debtEquityRatioTTM
                + ", interestCoverageTTM=" + interestCoverageTTM + ", cashFlowCoverageRatiosTTM="
                + cashFlowCoverageRatiosTTM + ", shortTermCoverageRatiosTTM=" + shortTermCoverageRatiosTTM
                + ", capitalExpenditureCoverageRatioTTM=" + capitalExpenditureCoverageRatioTTM
                + ", dividendPaidAndCapexCoverageRatioTTM=" + dividendPaidAndCapexCoverageRatioTTM + "]";
    }

    public static SolvencyRatio toSolvObj(JsonObject additionalRatio){
        SolvencyRatio sr = new SolvencyRatio();
        sr.setDebtRatioTTM(Utils.roundValue(additionalRatio.getJsonNumber("debtRatioTTM").doubleValue()));
        sr.setDebtEquityRatioTTM(Utils.roundValue(additionalRatio.getJsonNumber("debtEquityRatioTTM").doubleValue()));
        sr.setInterestCoverageTTM(Utils.roundValue(additionalRatio.getJsonNumber("interestCoverageTTM").doubleValue()));
        sr.setCashFlowCoverageRatiosTTM(Utils.roundValue(additionalRatio.getJsonNumber("cashFlowCoverageRatiosTTM").doubleValue()));
        sr.setShortTermCoverageRatiosTTM(Utils.roundValue(additionalRatio.getJsonNumber("shortTermCoverageRatiosTTM").doubleValue()));
        sr.setCapitalExpenditureCoverageRatioTTM(Utils.roundValue(additionalRatio.getJsonNumber("capitalExpenditureCoverageRatioTTM").doubleValue()));
        sr.setDividendPaidAndCapexCoverageRatioTTM(Utils.roundValue(additionalRatio.getJsonNumber("dividendPaidAndCapexCoverageRatioTTM").doubleValue()));
    
        return sr;
    }
    
    
    public static JsonObject buildSolvencyRatioJson(SolvencyRatio sr) {
        return Json.createObjectBuilder()
            .add("debtRatioTTM", sr.getDebtRatioTTM())
            .add("debtEquityRatioTTM", sr.getDebtEquityRatioTTM())
            .add("interestCoverageTTM", sr.getInterestCoverageTTM())
            .add("cashFlowCoverageRatiosTTM", sr.getCashFlowCoverageRatiosTTM())
            .add("shortTermCoverageRatiosTTM", sr.getShortTermCoverageRatiosTTM())
            .add("capitalExpenditureCoverageRatioTTM", sr.getCapitalExpenditureCoverageRatioTTM())
            .add("dividendPaidAndCapexCoverageRatioTTM", sr.getDividendPaidAndCapexCoverageRatioTTM())
            .build();
    }




}
