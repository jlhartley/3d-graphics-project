#version 330 core

// Constants
const float PI = 3.14159265359;

// Lighting

//const float MIN_BRIGHTNESS = 0.1;

const float AMBIENT_BRIGHTNESS = 0.15;
const vec3 AMBIENT_COLOUR = vec3(1.0, 1.0, 0.9);


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
	float diffuseBrightness = 1;
	
	if (lighting_enabled) {
		
		// Because of barycentric interpolation, the normalised status cannot be guaranteed.
		// Therefore, we normalise again.
		vec3 unitWorldNormal = normalize(world_normal);
		vec3 unitToLight = normalize(to_light);
		
		// Ensure that the brightness is not negative - range is between 0 and 1
		// Also correct for ambient lighting
		diffuseBrightness = max(dot(unitWorldNormal, unitToLight), 0) * (1 - AMBIENT_BRIGHTNESS);
		
	}
    
	vec3 finalLighting = AMBIENT_COLOUR * AMBIENT_BRIGHTNESS 
	                    + light_colour * diffuseBrightness;
	
    out_color = vec4(pass_colour * finalLighting, 1.0);
}
