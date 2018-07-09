package main;

import org.apache.log4j.Logger;
import thread.ReadFileThread;
import thread.InsertFailTradeThread;
import thread.RecordFailThread;
import thread.SaveTradeThread;
import utils.PropertiesUtils;
import utils.TradeNumUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Main {

    private final int READ_THREAD_NUM = Integer.valueOf(PropertiesUtils.get("readThread"));  // 读取线程数量
    private final int SAVE_THREAD_NUM = Integer.valueOf(PropertiesUtils.get("saveThread"));  // 写数据库线程数量
    private Logger logger  =  Logger.getLogger(Main.class );

    public static void main(String[] args) throws IOException {
        long startTime = new java.util.Date().getTime();
        TradeNumUtils.startTime = startTime;
        System.out.println("程序开始时间：" + startTime);
        new Main().readFile("E:/2017-07-10.csv");
    }

    /**
     * 读取文件
     * @param filename 文件名
     */
    public void readFile(String filename) throws IOException {
        File file = new File(filename);
        if(filename.lastIndexOf(".csv") == -1 || !file.isFile()){
            logger.debug("文件不存在或文件格式错误，文件名：" + filename);
        }
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        long fileLength = raf.length();  // 总长度
        long seekGroupNum = fileLength / READ_THREAD_NUM; // 平均段数
        long prevSeek = 0; // 上一段endSeek
        // 更具线程数量分配数据段
        for(int i=0; i<READ_THREAD_NUM; i++){
            long startSeek = prevSeek;
            long endSeek = prevSeek + seekGroupNum;
            System.out.println("数据分离：第" + (i+1) + "部分。" + startSeek + "  " + endSeek);
            // 判断分割点是否为一行，不是一行则找到一行结束
            raf.seek(endSeek);
            while (true){
                int seek = raf.read();
                if (seek == -1){  // 判断是否到文件末尾
                    endSeek = fileLength;
                    break;
                }
                if (seek == "\r".getBytes()[0] || seek == "\n".getBytes()[0]){
                    endSeek = raf.getFilePointer();
                    break;
                }
            }
            prevSeek = endSeek; // 保存endSeek
            // 启动读线程
            Thread readFileThread = new ReadFileThread(startSeek, endSeek, file);
//            readFileThread.setPriority();
            readFileThread.setName("readThread-"+i);
            readFileThread.start();
        }

        // 启动写线程
        for(int i=0; i<SAVE_THREAD_NUM; i++){
            Thread thread = new SaveTradeThread();
            thread.setPriority(7);
            thread.setName("saveThread-"+i);
            thread.start();
        }

        // 启动重写数据线程
        Thread insertFailThread = new InsertFailTradeThread();
        insertFailThread.setName("insertFailThread");
        insertFailThread.setPriority(2);
        insertFailThread.start();

        // 启动错误记录线程
        Thread recordFailThread = new RecordFailThread();
        recordFailThread.setName("recordFailThread");
        recordFailThread.setPriority(1);
        recordFailThread.start();
    }


}
