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

	//private int colorLoc;
	private int lightPositionLoc;
	private int lightDiffuseLoc;
	
	private int materialDiffuseLoc;
	
	public Shader3D() {
		String vertexShaderString;
		String fragmentShaderString;

		vertexShaderString = Gdx.files.internal("shaders/simple3D.vert").readString();
		fragmentShaderString =  Gdx.files.internal("shaders/simple3D.frag").readString();

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

		//this.colorLoc				= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_color");
		this.lightPositionLoc		= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_lightPosition");
		this.lightDiffuseLoc		= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_lightDiffuse");
		
		this.materialDiffuseLoc		= Gdx.gl.glGetUniformLocation(this.renderingProgramID, "u_materialDiffuse");

		Gdx.gl.glUseProgram(this.renderingProgramID);
	}
	
	/*public void setColour(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.colorLoc, r, g, b, a);
	}*/

	public void setLightPosition(float x, float y, float z, float w) {
		Gdx.gl.glUniform4f(this.lightPositionLoc, x, y, z, w);
	}
	
	public void setMaterialDiffuse(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.materialDiffuseLoc, r, g, b, a);
	}

	public void setLightDiffuse(float r, float g, float b, float a) {
		Gdx.gl.glUniform4f(this.lightDiffuseLoc, r, g, b, a);
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
