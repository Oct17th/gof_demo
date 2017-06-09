package concurrent.producer_consumer;
/**
 * 共享数据模型
 *
 * @author YiJie
 * @date May 16, 2017
 */
public class Data {
	private final int intData;
	
	public Data(int d) {
		intData = d;
	}
	
	public Data(String d) {
		intData=Integer.parseInt(d);
	}
	
	public int getData(){
		return intData;
	}
	
	@Override
	public String toString() {
		return "data:"+intData;
	}
}
