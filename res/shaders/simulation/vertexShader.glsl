#version 330 core

// The naming convention I use here is:
// Underscore case for global variables (uniforms, inputs / outputs)
// Camel case for local variables

// Constants
const float PI = 3.14159265359;

// Input - Vertex Attributes
in vec3 position;
in vec3 normal;
in vec3 colour;


// Uniforms
// Transformation Uniforms
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

// Lighting Uniforms
uniform bool lighting_enabled;
uniform vec3 light_position; // In world space


// Output
out vec3 pass_colour;

out vec3 to_light;
out vec3 world_normal;


void main()
{
	// Calculate world space vertex position
	vec4 worldPosition = model_matrix * vec4(position, 1);
	
	
	if (lighting_enabled)
	{
		// Transform the normals to world space, so that they are rotated
		// The w component is 0 because the normal represents a direction
		vec3 worldNormal = (model_matrix * vec4(normal, 0)).xyz;
		
		// Alternative:
		//vec3 worldNormal = mat3(model_matrix) * normal;
		
		// Note that the above will not work in cases where a non-uniform scale is applied.
		// This will distort the normal direction.
		//mat3 mi = transpose(inverse(mat3(model_matrix)));
		//world_normal = mi * normal;
		
		// The normal may have been scaled due to world space transformation
		// Therefore, normalise to ensure correct direction for interpolation
		world_normal = normalize(worldNormal);
		
		
		
		
		// Difference between light position and world vertex position
		// gives the direction vector to the light from the vertex
		to_light = light_position - worldPosition.xyz;
		
		// TODO: Also normalise to_light to ensure correct interpolation direction?
		// Am not sure if this is needed?
		//vec3 unit_to_light = normalize(toLight);
	}
	

	
	
	// Colour output
	
	// Use the attribute
	//pass_colour = colour;
	
	// White
    pass_colour = vec3(1, 1, 1);
    
    // World position direction colouring
    //pass_colour = normalize(abs(worldPosition.xyz));
    
    // Colour using the world space normals
    //pass_colour = abs(unitWorldNormal);
    
    // Colour using direction to vertex in local object coordinates
    //pass_colour = normalize(abs(position));
    
    
    gl_Position = projection_matrix * view_matrix * worldPosition;
}