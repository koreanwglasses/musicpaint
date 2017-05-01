/* cone-instanced.vert */
attribute vec3 cone;
attribute vec2 position;
attribute vec3 color;

varying vec3 vcolor;

void main() {
    vcolor = color;
    gl_Position = vec4(cone.xy + position, cone.z, 1.0);
}