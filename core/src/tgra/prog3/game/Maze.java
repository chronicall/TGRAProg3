package tgra.prog3.game;

import java.util.Random;

import com.badlogic.gdx.Gdx;

/**
 * Maze generation algorithm found here: https://github.com/joewing/maze/blob/master/Maze.java
 * BSD-3 license.
 * // Maze generator in Java
 * // Joe Wingbermuehle
 * // 2015-07-27
 * 
 * Modified for OpenGL by Sandra Rós Hrefnu Jónsdóttir.
 */
public class Maze {
	private static Shader3D shader;
	
	public static byte maze[][];

	private static final int WALL = 0;
	private static final int SPACE = 1;

	private static int width;
	private static int height;
	
	private static float angle;

	private static Random rand;
	
	public static int goalX, goalZ;

	public static void create(Shader3D shader, int width, int height) {
		Maze.shader = shader;
		Maze.width = width;
		Maze.height = height;
		Maze.angle = 0.0f;

		Maze.maze = new byte[width][];
		Maze.rand = new Random();
	}
	
	public static void newLevel() {
		Maze.generate();
		Maze.consolePrint();
	}
	
	private static void consolePrint() {
		for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
               if(maze[x][y] == WALL) {
                  System.out.print("[]");
               } else {
                  System.out.print("  ");
               }
            }
            System.out.println();
		}
	}

	private static void carve(int x, int y) {
		final int[] upx = { 1, -1, 0, 0 };
		final int[] upy = { 0, 0, 1, -1 };

		int dir = Maze.rand.nextInt(4);
		int count = 0;
		while(count < 4) {
			final int x1 = x + upx[dir];
			final int y1 = y + upy[dir];
			final int x2 = x1 + upx[dir];
			final int y2 = y1 + upy[dir];
			if(maze[x1][y1] == WALL && maze[x2][y2] == WALL) {
				maze[x1][y1] = SPACE;
				maze[x2][y2] = SPACE;
				carve(x2, y2);
			} else {
				dir = (dir + 1) % 4;
				count += 1;
			}
		}
	}

	private static void generate() {
		for(int x = 0; x < width; x++) {
			maze[x] = new byte[height];
			for(int y = 0; y < height; y++) {
				maze[x][y] = WALL;
			}
		}
		for(int x = 0; x < width; x++) {
			maze[x][0] = SPACE;
			maze[x][height - 1] = SPACE;
		}
		for(int y = 0; y < height; y++) {
			maze[0][y] = SPACE;
			maze[width - 1][y] = SPACE;
		}

		maze[2][2] = SPACE;
		Maze.carve(2, 2);

		maze[2][1] = SPACE;
		maze[width - 3][height - 2] = SPACE;
		
		goalX = width / 2;
		goalZ = height / 2;
		
		int randomizer = 0;
		while (maze[goalX][goalZ] == 0) {
			randomizer = Maze.rand.nextInt(100);
			if (randomizer >= 0 && randomizer <= 49) {
				goalX -= 1;
				goalZ += 1;
			} else {
				goalX += 1;
				goalZ -= 1;
			}
		}
	}

	public static void drawMaze() {
		Maze.drawSurrounding();
		Maze.shader.setMaterialAmbient(0.0f, 0.1f, 0.06f, 1.0f);
		Maze.shader.setMaterialDiffuse(0.0f, 0.50980392f, 0.50980392f, 1.0f);
		Maze.shader.setMaterialSpecular(0.50196078f, 0.50196078f, 0.50196078f, 1.0f);
		Maze.shader.setMaterialShiniess(0.25f);
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				if(maze[x][y] == WALL) {
					ModelMatrix.main.pushMatrix();
					ModelMatrix.main.addTranslation(x, 2.5f, y);
					ModelMatrix.main.addScale(1, 5, 1);
					Maze.shader.setModelMatrix(ModelMatrix.main.getMatrix());
					BoxGraphic.drawSolidCube();
					ModelMatrix.main.popMatrix();
				}
			}
		}
		
		angle += 45.0f * Gdx.graphics.getDeltaTime();
		float s = (float)Math.sin(angle * Math.PI / 180.0);
		
		// The goal/treasure
		ModelMatrix.main.pushMatrix();
		Maze.shader.setMaterialEmission(1.0f, 1.0f, 1.0f, 1.0f);
		ModelMatrix.main.addTranslation(goalX, 1.5f + s, goalZ);
		ModelMatrix.main.addScale(0.2f, 0.2f, 0.2f);
		Maze.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere();
		ModelMatrix.main.popMatrix();
		Maze.shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1.0f);
	}

	private static void drawSurrounding() {
		// Floor
		Maze.shader.setMaterialAmbient(0.02f, 0.02f, 0.02f, 1.0f);
		Maze.shader.setMaterialDiffuse(0.01f, 0.01f, 0.01f, 1.0f);
		Maze.shader.setMaterialSpecular(0.4f, 0.4f, 0.4f, 1.0f);
		Maze.shader.setMaterialShiniess(0.078125f);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(width / 2, 0.0f, width / 2);
		ModelMatrix.main.addScale(width, 0.2f, height);
		Maze.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();

		// Outer walls
		Maze.shader.setMaterialAmbient(0.0f, 0.1f, 0.06f, 1.0f);
		Maze.shader.setMaterialDiffuse(0.0f, 0.50980392f, 0.50980392f, 1.0f);
		Maze.shader.setMaterialSpecular(0.50196078f, 0.50196078f, 0.50196078f, 1.0f);
		Maze.shader.setMaterialShiniess(0.25f);
		// Left
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(width / 2, 2.5f, -0.5f);
		ModelMatrix.main.addScale(width, 5, 0.1f);
		Maze.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();

		// Top
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(width - 0.5f, 2.5f, height / 2);
		ModelMatrix.main.addScale(0.1f, 5, height);
		Maze.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();

		// Bottom
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(-0.5f, 2.5f, height / 2);
		ModelMatrix.main.addScale(0.1f, 5, height);
		Maze.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();

		// Right
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(width / 2, 2.5f, height - 0.5f);
		ModelMatrix.main.addScale(width, 5, 0.1f);
		Maze.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
	}
}
