package tgra.prog3.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;

public class Prog3Game extends ApplicationAdapter implements InputProcessor {
	private int renderingProgramID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private int positionLoc;
	private int normalLoc;

	private int modelMatrixLoc;
	private int viewMatrixLoc;
	private int projectionMatrixLoc;

	private int colorLoc;
	
	private Camera camera;
	private Camera miniMapCamera;
	private float fov;
	private float angle;

	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);

		String vertexShaderString;
		String fragmentShaderString;

		vertexShaderString = Gdx.files.internal("shaders/simple3D.vert").readString();
		fragmentShaderString =  Gdx.files.internal("shaders/simple3D.frag").readString();

		vertexShaderID = Gdx.gl.glCreateShader(GL20.GL_VERTEX_SHADER);
		fragmentShaderID = Gdx.gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);
	
		Gdx.gl.glShaderSource(vertexShaderID, vertexShaderString);
		Gdx.gl.glShaderSource(fragmentShaderID, fragmentShaderString);
	
		Gdx.gl.glCompileShader(vertexShaderID);
		Gdx.gl.glCompileShader(fragmentShaderID);

		renderingProgramID = Gdx.gl.glCreateProgram();
	
		Gdx.gl.glAttachShader(renderingProgramID, vertexShaderID);
		Gdx.gl.glAttachShader(renderingProgramID, fragmentShaderID);
	
		Gdx.gl.glLinkProgram(renderingProgramID);

		positionLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_position");
		Gdx.gl.glEnableVertexAttribArray(positionLoc);

		normalLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_normal");
		Gdx.gl.glEnableVertexAttribArray(normalLoc);

		modelMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_modelMatrix");
		viewMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_viewMatrix");
		projectionMatrixLoc	= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");

		colorLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_color");

		Gdx.gl.glUseProgram(renderingProgramID);

		// Non-OpenGL creation of assets.
		
		BoxGraphic.create(positionLoc, normalLoc);
		SphereGraphic.create(positionLoc, normalLoc);
		SincGraphic.create(positionLoc);
		CoordFrameGraphic.create(positionLoc);
		Pyramid.create(colorLoc);
		Maze.create(this.colorLoc, 50, 50);

		Gdx.gl.glClearColor(0.5f, 0.5f, 1.0f, 1.0f);

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		ModelMatrix.main.setShaderMatrix(modelMatrixLoc);

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		this.fov = 90.0f;
		// Perspective camera at the first person level, stuck at y = 1, so it will not allow going up/down.
		this.camera = new Camera(this.viewMatrixLoc, this.projectionMatrixLoc);
		this.camera.look(new Point3D(1.0f, 1.0f, 1.0f), new Point3D(5, 1, 2.5f), new Vector3D(0, 1, 0));
		
		this.miniMapCamera = new Camera(this.viewMatrixLoc, this.projectionMatrixLoc);
		this.miniMapCamera.orthographicProjection(-10, 10, -10, 10, 3.0f, 100);
	}

	private void input() {
	}
	
	private void update() {
		float deltaTime = Gdx.graphics.getDeltaTime();

		this.angle += 180.0f * deltaTime;
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			System.exit(0);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			this.camera.yaw(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			this.camera.yaw(-90.0f * deltaTime);
		}
		// Uncomment to be able to go above the maze and under
		// to better view it.
		/*if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			this.camera.pitch(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			this.camera.pitch(-90.0f * deltaTime);
		}*/
		
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			this.camera.slide(-3.0f * deltaTime, 0.0f, 0.0f);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			this.camera.slide(3.0f * deltaTime, 0.0f, 0.0f);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			this.camera.slide(0.0f, 0.0f, -3.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			this.camera.slide(0.0f, 0.0f, 3.0f * deltaTime);
		}
		/*if(Gdx.input.isKeyPressed(Input.Keys.R)) {
			this.camera.slide(0.0f, 3.0f * deltaTime, 0.0f);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.F)) {
			this.camera.slide(0.0f, -3.0f * deltaTime, 0.0f);
		}*/
		
		if(Gdx.input.isKeyPressed(Input.Keys.T)) {
			this.fov -= 30.0f * deltaTime;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.G)) {
			this.fov += 30.0f * deltaTime;
		}
		//do all updates to the game
	}
	
	private void display() {
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		for (int viewNum = 0; viewNum < 2; viewNum++) {
			if (viewNum == 0) {
				Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				this.camera.perspectiveProjection(this.fov, 1.0f, 0.4f, 10000.0f);
				this.camera.setShaderMatrices();
			}
			else {
				Gdx.gl.glViewport(Gdx.graphics.getWidth() * (2/3), Gdx.graphics.getHeight() * (2/3), Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 5);
				this.miniMapCamera.look(new Point3D(this.camera.eye.x, 20.0f, this.camera.eye.z), this.camera.eye, new Vector3D(0.0f, 0.0f, -1.0f));
				this.miniMapCamera.setShaderMatrices();
			}
			
			ModelMatrix.main.loadIdentityMatrix();
			
			// Temporary "grid" system. Red is X, green is Y, blue is Z
			// REMOVE BEFORE HANDIN
			ModelMatrix.main.pushMatrix();
			Gdx.gl.glUniform4f(this.colorLoc, 1, 0, 0, 1.0f);
			ModelMatrix.main.addTranslation(0, 0, 0);
			ModelMatrix.main.addScale(10.0f, 0.2f, 0.2f);
			ModelMatrix.main.setShaderMatrix();
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();
			
			ModelMatrix.main.pushMatrix();
			Gdx.gl.glUniform4f(this.colorLoc, 0, 1, 0, 1.0f);
			ModelMatrix.main.addTranslation(0, 0, 0);
			ModelMatrix.main.addScale(0.2f, 10.0f, 0.2f);
			ModelMatrix.main.setShaderMatrix();
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();
			
			ModelMatrix.main.pushMatrix();
			Gdx.gl.glUniform4f(this.colorLoc, 0, 0, 1, 1.0f);
			ModelMatrix.main.addTranslation(0, 0, 0);
			ModelMatrix.main.addScale(0.2f, 0.2f, 10.0f);
			ModelMatrix.main.setShaderMatrix();
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();
			// REMOVE BEFORE HANDIN
			
			Maze.drawMaze(0.237f, 0.201f, 0.175f);
			
			// Maybe include a pyramid or something inside the maze later on. Idk..
			//Pyramid.drawPyramid(15.0f, 0.0f, 15.0f, 0.8f, 0.3f, 1.0f);
			//Pyramid.drawPyramid(35.0f, 0.0f, 15.0f, 0.3f, 0.8f, 0.3f);
			//Pyramid.drawPyramid(15.0f, 0.0f, 35.0f, 0.4f, 0.3f, 1.0f);
			//Pyramid.drawPyramid(35.0f, 0.0f, 35.0f, 0.7f, 0.8f, 0.3f);
			
			if (viewNum == 1) {
				Gdx.gl.glUniform4f(this.colorLoc, 1.0f, 0.3f, 0.1f, 1.0f);
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(this.camera.eye.x, this.camera.eye.y, this.camera.eye.z);
				ModelMatrix.main.addScale(0.35f, 0.35f, 0.35f);
				ModelMatrix.main.setShaderMatrix();
				BoxGraphic.drawSolidCube();
				ModelMatrix.main.popMatrix();
			}
		}
	}

	@Override
	public void render () {
		input();
		//put the code inside the update and display methods, depending on the nature of the code
		update();
		display();

	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
