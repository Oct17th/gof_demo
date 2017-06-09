package concurrent.master_worker;

import java.util.Map;

/**
 * 求立方和的Master实现类。实现handle方法：结果集求和操作
 *
 * @author YiJie
 * @date May 12, 2017
 */
public class CubeSumMaster extends Master {

    public CubeSumMaster(Worker worker, int workerCount) {
        super(worker, workerCount);
    }

    @Override
    public Object handle(Map<String, Object> resultMap) {
        Integer result = 0;
        Integer value;
        String key;
        while (resultMap.size() > 0 || !this.isComplete()) {
            for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                key = entry.getKey();
                value = (Integer) entry.getValue();
                if (key != null && value != null) {
                    result += value;// 叠加得到最终结果
                    resultMap.remove(key);// TODO 不要在迭代器中使用修改集合结构的方法，否则会抛出ConcurrentModificationException?
                }
            }
        }
        return result;
    }
}
