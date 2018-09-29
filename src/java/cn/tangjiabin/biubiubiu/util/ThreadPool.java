package cn.tangjiabin.biubiubiu.util;

import java.util.concurrent.*;

/**
 * 线程池工具
 *
 * @author : J.Tang
 * @version : v1.0
 * @email : seven_tjb@163.com
 * @create : 2018-07-26 16:19
 **/

public class ThreadPool {

    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            final int a = i;
            System.out.println(getBlockingDequeSize());
            Runnable r1 = new Runnable() {
                @Override
                public void run() {
                    System.out.println("线程   " + a);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            getInstance().execute(r1);

        }


    }

    /**
     * 单例
     */
    private static ThreadPool instance;
    /**
     * 线程工具
     */
    private ThreadPoolExecutor mExecutor;
    /**
     * 核心线程数
     */
    private int corePoolSize;
    /**
     * 最大线程数
     */
    private int maximumPoolSize;
    /**
     * 闲置线程存活时间
     */
    private long keepAliveTime;
    /**
     * 线程工厂
     */
    private static ThreadFactory namedThreadFactory = Executors.defaultThreadFactory();
    /**
     * 线程队列
     */
    private static LinkedBlockingDeque linkedBlockingDeque = new LinkedBlockingDeque(Integer.MAX_VALUE);
    /**
     * 队列已满,而且当前线程数已经超过最大线程数时的异常处理策略
     */
    private static ThreadPoolExecutor.AbortPolicy abortPolicy = new ThreadPoolExecutor.AbortPolicy() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            super.rejectedExecution(r, e);
        }
    };


    /**
     * 获取单例的线程池对象
     */
    public static ThreadPool getInstance() {
        if (instance == null) {
            synchronized (ThreadPool.class) {
                if (instance == null) {
                    // 获取处理器数量
                    int cpuNum = Runtime.getRuntime().availableProcessors();
                    // 根据cpu数量,计算出合理的线程并发数
                    int threadNum = cpuNum * 2 + 1;
                    //默认是双核的cpu 每个核心走一个线程 一个等待线程
                    instance = new ThreadPool(threadNum - 1, threadNum, 1000 * 60);
                }
            }
        }
        return instance;
    }


    private ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
    }

    public void execute(Runnable runnable) {
        if (runnable == null) {
            return;
        }

        if (mExecutor == null) {
            mExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, linkedBlockingDeque, namedThreadFactory, abortPolicy);
        }
        mExecutor.execute(runnable);
    }


    /**
     * 获取缓存队列大小
     */
    public static int getBlockingDequeSize() {
        return linkedBlockingDeque.size();
    }

    /**
     * 从线程队列中移除对象
     */
    public void cancel(Runnable runnable) {
        if (mExecutor != null) {
            mExecutor.getQueue().remove(runnable);
        }
    }
}

