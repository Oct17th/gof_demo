package concurrent.producer_consumer;

/**
 * 消费者任务处理接口
 *
 * @author YiJie  2017/6/1
 **/
public interface Handler<T> {
    public void handle(T t);
}
