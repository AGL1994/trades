package utils;

import java.util.Arrays;

public class ThreadUtils {

    /**
     * 暂停指定线程
     * @param threadName
     */
//    public void suspend(String threadName){
//        Thread[] atualThreads = getAliveThread();
//        for (Thread thread : atualThreads) {
//            if(thread == null){
//                continue;
//            }
//            if(threadName.equals(thread.getName())){
//                thread.suspend();
//            }
//        }
//    }

    /**
     * 启动读文件线程
     * @param threadName
     */
    public static void resume(String threadName){
        Thread[] atualThreads = getAliveThread();
        for (Thread thread : atualThreads) {
            if(thread == null){
                continue;
            }
            if(thread.getName().indexOf(threadName) != -1){
                thread.resume();
            }
        }
    }

    /**
     * 是否还有存活的读线程
     * @return
     */
    public static boolean isThreadAlive(String threadName){
        Thread[] atualThreads = getAliveThread();
        for (Thread thread : atualThreads) {
            if(thread == null)
                continue;
            if(thread.getName().indexOf(threadName) != -1){
                if(thread.isAlive()){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 获取当前存活线程
     * @return
     */
    private static synchronized Thread[] getAliveThread(){
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup topGroup = group;
        // 遍历线程组树，获取根线程组
        while (group != null) {
            topGroup = group;
            group = group.getParent();
        }
        // 激活的线程数再加一倍，防止枚举时有可能刚好有动态线程生成
        int slackSize = topGroup.activeCount() * 2;
        Thread[] slackThreads = new Thread[slackSize];
        // 获取根线程组下的所有线程，返回的actualSize便是最终的线程数
        int actualSize = topGroup.enumerate(slackThreads);
        Thread[] atualThreads = new Thread[actualSize];
        return slackThreads;
    }
}
