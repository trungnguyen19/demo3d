package nehe.lesson20;

/*
 * Lesson06.java
 *
 * Created on July 16, 2003, 11:30 AM
 */

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

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;

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
public class Lesson20 {
	static Animator animator = null;

	static class Renderer implements GLEventListener, KeyListener {
		private float xrot; // X Rotation ( NEW )
		private float yrot; // Y Rotation ( NEW )
		private float zrot; // Z Rotation ( NEW )
		private int texture;

		private GLU glu; // for the GL Utility

		private Texture[] myTextures = new Texture[5];

		private boolean scene, masking;

		private float roll;

		/**
		 * Called by the drawable to initiate OpenGL rendering by the client.
		 * After all GLEventListeners have been notified of a display event, the
		 * drawable will swap its buffers if necessary.
		 * 
		 * @param gLDrawable
		 *            The GLDrawable object.
		 */
		public void display(GLAutoDrawable gLDrawable) {
			final GL gl = gLDrawable.getGL();

			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT); // Clear
																			// The
																			// Screen
																			// And
																			// The
																			// Depth
																			// Buffer
			gl.glLoadIdentity(); // Reset The Modelview Matrix
			gl.glTranslatef(0.0f, 0.0f, -2.0f); // Move Into The Screen 5 Units

			myTextures[0].enable();
			myTextures[0].bind();

			gl.glBegin(GL.GL_QUADS); // Start Drawing A Textured Quad
			gl.glTexCoord2f(0.0f, -roll + 0.0f);
			gl.glVertex3f(-1.1f, -1.1f, 0.0f); // Bottom Left
			gl.glTexCoord2f(3.0f, -roll + 0.0f);
			gl.glVertex3f(1.1f, -1.1f, 0.0f); // Bottom Right
			gl.glTexCoord2f(3.0f, -roll + 3.0f);
			gl.glVertex3f(1.1f, 1.1f, 0.0f); // Top Right
			gl.glTexCoord2f(0.0f, -roll + 3.0f);
			gl.glVertex3f(-1.1f, 1.1f, 0.0f); // Top Left
			gl.glEnd(); // Done Drawing The Quad

			gl.glEnable(GL.GL_BLEND); // Enable Blending
			gl.glDisable(GL.GL_DEPTH_TEST); // Disable Depth Testing

			myTextures[0].disable();

			if (masking) // Is Masking Enabled?
			{
				//////////////////////////
				//////////////////////////
				//////////////////////////
				////////////??////////////
				//////////////////////////
				//////////////////////////
				//////////////////////////
				gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
				//gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_ZERO); // Blend Screen
																// Color With
																// Zero (Black)
			}

			if (scene) // Are We Drawing The Second Scene?
			{
				gl.glTranslatef(0.0f, 0.0f, -1.0f); // Translate Into The Screen
													// One Unit
				gl.glRotatef(roll * 360, 0.0f, 0.0f, 1.0f); // Rotate On The Z
															// Axis 360 Degrees.
				if (masking) // Is Masking On?
				{

					myTextures[3].enable();
					myTextures[3].bind();

					gl.glBegin(GL.GL_QUADS); // Start Drawing A Textured Quad
					gl.glTexCoord2f(0.0f, 0.0f);
					gl.glVertex3f(-1.1f, -1.1f, 0.0f); // Bottom Left
					gl.glTexCoord2f(1.0f, 0.0f);
					gl.glVertex3f(1.1f, -1.1f, 0.0f); // Bottom Right
					gl.glTexCoord2f(1.0f, 1.0f);
					gl.glVertex3f(1.1f, 1.1f, 0.0f); // Top Right
					gl.glTexCoord2f(0.0f, 1.0f);
					gl.glVertex3f(-1.1f, 1.1f, 0.0f); // Top Left
					gl.glEnd(); // Done Drawing The Quad
					myTextures[3].disable();
				}

				gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE); // Copy Image 2 Color To
														// The Screen

				myTextures[4].enable();
				myTextures[4].bind();

				gl.glBegin(GL.GL_QUADS); // Start Drawing A Textured Quad
				gl.glTexCoord2f(0.0f, 0.0f);
				gl.glVertex3f(-1.1f, -1.1f, 0.0f); // Bottom Left
				gl.glTexCoord2f(1.0f, 0.0f);
				gl.glVertex3f(1.1f, -1.1f, 0.0f); // Bottom Right
				gl.glTexCoord2f(1.0f, 1.0f);
				gl.glVertex3f(1.1f, 1.1f, 0.0f); // Top Right
				gl.glTexCoord2f(0.0f, 1.0f);
				gl.glVertex3f(-1.1f, 1.1f, 0.0f); // Top Left
				gl.glEnd(); // Done Drawing The Quad
				myTextures[4].disable();
			} else // Otherwise
			{
				if (masking) // Is Masking On?
				{
					myTextures[1].enable();
					myTextures[1].bind();
					gl.glBegin(GL.GL_QUADS); // Start Drawing A Textured Quad
					gl.glTexCoord2f(roll + 0.0f, 0.0f);
					gl.glVertex3f(-1.1f, -1.1f, 0.0f); // Bottom Left
					gl.glTexCoord2f(roll + 4.0f, 0.0f);
					gl.glVertex3f(1.1f, -1.1f, 0.0f); // Bottom Right
					gl.glTexCoord2f(roll + 4.0f, 4.0f);
					gl.glVertex3f(1.1f, 1.1f, 0.0f); // Top Right
					gl.glTexCoord2f(roll + 0.0f, 4.0f);
					gl.glVertex3f(-1.1f, 1.1f, 0.0f); // Top Left
					gl.glEnd(); // Done Drawing The Quad
					myTextures[1].disable();
				}

				gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE); // Copy Image 1 Color To
														// The Screen

