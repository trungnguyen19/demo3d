package nehe.lesson42;

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
import java.util.Calendar;
import java.util.Date;
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
public class Lesson42 {
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

		// mine
		ByteBuffer tex_data; /* Holds Our Texture Data */

		final int width = 128; /* Maze Width (Must Be A Power Of 2) */
		final int height = 128; /* Maze Height (Must Be A Power Of 2) */

		/* Random Colors (4 Red, 4 Green, 4 Blue) */
		byte[] r = new byte[4];
		byte[] g = new byte[4];
		byte[] b = new byte[4];

		int mx, my; /* General Loops (Used For Seeking) */
		GLUquadric quadric; /* The Quadric Object */

		int window_width; /* Calculate The Width (Right Side-Left Side) */
		int window_height; /* Calculate The Height (Bottom-Top) */

		int lastTickCount;
		int tickCount;

		boolean done; /* Flag To Let Us Know When It's Done */

		void updateTex(int dmx, int dmy) /* Update Pixel dmx, dmy On The Texture */
		{
			 System.out.println("dmx=" + dmx + ",dmy=" + dmy);
			tex_data.put(0 + ((dmx + (width * dmy)) * 3), (byte) 255); /*
																		 * Set
																		 * Red
																		 * Pixel
																		 * To
																		 * Full
																		 * Bright
																		 */
			tex_data.put(1 + ((dmx + (width * dmy)) * 3), (byte) 255); /*
																		 * Set
																		 * Green
																		 * Pixel
																		 * To
																		 * Full
																		 * Bright
																		 */
			tex_data.put(2 + ((dmx + (width * dmy)) * 3), (byte) 255); /*
																		 * Set
																		 * Blue
																		 * Pixel
																		 * To
																		 * Full
																		 * Bright
																		 */
		}

