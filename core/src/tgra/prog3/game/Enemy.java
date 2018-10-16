package tgra.prog3.game;

public class Enemy extends Character {
	
	public Enemy(Shader3D shader, int width, int height, Point3D position, Vector3D matAmbient, Vector3D matDiffuse, Vector3D matSpecular, float shine) {
		super(shader, width, height, position, matAmbient, matDiffuse, matSpecular, shine);
	}
	
	public void display() {
		super.display();
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		// Move enemy
	}
}
