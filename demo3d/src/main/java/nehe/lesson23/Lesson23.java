package nehe.lesson23;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.imageio.ImageIO;
import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.vecmath.Color3f;

import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.ImageUtil;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

//import net.java.games.jogl.*;
//import net.java.games.jogl.util.*;

/**
 * Port of the NeHe OpenGL Tutorial (Lesson 6) to Java using the Jogl interface
 * to OpenGL. Jogl can be obtained at http://jogl.dev.java.net/
 * 
 * @author Kevin Duling (jattier@hotmail.com)
 */
public class Lesson23 {
	static Animator animator = null;

	static class Renderer implements GLEventListener, KeyListener {
		private float xrot; // X Rotation ( NEW )
		private float yrot; // Y Rotation ( NEW )
		private float zrot; // Z Rotation ( NEW )
		private int texture;

		private GLU glu; // for the GL Utility

		private Texture[] myTextures = new Texture[6];

		private boolean scene, masking;

		private float roll;

		private boolean f1 = false; // F1 key pressed

		boolean light; // Lighting ON/OFF
		boolean lp; // L Pressed?
		boolean fp; // F Pressed?
		boolean sp; // Spacebar Pressed?

		int filter; // Which Filter To Use
		int object = 1; // Which Object To Draw

		// float xrot; // X Rotation
		// float yrot; // Y Rotation
		float xspeed; // X Rotation Speed
		float yspeed; // Y Rotation Speed
		float z = -10.0f; // Depth Into The Screen

		private GL gl;

		// GLUquadricObj *quadratic; // Storage For Our Quadratic Objects
		Cylinder cylinder;
		Sphere sphere;

		float lightAmbient[] = { 0.5f, 0.5f, 0.5f, 1.0f };
		float lightDiffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float lightPosition[] = { 0.0f, 0.0f, 2.0f, 1.0f };

		GLUquadric quadratic; // Storage For Our Quadratic Objects

		private void glDrawCube() {
			gl.glBegin(GL.GL_QUADS);
			// Front Face
			gl.glNormal3f(0.0f, 0.0f, 0.5f);
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(-1.0f, -1.0f, 1.0f);
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex3f(1.0f, -1.0f, 1.0f);
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex3f(1.0f, 1.0f, 1.0f);
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex3f(-1.0f, 1.0f, 1.0f);
			// Back Face
			gl.glNormal3f(0.0f, 0.0f, -0.5f);
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex3f(-1.0f, -1.0f, -1.0f);
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex3f(-1.0f, 1.0f, -1.0f);
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex3f(1.0f, 1.0f, -1.0f);
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(1.0f, -1.0f, -1.0f);
			// Top Face
			gl.glNormal3f(0.0f, 0.5f, 0.0f);
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex3f(-1.0f, 1.0f, -1.0f);
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(-1.0f, 1.0f, 1.0f);
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex3f(1.0f, 1.0f, 1.0f);
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex3f(1.0f, 1.0f, -1.0f);
			// Bottom Face
			gl.glNormal3f(0.0f, -0.5f, 0.0f);
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex3f(-1.0f, -1.0f, -1.0f);
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex3f(1.0f, -1.0f, -1.0f);
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(1.0f, -1.0f, 1.0f);
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex3f(-1.0f, -1.0f, 1.0f);
			// Right Face
			gl.glNormal3f(0.5f, 0.0f, 0.0f);
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex3f(1.0f, -1.0f, -1.0f);
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex3f(1.0f, 1.0f, -1.0f);
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex3f(1.0f, 1.0f, 1.0f);
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(1.0f, -1.0f, 1.0f);
			// Left Face
			gl.glNormal3f(-0.5f, 0.0f, 0.0f);
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(-1.0f, -1.0f, -1.0f);
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex3f(-1.0f, -1.0f, 1.0f);
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex3f(-1.0f, 1.0f, 1.0f);
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex3f(-1.0f, 1.0f, -1.0f);
			gl.glEnd();
		}

