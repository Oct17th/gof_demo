package concurrent.future;

/**
 * Client实现，用于获取FutureData，开启RealData线程，在接收请求后，快速返回future
 *
 * @author YiJie
 * @date May 9, 2017
 */
public class Client {
	public Data request(final String queryString) {
		final FutureData futureData = new FutureData();
		new Thread(){//另开一个线程完成 耗时较长 性能瓶颈 阻塞 的业务
			public void run() {
				System.out.println("构造realData");
				RealData realData = new RealData(queryString);//单独做realData的构造
				System.out.println("装填futureData");
				futureData.setRealData(realData);//完成后填充到futureData
				 //调用future的set方法，将realData装入futureData，并唤醒线程直接return
			}
		}.start();
		return futureData;//立即返回的是futureData代理数据
	}
}
