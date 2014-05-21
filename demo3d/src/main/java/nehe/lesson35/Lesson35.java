package nehe.lesson35;

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
import java.nio.DoubleBuffer;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

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
public class Lesson35 {
	static Animator animator = null;

	static class Renderer implements GLEventListener, KeyListener {

		float tex_param_s = 1.0f;
		float tex_param_t = 1.0f;

		Byte[] ui_image_copy;

		// variables
		int win_width = 512;
		int win_height = 512;
		int TEX_WIDTH = 256;
		int TEX_HEIGHT = 256;

		int msec;
		int width; // width and height of video stream
		int height;
		double stream_fps = 0; // fps (for playback, since we don't use avi
								// player)

		// #if AVIFILE_VERSION_MINOR > 6
		final float ZERO = 0.0f;
		final float ONE = 1.0f;
		// #else
		// #define ZERO 0.0f
		// #define ONE 1.0f
		// #endif

		private float xrot; // X Rotation ( NEW )
		private float yrot; // Y Rotation ( NEW )
		private float zrot; // Z Rotation ( NEW )

		Texture myTexture;

		private GLU glu; // for the GL Utility

		private boolean scene, masking;

		private float roll;

		float angle = 0.0f;
		boolean bg = true; // enable background ('b' key)
		boolean env = true; // enable environment ('e' key)
		int effect = 0; // switch between the effects

		int cube_list;
		double tex_mat[];

		GLUquadric quadratic;

		private GLAutoDrawable gLDrawable;

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
																			// Screen
																			// And
																			// Depth
																			// Buffer

			// GL_TEXTURE_2D was enabled in init function !
			angle += 20.0f / 60.0f; // does some rotation

			if (bg) // Is Background Visible?
			{
				gl.glLoadIdentity(); // Reset The Modelview Matrix
				gl.glBegin(GL.GL_QUADS); // Begin Drawing The Background (One
											// Quad)
				// Front Face
				gl.glTexCoord2f(ONE, ONE);
				gl.glVertex3f(11.0f, 8.3f, -20.0f);
				gl.glTexCoord2f(ZERO, ONE);
				gl.glVertex3f(-11.0f, 8.3f, -20.0f);
				gl.glTexCoord2f(ZERO, ZERO);
				gl.glVertex3f(-11.0f, -8.3f, -20.0f);
				gl.glTexCoord2f(ONE, ZERO);
				gl.glVertex3f(11.0f, -8.3f, -20.0f);
				gl.glEnd(); // Done Drawing The Background
			}

			gl.glLoadIdentity(); // Reset The Modelview Matrix
			gl.glTranslatef(0.0f, 0.0f, -10.0f); // Translate 10 Units Into The
													// Screen

			if (env) // Is Environment Mapping On?
			{
				gl.glEnable(GL.GL_TEXTURE_GEN_S); // Enable Texture Coord
													// Generation
				// For S (NEW)
				gl.glEnable(GL.GL_TEXTURE_GEN_T); // Enable Texture Coord
													// Generation
				// For T (NEW)
			}

			gl.glRotatef(angle * 2.3f, 1.0f, 0.0f, 0.0f); // Throw In Some
															// Rotations To Move
															// Things Around A
															// Bit
			gl.glRotatef(angle * 1.8f, 0.0f, 1.0f, 0.0f); // Throw In Some
															// Rotations To Move
															// Things Around A
															// Bit
			gl.glTranslatef(0.0f, 0.0f, 2.0f); // After Rotating Translate To
												// New
												// Position

