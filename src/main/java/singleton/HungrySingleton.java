package singleton;

/**
 * 饿汉模式。两种实现方式：
 * 1.instance直接初始化
 * 2.在静态构造块里实例化instance
 *
 * @author YiJie  2017/6/9
 **/
public class HungrySingleton {
    //    private static HungrySingleton instance = new HungrySingleton();
    private static HungrySingleton instance;

    //在静态代码块里实例化
    static {
        instance = new HungrySingleton();
    }

    /**
     * 构造方法私有化
     */
    private HungrySingleton() {
    }

    /**
     * 饿汉模式在类加载时实例化，没有达到懒加载的效果
     *
     * @return
     */
    public static HungrySingleton getInstance() {
        return instance;
    }
}
