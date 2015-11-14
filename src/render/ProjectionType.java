package render;

// Consider renaming to just projection, and constructing
// actual projections using a Matrix4f - move the MathUtils code into
// here

public enum ProjectionType {
	PERSPECTIVE,
	ORTHOGRAPHIC;
}
