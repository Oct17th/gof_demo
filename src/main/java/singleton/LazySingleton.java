package singleton;

/**
 * 懒汉模式。三种实现方式：
 * 1.在getInstance方法里初始化instance
 * 2.方法加synchronized
 * 3.double-checked方式
 * 4.instance变量加volatile
 *
 * @author YiJie  2017/6/9
 **/
public class LazySingleton {
    //    private static LazySingleton instance;
    private static volatile LazySingleton instance;

    /**
     * 构造方法私有
     */
    private LazySingleton() {
    }

    /**
     * double-checked方式返回实例
     * 提升性能，避免在getInstance方法上加synchronized，带来的99%不必要的锁竞争同步问题
     *
     * @return
     */
    public static LazySingleton getInstance() {
        if (instance == null) {
            synchronized (LazySingleton.class) {
                if (instance == null) {
                    return instance = new LazySingleton();
                }
            }
        }
        return instance;
    }
}
