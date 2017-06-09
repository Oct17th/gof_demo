package concurrent.producer_consumer;

import java.text.MessageFormat;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <b>并发模型（三）生产者消费者模式</b><br>
 * <p>
 * Main:启动系统
 * Producer:生产者：提交用户请求，将任务装入缓存区
 * Consumer：消费者：从缓存区提取任务，处理生产者请求
 * Data:共享数据模型：生产者向内存区提交的数据结构
 *
 * @author YiJie
 * @date May 13, 2017
 */
/*
实现一个生产者-消费者的求整数平方并行计算
 */

public class Main {
    int b ;
    int[] a = new int[b];
    //建立缓冲区
    BlockingQueue<Data> queue = new LinkedBlockingQueue<Data>(10);
    //设置任务处理类
    Handler<Data> handler = new Handler<Data>() {
        public void handle(Data data) {
            int re = data.getData() * data.getData();
            System.out.println(MessageFormat.format("{0}*{1}={2}",
                    data.getData(), data.getData(), re
            ));
        }
    };
    Producer producer1 = new Producer(queue);
    Producer producer2 = new Producer(queue);
//TODO

}
