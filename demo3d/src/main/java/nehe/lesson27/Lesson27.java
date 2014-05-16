package nehe.lesson27;

/*--.          .-"-.
 /   o_O        / O o \
 \_  (__\       \_ v _/
 //   \\        //   \\
 ((     ))      ((     ))
 ��������������--""---""--����--""---""--��������������������������
 �                 |||            |||                             �
 �                  |              |                              �
 �                                                                �
 � Programmer:Abdul Bezrati                                       �
 � Program   :Nehe's 27th lesson port to JOGL                     �
 � Comments  :None                                                �
 �    _______                                                     �
 �  /` _____ `\;,    abezrati@hotmail.com                         �
 � (__(^===^)__)';,                                 ___           �
 �   /  :::  \   ,;                               /^   ^\         �
 �  |   :::   | ,;'                              ( �   � )        �
 ���'._______.'`��������������������������� --�oOo--(_)--oOo�--��*/

//import net.java.games.jogl.util.GLUT;
//import net.java.games.jogl.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;

public class Lesson27 implements KeyListener {

	private DisplayMode m_oOldDisplayMode;
	GraphicsDevice defaultScreen = null;

	nehe.lesson27.object3D.glObject obj;
	initRenderer renderer;
	GLUquadric quadratic; // Storage For Our Quadratic Objects ( NEW )
	GLDrawable glDrawable;
	GLUquadric q; // Quadratic For Drawing A Sphere
	GLCanvas canvas;
	Animator loop;
	object3D object;
	JFrame frame;
	GLUT glut;
	GLU glu;
	GL gl;

	boolean keys[] = new boolean[256], // Array Used For The Keyboard Routine
			light; // Lighting ON/OFF

	class GLvector4f {
		float[] e = new float[4];
	}; // Typedef's For VMatMult Procedure

	class GLmatrix16f {
		float[] e = new float[16];
	}; // Typedef's For VMatMult Procedure

	float LightPos[] = { 0.0f, 5.0f, -4.0f, 1.0f }, // Light Position
			LightAmb[] = { 0.2f, 0.2f, 0.2f, 1.0f }, // Ambient Light Values
			LightDif[] = { 0.6f, 0.6f, 0.6f, 1.0f }, // Diffuse Light Values
			LightSpc[] = { -0.2f, -0.2f, -0.2f, 1.0f }, // Specular Light Values

			MatAmb[] = { 0.4f, 0.4f, 0.4f, 1.0f }, // Material - Ambient Values
			MatDif[] = { 0.2f, 0.6f, 0.9f, 1.0f }, // Material - Diffuse Values
			MatSpc[] = { 0.0f, 0.0f, 0.0f, 1.0f }, // Material - Specular Values
			MatShn[] = { 0.0f }, // Material - Shininess

			SpherePos[] = { -4.0f, -5.0f, -6.0f }, ObjPos[] = { -2.0f, -2.0f,
					-5.0f }, // Object Position

			xspeed, // X Rotation Speed
			yspeed, // Y Rotation Speed
			xrot, // X Rotation
			yrot; // Y Rotation

	int screenWidth, screenHeight, canvasHeight, canvasWidth, xLocation,
			yLocation;

	void VMatMult(GLmatrix16f M, GLvector4f v) {

		float res[] = new float[4]; // Hold Calculated Results
		res[0] = M.e[0] * v.e[0] + M.e[4] * v.e[1] + M.e[8] * v.e[2] + M.e[12]
				* v.e[3];
		res[1] = M.e[1] * v.e[0] + M.e[5] * v.e[1] + M.e[9] * v.e[2] + M.e[13]
				* v.e[3];
		res[2] = M.e[2] * v.e[0] + M.e[6] * v.e[1] + M.e[10] * v.e[2] + M.e[14]
				* v.e[3];
		res[3] = M.e[3] * v.e[0] + M.e[7] * v.e[1] + M.e[11] * v.e[2] + M.e[15]
				* v.e[3];
		v.e[0] = res[0]; // Results Are Stored Back In v[]
		v.e[1] = res[1];
		v.e[2] = res[2];
		v.e[3] = res[3]; // Homogenous Coordinate
	}

