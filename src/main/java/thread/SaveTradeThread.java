package thread;

import entity.Trade;
import queue.InsertFailQueue;
import queue.TradeQueue;
import utils.DatabaseUtils;
import utils.ThreadUtils;
import utils.TradeNumUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SaveTradeThread extends Thread {


    public void run(){
        while (true){
            // 判断队列是否为空 为空睡眠1秒
            if(TradeQueue.isNull()){
                try {
                    Thread.sleep(2000);
                    if(TradeQueue.isNull()){  // 若队列两秒后还无数据，就认为文件读取完毕
                        // 判断读线程是否存活
                        if(ThreadUtils.isThreadAlive("readThread")){  // 若读线程存活，则表示数据未读完，继续执行程序, 否则结束写线程
                            // 启动暂停的读线程
//                            tu.resume("readThread");
                            continue;
                        }else {
                            System.out.println(Thread.currentThread().getName() + " 写线程退出");
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            List<Trade> tradeList = TradeQueue.get();
            save(tradeList);
        }

    }

    /**
     * 保存数据
     * @param tradeList
     */
    private synchronized void save(List<Trade> tradeList){
        if(tradeList == null || tradeList.size() < 1)
            return;
        StringBuffer sql = new StringBuffer("INSERT INTO time_series_data VALUES");
        Connection conn = DatabaseUtils.getConnection();

        for(Trade trade:tradeList){
            sql.append("('"+trade.getItemId()+"', '"+trade.getStockCode()+"','"+trade.getTradingDate()+"'" +
                    ", "+trade.getItemValueOne()+", "+trade.getItemValueTwo()+", "+trade.getItemValueThree()+"),");
        }
        Statement st = null;
        try {
            st = conn.createStatement();
            st.execute(sql.toString().substring(0, sql.length() - 1));
            // 加上成功条数
            TradeNumUtils.tradeSuccessAdd(tradeList.size());
        } catch (Exception e) {
            e.printStackTrace();
            // 将insert失败数据存入插入失败队列
            InsertFailQueue.add(tradeList);
        }finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
