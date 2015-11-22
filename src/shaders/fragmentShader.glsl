#version 400 core

// Constants
const float MIN_BRIGHTNESS = 0.1;

// Input
in vec3 pass_colour;
in vec3 unit_world_normal;
in vec3 unit_to_light;


// Uniforms
uniform bool lighting_enabled;


// Output
out vec4 out_color;


// Global state
vec3 colour = pass_colour;


// TODO: Get rid of global state "colour" variable
void colourByNormalisationError()
{
		// The error that results from vertex shader interpolation
		float toLightNormalisationError = abs(length(unit_to_light) - 1);
		float normalNormalisationError = abs(length(unit_world_normal) - 1);
		
		// Colour based on the error
		// Green for normal interpolation error
		if (normalNormalisationError > 0.0035) {
			colour = vec3(0, 1, 0);
		}
		// Orange for "to light" interpolation error
		if (toLightNormalisationError > 0.0001) {
			colour = vec3(1, 1, 0);
		}
}

void main()
{
	float brightness = 1;
	
	if (lighting_enabled) {
		
		//float distanceToLight = length(to_light);
		
		//colourByNormalisationError();
		
		// Because of interpolation, the normalised status cannot be guaranteed.
		// Therefore, we normalise again.
		vec3 unitToLight = normalize(unit_to_light);
		vec3 unitWorldNormal = normalize(unit_world_normal);
		
		// Ensure that the brightness does not fall below a minimum value
		//brightness = max(dot(unit_world_normal, unit_to_light), MIN_BRIGHTNESS);
		
		brightness = max(dot(unitWorldNormal, unitToLight), MIN_BRIGHTNESS);
		
		//brightness *= 10000 / (distanceToLight * distanceToLight);
		
	}
    
    out_color = vec4(colour * brightness, 1.0);
}