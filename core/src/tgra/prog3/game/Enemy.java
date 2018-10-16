package tgra.prog3.game;

import java.util.concurrent.ThreadLocalRandom;

public class Enemy extends Character {
	
	public Enemy(Shader3D shader, int width, int height, Point3D position, Vector3D matAmbient, Vector3D matDiffuse, Vector3D matSpecular, float shine) {
		super(shader, width, height, position, matAmbient, matDiffuse, matSpecular, shine);
	}
	
	public void display() {
		super.display();
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
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
		
		Vector3D vecUpZ = new Vector3D(0.0f, 0.0f, 4.0f * deltaTime);
		Vector3D vecDownZ = new Vector3D(0.0f, 0.0f, -4.0f * deltaTime);
		Vector3D vecUpX = new Vector3D(4.0f * deltaTime, 0.0f, 0.0f);
		Vector3D vecDownX = new Vector3D(-4.0f * deltaTime, 0.0f, 0.0f);
		
		Point3D movingTo = new Point3D();
		int randomNum = ThreadLocalRandom.current().nextInt(1, 5); //number between 1 and 4
		if (randomNum == 1) {
			movingTo = originPoint.add(vecUpZ);
		} else if (randomNum == 2) {
			movingTo = originPoint.add(vecDownZ);
		} else if (randomNum == 3) {
			movingTo = originPoint.add(vecUpX);
		} else if (randomNum == 4) {
			movingTo = originPoint.add(vecDownX);
		}
		
		// Check whether we'd be outside the boundary of the maze, avoiding index out of bounds errors.
		if (((int)movingTo.x > 0 && (int)movingTo.x < this.width - 1) && ((int)movingTo.z > 0 && (int)movingTo.z < this.height - 1)) {
			if(randomNum == 1) {
				if (Maze.maze[(int)movingTo.x][(int)(movingTo.z - 1)] == 0){
					this.origin.addTranslation(vecUpX.x - 2.0f * deltaTime, vecUpX.y, vecUpX.z);
				} else {
					this.origin.addTranslation(vecUpZ.x, vecUpZ.y, vecUpZ.z);
				}
			} else if (randomNum == 2) {			
				if (Maze.maze[(int)movingTo.x][(int)(movingTo.z + 1)] == 0){
					this.origin.addTranslation(vecDownZ.x + 2.0f * deltaTime, vecDownZ.y, vecDownZ.z);
				} else {
					this.origin.addTranslation(vecDownZ.x, vecDownZ.y, vecDownZ.z);
				}
			} else if (randomNum == 3) {			
				if (Maze.maze[(int)movingTo.x -1 ][(int)(movingTo.z)] == 0){
					this.origin.addTranslation(vecUpX.x, vecUpX.y, vecUpX.z - 2.0f * deltaTime);
				} else {
					this.origin.addTranslation(vecUpX.x, vecUpX.y, vecUpX.z);
				}
			} else if (randomNum == 4) {
				if (Maze.maze[(int)movingTo.x + 1 ][(int)(movingTo.z)] == 0){
					this.origin.addTranslation(vecDownX.x, vecDownX.y, vecDownX.z + 2.0f * deltaTime);
				} else {
					this.origin.addTranslation(vecDownX.x, vecDownX.y, vecDownX.z);
				}
			}
		}
	}
}