			switch (effect) // Which Effect?
			{
			case 1: // Effect 0 - Cube
				gl.glRotatef(angle * 1.3f, 1.0f, 0.0f, 0.0f); // Rotate On The
																// X-Axis By
																// angle
				gl.glRotatef(angle * 1.1f, 0.0f, 1.0f, 0.0f); // Rotate On The
																// Y-Axis By
																// angle
				gl.glRotatef(angle * 1.2f, 0.0f, 0.0f, 1.0f); // Rotate On The
																// Z-Axis By
																// angle
				gl.glCallList(cube_list); // Draw cube via display list
				break; // Done Effect 0

			case 2: // Effect 1 - Sphere
				gl.glRotatef(angle * 1.3f, 1.0f, 0.0f, 0.0f); // Rotate On The
																// X-Axis By
																// angle
				gl.glRotatef(angle * 1.1f, 0.0f, 1.0f, 0.0f); // Rotate On The
																// Y-Axis By
																// angle
				gl.glRotatef(angle * 1.2f, 0.0f, 0.0f, 1.0f); // Rotate On The
																// Z-Axis By
																// angle
				glu.gluSphere(quadratic, 1.3f, 20, 20); // Draw A Sphere
				break; // Done Drawing Sphere

			case 3: // Effect 2 - Cylinder
				gl.glRotatef(angle * 1.3f, 1.0f, 0.0f, 0.0f); // Rotate On The
																// X-Axis By
																// angle
				gl.glRotatef(angle * 1.1f, 0.0f, 1.0f, 0.0f); // Rotate On The
																// Y-Axis By
																// angle
				gl.glRotatef(angle * 1.2f, 0.0f, 0.0f, 1.0f); // Rotate On The
																// Z-Axis By
																// angle
				gl.glTranslatef(0.0f, 0.0f, -1.5f); // Center The Cylinder
				glu.gluCylinder(quadratic, 1.0f, 1.0f, 3.0f, 32, 32); // Draw A
																		// Cylinder
				break; // Done Drawing Cylinder
			}

