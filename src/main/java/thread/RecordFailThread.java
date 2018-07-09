package thread;

import entity.Trade;
import queue.InsertFailQueue;
import queue.ReadFailQueue;
import queue.TradeQueue;
import utils.PropertiesUtils;
import utils.ThreadUtils;
import utils.TradeNumUtils;

import java.io.*;
import java.util.List;

/**
 * 将失败数据保存在日志文件中
 */
public class RecordFailThread extends Thread{

    public void run(){
        while (true){
            // 获取数据
            List<String> fail = ReadFailQueue.get();
            if(fail == null || fail.size() < 1){
                try {
                    Thread.sleep(3000);
                    if(ReadFailQueue.isNull()){
                        // 判断重写写线程是否存活  （若重写线程不存活，写、读线程一定不存活）
                        if(ThreadUtils.isThreadAlive("insertFailThread")){
                            continue;
                        }else{
                            TradeNumUtils.endTime = new java.util.Date().getTime();
                            System.out.println("总记录：" + String.valueOf(TradeNumUtils.getTradeCount()));
                            System.out.println("成功插入" + String.valueOf(TradeNumUtils.getTradeSuccess()));
                            System.out.println("插入失败" + String.valueOf(TradeNumUtils.getTradeFail()));
                            System.out.println("耗时：" + String.valueOf(TradeNumUtils.endTime - TradeNumUtils.startTime));
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            save(fail);
        }
    }

    private synchronized void save(List<String> failList){
        String filename = PropertiesUtils.get("failFileName");
        File file = new File(filename);
        if(!file.isFile()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter pw = null;
        try {
            pw = new FileWriter(file, true);
            for(String str:failList){
                pw.write(str+"\r");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
