package factory;
/**
 * 原始的泛型工厂类型
 * @param <T> 构造结果的目标类型，一般是T是抽象类型或接口
 * 
 * @author YiJie
 * @date Apr 10, 2017
 */
public class RawGenericFactory<T> {
	/**
	 * 构造
	 * @param className 类型名称
	 * @return 构造实例结果
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	public T newInstance(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if((className == null)||(className.isEmpty()))
			throw new IllegalArgumentException("className");
		return (T)(Class.forName(className).newInstance());
	}
}