			if (env) // Environment Mapping Enabled?
			{
				gl.glDisable(GL.GL_TEXTURE_GEN_S); // Disable Texture Coord
													// Generation
													// For S (NEW)
				gl.glDisable(GL.GL_TEXTURE_GEN_T); // Disable Texture Coord
													// Generation
													// For T (NEW)
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

		private void baseInit(GLAutoDrawable gLDrawable) {
			// ("data/Face2.avi")
			// if (argc != 1) {
			// cout << "Opening " << argv[1] << endl;
			// avifile = avm::CreateReadFile(argv[1]);
			// } else
			// avifile = avm::CreateReadFile;
			//
			// if (avifile->IsOpened())
			// avistream = avifile->GetStream(0, avm::IReadStream::Video );
			// else
			// throw FileNotOpen("Error opening avi-file");
			//
			// avistream->StartStreaming();
			// streaminfo = avistream->GetStreamInfo();
			// width = streaminfo->GetVideoWidth();
			// height = streaminfo->GetVideoHeight();
			// stream_fps = streaminfo->GetFps();

			// adjust texture to video file (256 is standard (see declaration
			// above))
			if (width > 256)
				TEX_WIDTH = 512;
			else if (width > 512)
				TEX_WIDTH = 1024;
			else if (width > 1024)
				TEX_WIDTH = 2048;

			if (height > 256)
				TEX_HEIGHT = 512;
			else if (height > 512)
				TEX_HEIGHT = 1024;
			else if (height > 1024)
				TEX_HEIGHT = 2048;

			tex_param_s = (float) width / (float) TEX_WIDTH;
			tex_param_t = (float) height / (float) TEX_HEIGHT;

			ui_image_copy = new Byte[TEX_WIDTH * TEX_HEIGHT * 3 * Byte.SIZE];

			byte[] r = new byte[TEX_WIDTH * TEX_HEIGHT * 3 * Byte.SIZE];
			new Random().nextBytes(r);
			for (int i = 0; i < r.length; i++) {
				ui_image_copy[i] = r[i];
				// System.out.println("ui_image_copy[i]=" + ui_image_copy[i]);
			}
			System.out.println("TEX_WIDTH=" + TEX_WIDTH + ",TEX_HEIGHT="
					+ TEX_HEIGHT);

			/*
			 * create texture transform matrix (avi file must not be 2^n by 2^m
			 * but a texture has to be)
			 */
			tex_mat = new double[16];
			for (int i = 0; i < 16; ++i)
				tex_mat[i] = 0.0;
			tex_mat[0] = tex_param_s;
			tex_mat[5] = tex_param_t;
			tex_mat[10] = tex_mat[15] = 1.0;

			initialize(gLDrawable); // init gluQuadrics, glTexture, ... (needs
									// also tex_param_s and tex_param_t)

			// this will call our timer_func in 1 second (only once)
			// glu.glutTimerFunc(1000, timer_func, 0);
		}

		// let's set some things
		private void initialize(GLAutoDrawable gLDrawable) {
			final GL gl = gLDrawable.getGL();
			glu = new GLU(); // get GL Utilities

			quadratic = glu.gluNewQuadric(); // Create A Pointer To The Quadric
												// Object
			glu.gluQuadricNormals(quadratic, GLU.GLU_SMOOTH); // Create Smooth
																// Normals
			glu.gluQuadricTexture(quadratic, true); // Create Texture Coords

			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
			gl.glClearDepth(1.0f); // Depth Buffer Setup
			gl.glDepthFunc(GL.GL_LEQUAL); // The Type Of Depth Testing (Less Or
											// Equal)
			gl.glEnable(GL.GL_DEPTH_TEST); // Enable Depth Testing
			gl.glShadeModel(GL.GL_SMOOTH); // Select Smooth Shading
			gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
			gl.glClearDepth(1.0f); // Depth Buffer Setup
			gl.glDepthFunc(GL.GL_LEQUAL); // The Type Of Depth Testing (Less Or
											// Equal)
			gl.glEnable(GL.GL_DEPTH_TEST); // Enable Depth Testing
			gl.glShadeModel(GL.GL_SMOOTH); // Select Smooth Shading
			gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

			gl.glMatrixMode(GL.GL_PROJECTION);
			glu.gluPerspective(60.0, 1.0, 0.2, 40.0); // field of fiew, aspect,
														// near, far
			gl.glMatrixMode(GL.GL_MODELVIEW);

			myTexture.enable();
			myTexture.bind();
			// gl.glGenTextures(1, &texture); // generate OpenGL texture object
			gl.glEnable(GL.GL_TEXTURE_2D);
			// gl.glBindTexture(GL.GL_TEXTURE_2D, texture); // use previously
			// created texture
			// object and set
			// options
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
					GL.GL_NEAREST);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
					GL.GL_NEAREST);
			gl.glTexGeni(GL.GL_S, GL.GL_TEXTURE_GEN_MODE, GL.GL_SPHERE_MAP); // how
																				// to
																				// set
																				// texture
																				// coords
			gl.glTexGeni(GL.GL_T, GL.GL_TEXTURE_GEN_MODE, GL.GL_SPHERE_MAP);

			// display lists are much faster than drawing directly
			cube_list = gl.glGenLists(1); // generate display list
			gl.glNewList(cube_list, GL.GL_COMPILE_AND_EXECUTE); // fill display
																// list
			gl.glBegin(GL.GL_QUADS); // Begin Drawing A Cube
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
			// Back Face 0
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
			gl.glEnd(); // Done Drawing Our Cube
			gl.glEndList();

			// set texture transform matrix to alter (here only to scale)
			// texture coordinates
			// (our video stream must not be 2^n by 2^m but our texture has to
			// be)
			gl.glMatrixMode(GL.GL_TEXTURE);

			DoubleBuffer db = DoubleBuffer.allocate(16);
			db.put(tex_mat);
			gl.glLoadMatrixd(db);

			gl.glMatrixMode(GL.GL_MODELVIEW);