		/**
		 * Called by the drawable to initiate OpenGL rendering by the client.
		 * After all GLEventListeners have been notified of a display event, the
		 * drawable will swap its buffers if necessary.
		 * 
		 * @param gLDrawable
		 *            The GLDrawable object.
		 */
		public void display(GLAutoDrawable gLDrawable) {
			// final GL gl = gLDrawable.getGL();

			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT); // Clear
																			// The
																			// Screen
																			// And
																			// The
																			// Depth
																			// Buffer
			gl.glLoadIdentity(); // Reset The View

			gl.glTranslatef(0.0f, 0.0f, z);

			gl.glEnable(GL.GL_TEXTURE_GEN_S); // Enable Texture Coord Generation
												// For S (NEW)
			gl.glEnable(GL.GL_TEXTURE_GEN_T); // Enable Texture Coord Generation
												// For T (NEW)

			myTextures[filter + (filter + 1)].enable();
			myTextures[filter + (filter + 1)].bind();
			// gl.glBindTexture(GL.GL_TEXTURE_2D, texture[filter+(filter+1)]);
			// // This Will Select The Sphere Map
			gl.glPushMatrix();
			gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
			gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
			switch (object) {
			case 0:
				glDrawCube();
				break;
			case 1:
				gl.glTranslatef(0.0f, 0.0f, -1.5f); // Center The Cylinder
				// cylinder.draw(1.0f,1.0f,3.0f,32,32); // A Cylinder With A
				glu.gluCylinder(quadratic, 1.0f, 1.0f, 3.0f, 32, 32); // A
																		// Cylinder
																		// With
																		// A
																		// Radius
																		// Of
																		// 0.5
																		// And A
																		// Height
																		// Of 2
				// Radius Of 0.5 And A Height Of 2
				break;
			case 2:
				glu.gluSphere(quadratic, 1.3f, 32, 32);
				// sphere.draw(1.3f,32,32); // Draw A Sphere With A Radius Of 1
				// And 16 Longitude And 16 Latitude Segments
				break;
			case 3:
				gl.glTranslatef(0.0f, 0.0f, -1.5f); // Center The Cone
				// cylinder.draw(1.0f,0.0f,3.0f,32,32); // A Cone With A Bottom
				glu.gluCylinder(quadratic, 1.0f, 0.0f, 3.0f, 32, 32); // A
																		// Cylinder
																		// With
																		// A
																		// Radius
																		// Of
																		// 0.5
																		// And A
																		// Height
																		// Of 2
				// Radius Of .5 And A Height Of 2
				break;
			}

			gl.glPopMatrix();
			gl.glDisable(GL.GL_TEXTURE_GEN_S);
			gl.glDisable(GL.GL_TEXTURE_GEN_T);

			myTextures[filter * 2].enable();
			myTextures[filter * 2].bind();
			// gl.glBindTexture(GL.GL_TEXTURE_2D, texture[filter*2]); // This
			// Will Select The BG Maps...
			gl.glPushMatrix();
			gl.glTranslatef(0.0f, 0.0f, -24.0f);
			gl.glBegin(GL.GL_QUADS);
			gl.glNormal3f(0.0f, 0.0f, 1.0f);
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(-13.3f, -10.0f, 10.0f);
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex3f(13.3f, -10.0f, 10.0f);
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex3f(13.3f, 10.0f, 10.0f);
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex3f(-13.3f, 10.0f, 10.0f);
			gl.glEnd();
			gl.glPopMatrix();

			xrot += xspeed;
			yrot += yspeed;

			myTextures[filter + (filter + 1)].disable();
			myTextures[filter * 2].disable();
		}

