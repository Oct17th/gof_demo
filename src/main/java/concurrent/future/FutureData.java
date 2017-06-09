package concurrent.future;
/**
 * Future类实现，RealData的代理类，用于返回RealData的包装对象，封装了RealData的等待过程
 * 若realData未装填，调用getResult方法线程等待
 * 调用setRealData装填数据，唤醒线程，允许返回数据
 *
 * @author YiJie
 * @date May 9, 2017
 */
public class FutureData implements Data {

	
	protected RealData realData;//FutureData是RealData的一个包装类
	protected boolean isReady=false;//标记realData是否被写入
	protected synchronized void setRealData(RealData realData) {
		if(isReady){//不允许重新写入
			return;
		}
		this.realData=realData;
		isReady=true;//标记数据已被写入，能够获取realData
		notify();//调用set方法，realData构造完成，getResult方法的wait被唤醒
	}
	@Override
	public synchronized String getResult() {
		while(!isReady){//realData还未写入时，要进入等待，不允许获取RealData
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		
		/*
		 * 为什么一定要用wait-notify的形式，而不是直接返回一个临时数据?
		 * 
		 * 因为当主线程业务做完，realData的构造仍然没有做完的时候，我们还是返回一个临时数据。
		 * 就会出现我们不知道什么时候realData才装填完，难道一直调用getResult去问返回的是tempData还是realData吗？
		 * 那样就是同步非阻塞了，这里其实就是一个异步非阻塞...
		 * 其实也就是保证我们要么拿不到数据，拿到的就一定是realData
		 * 只要我们晚一点调getResult，这个wait的时间就会分摊到副线程去
		 */
//		if(!isReady){
//			return "this is tempData";
//		}
		return realData.getResult();
	}

}
