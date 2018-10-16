
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

struct PointLight {
	vec4 position;
	
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	
	float constant;
	float linear;
	float quadratic;
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

vec4 posLightValue(Light light, vec4 normal, vec4 fragmentPosition, vec4 v);
vec4 dirLightValue(DirLight light, vec4 normal, vec4 v);
vec4 pointLightValue(PointLight light, vec4 normal, vec4 fragmentPosition, vec4 v);
vec4 spotLightValue(SpotLight light, vec4 normal, vec4 fragmentPosition, vec4 v);

void main()
{
	vec4 normal = normalize(v_normal);
	vec4 v = normalize(u_eyePosition - v_position);
	
	// Treasure light
	vec4 lightIntensity = posLightValue(u_treasureLight, normal, v_position, v);
	
	// Direction lights of the two suns
	lightIntensity += dirLightValue(u_sun1, normal, v);
	lightIntensity += dirLightValue(u_sun2, normal, v);
	
	gl_FragColor = u_globalAmbient * u_material.ambient + u_material.emission + lightIntensity;
}

vec4 posLightValue(Light light, vec4 normal, vec4 fragmentPosition, vec4 v) {
	vec4 s = normalize(u_treasureLight.position - v_position);
	vec4 h = normalize(s + v);
	
	float lambert = max(0.0, dot(s, normal) / (length(s) * length(normal)));
	float phong = max(0.0, dot(h, normal) / (length(s) * length(normal)));
	
	vec4 ambient = light.ambient * u_material.ambient;
	vec4 diffuse = lambert * light.diffuse * u_material.diffuse;
	vec4 specular = pow(phong, u_material.shininess) * light.specular * u_material.specular;

	return (ambient + diffuse + specular);	
}

vec4 dirLightValue(DirLight light, vec4 normal, vec4 v) {
	vec4 lightDirection = normalize(-light.direction);
    vec4 reflectDirection = reflect(-lightDirection, normal);

    float lambert = max(0.0, dot(lightDirection, normal) / (length(lightDirection) * length(normal)));
	float phong = max(0.0, dot(reflectDirection, v) / (length(reflectDirection) * length(v)));
	
    vec4 ambient  = light.ambient  * u_material.ambient;
    vec4 diffuse  = lambert * light.diffuse  * u_material.diffuse;
    vec4 specular = pow(phong, u_material.shininess) * light.specular * u_material.specular;
    
    return (ambient + diffuse + specular);
}

vec4 pointLightValue(PointLight light, vec4 normal, vec4 fragmentPosition, vec4 v) {
	vec4 lightDirection = normalize(light.position - fragmentPosition);
	vec4 reflectionDirection = reflect(-lightDirection, normal);
	
	float lambert = max(0.0, dot(lightDirection, normal) / (length(lightDirection) * length(normal)));
	float phong = max(0.0, dot(reflectionDirection, v) / (length(reflectionDirection) * length(v)));
	
	float distance = length(light.position - fragmentPosition);
	float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
	
	vec4 ambient  = light.ambient  * u_material.ambient;
    vec4 diffuse  = lambert * light.diffuse  * u_material.diffuse;
    vec4 specular = pow(phong, u_material.shininess) * light.specular * u_material.specular;
    ambient  *= attenuation;
    diffuse  *= attenuation;
    specular *= attenuation;
    
    return (ambient + diffuse + specular);
}

vec4 spotLightValue(SpotLight light, vec4 normal, vec4 fragmentPosition, vec4 v) {
	vec4 lightDirection = normalize(light.position - fragmentPosition);
	vec4 reflectionDirection = reflect(-lightDirection, normal);
	
	float lambert = max(0.0, dot(lightDirection, normal) / (length(lightDirection) * length(normal)));
	float phong = max(0.0, dot(reflectionDirection, v) / (length(reflectionDirection) * length(v)));
	
	float distance = length(light.position - fragmentPosition);
	float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
	
	float theta = dot(lightDirection, normalize(-light.direction));
	float epsilon = light.cutOff - light.outerCutOff;
	float intensity = clamp((theta - light.outerCutOff) / epsilon, 0.0, 1.0);
	
	vec4 ambient  = light.ambient  * u_material.ambient;
    vec4 diffuse  = lambert * light.diffuse  * u_material.diffuse;
    vec4 specular = pow(phong, u_material.shininess) * light.specular * u_material.specular;
    ambient  *= attenuation * intensity;
    diffuse  *= attenuation * intensity;
    specular *= attenuation * intensity;
    
    return (ambient + diffuse + specular);
}