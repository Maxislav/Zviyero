attribute vec4 a_Position;
attribute vec2 a_Texture;

uniform mat4 u_Matrix;
varying vec2 v_Texture;

void main()
{
    gl_Position = u_Matrix * a_Position;
    v_Texture = a_Texture;
}
