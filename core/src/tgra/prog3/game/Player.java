package tgra.prog3.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class Player {
	private Shader3D shader;
	
	public ModelMatrix origin;
	
	private int width, height;
	
	private boolean xPosCollision, xNegCollision, zPosCollision, zNegCollision;
	
	public Player(Shader3D shader, int width, int height) {
		this.shader = shader;
		this.width = width;
		this.height = height;
		this.origin = new ModelMatrix();
		this.origin.loadIdentityMatrix();
		this.origin.addTranslation(2.0f, 1.0f, 1.0f);
		this.origin.addScale(0.4f, 1.0f, 0.4f);
		
		this.xPosCollision = false;
		this.xNegCollision = false;
		this.zPosCollision = false;
		this.zNegCollision = false;
	}
	
	public void update(float deltaTime) {
		// Boundary check
		Point3D originPoint = this.origin.getOrigin();
		if (originPoint.x >= this.width - 1) {
			this.origin.addTranslationBaseCoords(this.width - originPoint.x - 1.0f, 0.0f, 0.0f);
		} else if (originPoint.x <= 0) {
			this.origin.addTranslationBaseCoords(0 - originPoint.x, 0.0f, 0.0f);
		}
		if (originPoint.z >= this.height - 1) {
			this.origin.addTranslationBaseCoords(0.0f, 0.0f, this.height - originPoint.z - 1.0f);
		} else if (originPoint.z <= 0) {
			this.origin.addTranslationBaseCoords(0.0f, 0.0f, 0 - originPoint.z);
		}
		
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			this.origin.addRoatationY(-90.0f * deltaTime);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			this.origin.addRoatationY(90.0f * deltaTime);
		}
		
		this.xPosCollision = false;
		this.xNegCollision = false;
		this.zPosCollision = false;
		this.zNegCollision = false;
		
		if (Gdx.input.isKeyPressed(Keys.W)) {
			this.detectCollision();
			if (!this.xPosCollision) {
				this.origin.addTranslation(0.0f, 0.0f, 4.0f * deltaTime);
			}
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			this.detectCollision();
			if (!this.xNegCollision) {
				this.origin.addTranslation(0.0f, 0.0f, -4.0f * deltaTime);
			}
		}
		
		if (Gdx.input.isKeyPressed(Keys.A)) {
			this.detectCollision();
			if (!this.zPosCollision) {
				this.origin.addTranslation(4.0f * deltaTime, 0.0f, 0.0f);
			}
		}
		
		if (Gdx.input.isKeyPressed(Keys.D)) {
			this.detectCollision();
			if (!this.zNegCollision) {
				this.origin.addTranslation(-4.0f * deltaTime, 0.0f, 0.0f);
			}
		}
	}
	
	public void display() {
		// Player setup
		this.shader.setMaterialDiffuse(0.3f, 0.3f, 1.0f, 1.0f);
		this.origin.pushMatrix();
		ModelMatrix.main.addTransformation(this.origin.matrix);
		this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere();
		this.origin.popMatrix();
	}
	
	public void detectCollision() {
		Point3D originPoint = this.origin.getOrigin();
		
		if (((int)originPoint.x > 0 && (int)originPoint.x < this.width - 1) && ((int)originPoint.z > 0 && (int)originPoint.z < this.height - 1)) {
			float radiusSquared = (float)Math.pow(0.5, 0.4);
			System.out.println("1 === X: " + originPoint.x + " - Z: " + originPoint.z);
			if (Maze.maze[(int)originPoint.x + 1][(int)originPoint.z] == 0) {
				float distance = Math.abs((float)Math.pow((int)(originPoint.x + 1) - originPoint.x, 2) + (float)Math.pow(originPoint.z - originPoint.z, 2));
				if (distance < radiusSquared) {
					System.out.println("X POS COLLISION");
					this.xPosCollision = true;
				}
			}
			System.out.println("2 === X: " + originPoint.x + " - Z: " + originPoint.z);
			if (Maze.maze[(int)originPoint.x - 1][(int)originPoint.z] == 0) {
				float distance = Math.abs((float)Math.pow((int)(originPoint.x - 1) - originPoint.x, 2) + (float)Math.pow(originPoint.z - originPoint.z, 2));
				if (distance < radiusSquared) {
					System.out.println("X NEG COLLISION");
					this.xNegCollision = true;
				}
			}
			System.out.println("3 === X: " + originPoint.x + " - Z: " + originPoint.z);
			if (Maze.maze[(int)originPoint.x][(int)originPoint.z + 1] == 0) {
				float distance = Math.abs((float)Math.pow(originPoint.x - originPoint.x, 2) + (float)Math.pow((int)(originPoint.z + 1) - originPoint.z, 2));
				if (distance < radiusSquared) {
					System.out.println("Z POS COLLISION");
					this.zPosCollision = true;
				}
			}
			System.out.println("4 === X: " + originPoint.x + " - Z: " + originPoint.z);
			if (Maze.maze[(int)originPoint.x][(int)originPoint.z - 1] == 0) {
				float distance = Math.abs((float)Math.pow(originPoint.x - originPoint.x, 2) + (float)Math.pow((int)(originPoint.z - 1) - originPoint.z, 2));
				if (distance < radiusSquared) {
					System.out.println("Z NEG COLLISION");
					this.zNegCollision = true;
				}
			}
		}
	}
}
