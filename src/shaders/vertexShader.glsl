#version 400 core

// Constants
const float PI = 3.14159;

// Vertex data
in vec3 position;
in vec3 normal;
in vec3 colour;

out vec3 pass_colour;

// Uniforms
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

//uniform vec3 colour;

uniform float time;

void main()
{   
	pass_colour = colour;

    //pass_colour = vec3(1,1,1);
    
    //pass_colour = (vec4(normal, 0) * model_matrix).xyz;
    
    //pass_colour = (vec4(normalize(position), 0) * model_matrix).xyz;
    
    //vec3 timeBasedColour = vec3(sin(PI/2+time*10), sin(3*PI/2+time*10), sin(PI+time*10));
    
    //pass_colour = mix(colour, timeBasedColour, sin(time)/2-0.5);
    
    gl_Position = vec4(position, 1.0) * model_matrix * view_matrix * projection_matrix;
}