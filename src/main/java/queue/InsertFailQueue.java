package queue;

import entity.Trade;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InsertFailQueue {
    private static LinkedList<Trade> queue = new LinkedList<Trade>();

    /**
     * 放入队列
     */
    public synchronized static void add(List<Trade> tradeList) {
        // 失败数据不存验证
        queue.addAll(tradeList);
    }


    /**
     * 获取数据
     * @return
     */
    public synchronized static Trade get(){
        if(queue.size() <= 0){
            // 判断数据是否读取完毕
            return null;
        }
        return queue.removeFirst();
    }

    /**
     * 判断线程是否为空
     * @return
     */
    public static boolean isNull(){
        if(queue.size() < 1){
            return true;
        }else{
            return false;
        }
    }
}
