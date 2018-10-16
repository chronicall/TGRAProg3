package tgra.prog3.game;

public class Character {
	public ModelMatrix origin;
	public Shader3D shader;
	public int width, height;
	
	private Vector3D materialAmbient;
	private Vector3D materialDiffuse;
	private Vector3D materialSpecular;
	private float materialShine;
	
	public Character(Shader3D shader, int width, int height, Point3D position, Vector3D matAmbient, Vector3D matDiffuse, Vector3D matSpecular, float shine) {
		this.shader = shader;
		this.width = width;
		this.height = height;
		this.materialAmbient = matAmbient;
		this.materialDiffuse = matDiffuse;
		this.materialSpecular = matSpecular;
		this.materialShine = shine;
		this.origin = new ModelMatrix();
		this.origin.loadIdentityMatrix();
		this.origin.addTranslation(position.x, position.y, position.z);
		this.origin.addScale(0.4f, 1.0f, 0.4f);
	}
	
	public void display() {
		// Player setup
		this.shader.setMaterialAmbient(this.materialAmbient.x, this.materialAmbient.y, this.materialAmbient.z, 1.0f);
		this.shader.setMaterialDiffuse(this.materialDiffuse.x, this.materialDiffuse.y, this.materialDiffuse.z, 1.0f);
		this.shader.setMaterialSpecular(this.materialSpecular.x, this.materialSpecular.y, this.materialSpecular.z, 1.0f);
		this.shader.setMaterialShiniess(this.materialShine);
		this.shader.setMaterialEmission(0.2f, 0.2f, 0.2f, 0.2f);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTransformation(this.origin.matrix);
		this.shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere();
		ModelMatrix.main.popMatrix();
	}
	
	public void update(float deltaTime) {
	}
}
