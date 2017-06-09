package singleton;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 枚举单例模式测试类
 *
 * @author YiJie  2017/6/9
 **/
public class SingletonTest {
    /**
     * {@link EnumSingleton#INSTANCE}
     */
    @Test
    public void TestEnumSingleton(){
        assertEquals(EnumSingleton.INSTANCE,EnumSingleton.INSTANCE);
    }

    /**
     * {@link HungrySingleton}
     */
    @Test
    public void TestHungrySingleton(){
        assertEquals(HungrySingleton.getInstance(),HungrySingleton.getInstance());
    }

    /**
     * {@link InnerClassSingleton}
     */
    @Test
    public void TestInnerClassSingleton(){
        assertEquals(InnerClassSingleton.getInstance(),InnerClassSingleton.getInstance());
    }

    /**
     * {@link LazySingleton}
     */
    @Test
    public void TestLazySingleton(){
        assertEquals(LazySingleton.getInstance(),LazySingleton.getInstance());
    }
}