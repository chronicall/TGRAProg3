
#ifdef GL_ES
precision mediump float;
#endif

uniform vec4 u_lightColour;

uniform vec4 u_globalAmbient;

uniform vec4 u_materialAmbient;
uniform vec4 u_materialDiffuse;
uniform vec4 u_materialSpecular;
uniform vec4 u_materialEmission;
uniform float u_materialShiniess;

varying vec4 v_normal;
varying vec4 v_s;
varying vec4 v_h;

void main()
{
	float lambert = max(0.0, dot(v_normal, v_s) / (length(v_normal) * length(v_s)));
	float phong = max(0.0, dot(v_normal, v_h) / (length(v_normal) * length(v_h)));
	
	vec4 diffuseColour = lambert * u_lightColour * u_materialDiffuse;
	vec4 specularColour = pow(phong, u_materialShiniess) * u_lightColour * u_materialSpecular;
	
	vec4 light1Calc = diffuseColour + specularColour;
	
	gl_FragColor =  u_globalAmbient * u_materialAmbient + u_materialEmission + light1Calc;
}