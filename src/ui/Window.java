package ui;

import math.geometry.Vector2f;

public interface Window {
	public void show();
	public void hide();
	public void centre();
	public boolean isKeyPressed(Key key);
	public void enableCursor();
	public void disableCursor();
	public Vector2f getCursorPosition();
	public int getWidth();
	public int getHeight();
}