			myTexture.disable();
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
			String resourceName = "nehe/lesson23/data/BG.bmp";
			URL url = getResource(resourceName);
			if (url == null) {
				throw new RuntimeException("Error reading resources");
			}
			try {
				BufferedImage bufferedImage = ImageIO.read(new File(url
						.getFile()));
				ImageUtil.flipImageVertically(bufferedImage);
				myTexture = TextureIO.newTexture(bufferedImage, true);
			} catch (GLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.gLDrawable = gLDrawable;
			baseInit(gLDrawable);

			final GL gl = gLDrawable.getGL();

			quadratic = glu.gluNewQuadric(); // Create A Pointer To The Quadric
												// Object
			glu.gluQuadricNormals(quadratic, GLU.GLU_SMOOTH); // Create Smooth
																// Normals
			glu.gluQuadricTexture(quadratic, true); // Create Texture Coords

			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
			gl.glClearDepth(1.0f); // Depth Buffer Setup
			gl.glDepthFunc(GL.GL_LEQUAL); // The Type Of Depth Testing (Less Or
											// Equal)
			gl.glEnable(GL.GL_DEPTH_TEST); // Enable Depth Testing
			gl.glShadeModel(GL.GL_SMOOTH); // Select Smooth Shading
			gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
			gl.glClearDepth(1.0f); // Depth Buffer Setup
			gl.glDepthFunc(GL.GL_LEQUAL); // The Type Of Depth Testing (Less Or
											// Equal)
			gl.glEnable(GL.GL_DEPTH_TEST); // Enable Depth Testing
			gl.glShadeModel(GL.GL_SMOOTH); // Select Smooth Shading
			gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

			gl.glMatrixMode(GL.GL_PROJECTION);
			glu.gluPerspective(60.0, 1.0, 0.2, 40.0); // field of fiew, aspect,
														// near, far
			gl.glMatrixMode(GL.GL_MODELVIEW);

			myTexture.enable();
			myTexture.bind();
			// gl.glGenTextures(1, &texture); // generate OpenGL texture object
			// gl.glEnable(GL_TEXTURE_2D);
			// gl.glBindTexture( GL_TEXTURE_2D, texture ); // use previously
			// created texture object and set options
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
					GL.GL_NEAREST);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
					GL.GL_NEAREST);
			gl.glTexGeni(GL.GL_S, GL.GL_TEXTURE_GEN_MODE, GL.GL_SPHERE_MAP); // how
																				// to
																				// set
																				// texture
																				// coords
			gl.glTexGeni(GL.GL_T, GL.GL_TEXTURE_GEN_MODE, GL.GL_SPHERE_MAP);

			// display lists are much faster than drawing directly
			cube_list = gl.glGenLists(1); // generate display list
			gl.glNewList(cube_list, GL.GL_COMPILE_AND_EXECUTE); // fill display
																// list
			gl.glBegin(GL.GL_QUADS); // Begin Drawing A Cube
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
			// Back Face 0
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
			gl.glEnd(); // Done Drawing Our Cube
			gl.glEndList();

			// set texture transform matrix to alter (here only to scale)
			// texture coordinates
			// (our video stream must not be 2^n by 2^m but our texture has to
			// be)
			gl.glMatrixMode(GL.GL_TEXTURE);
			DoubleBuffer db = DoubleBuffer.allocate(16);
			db.put(tex_mat);
			gl.glLoadMatrixd(db);
			gl.glMatrixMode(GL.GL_MODELVIEW);

			myTexture.disable();
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
			case KeyEvent.VK_1:
			case KeyEvent.VK_2:
			case KeyEvent.VK_3:
				effect = e.getKeyCode() - '0';
				System.out.println("effect " + effect);
				//display(gLDrawable);
				break;
			case KeyEvent.VK_B:
				bg = !bg;
				if (bg) {
					System.out.println("background on");
				} else {
					System.out.println("background off");
				}
				//display(gLDrawable);
				break;
			case KeyEvent.VK_E:
				env = !env;
				if (env) {
					System.out.println("environment mapping on");
				} else {
					System.out.println("environment mapping off");
				}
				//display(gLDrawable);
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
