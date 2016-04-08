#version 330 core

// Constants
const float PI = 3.14159265359;

const float MIN_BRIGHTNESS = 0.1;


// Input
in vec3 pass_colour;
in vec3 world_normal;
in vec3 to_light;


// Uniforms
uniform bool lighting_enabled;
uniform vec3 light_colour;


// Output
out vec4 out_color;

void main()
{
	// Brightness will remain at a default of 1 if lighting is not enabled
	float brightness = 1;
	
	if (lighting_enabled) {
		
		// Because of barycentric interpolation, the normalised status cannot be guaranteed.
		// Therefore, we normalise again.
		vec3 unitWorldNormal = normalize(world_normal);
		vec3 unitToLight = normalize(to_light);
		
		// Ensure that the brightness does not fall below a minimum value
		brightness = max(dot(unitWorldNormal, unitToLight), MIN_BRIGHTNESS);
		
	}
    
    out_color = vec4(pass_colour * brightness * light_colour, 1.0);
}