	int InitGLObjects() { // Initialize Objects

		object = new object3D(gl);
		obj = new object3D.glObject();

		if (object.ReadObject("src/main/java/nehe/lesson27/data/Object2.txt",
				obj) == 0) { // Read
			// Object2
			// Into obj
			JOptionPane.showMessageDialog(null,
					"Unable to load Data/Object2.txt!", // If Loading Failed,
														// Exit
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		object.SetConnectivity(obj); // Set Face To Face Connectivity

		for (int i = 0; i < obj.nPlanes; i++)
			// Loop Through All Object Planes
			object.CalcPlane(obj, obj.planes[i]); // Compute Plane Equations For
													// All Faces

		return 1; // Return True
	}

	public static void main(String[] args) {
		Lesson27 demo = new Lesson27();
	}

	/**
	 * Retrieves the default screen on the current system.
	 * 
	 * @return A GraphicsDevice object for the default screen.
	 * @throws HeadlessException
	 *             If the current system does not support a display.
	 */
	private GraphicsDevice getDefaultScreen() throws HeadlessException {
		// Get the local graphic environment.
		GraphicsEnvironment graphicsEnv = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		// Get the default screen device.
		return graphicsEnv.getDefaultScreenDevice();
	}

	Lesson27() {
		int fullScreen = JOptionPane.showConfirmDialog(null,
				"Would you like to run in fullscreen mode?", "Fullscreen",
				JOptionPane.YES_NO_OPTION);
		if (fullScreen != 0)
			JFrame.setDefaultLookAndFeelDecorated(true);

		frame = new JFrame("Banu Octavian & NeHe's Shadow Casting Tutorial");
		screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

		switch (fullScreen) {
		case 0:
			frame.setUndecorated(true);
			break;
		default:
			canvasWidth = 640;
			canvasHeight = 480;
			xLocation = (screenWidth - canvasWidth) >> 1;
			yLocation = (screenHeight - canvasHeight) >> 1;
			frame.setLocation(xLocation, yLocation);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setIconImage(new ImageIcon(
					"src/main/java/nehe/lesson27/data/icon.png").getImage());
		}

		GLCapabilities capabilities = new GLCapabilities();
		capabilities.setStencilBits(1);
		canvas = new GLCanvas();// GLDrawableFactory.getFactory().createGLCanvas(capabilities);
		canvas.setSize(new Dimension(canvasWidth, canvasHeight));
		canvas.addGLEventListener((renderer = new initRenderer()));
		canvas.requestFocus();
		canvas.addKeyListener(this);

		frame.addKeyListener(this);
		frame.addWindowListener(new shutDownWindow());
		frame.getContentPane().add(canvas, BorderLayout.CENTER);

		try {
			// Get the default screen device.
			defaultScreen = getDefaultScreen();
		} catch (HeadlessException eHeadless) {
			// Alright, where did the head go??!! Must have been eaten by
			// Diablo ... or was it Baal??
			System.out.println("Cannot switch between windowed and full "
					+ "screen mode: " + eHeadless.getMessage());
			return;
		}
		m_oOldDisplayMode = defaultScreen.getDisplayMode(); // The main window.

		if (fullScreen == 0) {
			GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getDefaultScreenDevice().setFullScreenWindow(frame);
			GraphicsEnvironment
					.getLocalGraphicsEnvironment()
					.getDefaultScreenDevice()
					.setDisplayMode(
							(new DisplayMode(640, 480, 32,
									DisplayMode.REFRESH_RATE_UNKNOWN)));
		} else
			frame.pack();
		frame.setVisible(true);
	}

	public class initRenderer implements GLEventListener {
		public void init(GLAutoDrawable drawable) {

			gl = drawable.getGL();
			glu = new GLU();// drawable.getGLU();
			glut = new GLUT();
			glDrawable = drawable;
			drawable.setGL(new DebugGL(drawable.getGL()));
			if (InitGLObjects() == 0) {
				System.out.println("Couldn't load model");
				System.exit(0);
			}

			gl.glShadeModel(gl.GL_SMOOTH); // Enable Smooth Shading
			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
			gl.glClearDepth(1.0f); // Depth Buffer Setup
			gl.glClearStencil(0); // Stencil Buffer Setup
			gl.glEnable(gl.GL_DEPTH_TEST); // Enables Depth Testing
			gl.glDepthFunc(gl.GL_LEQUAL); // The Type Of Depth Testing To Do
			gl.glHint(gl.GL_PERSPECTIVE_CORRECTION_HINT, gl.GL_NICEST); // Really
																		// Nice
																		// Perspective
																		// Calculations

			ByteBuffer temp = ByteBuffer.allocateDirect(16);
			temp.order(ByteOrder.nativeOrder());
			gl.glLightfv(gl.GL_LIGHT1, gl.GL_POSITION, (FloatBuffer) temp
					.asFloatBuffer().put(LightPos).flip()); // Set Light1
															// Position
			gl.glLightfv(gl.GL_LIGHT1, gl.GL_AMBIENT, (FloatBuffer) temp
					.asFloatBuffer().put(LightAmb).flip()); // Set Light1
															// Ambience
			gl.glLightfv(gl.GL_LIGHT1, gl.GL_DIFFUSE, (FloatBuffer) temp
					.asFloatBuffer().put(LightDif).flip()); // Set Light1
															// Diffuse
			gl.glLightfv(gl.GL_LIGHT1, gl.GL_SPECULAR, (FloatBuffer) temp
					.asFloatBuffer().put(LightSpc).flip()); // Set Light1
															// Specular
			gl.glEnable(gl.GL_LIGHT1); // Enable Light1
			gl.glEnable(gl.GL_LIGHTING); // Enable Lighting

			gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT, (FloatBuffer) temp
					.asFloatBuffer().put(MatAmb).flip()); // Set Material
															// Ambience
			gl.glMaterialfv(gl.GL_FRONT, gl.GL_DIFFUSE, (FloatBuffer) temp
					.asFloatBuffer().put(MatDif).flip()); // Set Material
															// Diffuse
			gl.glMaterialfv(gl.GL_FRONT, gl.GL_SPECULAR, (FloatBuffer) temp
					.asFloatBuffer().put(MatSpc).flip()); // Set Material
															// Specular
			gl.glMaterialfv(gl.GL_FRONT, gl.GL_SHININESS, (FloatBuffer) temp
					.asFloatBuffer().put(MatShn).flip()); // Set Material
															// Shininess

			gl.glCullFace(gl.GL_BACK); // Set Culling Face To Back Face
			gl.glEnable(gl.GL_CULL_FACE); // Enable Culling
			gl.glClearColor(0.1f, 1.0f, 0.5f, 1.0f); // Set Clear Color
														// (Greenish Color)

			q = glu.gluNewQuadric(); // Initialize Quadratic
			glu.gluQuadricNormals(q, gl.GL_SMOOTH); // Enable Smooth Normal
													// Generation
			glu.gluQuadricTexture(q, false); // Disable Auto Texture Coords
			loop = new Animator(drawable);// glDrawable);
			loop.start();
		}

		public void display(GLAutoDrawable drawable) {
			// Clear Color Buffer, Depth Buffer, Stencil Buffer
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT
					| GL.GL_STENCIL_BUFFER_BIT);

			GLmatrix16f Minv = new GLmatrix16f();
			GLvector4f wlp = new GLvector4f(), lp = new GLvector4f();

			gl.glLoadIdentity(); // Reset Modelview Matrix
			gl.glTranslatef(0.0f, 0.0f, -20.0f); // Zoom Into Screen 20 Units

			ByteBuffer temp = ByteBuffer.allocateDirect(16);
			temp.order(ByteOrder.nativeOrder());
			gl.glLightfv(gl.GL_LIGHT1, gl.GL_POSITION, (FloatBuffer) temp
					.asFloatBuffer().put(LightPos).flip()); // Position Light1

			gl.glTranslatef(SpherePos[0], SpherePos[1], SpherePos[2]); // Position
																		// The
																		// Sphere
			glu.gluSphere(q, 1.5f, 32, 16); // Draw A Sphere

			// calculate light's position relative to local coordinate system
			// dunno if this is the best way to do it, but it actually works
			// if u find another aproach, let me know ;)

			// we build the inversed matrix by doing all the actions in reverse
			// order
			// and with reverse parameters (notice -xrot, -yrot, -ObjPos[],
			// etc.)
			gl.glLoadIdentity(); // Reset Matrix
			gl.glRotatef(-yrot, 0.0f, 1.0f, 0.0f); // Rotate By -yrot On Y Axis
			gl.glRotatef(-xrot, 1.0f, 0.0f, 0.0f); // Rotate By -xrot On X Axis
			
			temp = ByteBuffer.allocateDirect(16*16);
			temp.order(ByteOrder.nativeOrder());
			gl.glGetFloatv(gl.GL_MODELVIEW_MATRIX, (FloatBuffer) temp
					.asFloatBuffer().put(Minv.e)); // Retrieve ModelView
															// Matrix (Stores In
															// Minv)
			lp.e[0] = LightPos[0]; // Store Light Position X In lp[0]
			lp.e[1] = LightPos[1]; // Store Light Position Y In lp[1]
			lp.e[2] = LightPos[2]; // Store Light Position Z In lp[2]
			lp.e[3] = LightPos[3]; // Store Light Direction In lp[3]
			VMatMult(Minv, lp); // We Store Rotated Light Vector In 'lp' Array
			gl.glTranslatef(-ObjPos[0], -ObjPos[1], -ObjPos[2]);// Move Negative
																// On All Axis
																// Based On
																// ObjPos[]
																// Values (X, Y,
																// Z)
			gl.glGetFloatv(gl.GL_MODELVIEW_MATRIX, (FloatBuffer) temp
					.asFloatBuffer().put(Minv.e).flip()); // Retrieve ModelView
															// Matrix From Minv
			wlp.e[0] = 0.0f; // World Local Coord X To 0
			wlp.e[1] = 0.0f; // World Local Coord Y To 0
			wlp.e[2] = 0.0f; // World Local Coord Z To 0
			wlp.e[3] = 1.0f;
			VMatMult(Minv, wlp); // We Store The Position Of The World Origin
									// Relative To The
									// Local Coord. System In 'wlp' Array
			lp.e[0] += wlp.e[0]; // Adding These Two Gives Us The
			lp.e[1] += wlp.e[1]; // Position Of The Light Relative To
			lp.e[2] += wlp.e[2]; // The Local Coordinate System

			gl.glColor4f(0.7f, 0.4f, 0.0f, 1.0f); // Set Color To An Orange
			gl.glLoadIdentity(); // Reset Modelview Matrix
			gl.glTranslatef(0.0f, 0.0f, -20.0f); // Zoom Into The Screen 20
													// Units
			DrawGLRoom(); // Draw The Room
			gl.glTranslatef(ObjPos[0], ObjPos[1], ObjPos[2]); // Position The
																// Object
			gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); // Spin It On The X Axis By
													// xrot
			gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); // Spin It On The Y Axis By
													// yrot
			object.DrawGLObject(obj); // Procedure For Drawing The Loaded Object
			object.CastShadow(obj, lp.e); // Procedure For Casting The Shadow
											// Based On The Silhouette

