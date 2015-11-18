#version 400 core

const float MIN_BRIGHTNESS = 0.1;

in vec3 pass_colour;
in vec3 world_normal;
in vec3 to_light;

uniform bool lighting_enabled;


out vec4 out_color;

void main()
{
	
	float brightness = 1;
	
	if (lighting_enabled) {
		
		// Ensure that the brightness does not fall below a minimum value
		brightness = max(dot(world_normal, to_light), MIN_BRIGHTNESS);
		
	}
    
    out_color = vec4(pass_colour * brightness, 1.0);
}