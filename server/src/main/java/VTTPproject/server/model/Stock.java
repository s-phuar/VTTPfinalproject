package VTTPproject.server.model;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Stock {
    private MiscItems mi;
    private ActivityRatio ar;
    private LiquidityRatio lr;
    private ProfitabilityRatio pr;
    private SolvencyRatio sr;
    private ValuationRatio vr;

    public MiscItems getMi() {return mi;}
    public void setMi(MiscItems mi) {this.mi = mi;}
    public ActivityRatio getAr() {return ar;}
    public void setAr(ActivityRatio ar) {this.ar = ar;}
    public LiquidityRatio getLr() {return lr;}
    public void setLr(LiquidityRatio lr) {this.lr = lr;}
    public ProfitabilityRatio getPr() {return pr;}
    public void setPr(ProfitabilityRatio pr) {this.pr = pr;}
    public SolvencyRatio getSr() {return sr;}
    public void setSr(SolvencyRatio sr) {this.sr = sr;}
    public ValuationRatio getVr() {return vr;}
    public void setVr(ValuationRatio vr) {this.vr = vr;}

    @Override
    public String toString() {
        return "Stock [mi=" + mi + ", ar=" + ar + ", lr=" + lr + ", pr=" + pr + ", sr=" + sr + ", vr="
                + vr + "]";
    }


    public static JsonObject toStockJson(JsonObject mi, JsonObject ar, JsonObject lr, JsonObject pr, JsonObject sr, JsonObject vr) {
        JsonObjectBuilder builder = Json.createObjectBuilder(); 
        builder.add("mi", mi);
        builder.add("ar", ar);
        builder.add("lr", lr);
        builder.add("pr", pr);
        builder.add("sr", sr);
        builder.add("vr", vr);

        return builder.build();
    }

    
}
