package cn.seu.edu.LANComm.communication.util;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2018/2/1.
 */
public class BlockingQueueTest {
    public static void main(String[] args) throws InterruptedException{
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();

        Producer producer1 = new Producer(queue);
        Producer producer2 = new Producer(queue);
        Producer producer3 = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        ExecutorService service = Executors.newCachedThreadPool();
        // 启动线程
        service.execute(producer1);
        service.execute(producer2);
        service.execute(producer3);
        service.execute(consumer);

        // 执行10s
        Thread.sleep(5 * 1000);
        producer1.stop();
        producer2.stop();
        producer3.stop();

        Thread.sleep(2000);
        service.shutdown();
    }
}
