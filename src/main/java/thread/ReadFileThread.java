package thread;


import entity.Trade;
import exception.ReadFileDataException;
import queue.ReadFailQueue;
import queue.TradeQueue;
import utils.PropertiesUtils;
import utils.TradeNumUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class ReadFileThread extends Thread {
    private long startSeek = 0;  // 开始指针
    private long endSeek = 0;  // 结束指针
    private File file = null;  // 文件
    private final String SEPARATOR = PropertiesUtils.get("separator");  // 分隔符

    private static org.apache.log4j.Logger loggerFileError =org.apache.log4j.Logger.getLogger("fileError");
    private static org.apache.log4j.Logger loggerFileInfo =org.apache.log4j.Logger.getLogger("fileInfo");

    public ReadFileThread(long startSeek, long endSeek, File file){
        this.startSeek = startSeek;
        this.endSeek = endSeek;
        this.file = file;
    }

    public void run(){
        long startTime = new Date().getTime();
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            if(raf.getFilePointer() > endSeek)  // 判断坐标是否在可读范围内
                return;
            raf.seek(startSeek);  // 设置文件开始读取的坐标
            while(true){
                if(raf.getFilePointer() >= endSeek){
                    System.out.println("数据读取完毕。start:" + startSeek + " end:" + endSeek);
                    break;
                }
                // 判断队列是否已满 如果已满睡眠1秒钟
                if(TradeQueue.isFull()){
//                    Thread.currentThread().suspend();  线程暂停方法
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                String tradeStr = raf.readLine(); // csv一行内容
                if(tradeStr== null || "".equals(tradeStr) || tradeStr.length() < 1)
                    continue;
                TradeNumUtils.tradeCountAdd(); //  总条数 + 1
                long tempSeek = raf.getFilePointer();  // 获取当前坐标
                try {
                    Trade trade = getTrade(tradeStr);
                    TradeQueue.add(trade); // 将存取对象放入存储队列
                } catch (Exception e) {
                    e.printStackTrace();
                    loggerFileError.error("filename:"+file.getName()+", data: "+tradeStr+", startSeek:" + tempSeek, e);
                    // 将失败数据放入队列
                    ReadFailQueue.add("filename:"+file.getName()+", "+tradeStr);
                }
            }

            long endTime = new Date().getTime();
            loggerFileInfo.info("filename:"+file.getName()+", 耗时(ms):" + String.valueOf(endTime - startTime));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过读取的字符串封装成对象
     * @param tradeStr
     * @return
     */
    private Trade getTrade(String tradeStr) throws ReadFileDataException {
        try {

            String[] tradeData = tradeStr.split(SEPARATOR);
            if(tradeData.length < 1){
                return null;
            }
            String uuid = UUID.randomUUID().toString();  // id
            String stockCode = tradeData[0];  // 股票代码
            String filename = file.getName();
            String tradingDate = filename.substring(0, filename.lastIndexOf(".")); // 交易日期
            double itemValueOne = Double.valueOf(tradeData[1]);  // 数据
            double itemValueTwo = Double.valueOf(tradeData[2]);  // 数据
            double itemValueThree = Double.valueOf(tradeData[3]);  // 数据
            Trade trade = new Trade(uuid, stockCode, tradingDate, itemValueOne, itemValueTwo, itemValueThree);
            return trade;
        }catch (Exception e){
            throw new ReadFileDataException("数据读取出错", e);
        }

    }

}
