#version 400 core

in vec3 pass_colour;
in vec3 world_normal;
in vec3 to_light;

out vec4 out_color;

void main()
{
    float brightness = max(dot(world_normal, to_light), 0.1);
    
    
    out_color = vec4(pass_colour * brightness, 1.0);
}