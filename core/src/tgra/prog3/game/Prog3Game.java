package tgra.prog3.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;

public class Prog3Game extends ApplicationAdapter implements InputProcessor {
	private Shader3D shader;
	
	private Camera camera;
	private Camera miniMapCamera;
	private float fov;
	private float angle;
	private boolean firstPersonView;
	private Point3D eye;
	private Point3D center;
	private Vector3D up;
	
	private int width;
	private int height;
	
	private float timer;
	
	private Player player;
	private Enemy enemy;
	private int playerScore;
	private int enemyScore;

	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);

		this.shader = new Shader3D();
		
		BoxGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SphereGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SincGraphic.create(shader.getVertexPointer());
		CoordFrameGraphic.create(shader.getVertexPointer());
		Pyramid.create(shader);
		this.height = 21;
		this.width = 19;
		Maze.create(shader, this.width, this.height);

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		this.fov = 90.0f;
		this.playerScore = 0;
		this.enemyScore = 0;
		// Perspective camera at the first person level, stuck at y = 1, so it will not allow going up/down.
		this.setup();
	}
	
	private void setup() {
		Maze.newLevel();
		System.out.println("SCORE:");
		System.out.println("Player: " + this.playerScore + " - Enemy: " + this.enemyScore);
		this.camera = new Camera();
		this.eye = new Point3D(2.0f, 1.0f, 1.0f);
		this.center = new Point3D(2.0f, 1.0f, 5.0f);
		this.up = new Vector3D(0.0f, 1.0f, 0.0f);
		this.camera.look(this.eye, this.center, this.up);
		this.firstPersonView = true;
		
		this.miniMapCamera = new Camera();
		this.miniMapCamera.orthographicProjection(-10, 10, -10, 10, 3.0f, 1000);
		
		this.timer = 0;
		
		Point3D playerPosition = new Point3D(2.0f, 1.0f, 1.0f);
		Vector3D playerDiffuse = new Vector3D(0.2f, 0.2f, 1.0f);
		float playerShine = 10.0f;
		this.player = new Player(this.shader, this.width, this.height, playerPosition, playerDiffuse, playerShine);
		
		Point3D enemyPosition = new Point3D(this.width - 3.0f, 1.0f, this.height - 2.0f);
		Vector3D enemyDiffuse = new Vector3D(1.0f, 0.2f, 2.0f);
		float enemyShine = 10.0f;
		this.enemy = new Enemy(this.shader, this.width, this.height, enemyPosition, enemyDiffuse, enemyShine);
	}

	private void input() {
	}
	
	private void update() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		//this.timer += deltaTime * 60.0f;

		this.angle += 90.0f * deltaTime;
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		// Toggle between first and third person view
		if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
			this.firstPersonView = !this.firstPersonView;
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			this.camera.pitch(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			this.camera.pitch(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			this.camera.yaw(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			this.camera.yaw(-90.0f * deltaTime);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.T)) {
			this.fov -= 30.0f * deltaTime;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.G)) {
			this.fov += 30.0f * deltaTime;
		}
		//do all updates to the game
		this.player.update(deltaTime);
		this.enemy.update(deltaTime);
		
		Point3D playerOriginPoint = this.player.origin.getOrigin();
		// Check if the goal has been reached.
		if ((int)playerOriginPoint.x == Maze.goalX && (int)playerOriginPoint.z == Maze.goalZ) {
			this.playerScore++;
			this.setup();
		}
		
		Point3D enemyOriginPoint = this.enemy.origin.getOrigin();
		// Check if enemy has reached the goal
		if ((int)enemyOriginPoint.x == Maze.goalX && (int)enemyOriginPoint.z == Maze.goalZ) {
			this.enemyScore++;
			this.setup();
		}
		
		this.camera.setEye(playerOriginPoint.x, playerOriginPoint.y + 1.0f, playerOriginPoint.z);
		
		if (this.firstPersonView) {
			this.eye.set(playerOriginPoint.x, playerOriginPoint.y + 1.0f, playerOriginPoint.z);
		} else {
			this.eye.set(playerOriginPoint.x, playerOriginPoint.y + 2.0f, playerOriginPoint.z - 1.5f);
		}
		this.camera.setEye(this.eye.x, this.eye.y, this.eye.z);
	}
	
	private void display() {
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		for (int viewNum = 0; viewNum < 2; viewNum++) {
			if (viewNum == 0) {
				Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				this.camera.perspectiveProjection(this.fov, 1.0f, 0.4f, 10000.0f);
				this.shader.setProjectionMatrix(this.camera.getProjectionMatrix());
				this.shader.setViewMatrix(this.camera.getViewMatrix());
				this.shader.setEyePosition(this.camera.eye.x, this.camera.eye.y, this.camera.eye.z, 1.0f);
			} else {
				Gdx.gl.glViewport(Gdx.graphics.getWidth() * (2/3), Gdx.graphics.getHeight() * (2/3), Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 5);
				this.miniMapCamera.look(new Point3D(this.camera.eye.x, 20.0f, this.camera.eye.z), this.camera.eye, new Vector3D(0.0f, 0.0f, -1.0f));
				this.shader.setProjectionMatrix(this.miniMapCamera.getProjectionMatrix());
				this.shader.setViewMatrix(this.miniMapCamera.getViewMatrix());
				this.shader.setEyePosition(this.miniMapCamera.eye.x, this.miniMapCamera.eye.y, this.miniMapCamera.eye.z, 1.0f);
			}
			
			ModelMatrix.main.loadIdentityMatrix();
			
			float s = (float)Math.sin(this.angle * Math.PI / 180.0);
			float c = (float)Math.cos(this.angle * Math.PI / 180.0);
			
			this.shader.setLightPosition(2.0f, 4.5f, 1.0f, 1.0f);
			this.shader.setLightColour(1.0f, 1.0f, 1.0f, 1.0f);
			
			this.shader.setGlobalAmbient(0.1f, 0.1f, 0.1f, 1.0f);
			
			// Temporary coordinate frame. Red is X, green is Y, blue is Z
			// REMOVE BEFORE HANDIN
			ModelMatrix.main.pushMatrix();
			this.shader.setMaterialDiffuse(1, 0, 0, 1.0f);
			ModelMatrix.main.addTranslation(0, 0, 0);
			ModelMatrix.main.addScale(10.0f, 0.2f, 0.2f);
			this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();
			
			ModelMatrix.main.pushMatrix();
			this.shader.setMaterialDiffuse(0, 1, 0, 1.0f);
			ModelMatrix.main.addTranslation(0, 0, 0);
			ModelMatrix.main.addScale(0.2f, 10.0f, 0.2f);
			this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();
			
			ModelMatrix.main.pushMatrix();
			this.shader.setMaterialDiffuse(0, 0, 1, 1.0f);
			ModelMatrix.main.addTranslation(0, 0, 0);
			ModelMatrix.main.addScale(0.2f, 0.2f, 10.0f);
			this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();
			// END REMOVE BEFORE HANDIN
			
			Maze.drawMaze(this.angle);
			
			this.shader.setMaterialDiffuse(1.0f, 0.0f, 0.0f, 1.0f);
			this.shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
			this.shader.setMaterialShiniess(20.0f);
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(2.0f, 1.5f, 0.0f);
			ModelMatrix.main.addScale(0.5f, 1.0f, 0.5f);
			this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
			SphereGraphic.drawSolidSphere();
			ModelMatrix.main.popMatrix();
			
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(3.0f, 1.5f, 0.0f);
			ModelMatrix.main.addScale(0.5f, 1.0f, 0.5f);
			this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
			SphereGraphic.drawSolidSphere();
			ModelMatrix.main.popMatrix();
			
			this.shader.setMaterialDiffuse(0.0f, 0.0f, 1.0f, 1.0f);
			this.shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
			this.shader.setMaterialShiniess(20.0f);
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(0.0f, 1.5f, 2.0f);
			ModelMatrix.main.addScale(0.5f, 1.0f, 0.5f);
			this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
			SphereGraphic.drawSolidSphere();
			ModelMatrix.main.popMatrix();
			
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(0.0f, 1.5f, 3.0f);
			ModelMatrix.main.addScale(0.5f, 1.0f, 0.5f);
			this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
			SphereGraphic.drawSolidSphere();
			ModelMatrix.main.popMatrix();

			this.enemy.display();
			
			this.player.display();
			// Maybe include a pyramid or something inside the maze later on. Idk..
			//Pyramid.drawPyramid(15.0f, 0.0f, 15.0f, 0.8f, 0.3f, 1.0f);
			//Pyramid.drawPyramid(35.0f, 0.0f, 15.0f, 0.3f, 0.8f, 0.3f);
			//Pyramid.drawPyramid(15.0f, 0.0f, 35.0f, 0.4f, 0.3f, 1.0f);
			//Pyramid.drawPyramid(35.0f, 0.0f, 35.0f, 0.7f, 0.8f, 0.3f);
			
			if (viewNum == 1) {
				this.shader.setMaterialDiffuse(1.0f, 0.3f, 0.1f, 1.0f);
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(this.camera.eye.x, this.camera.eye.y, this.camera.eye.z);
				ModelMatrix.main.addScale(0.35f, 0.35f, 0.35f);
				this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
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
