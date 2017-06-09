package concurrent.producer_consumer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * 消费者
 *
 * @author YiJie
 * @date May 16, 2017
 */
public class Consumer implements Runnable {
    //内部维护一个阻塞队列做缓存区
    private BlockingQueue<Data> queue;//TODO 此处应同步

    private static final int SLEEPTIME = 1000;

    private Handler<Data> handler;

    /**
     * @param queue 缓存队列
     */
    public Consumer(BlockingQueue<Data> queue, Handler<Data> handler) {
        this.queue = queue;
        this.handler = handler;
    }

    public void run() {
        System.out.println("start Consumer " + Thread.currentThread().getId());
        Random random = new Random();
        Data data = null;
        try {
            while (true) {
                Thread.sleep(random.nextInt(SLEEPTIME));//用随机数模拟处理任务的时间点随机
                data = queue.take();//除非线程中断，否则没有取到数据前一直等待
                System.out.println("get data(" + data + ") form queue");
                if (null != data) {
                    handler.handle(data);//处理数据
                    System.out.println("handle data(" + data + ")");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
