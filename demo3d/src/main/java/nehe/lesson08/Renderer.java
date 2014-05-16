package nehe.lesson08;

//import net.java.games.jogl.GL;
//import net.java.games.jogl.GLDrawable;
//import net.java.games.jogl.GLEventListener;
//import net.java.games.jogl.GLU;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.FloatBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.ImageUtil;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

class Renderer implements GLEventListener {
	private boolean lightingEnabled; // Lighting ON/OFF
	private boolean lightingChanged = false; // Lighting changed
	private boolean blendingEnabled; // Blending OFF/ON
	private boolean blendingChanged = false; // Blending changed

	private int filter; // Which texture to use
	private int[] textures = new int[3]; // Storage For 3 Textures

	private float xrot; // X Rotation
	private float yrot; // Y Rotation
	private float xspeed = 0.5f; // X Rotation Speed
	private float yspeed = 0.3f; // Y Rotation Speed
	private float z = -5.0f; // Depth Into The Screen

	private float[] lightAmbient = { 0.5f, 0.5f, 0.5f, 1.0f };
	private float[] lightDiffuse = { 1.0f, 1.0f, 1.0f, 1.0f };
	private float[] lightPosition = { 0.0f, 0.0f, 2.0f, 1.0f };

	private GLU glu;
	private Texture myTexture;

	public boolean isBlendingEnabled() {
		return blendingEnabled;
	}

	public void setBlendingEnabled(boolean blendingEnabled) {
		this.blendingEnabled = blendingEnabled;
		blendingChanged = true;
	}

	public boolean isLightingEnabled() {
		return lightingEnabled;
	}

	public void setLightingEnabled(boolean lightingEnabled) {
		this.lightingEnabled = lightingEnabled;
		lightingChanged = true;
	}

	public float getXspeed() {
		return xspeed;
	}

	public void setXspeed(float xspeed) {
		this.xspeed = xspeed;
	}

	public float getYspeed() {
		return yspeed;
	}

	public void setYspeed(float yspeed) {
		this.yspeed = yspeed;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public void switchFilter() {
		filter = (filter + 1) % textures.length;
	}

	private boolean loadGLTextures(GLAutoDrawable gldrawable) {
		TextureReader.Texture texture = null;
		try {
			texture = TextureReader.readTexture("nehe/lesson08/data/glass.png");
		} catch (IOException e) {
			return false;
		}

		GL gl = gldrawable.getGL();
		// GLU glu = gldrawable.getGLU();

		// Create Nearest Filtered Texture
		// gl.glGenTextures(3, textures);
		// gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);

		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
				GL.GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
				GL.GL_NEAREST);

		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, texture.getWidth(),
				texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE,
				texture.getPixels());

