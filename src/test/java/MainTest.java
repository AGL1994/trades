import main.Main;
import org.junit.Test;
import utils.ThreadUtils;
import utils.TradeNumUtils;

import java.io.IOException;

public class MainTest {
    @Test
    public void mainTest(){
        long startTime = new java.util.Date().getTime();
        TradeNumUtils.startTime = startTime;
        System.out.println("程序开始时间：" + startTime);
        try {
            new Main().readFile("E:/2017-07-10.csv");
            // 如若子线程存活，就阻塞主线程
            while (true){
                if(ThreadUtils.isThreadAlive("recordFailThread")){
                    Thread.yield();
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