		/* Perform Motion Updates Here */
		void update(float milliseconds) {
			int dir; /* Will Hold Current Direction */
			int x, y;

//			System.out.println("milliseconds = "+milliseconds);
			
			xrot += (float) (milliseconds) * 0.02f; /*
													 * Increase Rotation On The
													 * X-Axis
													 */
			yrot += (float) (milliseconds) * 0.03f; /*
													 * Increase Rotation On The
													 * Y-Axis
													 */
			zrot += (float) (milliseconds) * 0.015f; /*
													 * Increase Rotation On The
													 * Z-Axis
													 */

//			System.out.println("xrot="+xrot+",yrot="+yrot+",zrot="+zrot);
			
			done = true; /* Set done To True */
			for (x = 0; x < width; x += 2) /* Loop Through All The Rooms */
			{
				for (y = 0; y < height; y += 2) /* On X And Y Axis */
				{
					if (tex_data.get(((x + (width * y)) * 3)) == 0) /*
																	 * If
																	 * Current
																	 * Texture
																	 * Pixel
																	 * (Room) Is
																	 * Blank
																	 */
						done = false; /*
									 * We Have To Set done To False (Not
									 * Finished Yet)
									 */
				}
			}

			if (done) /* If done Is True Then There Were No Unvisited Rooms */
			{
				/*
				 * Display A Message At The Top Of The Window, Pause For A Bit
				 * And Then Start Building A New Maze!
				 */
				System.out
						.println("Lesson 42: Multiple Viewports... 2003 NeHe Productions... Maze Complete!");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out
						.println("Lesson 42: Multiple Viewports... 2003 NeHe Productions... Building Maze!");
				reset();
			}

				int A = (((((mx + 2) + (width * my)) * 3)) );
				int B = (((((mx - 2) + (width * my)) * 3)) );
				int C = ((((mx + (width * (my + 2))) * 3)) );
				int D = (((mx + (width * (my - 2))) * 3) );
				int max = -1;
				if (A > max || B > max || C > max || D > max) {
					System.out.println("");
					System.out.println(":"+(width * height * 3));
					System.out.println("mx = "+mx);
					System.out.println("width = "+width);
					System.out.println("my = "+my);
					System.out.println("tex_data.capacity="+tex_data.capacity());
					System.out.println("A:"+A );
					System.out.println("B:"+B);
					System.out.println("C:"+C);
					System.out.println("D:"+D);
				}
			/* Check To Make Sure We Are Not Trapped (Nowhere Else To Move) */
			if ((((tex_data.get((((mx + 2) + (width * my)) * 3)) & 0xff) == 255) || mx > (width - 4))
					&& (((tex_data.get((((mx - 2) + (width * my)) * 3)) & 0xff) == 255) || mx < 2)
					&& (((tex_data.get(((mx + (width * (my + 2))) * 3)) & 0xff) == 255) || my > (height - 4))
					&& (((tex_data.get(((mx + (width * (my - 2))) * 3)) & 0xff) == 255) || my < 2)) {
				do /* If We Are Trapped */
				{
					mx = random.nextInt(width); /*
												 * Pick A New Random X Position
												 */
					my = random.nextInt(height); /*
												 * Pick A New Random Y Position
												 */
				} while (tex_data.get(((mx + (width * my)) * 3)) == 0); /*
																		 * Keep
																		 * Picking
																		 * A
																		 * Random
																		 * Position
																		 * Until
																		 * We
																		 * Find
																		 */
			} /* One That Has Already Been Tagged (Safe Starting Point) */

			dir = random.nextInt(4); /* Pick A Random Direction */

			if ((dir == 0) && (mx <= (width - 4))) /*
													 * If The Direction Is 0
													 * (Right) And We Are Not At
													 * The Far Right
													 */
			{
				if (tex_data.get((((mx + 2) + (width * my)) * 3)) == 0) /*
																		 * And
																		 * If
																		 * The
																		 * Room
																		 * To
																		 * The
																		 * Right
																		 * Has
																		 * Not
																		 * Already
																		 * Been
																		 * Visited
																		 */
				{
					updateTex(mx + 1, my); /*
											 * Update The Texture To Show Path
											 * Cut Out Between Rooms
											 */
					mx += 2; /* Move To The Right (Room To The Right) */
				}
			}

			if ((dir == 1) && (my <= (height - 4))) /*
													 * If The Direction Is 1
													 * (Down) And We Are Not At
													 * The Bottom
													 */
			{
				if (tex_data.get(((mx + (width * (my + 2))) * 3)) == 0) /*
																		 * And
																		 * If
																		 * The
																		 * Room
																		 * Below
																		 * Has
																		 * Not
																		 * Already
																		 * Been
																		 * Visited
																		 */
				{
					updateTex(mx, my + 1); /*
											 * Update The Texture To Show Path
											 * Cut Out Between Rooms
											 */
					my += 2; /* Move Down (Room Below) */
				}
			}

			if ((dir == 2) && (mx >= 2)) /*
										 * If The Direction Is 2 (Left) And We
										 * Are Not At The Far Left
										 */
			{
				if (tex_data.get((((mx - 2) + (width * my)) * 3)) == 0) /*
																		 * And
																		 * If
																		 * The
																		 * Room
																		 * To
																		 * The
																		 * Left
																		 * Has
																		 * Not
																		 * Already
																		 * Been
																		 * Visited
																		 */
				{
					updateTex(mx - 1, my); /*
											 * Update The Texture To Show Path
											 * Cut Out Between Rooms
											 */
					mx -= 2; /* Move To The Left (Room To The Left) */
				}
			}

			if ((dir == 3) && (my >= 2)) /*
										 * If The Direction Is 3 (Up) And We Are
										 * Not At The Top
										 */
			{
				if (tex_data.get(((mx + (width * (my - 2))) * 3)) == 0) // And
																		// If
																		// The
																		// Room
																		// Above
																		// Has
																		// Not
																		// Already
																		// Been
																		// Visited
				{
					updateTex(mx, my - 1); /*
											 * Update The Texture To Show Path
											 * Cut Out Between Rooms
											 */
					my -= 2; /* Move Up (Room Above) */
				}
			}

			updateTex(mx, my); /* Update Current Room */
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
			final GL gl = gLDrawable.getGL();

			int loop;

			tickCount = getCurrentTimeByMillisecond();
//			System.out.println("\ntickCount = "+tickCount);
//			System.out.println("lastTickCount = "+lastTickCount);
//			System.out.println("tickCount - lastTickCount = "+(tickCount - lastTickCount));
			update((float)tickCount - lastTickCount);
			lastTickCount = tickCount;

//			System.out.println("xrot="+xrot+",yrot="+yrot+",zrot="+zrot);
			
			/*
			 * Update Our Texture... This Is The Key To The Programs Speed...
			 * Much Faster Than Rebuilding The Texture Each Time
			 */
			gl.glTexSubImage2D(GL.GL_TEXTURE_2D, 0, 0, 0, width, height,
					GL.GL_RGB, GL.GL_UNSIGNED_BYTE, tex_data);

			gl.glClear(GL.GL_COLOR_BUFFER_BIT); /* Clear Screen */

			for (loop = 0; loop < 4; loop++) /* Loop To Draw Our 4 Views */
			{
				// gl.glColor3ub(r[loop], g[loop], b[loop]);
				gl.glColor3ub(r[loop], g[loop], b[loop]); /*
														 * Assign Color To
														 * Current View
														 */

				if (loop == 0) /* If We Are Drawing The First Scene */
				{
					/*
					 * Set The Viewport To The Top Left. It Will Take Up Half
					 * The Screen Width And Height
					 */
					gl.glViewport(0, window_height / 2, window_width / 2,
							window_height / 2);
					gl.glMatrixMode(GL.GL_PROJECTION); /*
														 * Select The Projection
														 * Matrix
														 */
					gl.glLoadIdentity(); /* Reset The Projection Matrix */
					/*
					 * Set Up Ortho Mode To Fit 1/4 The Screen (Size Of A
					 * Viewport)
					 */
					glu.gluOrtho2D(0, window_width / 2, window_height / 2, 0);
				}

				if (loop == 1) /* If We Are Drawing The Second Scene */
				{
					/*
					 * Set The Viewport To The Top Right. It Will Take Up Half
					 * The Screen Width And Height
					 */
					gl.glViewport(window_width / 2, window_height / 2,
							window_width / 2, window_height / 2);
					gl.glMatrixMode(GL.GL_PROJECTION); /*
														 * Select The Projection
														 * Matrix
														 */
					gl.glLoadIdentity(); /* Reset The Projection Matrix */
					/*
					 * Set Up Perspective Mode To Fit 1/4 The Screen (Size Of A
					 * Viewport)
					 */
					glu.gluPerspective(45.0,
							(float) (width) / (float) (height), 0.1f, 500.0);
				}

				if (loop == 2) /* If We Are Drawing The Third Scene */
				{
					/*
					 * Set The Viewport To The Bottom Right. It Will Take Up
					 * Half The Screen Width And Height
					 */
					gl.glViewport(window_width / 2, 0, window_width / 2,
							window_height / 2);
					gl.glMatrixMode(GL.GL_PROJECTION); /*
														 * Select The Projection
														 * Matrix
														 */
					gl.glLoadIdentity(); /* Reset The Projection Matrix */
					/*
					 * Set Up Perspective Mode To Fit 1/4 The Screen (Size Of A
					 * Viewport)
					 */
					glu.gluPerspective(45.0,
							(float) (width) / (float) (height), 0.1f, 500.0);
				}

				if (loop == 3) /* If We Are Drawing The Fourth Scene */
				{
					/*
					 * Set The Viewport To The Bottom Left. It Will Take Up Half
					 * The Screen Width And Height
					 */
					gl.glViewport(0, 0, window_width / 2, window_height / 2);
					gl.glMatrixMode(GL.GL_PROJECTION); /*
														 * Select The Projection
														 * Matrix
														 */
					gl.glLoadIdentity(); /* Reset The Projection Matrix */
					/*
					 * Set Up Perspective Mode To Fit 1/4 The Screen (Size Of A
					 * Viewport)
					 */
					glu.gluPerspective(45.0,
							(float) (width) / (float) (height), 0.1f, 500.0);
				}

				gl.glMatrixMode(GL.GL_MODELVIEW); /* Select The Modelview Matrix */
				gl.glLoadIdentity(); /* Reset The Modelview Matrix */

				gl.glClear(GL.GL_DEPTH_BUFFER_BIT); /* Clear Depth Buffer */

				if (loop == 0) /*
								 * Are We Drawing The First Image? (Original
								 * Texture... Ortho)
								 */
				{
					gl.glBegin(GL.GL_QUADS); /* Begin Drawing A Single Quad */
					/*
					 * We Fill The Entire 1/4 Section With A Single Textured
					 * Quad.
					 */
					gl.glTexCoord2f(1.0f, 0.0f);
					gl.glVertex2i(window_width / 2, 0);
					gl.glTexCoord2f(0.0f, 0.0f);
					gl.glVertex2i(0, 0);
					gl.glTexCoord2f(0.0f, 1.0f);
					gl.glVertex2i(0, window_height / 2);
					gl.glTexCoord2f(1.0f, 1.0f);
					gl.glVertex2i(window_width / 2, window_height / 2);
					gl.glEnd(); /* Done Drawing The Textured Quad */
				}

				if (loop == 1) /*
								 * Are We Drawing The Second Image? (3D Texture
								 * Mapped Sphere... Perspective)
								 */
				{
					gl.glTranslatef(0.0f, 0.0f, -14.0f); /*
														 * Move 14 Units Into
														 * The Screen
														 */

					gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); /*
														 * Rotate By xrot On The
														 * X-Axis
														 */
					gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); /*
														 * Rotate By yrot On The
														 * Y-Axis
														 */
					gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f); /*
														 * Rotate By zrot On The
														 * Z-Axis
														 */

