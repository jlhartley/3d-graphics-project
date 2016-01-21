package window;

import ui.Key;

public interface InputCallbacks {
	void onKeyPressed(Key key);
	void onKeyReleased(Key key);
	void onMouseDown(MouseButton mouseButton);
	void onMouseUp(MouseButton mouseButton);
	//void onMousePositionChanged(Vector2f mousePosition);
}
