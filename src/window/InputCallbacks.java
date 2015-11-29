package window;

public interface InputCallbacks {
	void onKeyPressed(int keyCode);
	void onKeyReleased(int keyCode);
	void onMouseDown(MouseButton mouseButton);
	void onMouseUp(MouseButton mouseButton);
	//void onMousePositionChanged(Vector2f mousePosition);
}
