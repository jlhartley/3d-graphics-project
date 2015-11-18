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
out vec3 to_light;
out vec3 world_normal;
 
//uniform vec3 colour;

void main()
{
	// The w component is 0 because the normal represents a direction
	// Normalise so scale does not affect the dot product
	vec3 worldNormal = normalize((vec4(normal, 0) * model_matrix).xyz);
	
	// Alternative?
	//vec3 worldNormal = normalize(normal * mat3(model_matrix));
	
	
	// Calculate world space position
	vec4 worldPosition = model_matrix * vec4(position, 1);
	
	// Difference between light position and world vertex position
	// gives the direction vector to the light from the vertex
	// Also normalise so scale does not affect the dot product
	vec3 toLight = normalize(light_position - worldPosition.xyz);
	
	
	
	pass_colour = colour;
	
	world_normal = worldNormal;
	
	to_light = toLight;
	
	
	// Just white
    //pass_colour = vec3(1,1,1);
    
    // World position magnitude colouring
    //pass_colour = normalize(vec3(abs(worldPosition.x), abs(worldPosition.y), abs(worldPosition.z))) * 2;
    
    //pass_colour = worldNormal * 3;
    
    // Colour using direction to vertex
    //pass_colour = normalize((vec4((position), 0) * model_matrix).xyz) * 2;
    
    //vec3 timeBasedColour = vec3(sin(PI/2+time*10), sin(3*PI/2+time*10), sin(PI+time*10));
    
    //pass_colour = mix(colour, timeBasedColour, sin(time)/2-0.5);
    
    
    gl_Position = projection_matrix * view_matrix * worldPosition;
}