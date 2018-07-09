package entity;

import utils.StringUtils;

import java.util.Date;

public class Trade {
    // id
    private String itemId;
    // 股票代码
    private String stockCode;
    // 时间
    private String tradingDate;
    // 数据1
    private double itemValueOne;
    // 数据2
    private double itemValueTwo;
    // 数据3
    private double itemValueThree;

    public Trade(String itemId, String stockCode, String tradingDate, double itemValueOne, double itemValueTwo, double itemValueThree) {
        this.itemId = itemId;
        this.stockCode = stockCode;
        this.tradingDate = tradingDate;
        this.itemValueOne = itemValueOne;
        this.itemValueTwo = itemValueTwo;
        this.itemValueThree = itemValueThree;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Trade() {
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getTradingDate() {
        return tradingDate;
    }

    public void setTradingDate(String tradingDate) {
        this.tradingDate = tradingDate;
    }

    public double getItemValueOne() {
        return itemValueOne;
    }

    public void setItemValueOne(double itemValueOne) {
        this.itemValueOne = itemValueOne;
    }

    public double getItemValueTwo() {
        return itemValueTwo;
    }

    public void setItemValueTwo(double itemValueTwo) {
        this.itemValueTwo = itemValueTwo;
    }

    public double getItemValueThree() {
        return itemValueThree;
    }

    public void setItemValueThree(double itemValueThree) {
        this.itemValueThree = itemValueThree;
    }

    /**
     * 检查是否有空值
     * @return
     */
    public boolean isBlank(){
        if(StringUtils.isBlank(this.itemId)){
            return true;
        }
        if(StringUtils.isBlank(this.stockCode)){
            return true;
        }
        if(StringUtils.isBlank(this.tradingDate)){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "itemId='" + itemId + '\'' +
                ", stockCode='" + stockCode + '\'' +
                ", tradingDate='" + tradingDate + '\'' +
                ", itemValueOne=" + itemValueOne +
                ", itemValueTwo=" + itemValueTwo +
                ", itemValueThree=" + itemValueThree +
                '}';
    }
}
