package time;

public class Timer {
	
	//private double time;
	private double startTime;
	
	public Timer() {
		startTime = getSystemTime();
	}
	
	public double getTime() {
		return getSystemTime() - startTime;
	}
	
	public void setTime(double time) {
		
	}
	
	public void zero() {
		setTime(0);
		startTime = getSystemTime();
	}

	private double getSystemTime() {
		return System.nanoTime() / 10E8;
	}
}
