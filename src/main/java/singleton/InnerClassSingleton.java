package singleton;

/**
 * 静态内部类单例模式
 *
 * @author YiJie  2017/6/9
 **/
public class InnerClassSingleton {
    /**
     * 构造方法私有化
     */
    private InnerClassSingleton() {
    }

    /**
     * 加载InnerClassSingleton类时不会初始化SingletonHolder
     * 调用getInstance方法时才会加载SingletonHolder
     * 可以保证延迟加载
     *
     * @return
     */
    public static final InnerClassSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final InnerClassSingleton INSTANCE = new InnerClassSingleton();//final只能初始化不能赋值
    }
}
