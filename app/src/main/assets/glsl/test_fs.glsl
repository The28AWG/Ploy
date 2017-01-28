precision mediump float;

uniform sampler2D u_TextureSampler;
varying vec2 v_TextureCoordinate;

void main() {
    gl_FragColor = texture2D(u_TextureSampler, v_TextureCoordinate);
//    gl_FragColor = vec4(1, 1, 1, 1);
}