package queue;

import entity.Trade;
import exception.QueueException;
import utils.TradeNumUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 保存失败数据
 * 保存 不能成功转成trade对象的数据和二次insert失败的数据
 */
public class ReadFailQueue {
    private static LinkedList<String> queue = new LinkedList<String>();

    /**
     * 放入队列
     */
    public synchronized static void add(String trade) {
        // 失败数 + 1
        TradeNumUtils.tradeFailAdd();
        // 失败数据不存验证
        queue.add(trade);
    }


    /**
     * 获取数据
     * @return
     */
    public synchronized static List<String> get(){
        // 取出队列中所有数据
        List<String> fail = new ArrayList<String>();
        fail.addAll(queue);
        queue.clear();
        return fail;
    }

    public static boolean isNull(){
        if(queue.size() < 1){
            return true;
        }else{
            return false;
        }
    }
}


