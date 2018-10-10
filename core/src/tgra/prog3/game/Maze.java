package tgra.prog3.game;

import com.badlogic.gdx.Gdx;

public class Maze {
	private static int colorPointer;
	private static char maze[][];
	
	public static void create(int colorPointer, int dimX, int dimY) {
		Maze.colorPointer = colorPointer;
		
		Maze.maze = new char[dimX][dimY];
		int randomizer = 0;
		// Create a random maze
		// TODO: Make it s a solvable maze.
		// 		 Make it an actual maze and not just some random shit all over the place..
		for (int i = 1; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				randomizer = (int)(Math.random() * 100 + 1);
				if (randomizer >= 15 && randomizer <= 35 || randomizer >= 75 && randomizer <= 80)
					maze[i][j] = 'x';
			}
		}
		// Make sure the start point is never a wall. 
		// Later on, maybe have a dynamic starting point.. or something.
		maze[0][0] = '0';
	}

	public static void drawMaze(float r, float g, float b) {
		Gdx.gl.glUniform4f(Maze.colorPointer, r, g, b, 1.0f);
		Maze.drawSurrounding();
		Maze.drawPath();
	}
	
	private static void drawSurrounding() {
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(25.0f, 0.0f, 25.0f);
		ModelMatrix.main.addScale(50.0f, 0.2f, 50.0f);
		ModelMatrix.main.setShaderMatrix();
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
		
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(25, 2.5f, 0);
		ModelMatrix.main.addScale(50, 5, 0.1f);
		ModelMatrix.main.setShaderMatrix();
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
		
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(50, 2.5f, 25);
		ModelMatrix.main.addScale(0.1f, 5, 50);
		ModelMatrix.main.setShaderMatrix();
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
		
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(0, 2.5f, 25);
		ModelMatrix.main.addScale(0.1f, 5, 50);
		ModelMatrix.main.setShaderMatrix();
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
		
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(25, 2.5f, 50);
		ModelMatrix.main.addScale(50, 5, 0.1f);
		ModelMatrix.main.setShaderMatrix();
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
	}
	
	private static void drawPath() {
		// Wall colour.
		Gdx.gl.glUniform4f(Maze.colorPointer, 0.5f, 0.5f, 0.5f, 1.0f);
		// Draw the actual maze.
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (maze[i][j] == 'x') {
					ModelMatrix.main.pushMatrix();
					ModelMatrix.main.addTranslation(i + 0.5f, 2.5f, j + 0.5f);
					ModelMatrix.main.addScale(1, 5, 1);
					ModelMatrix.main.setShaderMatrix();
					BoxGraphic.drawSolidCube();
					ModelMatrix.main.popMatrix();
				}
			}
		}
	}
}
