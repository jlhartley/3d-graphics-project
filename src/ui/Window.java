package ui;

import math.geometry.Vector2f;

public interface Window {
	public void show();
	public void hide();
	public void centre();
	public boolean shouldClose();
	public int getWidth();
	public int getHeight();
	public boolean isKeyPressed(int keyCode);
	public void enableCursor();
	public void disableCursor();
	public Vector2f getCursorPosition();
}
