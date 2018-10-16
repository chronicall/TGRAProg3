
#ifdef GL_ES
precision mediump float;
#endif

attribute vec3 a_position;
attribute vec3 a_normal;

uniform mat4 u_modelMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

varying vec4 v_normal;
varying vec4 v_position;

void main()
{
	// Global coordinate.
	v_position = u_modelMatrix * vec4(a_position.x, a_position.y, a_position.z, 1.0);
	v_normal = u_modelMatrix * vec4(a_normal.x, a_normal.y, a_normal.z, 0.0);
	
	// Local coordinates.

	gl_Position = u_projectionMatrix * u_viewMatrix * v_position;
}