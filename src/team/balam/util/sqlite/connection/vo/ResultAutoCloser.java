package team.balam.util.sqlite.connection.vo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ResultAutoCloser {
	private static ResultAutoCloser self = new ResultAutoCloser();
	
	private BlockingQueue<QueryVO> queryVoList;
	private AtomicBoolean isRunning;
	
	private ResultAutoCloser() {
		this.queryVoList = new LinkedBlockingQueue<QueryVO>();
		this.isRunning = new AtomicBoolean(false);
	}
	
	public static ResultAutoCloser getInstance() {
		return self;
	}
	
	public void start() {
		if (this.isRunning.compareAndSet(false, true)) {
			new Thread(){
				@Override
				public void run() {
					while (self.isRunning.get()) {
						try {
							QueryVO vo = self.queryVoList.poll(1, TimeUnit.SECONDS);
							if (vo != null) {
								Result result = vo.getResult();
								if (result != null) {
									result.close();
								} else {
									self.queryVoList.put(vo);
								}
							}
						} catch (Exception e) {
						}
					}
				}
			}.start();
		}
	}
	
	public void stop() {
		this.isRunning.compareAndSet(true, false);
	}
	
	public void add(QueryVO _vo) {
		this.queryVoList.add(_vo);
	}
}