					gl.glEnable(GL.GL_LIGHTING); /* Enable Lighting */
					glu.gluSphere(quadric, 4.0f, 32, 32); /* Draw A Sphere */
					gl.glDisable(GL.GL_LIGHTING); /* Disable Lighting */
				}

				if (loop == 2) /*
								 * Are We Drawing The Third Image? (Texture At
								 * An Angle... Perspective)
								 */
				{
					gl.glTranslatef(0.0f, 0.0f, -2.0f); /*
														 * Move 2 Units Into The
														 * Screen
														 */
					gl.glRotatef(-45.0f, 1.0f, 0.0f, 0.0f); /*
															 * Tilt The Quad
															 * Below Back 45
															 * Degrees.
															 */
					gl.glRotatef(zrot / 1.5f, 0.0f, 0.0f, 1.0f); /*
																 * Rotate By
																 * zrot/1.5 On
																 * The Z-Axis
																 */

					gl.glBegin(GL.GL_QUADS); /* Begin Drawing A Single Quad */
					gl.glTexCoord2f(1.0f, 1.0f);
					gl.glVertex3f(1.0f, 1.0f, 0.0f);
					gl.glTexCoord2f(0.0f, 1.0f);
					gl.glVertex3f(-1.0f, 1.0f, 0.0f);
					gl.glTexCoord2f(0.0f, 0.0f);
					gl.glVertex3f(-1.0f, -1.0f, 0.0f);
					gl.glTexCoord2f(1.0f, 0.0f);
					gl.glVertex3f(1.0f, -1.0f, 0.0f);
					gl.glEnd(); /* Done Drawing The Textured Quad */
				}

				if (loop == 3) /*
								 * Are We Drawing The Fourth Image? (3D Texture
								 * Mapped Cylinder... Perspective)
								 */
				{
					gl.glTranslatef(0.0f, 0.0f, -7.0f); /*
														 * Move 7 Units Into The
														 * Screen
														 */
					gl.glRotatef(-xrot / 2, 1.0f, 0.0f, 0.0f); /*
																 * Rotate By
																 * -xrot/2 On
																 * The X-Axis
																 */
					gl.glRotatef(-yrot / 2, 0.0f, 1.0f, 0.0f); /*
																 * Rotate By
																 * -yrot/2 On
																 * The Y-Axis
																 */
					gl.glRotatef(-zrot / 2, 0.0f, 0.0f, 1.0f); /*
																 * Rotate By
																 * -zrot/2 On
																 * The Z-Axis
																 */

					gl.glEnable(GL.GL_LIGHTING); /* Enable Lighting */
					gl.glTranslatef(0.0f, 0.0f, -2.0f); /*
														 * Translate -2 On The
														 * Z-Axis (To Rotate
														 * Cylinder Around The
														 * Center, Not An End)
														 */
					glu.gluCylinder(quadric, 1.5f, 1.5f, 4.0f, 32, 16); /*
																		 * Draw
																		 * A
																		 * Cylinder
																		 */
					gl.glDisable(GL.GL_LIGHTING); /* Disable Lighting */
				}
			}

			gl.glFlush(); /* Flush The GL Rendering Pipeline */
