package tgra.prog3.game;

public class Pyramid {
	private static Shader3D shader;
	
	public static void create(Shader3D shader) {
		Pyramid.shader = shader;
	}

	public static void drawPyramid(float x, float y, float z, float r, float g, float b, float a) {
		int maxLevel = 9;
		
		ModelMatrix.main.pushMatrix();
		Pyramid.shader.setMaterialDiffuse(r, g, b, a);
		ModelMatrix.main.addTranslation(x, y, z);
		
		ModelMatrix.main.pushMatrix();
		for (int level = 0; level < maxLevel; level++) {
			ModelMatrix.main.addTranslation(0.55f, 1.0f, -0.55f);
			ModelMatrix.main.pushMatrix();
			
			for (int i = 0; i < maxLevel - level; i++) {
				ModelMatrix.main.addTranslation(1.1f, 0.0f, 0.0f);
				ModelMatrix.main.pushMatrix();
				
				for (int j = 0; j < maxLevel - level; j++) {
					ModelMatrix.main.addTranslation(0.0f, 0.0f, -1.1f);
					ModelMatrix.main.pushMatrix();
					if (i % 2 == 0) {
						ModelMatrix.main.addScale(0.2f, 1.0f, 1.0f);
					}
					else {
						ModelMatrix.main.addScale(1.0f, 1.0f, 0.2f);
					}
					Pyramid.shader.setModelMatrix(ModelMatrix.main.getMatrix());
					BoxGraphic.drawSolidCube();
					ModelMatrix.main.popMatrix();
				}
				
				ModelMatrix.main.popMatrix();
			}
			ModelMatrix.main.popMatrix();
		}
		ModelMatrix.main.popMatrix();
		ModelMatrix.main.popMatrix();
	}
}
