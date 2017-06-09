package concurrent.future;

/**
 * <b>并发模型（一）Future模式</b><br>
 * 异步调用，利用等待时间，先做其他业务逻辑，最后返回执行较慢的future数据，提高响应速度<br>
 * <p/>
 * Main：启动系统，调用Client发出请求；<br>
 * Client：返回Data对象，理解返回FutureData，并开启ClientThread线程装配RealData；<br>
 * Data：返回数据的接口；<br>
 * FutureData：Future数据，构造很快，但是是一个虚拟的数据，需要装配RealData；<br>
 * RealData：真实数据，构造比较慢。<br>
 * 
 * @author YiJie
 * @date May 4, 2017
 */
public class Main {
	public static void main(String[] args) {
		Main main = new Main();
		long time=System.currentTimeMillis();
		main.futureMode();//用并发future模式完成业务
//		main.oldMode();//用传统模式完成业务
		System.out.println(System.currentTimeMillis()-time);
	}
	public void futureMode() {
		Client client = new Client();
		// 这里会立即返回结果，因为得到的是FutureData 而非RealData
		Data data = client.request("name");
		System.out.println("请求完毕！");
		System.out.println(data.toString());//此时装入的是临时的代理数据

		try {
			// 代表对其他业务的处理
			// 在处理过程中，RealData被创建，充分利用了等待时间
			for (int i = 0; i < 7; i++) {
				Thread.sleep(200);
				System.out.println("我也在被处理哦~");
			}
		} catch (Exception e) {
		}
		System.out.println("真实数据：" + data.getResult());
	}
	public void oldMode() {
		Client client = new Client();
		Data data = client.request("name");
		System.out.println("请求完毕！");
		System.out.println("真实数据：" + data.getResult());
		try {
			// 代表对其他业务的处理
			// 在处理过程中，RealData被创建，充分利用了等待时间
			for (int i = 0; i < 7; i++) {
				Thread.sleep(200);
				System.out.println("我也在被处理哦~");
			}
		} catch (Exception e) {
		}
	}
}