//			System.out.println("Xrot="+xrot+",yrot="+yrot+",zrot="+zrot);
		}

		Random random = new Random();

		int getRandom0To255() {
			int min = 0, max = 255;
			return random.nextInt(max - min + 1) + min;
			// nextInt(255) ok
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

		void reset() {
			int loop;

			for (int i = 0; i < width * height * 3; i++) {
				tex_data.put(i, (byte) 0);
			}

			for (loop = 0; loop < 4; loop++) /*
											 * Loop So We Can Assign 4 Random
											 * Colors
											 */
			{
				r[loop] = (byte) getRandom0To255(); /*
													 * Pick A Random Red Color
													 * (Bright)
													 */
				g[loop] = (byte) getRandom0To255(); /*
													 * Pick A Random Green Color
													 * (Bright)
													 */
				b[loop] = (byte) getRandom0To255(); /*
													 * Pick A Random Blue Color
													 * (Bright)
													 */
			}

			// Pick A New Random X Position
			mx = random.nextInt(width);
			// Pick A New Random Y Position
			my = random.nextInt(height);

			System.out.println("mx=" + mx + ",my=" + my);
		}

		long startTime;
		Date date;
		int getCurrentTimeByMillisecond() {
			long lDateTime = new Date().getTime();
			return (int)(lDateTime - startTime);
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

			date = new Date();
			startTime = date.getTime();
			System.out.println("startTime: "+startTime);
			
			// String resourceName1 = "nehe/lesson20/data/logo.bmp";
			// URL url1 = getResource(resourceName1);
			// if (url1 == null) {
			// throw new RuntimeException("Error reading 5 resources");
			// }
			// try {
			// BufferedImage tBufferedImage = ImageIO.read(new File(url1
			// .getFile()));
			// ImageUtil.flipImageVertically(tBufferedImage);
			// myTextures[0] = TextureIO.newTexture(tBufferedImage, true);
			// } catch (GLException | IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			window_width = gLDrawable.getWidth();
			window_height = gLDrawable.getHeight();

			tex_data = ByteBuffer.allocateDirect(width * height * 3); /*
																	 * Allocate
																	 * Space For
																	 * Our
																	 * Texture
																	 */

			reset(); /* Call Reset To Build Our Initial Texture, Etc. */

			/* Start Of User Initialization */
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S,
					GL.GL_CLAMP);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T,
					GL.GL_CLAMP);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
					GL.GL_LINEAR);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
					GL.GL_LINEAR);
			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, width, height, 0,
					GL.GL_RGB, GL.GL_UNSIGNED_BYTE, tex_data);

			gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); /*
																		 * Realy
																		 * Nice
																		 * perspective
																		 * calculations
																		 */

			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); /* Light Grey Background */
			gl.glClearDepth(1.0f); /* Depth Buffer Setup */

			gl.glDepthFunc(GL.GL_LEQUAL); /* The Type Of Depth Test To Do */
			gl.glEnable(GL.GL_DEPTH_TEST); /* Enable Depth Testing */

			gl.glShadeModel(GL.GL_SMOOTH); /* Enables Smooth Color Shading */
			gl.glDisable(GL.GL_LINE_SMOOTH); /* Initially Disable Line Smoothing */

			gl.glEnable(GL.GL_COLOR_MATERIAL); /*
												 * Enable Color Material (Allows
												 * Us To Tint Textures)
												 */

			gl.glEnable(GL.GL_TEXTURE_2D); /* Enable Texture Mapping */

			gl.glEnable(GL.GL_LIGHT0); /* Enable Light0 (Default GL Light) */

			quadric = glu.gluNewQuadric(); /*
											 * Create A Pointer To The Quadric
											 * Object
											 */
			glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH); /*
															 * Create Smooth
															 * Normals
															 */
			glu.gluQuadricTexture(quadric, true); /* Create Texture Coords */
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

		byte b = (byte) (int) (233 & 0xff);
		System.out.println("b=" + b);
		System.out.println("b&0xff=" + (b & 0xff));
		System.out.println("Byte.MAX_VALUE=" + Byte.MAX_VALUE);
		System.out.println("Byte.MAX_VALUE&0xff=" + (Byte.MAX_VALUE & 0xff));
		b = (byte) (int) (255 & 0xff);
		System.out.println("255=" + b);
		System.out.println("255&0xff=" + (b & 0xff));
		b = (byte) (int) (256 & 0xff);
		System.out.println("256=" + b);
		System.out.println("256&0xff=" + (b & 0xff));
		b = (byte) (int) (257 & 0xff);
		System.out.println("257=" + b);
		System.out.println("257&0xff=" + (b & 0xff));
		b = (byte) (int) (0 & 0xff);
		System.out.println("0=" + b);
		System.out.println("0&0xff=" + (b & 0xff));

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
