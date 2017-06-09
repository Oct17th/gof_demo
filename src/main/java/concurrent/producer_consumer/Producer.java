package concurrent.producer_consumer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一个生产者线程
 *
 * @author YiJie
 * @date May 16, 2017
 */
public class Producer implements Runnable {
    //内部维护一个线程安全的阻塞队列做缓存区
    private BlockingQueue<Data> queue;

    //任务总数计数，原子操作
    private static AtomicInteger count = new AtomicInteger();

    //保证可见性
    private volatile boolean isRunning = true;

    private static final int SLEEPTIME = 1000;

    /**
     * @param queue 缓存队列
     */
    public Producer(BlockingQueue<Data> queue) {
        this.queue = queue;
    }

    /**
     * 1.判断线程是否stop
     * （用随机数模拟提交任务到队列时间点随机）
     * 2.构造任务数据
     * 3.提交数据到缓存区
     */
    public void run() {
        System.out.println("start Producer " + Thread.currentThread().getId());
        Random random = new Random();
        Data data;
        try {
            while (isRunning) {
                Thread.sleep(random.nextInt(SLEEPTIME));//线程睡眠SLEEPTIME内的一个随机时间
                data = new Data(count.incrementAndGet());//产生数据
                System.out.println("put data(" + data.getData() + ") into queue");
                if (!queue.offer(data, 2, TimeUnit.SECONDS)) {//data插入队列，阻塞则等待两秒
                    System.out.println("fail to put data(" + data + ")");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        isRunning = false;
    }
}
