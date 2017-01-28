attribute vec2 a_Position;
attribute vec2 a_TextureCoordinate;
uniform mat4 u_Projection;
uniform mat4 u_View;
varying vec2 v_TextureCoordinate;

void main() {
    v_TextureCoordinate = a_TextureCoordinate;
    mat4 mvp = u_Projection * u_View ;
    gl_Position = mvp * vec4(a_Position, 0.0, 1.0);
}