package utils;

/**
 * 记录插入条数 失败条数
 * 空白行不计入总条数
 */
public class TradeNumUtils {
    private static long tradeCount = 0;
    private static int tradeFail = 0;
    private static long tradeSuccess = 0;
    public static long endTime = 0; // 程序结束时间
    public static long startTime = 0; // 开始时间

    /**
     * 总数+1
     */
    public synchronized static void tradeCountAdd(){
        tradeCount += 1;
    }

    /**
     * 失败数+1
     */
    public synchronized static void tradeFailAdd(){
        tradeFail += 1;
    }

    /**
     * 成功+1
     */
    public synchronized static void tradeSuccessAdd(long num){
        tradeSuccess = tradeSuccess + num;
    }

    public static long getTradeCount(){
        return tradeCount;
    }

    public static int getTradeFail(){
        return tradeFail;
    }

    public static long getTradeSuccess(){
        return tradeSuccess;
    }
}
