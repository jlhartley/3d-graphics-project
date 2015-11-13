#version 400 core

// Constants
const float PI = 3.14159;

const float width = 1280;
const float height = 720;

const float ratio = width / height;

// Near and far planes
const float far = 1000;
const float near = 0.001;

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
    
    /*
    mat4 orthographic = mat4(vec4(1/ratio * 0.005, 0, 0, 0),
    				 		 vec4(0, 0.005, 0, 0),
    				 		 vec4(0, 0, -2/(far-near), -(far+near)/(far-near)),
    				 		 vec4(0, 0, 0, 1));
    */
    
    gl_Position = vec4(position, 1) * model_matrix * view_matrix * projection_matrix;
}