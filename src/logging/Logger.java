package logging;

import java.text.DecimalFormat;

public class Logger {
	
	private static final int REPORT_INTERVAL = 5;
	
	private int frameCount = 0;
	private double lastReportTime = 0;
	
	DecimalFormat frameRateFormatter = new DecimalFormat("00.0 FPS");
	DecimalFormat frameTimeFormatter = new DecimalFormat("00.000 ms");
	
	public Logger() {
		
	}
	
	public void log(String message) {
		System.out.println(message);
	}
	
	public void onFrame(double currentTime) {
		
		double elapsedTime = currentTime - lastReportTime;
		
		if (elapsedTime >= REPORT_INTERVAL) {
			
			double frameRate = frameCount / elapsedTime;
			double frameTime = 1000 / frameRate;
			
			log("Frame Rate: " + frameRateFormatter.format(frameRate) + 
				", Frame Time: " + frameTimeFormatter.format(frameTime));
			
			lastReportTime = currentTime;
			frameCount = 0;
		}
		
		frameCount++;
	}
	
	

}
