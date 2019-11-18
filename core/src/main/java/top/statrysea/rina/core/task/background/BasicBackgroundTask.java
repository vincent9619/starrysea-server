package top.statrysea.rina.core.task.background;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public abstract class BasicBackgroundTask {
	// 后台任务抽象类
	private int time;
	private TimeUnit timeUnit;

	public abstract void execute();
}
