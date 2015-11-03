package logging;

public class Logger {
	
	private static final int REPORT_INTERVAL = 5;
	
	private int frameCount = 0;
	private double lastReportTime = 0;
	
	public Logger() {
		
	}
	
	public void onFrame(double currentTime) {
		
		double elapsedTime = currentTime - lastReportTime;
		
		if (elapsedTime > REPORT_INTERVAL) {
			
			double frameRate = frameCount / elapsedTime;
			
			//System.out.println("Framerate: " + frameRate + " FPS");
			System.out.println("Frame time: " + 1000 / frameRate + " ms");
			
			lastReportTime = currentTime;
			frameCount = 0;
		}
		
		frameCount++;
	}
	
	

}
