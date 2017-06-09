package concurrent.master_worker;
/**
 * 求立方和的Worker实现类。实现handle方法：子任务立方操作
 *
 * @author YiJie
 * @date May 9, 2017
 */
public class CubeSumWorkerWorker extends Worker {

	@Override
	public Object handle(Object input) {
		Integer i=(Integer)input;  
        return i*i*i;  
	}

}
