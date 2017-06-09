package concurrent.master_worker;

/**
 * <b>并发模型（二）Master-Worker模式</b><br>
 *  Master-worker常用的并行模式之一，核心思想是由两个进程协作工作，master负责接收和分配任务，
 *  worker负责处理任务，并把处理结果返回给Master进程，由Master进行汇总，返回给客户端。<br>
 *  感觉Map-Reduce就用的这个模型....<br>
 *  它的好处在于能把一个大任务分解成若干个小任务，并行执行，提高系统吞吐量。
 *  而对于客户端而言，一旦提交任务，master进程立刻返回一个处理结果，并非等待系统处理完毕再返回。<br>
 *  Master-Worker模式是一种将串行任务并行化的方案，被分解的子任务在系统中可以被并行处理，
 *  Master进程可以不需要等待所有子任务都完成计算，就可以根据已有的部分结果集计算最终结果集。<br>
 * <p/>
 * Main：启动程序，调度开启Master<br>
 * Master：任务的分配和最终结果的合成<br>
 * Worker：用于实际处理一个任务<br>
 *
 * @author YiJie
 * @date May 9, 2017
 */

/*
 * 利用Master-Worker模型实现一个计算1-100立方和，思路如下：
 * 1、将计算任务分配成100个子任务，每个子任务用于计算单独数字的立方和
 * 2、master产生固定个数的worker用于处理这个子任务
 * 3、worker开始计算，并把结果写入resultMap中
 * 4、master负责汇总map中的数据，求和后将最终结果返回给客户端
 */

public class Main {
	public static void main(String[] args) {
		// 创建一个含5个工作线程的Master
		Master master = new CubeSumMaster(new CubeSumWorkerWorker(), 5);
		// 添加100个子任务
		for (int i = 0; i < 100; i++) {
			master.submit(i);
		}
		// 执行
		int result = (Integer) master.execute();
		System.out.println(result);
	}
}
