package concurrent.future;

/**
 * 真实数据的构造。模拟一个较慢的构造速度
 *
 * @author YiJie
 * @date May 9, 2017
 */
public class RealData implements Data {

	protected String result;

	public RealData(String para) {
		// 模拟一个构造很慢的过程
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 20; i++) {
			sb.append(para);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		System.out.println("构造完成");
		result = sb.toString();
	}

	@Override
	public String getResult() {
		return result;
	}

}