			gl.glColor4f(0.7f, 0.4f, 0.0f, 1.0f); // Set Color To Purplish Blue
			gl.glDisable(gl.GL_LIGHTING); // Disable Lighting
			gl.glDepthMask(false); // Disable Depth Mask
			gl.glTranslatef(lp.e[0], lp.e[1], lp.e[2]); // Translate To Light's
														// Position
														// Notice We're Still In
														// Local Coordinate
														// System
			glu.gluSphere(q, 0.2f, 16, 8); // Draw A Little Yellow Sphere
											// (Represents Light)
			gl.glEnable(gl.GL_LIGHTING); // Enable Lighting
			gl.glDepthMask(true); // Enable Depth Mask

			xrot += xspeed; // Increase xrot By xspeed
			yrot += yspeed; // Increase yrot By yspeed

			gl.glFlush(); // Flush The OpenGL Pipeline
			processKeyboard();
		}

		public void reshape(GLAutoDrawable drawable, int xstart, int ystart,
				int width, int height) {

			height = (height == 0) ? 1 : height;

			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(gl.GL_PROJECTION);
			gl.glLoadIdentity();

			// Calculate The Aspect Ratio Of The Window
			glu.gluPerspective(45.0f, (float) width / height, 0.001f, 100.0f);
			gl.glMatrixMode(gl.GL_MODELVIEW);
			gl.glLoadIdentity();
		}

		public void displayChanged(GLAutoDrawable drawable,
				boolean modeChanged, boolean deviceChanged) {
		}
	}

	void DrawGLRoom() { // Draw The Room (Box)

		gl.glBegin(gl.GL_QUADS); // Begin Drawing Quads
		// Floor
		gl.glNormal3f(0.0f, 1.0f, 0.0f); // Normal Pointing Up
		gl.glVertex3f(-10.0f, -10.0f, -20.0f); // Back Left
		gl.glVertex3f(-10.0f, -10.0f, 20.0f); // Front Left
		gl.glVertex3f(10.0f, -10.0f, 20.0f); // Front Right
		gl.glVertex3f(10.0f, -10.0f, -20.0f); // Back Right
		// Ceiling
		gl.glNormal3f(0.0f, -1.0f, 0.0f); // Normal Point Down
		gl.glVertex3f(-10.0f, 10.0f, 20.0f); // Front Left
		gl.glVertex3f(-10.0f, 10.0f, -20.0f); // Back Left
		gl.glVertex3f(10.0f, 10.0f, -20.0f); // Back Right
		gl.glVertex3f(10.0f, 10.0f, 20.0f); // Front Right
		// Front Wall
		gl.glNormal3f(0.0f, 0.0f, 1.0f); // Normal Pointing Away From Viewer
		gl.glVertex3f(-10.0f, 10.0f, -20.0f); // Top Left
		gl.glVertex3f(-10.0f, -10.0f, -20.0f); // Bottom Left
		gl.glVertex3f(10.0f, -10.0f, -20.0f); // Bottom Right
		gl.glVertex3f(10.0f, 10.0f, -20.0f); // Top Right
		// Back Wall
		gl.glNormal3f(0.0f, 0.0f, -1.0f); // Normal Pointing Towards Viewer
		gl.glVertex3f(10.0f, 10.0f, 20.0f); // Top Right
		gl.glVertex3f(10.0f, -10.0f, 20.0f); // Bottom Right
		gl.glVertex3f(-10.0f, -10.0f, 20.0f); // Bottom Left
		gl.glVertex3f(-10.0f, 10.0f, 20.0f); // Top Left
		// Left Wall
		gl.glNormal3f(1.0f, 0.0f, 0.0f); // Normal Pointing Right
		gl.glVertex3f(-10.0f, 10.0f, 20.0f); // Top Front
		gl.glVertex3f(-10.0f, -10.0f, 20.0f); // Bottom Front
		gl.glVertex3f(-10.0f, -10.0f, -20.0f); // Bottom Back
		gl.glVertex3f(-10.0f, 10.0f, -20.0f); // Top Back
		// Right Wall
		gl.glNormal3f(-1.0f, 0.0f, 0.0f); // Normal Pointing Left
		gl.glVertex3f(10.0f, 10.0f, -20.0f); // Top Back
		gl.glVertex3f(10.0f, -10.0f, -20.0f); // Bottom Back
		gl.glVertex3f(10.0f, -10.0f, 20.0f); // Bottom Front
		gl.glVertex3f(10.0f, 10.0f, 20.0f); // Top Front
		gl.glEnd(); // Done Drawing Quads
	}

	public void processKeyboard() {

		if (keys[KeyEvent.VK_LEFT])
			yspeed -= 0.1f; // 'Arrow Left' Decrease yspeed
		if (keys[KeyEvent.VK_RIGHT])
			yspeed += 0.1f; // 'Arrow Right' Increase yspeed
		if (keys[KeyEvent.VK_UP])
			xspeed -= 0.1f; // 'Arrow Up' Decrease xspeed
		if (keys[KeyEvent.VK_DOWN])
			xspeed += 0.1f; // 'Arrow Down' Increase xspeed

		// Adjust Light's Position
		if (keys[KeyEvent.VK_L])
			LightPos[0] += 0.05f; // 'L' Moves Light Right
		if (keys[KeyEvent.VK_J])
			LightPos[0] -= 0.05f; // 'J' Moves Light Left

		if (keys[KeyEvent.VK_I])
			LightPos[1] += 0.05f; // 'I' Moves Light Up
		if (keys[KeyEvent.VK_K])
			LightPos[1] -= 0.05f; // 'K' Moves Light Down

		if (keys[KeyEvent.VK_O])
			LightPos[2] += 0.05f; // 'O' Moves Light Toward Viewer
		if (keys[KeyEvent.VK_U])
			LightPos[2] -= 0.05f; // 'U' Moves Light Away From Viewer

		// Adjust Object's Position
		if (keys[KeyEvent.VK_NUMPAD6])
			ObjPos[0] += 0.05f; // 'Numpad6' Move Object Right
		if (keys[KeyEvent.VK_NUMPAD4])
			ObjPos[0] -= 0.05f; // 'Numpad4' Move Object Left

		if (keys[KeyEvent.VK_NUMPAD8])
			ObjPos[1] += 0.05f; // 'Numpad8' Move Object Up
		if (keys[KeyEvent.VK_NUMPAD5])
			ObjPos[1] -= 0.05f; // 'Numpad5' Move Object Down

		if (keys[KeyEvent.VK_NUMPAD9])
			ObjPos[2] += 0.05f; // 'Numpad9' Move Object Toward Viewer
		if (keys[KeyEvent.VK_NUMPAD7])
			ObjPos[2] -= 0.05f; // 'Numpad7' Move Object Away From Viewer

		// Adjust Ball's Position
		if (keys[KeyEvent.VK_D])
			SpherePos[0] += 0.05f; // 'D' Move Ball Right
		if (keys[KeyEvent.VK_A])
			SpherePos[0] -= 0.05f; // 'A' Move Ball Left

		if (keys[KeyEvent.VK_W])
			SpherePos[1] += 0.05f; // 'W' Move Ball Up
		if (keys[KeyEvent.VK_S])
			SpherePos[1] -= 0.05f; // 'S' Move Ball Down

		if (keys[KeyEvent.VK_E])
			SpherePos[2] += 0.05f; // 'E' Move Ball Toward Viewer
		if (keys[KeyEvent.VK_Q])
			SpherePos[2] -= 0.05f; // 'Q' Move Ball Away From Viewer
	}

	public void keyReleased(KeyEvent evt) {
		keys[evt.getKeyCode()] = false;
	}

	public void keyPressed(KeyEvent evt) {

		keys[evt.getKeyCode()] = true;

		if (keys[KeyEvent.VK_ESCAPE]) {
			loop.stop();
			System.exit(0);
		}
	}

	public void keyTyped(KeyEvent evt) {
	}

	public class shutDownWindow extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			loop.stop();
		}
	}
}