		/**
		 * Called when the display mode has been changed. <B>!! CURRENTLY
		 * UNIMPLEMENTED IN JOGL !!</B>
		 * 
		 * @param gLDrawable
		 *            The GLDrawable object.
		 * @param modeChanged
		 *            Indicates if the video mode has changed.
		 * @param deviceChanged
		 *            Indicates if the video device has changed.
		 */
		public void displayChanged(GLAutoDrawable gLDrawable,
				boolean modeChanged, boolean deviceChanged) {
		}

		/**
		 * Called by the drawable immediately after the OpenGL context is
		 * initialized for the first time. Can be used to perform one-time
		 * OpenGL initialization such as setup of lights and display lists.
		 * 
		 * @param gLDrawable
		 *            The GLDrawable object.
		 */
		public void init(GLAutoDrawable gLDrawable) {
			// final GL gl = gLDrawable.getGL();
			gl = gLDrawable.getGL();
			glu = new GLU(); // get GL Utilities

			quadratic = glu.gluNewQuadric(); // Create A Pointer To The Quadric
												// Object (Return 0 If No
												// Memory)
			glu.gluQuadricNormals(quadratic, GLU.GLU_SMOOTH); // Create Smooth
																// Normals
			glu.gluQuadricTexture(quadratic, true); // Create Texture Coords

			String resourceName0 = "nehe/lesson23/data/BG.bmp";
			String resourceName1 = "nehe/lesson23/data/Reflect.bmp";
			String resourceName2 = "nehe/lesson23/data/BG.bmp";
			String resourceName3 = "nehe/lesson23/data/Reflect.bmp";
			String resourceName4 = "nehe/lesson23/data/BG.bmp";
			String resourceName5 = "nehe/lesson23/data/Reflect.bmp";
			URL url0 = getResource(resourceName0);
			URL url1 = getResource(resourceName1);
			URL url2 = getResource(resourceName2);
			URL url3 = getResource(resourceName3);
			URL url4 = getResource(resourceName4);
			URL url5 = getResource(resourceName5);
			if (url0 == null || url1 == null || url2 == null || url3 == null
					|| url4 == null || url5 == null) {
				throw new RuntimeException("Error reading resources");
			}
			try {
				BufferedImage tBufferedImage = ImageIO.read(new File(url0
						.getFile()));
				ImageUtil.flipImageVertically(tBufferedImage);
				myTextures[0] = TextureIO.newTexture(tBufferedImage, true);

				tBufferedImage = ImageIO.read(new File(url1.getFile()));
				ImageUtil.flipImageVertically(tBufferedImage);
				myTextures[1] = TextureIO.newTexture(tBufferedImage, false);

				tBufferedImage = ImageIO.read(new File(url2.getFile()));
				ImageUtil.flipImageVertically(tBufferedImage);
				myTextures[2] = TextureIO.newTexture(tBufferedImage, false);

				tBufferedImage = ImageIO.read(new File(url3.getFile()));
				ImageUtil.flipImageVertically(tBufferedImage);
				myTextures[3] = TextureIO.newTexture(tBufferedImage, false);

				tBufferedImage = ImageIO.read(new File(url4.getFile()));
				ImageUtil.flipImageVertically(tBufferedImage);
				myTextures[4] = TextureIO.newTexture(tBufferedImage, false);

				tBufferedImage = ImageIO.read(new File(url5.getFile()));
				ImageUtil.flipImageVertically(tBufferedImage);
				myTextures[4] = TextureIO.newTexture(tBufferedImage, false);
			} catch (GLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			gl.glShadeModel(GL.GL_SMOOTH); // Enable Smooth Shading
			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
			gl.glClearDepth(1.0f); // Depth Buffer Setup
			gl.glEnable(GL.GL_DEPTH_TEST); // Enables Depth Testing
			// gl.glDepthFunc(GL.GL_LEQUAL); // The Type Of Depth Testing To Do
			// gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); //
			// Really
			// // Nice
			// // Perspective
			// // Calculations
			gl.glEnable(GL.GL_TEXTURE_2D);
			// gLDrawable.addKeyListener(this);
			// texture = genTexture(gl);
			// gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
			// BufferedImage img = readPNGImage(resourceName);
			// makeRGBTexture(gl, glu, img, GL.GL_TEXTURE_2D, false);
			// gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
			// GL.GL_LINEAR);
			// gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
			// GL.GL_LINEAR);

			int width = 640;
			int height = 480;
			gl.glEnable(GL.GL_TEXTURE_2D); // Enable Texture Mapping
			gl.glShadeModel(GL.GL_SMOOTH); // Enable Smooth Shading
			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
			gl.glClearDepth(1.0f); // Depth Buffer Setup
			gl.glEnable(GL.GL_DEPTH_TEST); // Enables Depth Testing
			gl.glDepthFunc(GL.GL_LEQUAL); // The Type Of Depth Testing To Do
			gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // Really
																		// Nice
																		// Perspective
																		// Calculations

			ByteBuffer temp = ByteBuffer.allocateDirect(16);
			temp.order(ByteOrder.nativeOrder());
			gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, (FloatBuffer) temp
					.asFloatBuffer().put(lightAmbient).flip()); // Setup The
																// Ambient Light
			gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, (FloatBuffer) temp
					.asFloatBuffer().put(lightDiffuse).flip()); // Setup The
																// Diffuse Light
			gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, (FloatBuffer) temp
					.asFloatBuffer().put(lightPosition).flip()); // Position The
																	// Light
			gl.glEnable(GL.GL_LIGHT1); // Enable Light One

			cylinder = new Cylinder();
			// cylinder.setNormals(GLU.GLU_SMOOTH); // Create Smooth Normals
			// cylinder.setTextureFlag(true); // Create Texture Coords
			sphere = new Sphere();
			// sphere.setNormals(GLU.GLU_SMOOTH); // Create Smooth Normals
			// sphere.setTextureFlag(true); // Create Texture Coords

			// ///////////////////////////////////
			// ///////////////////////////////////
			// ///////////////////////////////////
			// ///////////////////////////////////
			// ///////////////////////////////////
			// ///////////////////////////////////
			// ///////////////////////////////////
			// ///////////////////////////////////
			// ///////////////////////////////////
			// ///////////////////////////////////
			// ///////////////////////////////////
			Color3f lightGray = new Color3f(0.7f, 0.7f, 0.7f);
			Color3f black = new Color3f(0, 0, 0);
			Color3f white = new Color3f(1, 1, 1);
			Color3f gray = new Color3f(.2f, .2f, .2f);
			Color3f red = new Color3f(1, 0, 0);
			Color3f yellow = new Color3f(0, 1, 1);
			Material lightGrayMaterial = new Material(lightGray, black,
					lightGray, white, 100.0f);
			Material blackMaterial = new Material(lightGray, black, black,
					lightGray, 10.0f);
			Material whiteMaterial = new Material(white, white, white, white,
					100.0f);
			Material grayMaterial = new Material(gray, black, gray, gray,
					100.0f);
			Material redMaterial = new Material(red, black, red, red, 100.0f);
			Material yellowMaterial = new Material(yellow, black, yellow,
					yellow, 100.0f);
			final Appearance lightGrayLook = new Appearance();
			lightGrayLook.setMaterial(lightGrayMaterial);
			Appearance blackLook = new Appearance();
			blackLook.setMaterial(blackMaterial);
			Appearance whiteLook = new Appearance();
			whiteLook.setMaterial(whiteMaterial);
			Appearance grayLook = new Appearance();
			grayLook.setMaterial(grayMaterial);
			final Appearance redLook = new Appearance();
			redLook.setMaterial(redMaterial);
			final Appearance yellowLook = new Appearance();
			yellowLook.setMaterial(yellowMaterial);

			// grayLook.setCapability(Appearance.ALLOW_MATERIAL_READ);
			// grayLook.setCapability(Appearance.ALLOW_MATERIAL_WRITE);

			// final Cylinder
			cylinder = new Cylinder(1, 1, Cylinder.GENERATE_NORMALS
					| Cylinder.ENABLE_GEOMETRY_PICKING, 26, 26, lightGrayLook);
			// final Sphere
			sphere = new Sphere(10, Sphere.GENERATE_NORMALS
					| Sphere.ENABLE_GEOMETRY_PICKING, redLook);
			// final Box
			// box = new Box(10, 10, 10, Box.GENERATE_NORMALS
			// | Box.ENABLE_GEOMETRY_PICKING, redLook);

			gl.glTexGeni(GL.GL_S, GL.GL_TEXTURE_GEN_MODE, GL.GL_SPHERE_MAP); // Set
																				// The
																				// Texture
																				// Generation
																				// Mode
																				// For
																				// S
																				// To
																				// Sphere
																				// Mapping
																				// (NEW)
			gl.glTexGeni(GL.GL_T, GL.GL_TEXTURE_GEN_MODE, GL.GL_SPHERE_MAP); // Set
																				// The
																				// Texture
																				// Generation
																				// Mode
																				// For
																				// T
																				// To
																				// Sphere
																				// Mapping
																				// (NEW)

			gl.glViewport(0, 0, width, height); // Reset The Current Viewport

			gl.glMatrixMode(GL.GL_PROJECTION); // Select The Projection Matrix
			gl.glLoadIdentity(); // Reset The Projection Matrix

			// Calculate The Aspect Ratio Of The Window
			glu.gluPerspective(45.0f, (float) width / (float) height, 0.1f,
					100.0f);

			gl.glMatrixMode(GL.GL_MODELVIEW); // Select The Modelview Matrix
			gl.glLoadIdentity(); // Reset The Modelview Matrix

		}

		/**
		 * Called by the drawable during the first repaint after the component
		 * has been resized. The client can update the viewport and view volume
		 * of the window appropriately, for example by a call to
		 * GL.glViewport(int, int, int, int); note that for convenience the
		 * component has already called GL.glViewport(int, int, int, int)(x, y,
		 * width, height) when this method is called, so the client may not have
		 * to do anything in this method.
		 * 
		 * @param gLDrawable
		 *            The GLDrawable object.
		 * @param x
		 *            The X Coordinate of the viewport rectangle.
		 * @param y
		 *            The Y coordinate of the viewport rectanble.
		 * @param width
		 *            The new width of the window.
		 * @param height
		 *            The new height of the window.
		 */
		public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width,
				int height) {
			final GL gl = gLDrawable.getGL();

			if (height <= 0) // avoid a divide by zero error!
				height = 1;
			final float h = (float) width / (float) height;
			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glLoadIdentity();
			// glu.gluPerspective(45.0f, h, 1.0, 20.0);
			glu.gluPerspective(45.0f, h, 0.1f, 100.0f);
			gl.glMatrixMode(GL.GL_MODELVIEW);
			gl.glLoadIdentity();
		}

		private void switchMode() {
			// fullscreen = !fullscreen;
			// try {
			// Display.setFullscreen(fullscreen);
			// }
			// catch(Exception e) {
			// e.printStackTrace();
			// }
		}

		/**
		 * Invoked when a key has been pressed. See the class description for
		 * {@link KeyEvent} for a definition of a key pressed event.
		 * 
		 * @param e
		 *            The KeyEvent.
		 */
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch (keyCode) {
			case KeyEvent.VK_ESCAPE:
				animator.stop();
				System.exit(0);
				break;
			case KeyEvent.VK_F1:
				switchMode(); // Toggle Fullscreen / Windowed Mode
				break;
			case KeyEvent.VK_L:
				light = !light;
				if (!light) {
					gl.glDisable(GL.GL_LIGHTING);
				} else {
					gl.glEnable(GL.GL_LIGHTING);
				}
				break;
			case KeyEvent.VK_F:
				filter++;
				if (filter > 2) {
					filter = 0;
				}
				break;
			case KeyEvent.VK_SPACE:
				object++;
				if (object > 3) {
					object = 0;
				}
				break;
			case KeyEvent.VK_PAGE_UP:// KEY_PRIOR
				z -= 0.02f;
				break;
			case KeyEvent.VK_PAGE_DOWN:// KEY_NEXT
				z += 0.02f;
				break;
			case KeyEvent.VK_UP:
				xspeed -= 0.01f;
				yspeed -= 0.01f;
				break;
			case KeyEvent.VK_DOWN:
				xspeed += 0.01f;
				yspeed += 0.01f;
				break;
			default:
			}
		}

		/**
		 * Invoked when a key has been released. See the class description for
		 * {@link KeyEvent} for a definition of a key released event.
		 * 
		 * @param e
		 *            The KeyEvent.
		 */
		public void keyReleased(KeyEvent e) {
		}

		/**
		 * Invoked when a key has been typed. See the class description for
		 * {@link KeyEvent} for a definition of a key typed event.
		 * 
		 * @param e
		 *            The KeyEvent.
		 */
		public void keyTyped(KeyEvent e) {
		}

		private BufferedImage readPNGImage(String resourceName) {
			try {
				URL url = getResource(resourceName);
				if (url == null) {
					throw new RuntimeException("Error reading resource "
							+ resourceName);
				}
				BufferedImage img = ImageIO.read(url);
				java.awt.geom.AffineTransform tx = java.awt.geom.AffineTransform
						.getScaleInstance(1, -1);
				tx.translate(0, -img.getHeight(null));
				AffineTransformOp op = new AffineTransformOp(tx,
						AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				img = op.filter(img, null);
				return img;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void makeRGBTexture(GL gl, GLU glu, BufferedImage img,
				int target, boolean mipmapped) {
			ByteBuffer dest = null;
			switch (img.getType()) {
			case BufferedImage.TYPE_3BYTE_BGR:
			case BufferedImage.TYPE_CUSTOM: {
				System.out.println("or here");
				byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer())
						.getData();
				System.out.println("data.length = " + data.length);
				dest = ByteBuffer.allocateDirect(data.length);
				dest.order(ByteOrder.nativeOrder());
				dest.put(data, 0, data.length);
				break;
			}
			case BufferedImage.TYPE_INT_RGB: {
				int[] data = ((DataBufferInt) img.getRaster().getDataBuffer())
						.getData();
				System.out.println("here");
				dest = ByteBuffer.allocateDirect(data.length * 4);// BufferUtils.SIZEOF_INT);
				dest.order(ByteOrder.nativeOrder());
				dest.asIntBuffer().put(data, 0, data.length);
				break;
			}
			default:
				throw new RuntimeException("Unsupported image type "
						+ img.getType());
			}

			if (mipmapped) {
				glu.gluBuild2DMipmaps(target, GL.GL_RGB8, img.getWidth(),
						img.getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, dest);
			} else {
				System.out.println("dest.capacity() = " + dest.capacity());
				gl.glTexImage2D(target, 0, GL.GL_RGB, img.getWidth() / 2,
						img.getHeight() / 2, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE,
						dest);
			}
		}

		private int genTexture(GL gl) {
			final int[] tmp = new int[1];
			// gl.glGenTextures(1, tmp);
			gl.glGenTextures(1, tmp, 0);
			return tmp[0];
		}
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

	/**
	 * Program's main entry point
	 * 
	 * @param args
	 *            command line arguments.
	 */
	public static void main(String[] args) {
		Frame frame = new Frame("Lesson 6: Texture Mapping");
		// Create the OpenGL rendering canvas
		GLCanvas canvas = new GLCanvas(); // heavy-weight GLCanvas
		Renderer renderer = new Renderer();
		canvas.addGLEventListener(renderer);
		canvas.addKeyListener(renderer);
		frame.add(canvas);
		frame.setSize(640, 480);
		animator = new Animator(canvas);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				animator.stop();
				System.exit(0);
			}
		});
		frame.show();
		animator.start();
		canvas.requestFocus();
	}
}
