package VTTPproject.server.model;

import VTTPproject.server.utils.Utils;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class MiscItems {
    private String symbol;
    private String companyName;
    private double price;
    private double dcf;
    private double beta;
    private int mktCap;
    private String image;
    private String description;
    private String todayEpochMilli;

    public String getSymbol() {return symbol;}
    public void setSymbol(String symbol) {this.symbol = symbol;}
    public String getCompanyName() {return companyName;}
    public void setCompanyName(String companyName) {this.companyName = companyName;}
    public double getPrice() {return price;}
    public void setPrice(double price) {this.price = price;}
    public double getDcf() {return dcf;}
    public void setDcf(double dcf) {this.dcf = dcf;}
    public double getBeta() {return beta;}
    public void setBeta(double beta) {this.beta = beta;}
    public int getMktCap() {return mktCap;}
    public void setMktCap(int mktCap) {this.mktCap = mktCap;}
    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getTodayEpochMilli() {return todayEpochMilli;}
    public void setTodayEpochMilli(String todayEpochMilli) {this.todayEpochMilli = todayEpochMilli;}

    @Override
    public String toString() {
        return "MiscItems [symbol=" + symbol + ", companyName=" + companyName + ", price=" + price + ", dcf=" + dcf
                + ", beta=" + beta + ", mktCap=" + mktCap + ", image=" + image + ", description=" + description
                + ", todayEpochMilli=" + todayEpochMilli + "]";
    }
    

    public static MiscItems toMiscObj(JsonObject companyInfo){
        MiscItems mi = new MiscItems();
        mi.setSymbol(companyInfo.getString("symbol"));
        mi.setCompanyName(companyInfo.getString("companyName"));
        mi.setPrice(Utils.roundValue(companyInfo.getJsonNumber("price").doubleValue()));
        mi.setDcf(Utils.roundValue(companyInfo.getJsonNumber("dcf").doubleValue()));
        mi.setBeta(Utils.roundValue(companyInfo.getJsonNumber("beta").doubleValue()));
        mi.setMktCap(companyInfo.getInt("mktCap"));
        mi.setImage(companyInfo.getString("image"));
        mi.setDescription(companyInfo.getString("description"));
        mi.setTodayEpochMilli(Utils.currEpochMilli());
    
        return mi;
    }


    public static JsonObject buildMiscItemsJson(MiscItems mi) {
        return Json.createObjectBuilder()
            .add("symbol", mi.getSymbol())
            .add("companyName", mi.getCompanyName())
            .add("price", mi.getPrice())
            .add("dcf", mi.getDcf())
            .add("beta", mi.getBeta())
            .add("mktCap", mi.getMktCap())
            .add("image", mi.getImage())
            .add("description", mi.getDescription())
            .add("todayEpochMilli", mi.getTodayEpochMilli())
            .build();
    }



    
    
}
