package queue;

import entity.Trade;
import exception.QueueException;
import utils.PropertiesUtils;

import java.util.*;

/**
 * 数据队列
 */
public class TradeQueue {
    private static LinkedList<Trade> queue = new LinkedList<Trade>();
    private static final int NUM = Integer.valueOf(PropertiesUtils.get("insertNum"));  // 一次取出多少条数据
    private static final int MAX_NUM = Integer.valueOf(PropertiesUtils.get("maxLength"));  // 队列允许最大数据

    /**
     * 获取数据
     * 一次获取固定条数
     * @return
     */
    public synchronized static List<Trade> get(){
        List<Trade> list = new ArrayList<Trade>();
        for(int i=0; i< NUM; i++){
            if(queue.size() <= 0){
                // 判断数据是否读取完毕
                break;
            }
            try{
                list.add(queue.removeFirst());
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return list;
    }

    /**
     * 放入队列
     */
    public synchronized static void add(Trade trade) throws QueueException {
        if(trade == null){
            throw new QueueException("加入队列失败，trade不能为空");
        }
        if(trade.isBlank()){
            throw new QueueException("加入队列失败，trade含有控制参数");
        }
        queue.add(trade);

    }

    /**
     * 判断队列是否已满
     * @return
     */
    public static boolean isFull(){
        if(queue.size() >= MAX_NUM){
            return true;
        }
        return false;
    }

    /**
     * 判断队列是否为空
     * @return
     */
    public static boolean isNull(){
        if(queue.size() <= 0){
            return true;
        }
        return false;
    }

}
