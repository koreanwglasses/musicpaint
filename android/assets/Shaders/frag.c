/* cone-instanced.frag */
varying vec3 vcolor;

void main() {
    gl_FragColor = vec4(vcolor, 1.0);
}