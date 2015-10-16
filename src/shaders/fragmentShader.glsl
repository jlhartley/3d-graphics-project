#version 400 core

in vec3 pass_colour;

out vec4 out_color;

void main()
{
    out_color = vec4(pass_colour, 1.0);
}