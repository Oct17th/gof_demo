package concurrent.master_worker;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Master负责分配任务和任务结果合成
 *
 * @author YiJie
 * @date May 9, 2017
 */
public abstract class Master {
	// 任务队列
	protected Queue<Object> taskQueue = new ConcurrentLinkedQueue<Object>();
	// work线程队列
	protected Map<String, Thread> threadMap = new HashMap<String, Thread>();
	// 子任务处理结果集
	protected Map<String, Object> resultMap = new ConcurrentHashMap<String, Object>();

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

	/**
	 * 构造函数。创建workerCount个装有（统一的/本类中的）workQueue，resultMap的worker线程，写入threadMap线程集合
	 * @param worker 一个worker实现类
	 * @param workerCount 线程数
	 */
	public Master(Worker worker, int workerCount) {
		worker.setResultMap(resultMap);
		worker.setWorkQueue(taskQueue);
		for (int i = 0; i < workerCount; i++) {
			String name = Integer.toString(i);// 新线程名称
			threadMap.put(name, new Thread(worker, name));
		}
	}

	/**
	 * 判断进程队列里的线程状态是否都终止了
	 * @return
	 */
	public boolean isComplete() {
		for (Map.Entry<String, Thread> entry : threadMap.entrySet()) {// entrySet方法遍历threadMap
			if (entry.getValue().getState() != Thread.State.TERMINATED) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 提交任务。放入任务队列
	 * @param job
	 */
	public void submit(Object job) {
		taskQueue.add(job);
		System.out.println("任务队列size:" + taskQueue.size());
	}
	
	//Master对结果集的处理，在子类中具体实现.不用等到子线程全部执行完，只要结果集有数据，就可开始主线程的求和计算.
	public abstract Object handle(Map<String, Object> resultMap);

	/**
	 * 执行。开始运行所有的worker线程，并执行主线程计算
	 * @return 返回执行结果
	 */
	public Object execute() {
		for(Map.Entry<String, Thread> entry:threadMap.entrySet()){
			entry.getValue().start();//开启线程
			System.out.println(entry.getValue());//Thread类覆写了toString()方法，打印出Thread[线程名,优先级,线程组]
		}
		return handle(resultMap);
	}
}
