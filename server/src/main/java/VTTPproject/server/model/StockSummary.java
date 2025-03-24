package VTTPproject.server.model;

public class StockSummary {
    private String ticker;
    private String companyName;

    public String getTicker() {return ticker;}
    public void setTicker(String ticker) {this.ticker = ticker;}
    public String getCompanyName() {return companyName;}
    public void setCompanyName(String companyName) {this.companyName = companyName;}
    @Override
    public String toString() {
        return "StockSummary [ticker=" + ticker + ", companyName=" + companyName + "]";
    }
    


    
    

}
