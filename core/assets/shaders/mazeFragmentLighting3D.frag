
#ifdef GL_ES
precision mediump float;
#endif

struct Material {
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	float shininess;
	
	vec4 emission;
};

struct DirLight {
	vec4 direction;
	
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
};

struct Light {
	vec4 position;
	
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
};

struct SpotLight {
	vec4 position;
	vec4 direction;
	
	float cutOff;
	float outerCutOff;
  
    float constant;
    float linear;
    float quadratic;
  
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
};

uniform vec4 u_eyePosition;

uniform Light u_treasureLight;
uniform DirLight u_sun1;
uniform DirLight u_sun2;
uniform Material u_material;

uniform vec4 u_globalAmbient;

varying vec4 v_normal;
varying vec4 v_position;

vec4 dirLightValue(DirLight light, vec4 normal, vec4 v);

void main()
{
	vec4 normal = normalize(v_normal);
	vec4 v = normalize(u_eyePosition - v_position);
	
	// Treasure light
	vec4 s = normalize(u_treasureLight.position - v_position);
	vec4 h = normalize(s + v);
	
	float lambert = max(0.0, dot(normal, s));
	float phong = max(0.0, dot(normal, h));
	
	vec4 lightIntensity = u_treasureLight.ambient * u_material.ambient +										// Ambient
						  lambert * u_treasureLight.diffuse * u_material.diffuse +								// Diffuse
						  pow(phong, u_material.shininess) * u_treasureLight.specular * u_material.specular;	// Specular
	
	lightIntensity += dirLightValue(u_sun1, normal, v);
	lightIntensity += dirLightValue(u_sun2, normal, v);
	
	gl_FragColor = u_globalAmbient * u_material.ambient + u_material.emission + lightIntensity;
}

vec4 dirLightValue(DirLight light, vec4 normal, vec4 v) {
	vec4 lightDirection = normalize(-light.direction);
    vec4 reflectDirection = reflect(-lightDirection, normal);

    float lambert = max(0.0, dot(normal, lightDirection));
	float phong = max(0.0, dot(v, reflectDirection));
	
    vec4 ambient  = light.ambient  * u_material.diffuse;
    vec4 diffuse  = light.diffuse  * lambert * u_material.diffuse;
    vec4 specular = light.specular * pow(phong, u_material.shininess) * u_material.specular;
    return (ambient + diffuse + specular);
}