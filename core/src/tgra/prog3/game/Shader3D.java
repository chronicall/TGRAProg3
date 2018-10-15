package tgra.prog3.game;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Shader3D {
	private int renderingProgramID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private int positionLoc;
	private int normalLoc;

	private int modelMatrixLoc;
	private int viewMatrixLoc;
	private int projectionMatrixLoc;

	private int eyePositionLoc;
	
	private int globalAmbientLoc;
	
	private int lightPositionLoc;
	private int lightColourLoc;
	
	private int materialAmbientLoc;
	private int materialDiffuseLoc;
	private int materialSpecularLoc;
	private int materialEmissionLoc;
	private int materialShininessLoc;
	
	public Shader3D() {
		String vertexShaderString;
		String fragmentShaderString;

		vertexShaderString = Gdx.files.internal("shaders/mazeFragmentLighting3D.vert").readString();
		fragmentShaderString =  Gdx.files.internal("shaders/mazeFragmentLighting3D.frag").readString();

		this.vertexShaderID = Gdx.gl.glCreateShader(GL20.GL_VERTEX_SHADER);
		this.fragmentShaderID = Gdx.gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);
	
		Gdx.gl.glShaderSource(this.vertexShaderID, vertexShaderString);
		Gdx.gl.glShaderSource(this.fragmentShaderID, fragmentShaderString);
	
		Gdx.gl.glCompileShader(this.vertexShaderID);
		Gdx.gl.glCompileShader(this.fragmentShaderID);

		this.renderingProgramID = Gdx.gl.glCreateProgram();
	
		Gdx.gl.glAttachShader(this.renderingProgramID, this.vertexShaderID);
		Gdx.gl.glAttachShader(this.renderingProgramID, this.fragmentShaderID);
	
		Gdx.gl.glLinkProgram(this.renderingProgramID);

		this.positionLoc			= Gdx.gl.glGetAttribLocation(this.renderingProgramID, "a_position");
		Gdx.gl.glEnableVertexAttribArray(this.positionLoc);

		this.normalLoc				= Gdx.gl.glGetAttribLocation(this.renderingProgramID, "a_normal");
		Gdx.gl.glEnableVertexAttribArray(this.normalLoc);

		this.modelMatrixLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_modelMatrix");
		this.viewMatrixLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_viewMatrix");
		this.projectionMatrixLoc	= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_projectionMatrix");

		this.eyePositionLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_eyePosition");
		
		this.globalAmbientLoc		= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_globalAmbient");
		
		this.lightPositionLoc		= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_lightPosition");
		this.lightColourLoc			= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_lightColour");
		
		this.materialAmbientLoc		= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_materialAmbient");
		this.materialDiffuseLoc		= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_materialDiffuse");
		this.materialSpecularLoc	= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_materialSpecular");
		this.materialEmissionLoc	= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_materialEmission");
		this.materialShininessLoc	= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_materialShiniess");

		Gdx.gl.glUseProgram(this.renderingProgramID);
	}

	public void setEyePosition(float x, float y, float z, float w) {
		Gdx.gl.glUniform4f(this.eyePositionLoc, x, y, z, w);
	}
	
	public void setGlobalAmbient(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.globalAmbientLoc, r, g, b, a);
	}
	
	public void setLightPosition(float x, float y, float z, float w) {
		Gdx.gl.glUniform4f(this.lightPositionLoc, x, y, z, w);
	}
	public void setLightColour(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.lightColourLoc, r, g, b, a);
	}
	
	public void setMaterialAmbient(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.materialAmbientLoc, r, g, b, a);
	}
	public void setMaterialDiffuse(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.materialDiffuseLoc, r, g, b, a);
	}
	public void setMaterialSpecular(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.materialSpecularLoc, r, g, b, a);
	}
	public void setMaterialEmission(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.materialEmissionLoc, r, g, b, a);
	}
	public void setMaterialShiniess(float shine) {
		Gdx.gl.glUniform1f(this.materialShininessLoc, shine);
	}
	
	public int getVertexPointer() {
		return this.positionLoc;
	}
	
	public int getNormalPointer() {
		return this.normalLoc;
	}
	
	public void setModelMatrix(FloatBuffer matrix) {
		Gdx.gl.glUniformMatrix4fv(this.modelMatrixLoc, 1, false, matrix);
	}
	
	public void setViewMatrix(FloatBuffer matrix) {
		Gdx.gl.glUniformMatrix4fv(this.viewMatrixLoc, 1, false, matrix);
	}
	
	public void setProjectionMatrix(FloatBuffer matrix) {
		Gdx.gl.glUniformMatrix4fv(this.projectionMatrixLoc, 1, false, matrix);
	}
}
