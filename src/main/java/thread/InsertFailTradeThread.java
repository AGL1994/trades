package thread;

import entity.Trade;
import queue.InsertFailQueue;
import queue.ReadFailQueue;
import utils.DatabaseUtils;
import utils.ThreadUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 重新保存错误数据
 * 存储粒度为1，
 * 可逐渐减少粒度（未实现）
 */
public class InsertFailTradeThread extends Thread {

    private org.apache.log4j.Logger loggerFileError =org.apache.log4j.Logger.getLogger("fileError");

    public void run(){
        while (true){
            // 获取数据
            Trade trade = InsertFailQueue.get();
            if(trade == null){
                try {
                    Thread.sleep(3000);
                    if(InsertFailQueue.isNull()){
                        // 判断写线程是否存活
                        if(ThreadUtils.isThreadAlive("saveThread")){
                            continue;
                        }else{
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            save(trade);

        }
    }

    /**
     * 入库
     * @param trade
     */
    private void save(Trade trade){
        Connection conn = null;
        PreparedStatement pst = null;
        try{
            String sql = "INSERT INTO time_series_data VALUES (?, ?, ?, ?, ?, ?)";
            conn = DatabaseUtils.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, trade.getItemId());
            pst.setString(2, trade.getStockCode());
            pst.setString(3, trade.getTradingDate());
            pst.setDouble(4, trade.getItemValueOne());
            pst.setDouble(5, trade.getItemValueTwo());
            pst.setDouble(6, trade.getItemValueThree());

            pst.execute();
        }catch (Exception e){
            // 将失败的数据放入失败队列
            ReadFailQueue.add(trade.toString());
            loggerFileError.error("数据存储失败， trade：" + trade.toString());
        }finally {
            try {
                pst.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