		// Create Linear Filtered Texture
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
				GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
				GL.GL_LINEAR);

		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, texture.getWidth(),
				texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE,
				texture.getPixels());

		// Create MipMapped Texture (Only with GL4Java 2.1.2.1 and later!)
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
				GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
				GL.GL_LINEAR_MIPMAP_NEAREST);

		glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, 3, texture.getWidth(),
				texture.getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE,
				texture.getPixels());
		return true;
	}

	/**
	 * Retrieve a URL resource from the jar. If the resource is not found, then
	 * the local disk is also checked.
	 * 
	 * @param filename
	 *            Complete filename, including parent path
	 * @return a URL object if resource is found, otherwise null.
	 */
	public final static URL getResource(final String filename) {
		// Try to load resource from jar
		URL url = ClassLoader.getSystemResource(filename);
		// If not found in jar, then load from disk
		if (url == null) {
			try {
				url = new URL("file", "localhost", filename);
			} catch (Exception urlException) {
			} // ignore
		}
		return url;
	}

	public void init(GLAutoDrawable glDrawable) {
		String resourceName = "nehe/lesson08/data/glass.png";
		URL url = getResource(resourceName);
		if (url == null) {
			throw new RuntimeException("Error reading resource " + resourceName);
		}
		try {
			BufferedImage tBufferedImage = ImageIO
					.read(new File(url.getFile()));
			ImageUtil.flipImageVertically(tBufferedImage);
			myTexture = TextureIO.newTexture(tBufferedImage, true);
		} catch (GLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// if (!loadGLTextures(glDrawable)) {
		// System.out.println("Unable to load textures,Bailing!");
		// System.exit(0);
		// }

		GL gl = glDrawable.getGL();
		glu = new GLU();
		gl.glEnable(GL.GL_TEXTURE_2D); // Enable Texture Mapping
		gl.glShadeModel(GL.GL_SMOOTH); // Enables Smooth Color Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // This Will Clear The
													// Background Color To Black
		gl.glClearDepth(1.0); // Enables Clearing Of The Depth Buffer
		gl.glEnable(GL.GL_DEPTH_TEST); // Enables Depth Testing
		gl.glDepthFunc(GL.GL_LEQUAL); // The Type Of Depth Test To Do
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // Really
																	// Nice
																	// Perspective
																	// Calculations

		// ByteBuffer temp = ByteBuffer.allocateDirect(16);
		// temp.order(ByteOrder.nativeOrder());
		// gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT,
		// (FloatBuffer)temp.asFloatBuffer().put(lightAmbient).flip()); // Setup
		// The Ambient Light
		FloatBuffer unpackedPixels = FloatBuffer.allocate(lightAmbient.length);
		unpackedPixels.put(lightAmbient);
		unpackedPixels.rewind();
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, unpackedPixels); // Setup The
																	// Ambient
																	// Light

		unpackedPixels = FloatBuffer.allocate(lightDiffuse.length);
		unpackedPixels.put(lightDiffuse);
		unpackedPixels.rewind();
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, unpackedPixels); // Setup The
																	// Diffuse
																	// Light

		unpackedPixels = FloatBuffer.allocate(lightPosition.length);
		unpackedPixels.put(lightPosition);
		unpackedPixels.rewind();
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, unpackedPixels); // Position
																	// The Light
		gl.glEnable(GL.GL_LIGHT1); // Enable Light One

		gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f); // Full Brightness. 50% Alpha (new
												// )
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE); // Set The Blending Function
													// For Translucency (new )
	}

	public void display(GLAutoDrawable glDrawable) {
		GL gl = glDrawable.getGL();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT); // Clear
																		// The
																		// Screen
																		// And
																		// The
																		// Depth
																		// Buffer
		gl.glLoadIdentity(); // Reset The View
		gl.glTranslatef(0.0f, 0.0f, z);

		gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);

		myTexture.enable();
		myTexture.bind();
		// gl.glBindTexture(GL.GL_TEXTURE_2D, textures[filter]);

		gl.glBegin(GL.GL_QUADS);
		// Front Face
		gl.glNormal3f(0.0f, 0.0f, 1.0f);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(-1.0f, -1.0f, 1.0f);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(1.0f, -1.0f, 1.0f);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(1.0f, 1.0f, 1.0f);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(-1.0f, 1.0f, 1.0f);
		// Back Face
		gl.glNormal3f(0.0f, 0.0f, -1.0f);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(-1.0f, -1.0f, -1.0f);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(-1.0f, 1.0f, -1.0f);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(1.0f, 1.0f, -1.0f);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(1.0f, -1.0f, -1.0f);
		// Top Face
		gl.glNormal3f(0.0f, 1.0f, 0.0f);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(-1.0f, 1.0f, -1.0f);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(-1.0f, 1.0f, 1.0f);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(1.0f, 1.0f, 1.0f);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(1.0f, 1.0f, -1.0f);
		// Bottom Face
		gl.glNormal3f(0.0f, -1.0f, 0.0f);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(-1.0f, -1.0f, -1.0f);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(1.0f, -1.0f, -1.0f);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(1.0f, -1.0f, 1.0f);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(-1.0f, -1.0f, 1.0f);
		// Right face
		gl.glNormal3f(1.0f, 0.0f, 0.0f);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(1.0f, -1.0f, -1.0f);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(1.0f, 1.0f, -1.0f);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(1.0f, 1.0f, 1.0f);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(1.0f, -1.0f, 1.0f);
		// Left Face
		gl.glNormal3f(-1.0f, 0.0f, 0.0f);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(-1.0f, -1.0f, -1.0f);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(-1.0f, -1.0f, 1.0f);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(-1.0f, 1.0f, 1.0f);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(-1.0f, 1.0f, -1.0f);
		gl.glEnd();

		xrot += xspeed;
		yrot += yspeed;

		// process keys that are down ( kinda NON javaish, but i like it )

		// toggle lighting
		if (lightingChanged) {
			if (lightingEnabled)
				gl.glEnable(GL.GL_LIGHTING);
			else
				gl.glDisable(GL.GL_LIGHTING);
			lightingChanged = false;
		}

		// Blending Code Starts Here
		if (blendingChanged) {
			if (blendingEnabled) {
				gl.glEnable(GL.GL_BLEND); // Turn Blending On
				gl.glDisable(GL.GL_DEPTH_TEST); // Turn Depth Testing Off
			} else {
				gl.glDisable(GL.GL_BLEND); // Turn Blending Off
				gl.glEnable(GL.GL_DEPTH_TEST); // Turn Depth Testing On
			}
			blendingChanged = false;
		}

		myTexture.disable();
	}

	public void reshape(GLAutoDrawable glDrawable, int x, int y, int w, int h) {
		if (h == 0)
			h = 1;
		GL gl = glDrawable.getGL();
		// GLU glu = glDrawable.getGLU();
		gl.glViewport(0, 0, w, h); // Reset The Current Viewport And Perspective
									// Transformation
		gl.glMatrixMode(GL.GL_PROJECTION); // Select The Projection Matrix
		gl.glLoadIdentity(); // Reset The Projection Matrix
		glu.gluPerspective(45.0f, w / h, 0.1f, 100.0f); // Calculate The Aspect
														// Ratio Of The Window
		gl.glMatrixMode(GL.GL_MODELVIEW); // Select The Modelview Matrix
		gl.glLoadIdentity(); // Reset The ModalView Matrix
	}

	public void displayChanged(GLAutoDrawable glDrawable, boolean b, boolean b1) {
	}
}
