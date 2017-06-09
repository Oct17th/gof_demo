package concurrent.master_worker;

import java.util.Map;
import java.util.Queue;

/**
 * Worker实际处理单个子任务
 * 1.从workQueue出队列获取子任务
 * 2.handle()处理子任务
 * 3.将结果写入resultMap
 *
 * @author YiJie
 * @date May 9, 2017
 */
public abstract class Worker implements Runnable{

	//任务队列，用于每个子任务
	protected Queue<Object> taskQueue;
	//子任务处理结果集
	protected Map<String,Object> resultMap;
	
	public void setWorkQueue(Queue<Object> workQueue) {
		this.taskQueue = workQueue;
	}
	
	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}
	
	//子任务处理逻辑，在子类中具体实现
	public abstract Object handle(Object input);
	
	@Override
	public void run() {
		while(true){
			//获取子任务
			Object input = taskQueue.poll();//出队列
			if(input==null)
				break;
			//处理子任务
			Object result = handle(input);
			//将处理结果写入结果集，key为子任务哈希码
			resultMap.put(Integer.toString(input.hashCode()), result);
		}
	}
}
