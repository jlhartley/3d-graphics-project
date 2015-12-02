#version 400 core

// Constants
const float PI = 3.14159;

// Input - vertex data
in vec3 position;
in vec3 normal;
in vec3 colour;


// Uniforms
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

uniform vec3 light_position;


// Output
out vec3 pass_colour;

out vec3 unit_to_light;
out vec3 unit_world_normal;


void main()
{
	// The w component is 0 because the normal represents a direction
	vec3 worldNormal = (model_matrix * vec4(normal, 0)).xyz;
	
	// Alternative:
	//vec3 worldNormal = normalize(normal * mat3(model_matrix));
	
	// Normalise so scale does not affect the dot product
	vec3 unitWorldNormal = normalize(worldNormal);
	
	
	
	
	// Calculate world space vertex position
	vec4 worldPosition = model_matrix * vec4(position, 1);
	
	// Difference between light position and world vertex position
	// gives the direction vector to the light from the vertex
	vec3 toLight = light_position - worldPosition.xyz;
	
	// Also normalise so scale does not affect the dot product
	vec3 unitToLight = normalize(toLight);
	
	
	
	//pass_colour = colour;
	
	unit_world_normal = unitWorldNormal;
	
	unit_to_light = unitToLight;
	
	
	
	// Just white
    pass_colour = vec3(1, 1, 1);
    
    // World position direction colouring
    //pass_colour = normalize(abs(worldPosition.xyz));
    
    // Colour using the world space normals
    //pass_colour = abs(unitWorldNormal);
    
    // Colour using direction to vertex in local object coordinates
    //pass_colour = normalize(abs(position));
    
    
    gl_Position = projection_matrix * view_matrix * worldPosition;
}