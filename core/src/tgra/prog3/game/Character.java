package tgra.prog3.game;

public class Character {
	public ModelMatrix origin;
	public Shader3D shader;
	public int width, height;
	
	private Vector3D materialColour;
	private float materialShine;
	
	public Character(Shader3D shader, int width, int height, Point3D position, Vector3D matColour, float shine) {
		this.shader = shader;
		this.width = width;
		this.height = height;
		this.materialColour = matColour;
		this.materialShine = shine;
		this.origin = new ModelMatrix();
		this.origin.loadIdentityMatrix();
		this.origin.addTranslation(position.x, position.y, position.z);
		this.origin.addScale(0.4f, 1.0f, 0.4f);
	}
	
	public void display() {
		// Player setup
		this.shader.setMaterialDiffuse(this.materialColour.x, this.materialColour.y, this.materialColour.z, 1.0f);
		this.shader.setMaterialShiniess(this.materialShine);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTransformation(this.origin.matrix);
		this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere();
		ModelMatrix.main.popMatrix();
	}
	
	public void update(float deltaTime) {
	}
}
