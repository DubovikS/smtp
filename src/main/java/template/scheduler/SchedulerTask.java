package template.scheduler;

import java.util.TimerTask;

public class SchedulerTask extends TimerTask {
	int count = 1;

	// run is a abstract method that defines task performed at scheduled time.
	@Override
	public void run() {
		System.out.println(count + " : Sinhronization");
		count++;
	}

}