				myTextures[2].enable();
				myTextures[2].bind();

				gl.glBegin(GL.GL_QUADS); // Start Drawing A Textured Quad
				gl.glTexCoord2f(roll + 0.0f, 0.0f);
				gl.glVertex3f(-1.1f, -1.1f, 0.0f); // Bottom Left
				gl.glTexCoord2f(roll + 4.0f, 0.0f);
				gl.glVertex3f(1.1f, -1.1f, 0.0f); // Bottom Right
				gl.glTexCoord2f(roll + 4.0f, 4.0f);
				gl.glVertex3f(1.1f, 1.1f, 0.0f); // Top Right
				gl.glTexCoord2f(roll + 0.0f, 4.0f);
				gl.glVertex3f(-1.1f, 1.1f, 0.0f); // Top Left
				gl.glEnd(); // Done Drawing The Quad
				myTextures[2].disable();
			}

			gl.glEnable(GL.GL_DEPTH_TEST); // Enable Depth Testing
			gl.glDisable(GL.GL_BLEND); // Disable Blending

			roll += 0.002f; // Increase Our Texture Roll Variable
			if (roll > 1.0f) // Is Roll Greater Than One
			{
				roll -= 1.0f; // Subtract 1 From Roll
			}

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
			final GL gl = gLDrawable.getGL();
			glu = new GLU(); // get GL Utilities

			String resourceName1 = "nehe/lesson20/data/logo.bmp";
			String resourceName2 = "nehe/lesson20/data/image1.bmp";
			String resourceName3 = "nehe/lesson20/data/mask1.bmp";
			String resourceName4 = "nehe/lesson20/data/image2.bmp";
			String resourceName5 = "nehe/lesson20/data/mask2.bmp";
			URL url1 = getResource(resourceName1);
			URL url2 = getResource(resourceName2);
			URL url3 = getResource(resourceName3);
			URL url4 = getResource(resourceName4);
			URL url5 = getResource(resourceName5);
			if (url1 == null || url2 == null || url3 == null || url4 == null
					|| url5 == null) {
				throw new RuntimeException("Error reading 5 resources");
			}
			try {
				BufferedImage tBufferedImage = ImageIO.read(new File(url1
						.getFile()));
				ImageUtil.flipImageVertically(tBufferedImage);
				myTextures[0] = TextureIO.newTexture(tBufferedImage, true);
				tBufferedImage = ImageIO.read(new File(url2.getFile()));
				ImageUtil.flipImageVertically(tBufferedImage);
				myTextures[1] = TextureIO.newTexture(url2, false, ".bmp");
				tBufferedImage = ImageIO.read(new File(url3.getFile()));
				ImageUtil.flipImageVertically(tBufferedImage);
				myTextures[2] = TextureIO.newTexture(url3, false, ".bmp");
				tBufferedImage = ImageIO.read(new File(url4.getFile()));
				ImageUtil.flipImageVertically(tBufferedImage);
				myTextures[3] = TextureIO.newTexture(url4, false, ".bmp");
				tBufferedImage = ImageIO.read(new File(url5.getFile()));
				ImageUtil.flipImageVertically(tBufferedImage);
				myTextures[4] = TextureIO.newTexture(url5, false, ".bmp");
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
//			gl.glDepthFunc(GL.GL_LEQUAL); // The Type Of Depth Testing To Do
//			gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // Really
//																		// Nice
//																		// Perspective
//																		// Calculations
			gl.glEnable(GL.GL_TEXTURE_2D);
			// gLDrawable.addKeyListener(this);
			// texture = genTexture(gl);
			// gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
			// BufferedImage img = readPNGImage(resourceName);
			// makeRGBTexture(gl, glu, img, GL.GL_TEXTURE_2D, false);
//			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
//					GL.GL_LINEAR);
//			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
//					GL.GL_LINEAR);
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

		/**
		 * Invoked when a key has been pressed. See the class description for
		 * {@link KeyEvent} for a definition of a key pressed event.
		 * 
		 * @param e
		 *            The KeyEvent.
		 */
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				animator.stop();
				System.exit(0);
				break;
			case KeyEvent.VK_SPACE:
				scene = !scene;
				System.out.println("scene=" + scene);
				break;
			case KeyEvent.VK_M:
				masking = !masking;
				System.out.println("masking=" + masking);
				break;
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
