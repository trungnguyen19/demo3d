//package nehe.lesson22;
//
///*--.          .-"-.
// /   o_O        / O o \
// \_  (__\       \_ v _/
// //   \\        //   \\
// ((     ))      ((     ))
// ��������������--""---""--����--""---""--��������������������������
// �                 |||            |||                             �
// �                  |              |                              �
// �                                                                �
// � Programmer:Abdul Bezrati                                       �
// � Program   :Nehe's 22nd lesson port to JOGL                     �
// � Comments  :None                                                �
// �    _______                                                     �
// �  /` _____ `\;,    abezrati@hotmail.com                         �
// � (__(^===^)__)';,                                 ___           �
// �   /  :::  \   ,;                               /^   ^\         �
// �  |   :::   | ,;'                              ( �   � )        �
// ���'._______.'`��������������������������� --�oOo--(_)--oOo�--��*/
//
////import net.java.games.jogl.*;
////import net.java.games.jogl.util.GLUT;
//import java.awt.BorderLayout;
//import java.awt.Dimension;
//import java.awt.DisplayMode;
//import java.awt.GraphicsEnvironment;
//import java.awt.Toolkit;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//
//import javax.media.opengl.DebugGL;
//import javax.media.opengl.GL;
//import javax.media.opengl.GLAutoDrawable;
//import javax.media.opengl.GLCanvas;
//import javax.media.opengl.GLCapabilities;
//import javax.media.opengl.GLDrawable;
//import javax.media.opengl.GLDrawableFactory;
//import javax.media.opengl.GLEventListener;
//import javax.media.opengl.glu.GLU;
//import javax.swing.ImageIcon;
//import javax.swing.JFrame;
//import javax.swing.JOptionPane;
//
//import com.sun.opengl.util.Animator;
//import com.sun.opengl.util.GLUT;
//
//public class Lesson22 implements KeyListener {
//
//	initRenderer renderer;
//	GLDrawable glDrawable;
//	GLCanvas canvas;
//	Animator loop;
//	JFrame frame;
//	GLUT glut;
//	GLU glu;
//	GL gl;
//
//	boolean multitextureSupported, // Flag Indicating Whether Multitexturing Is
//									// Supported
//			useMultitexture = true, // Use It If It Is Supported?
//			__ARB_ENABLE = true, // Used To Disable ARB Extensions Entirely
//			keys[] = new boolean[256], // Array Used For The Keyboard Routine
//			emboss = false, // Emboss Only, No Basetexture?
//			bumps = true, // Do Bumpmapping?
//			light; // Lighting ON/OFF
//
//	float LightPosition[] = { 0.0f, 0.0f, 2.0f }, // Position is somewhat in
//													// front of screen
//			LightAmbient[] = { 0.2f, 0.2f, 0.2f }, // Ambient Light is 20% white
//			LightDiffuse[] = { 1.0f, 1.0f, 1.0f }, // Diffuse Light is white
//			MAX_EMBOSS = 8e-3f, // Maximum Emboss-Translate. Increase To Get
//								// Higher Immersion
//								// At A Cost Of Lower Quality (More Artifacts
//								// Will Occur!)
//			Gray[] = { .5f, 0.5f, .5f, 1.0f }, // Gray Color
//
//			// Data Contains The Faces For The Cube In Format 2xTexCoord,
//			// 3xVertex;
//			// Note That The Tesselation Of The Cube Is Only Absolute Minimum.
//
//			data[] = {
//					0f,
//					0f,
//					-1f,
//					-1f,
//					1f,// FRONT FACE
//					1f, 0f, 1f, -1f, 1f, 1f, 1f, 1f, 1f, 1f, 0f, 1f, -1f, 1f,
//					1f,
//
//					1f,
//					0f,
//					-1f,
//					-1f,
//					-1f,// BACK FACE
//					1f, 1f, -1f, 1f, -1f, 0f, 1f, 1f, 1f, -1f, 0f, 0f, 1f, -1f,
//					-1f,
//
//					0f,
//					1f,
//					-1f,
//					1f,
//					-1f,// Top Face
//					0f, 0f, -1f, 1f, 1f, 1f, 0f, 1f, 1f, 1f, 1f, 1f, 1f, 1f,
//					-1f,
//
//					1f, 1f,
//					-1f,
//					-1f,
//					-1f,// Bottom Face
//					0f, 1f, 1f, -1f, -1f, 0f, 0f, 1f, -1f, 1f, 1f, 0f, -1f,
//					-1f, 1f,
//
//					1f, 0f, 1f,
//					-1f,
//					-1f, // Right Face
//					1f, 1f, 1f, 1f, -1f, 0f, 1f, 1f, 1f, 1f, 0f, 0f, 1f, -1f,
//					1f,
//
//					0f, 0f, -1f, -1f,
//					-1f,// Left Face
//					1f, 0f, -1f, -1f, 1f, 1f, 1f, -1f, 1f, 1f, 0f, 1f, -1f, 1f,
//					-1f },
//
//			yspeed, // Y Rotation Speed
//			xspeed, // X Rotation Speed
//			xrot, // X Rotation
//			yrot, // Y Rotation
//			z = -5; // Depth Into The Screen
//
//	int screenWidth, screenHeight, canvasHeight, canvasWidth, xLocation,
//			yLocation, maxTexelUnits[] = new int[1], // Number Of
//														// Texel-Pipelines. This
//														// Is At Least 1.
//			multiLogo[] = new int[1], // Handle For Multitexture-Enabled-Logo
//			invbump[] = new int[3], // Inverted Bumpmap
//			texture[] = new int[3], // Storage For 3 Textures
//			glLogo[] = new int[1], // Handle For OpenGL-Logo
//			filter = 1, // Which Filter To Use
//			bump[] = new int[3]; // Our Bumpmappings
//
//	public static void main(String[] args) {
//		Lesson22 demo = new Lesson22();
//	}
//
//	Lesson22() {
//		int fullScreen = JOptionPane.showConfirmDialog(null,
//				"Would you like to run in fullscreen mode?", "Fullscreen",
//				JOptionPane.YES_NO_OPTION);
//		if (fullScreen != 0)
//			JFrame.setDefaultLookAndFeelDecorated(true);
//
//		frame = new JFrame("NeHe's GL_ARB_multitexture & Bump Mapping Tutorial");
//		screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
//		screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
//
//		switch (fullScreen) {
//		case 0:
//			frame.setUndecorated(true);
//			break;
//		default:
//			canvasWidth = 640;
//			canvasHeight = 480;
//			xLocation = (screenWidth - canvasWidth) >> 1;
//			yLocation = (screenHeight - canvasHeight) >> 1;
//			frame.setLocation(xLocation, yLocation);
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			frame.setIconImage(new ImageIcon("Data/icon.png").getImage());
//		}
//
//		canvas = GLDrawableFactory.getFactory().createGLCanvas(
//				new GLCapabilities());
//		canvas.setSize(new Dimension(canvasWidth, canvasHeight));
//		canvas.addGLEventListener((renderer = new initRenderer()));
//		canvas.requestFocus();
//		canvas.addKeyListener(this);
//
//		frame.addKeyListener(this);
//		frame.addWindowListener(new shutDownWindow());
//		frame.getContentPane().add(canvas, BorderLayout.CENTER);
//
//		if (fullScreen == 0) {
//			GraphicsEnvironment.getLocalGraphicsEnvironment()
//					.getDefaultScreenDevice().setFullScreenWindow(frame);
//			GraphicsEnvironment
//					.getLocalGraphicsEnvironment()
//					.getDefaultScreenDevice()
//					.setDisplayMode(
//							(new DisplayMode(640, 480, 32,
//									DisplayMode.REFRESH_RATE_UNKNOWN)));
//		} else
//			frame.pack();
//		frame.setVisible(true);
//	}
//
//	public class initRenderer implements GLEventListener {
//		public void init(GLAutoDrawable drawable) {
//
//			gl = drawable.getGL();
//			glu = new GLU();//drawable.getGLU();
//			glut = new GLUT();
//			glDrawable = drawable;
//			drawable.setGL(new DebugGL(drawable.getGL()));
//
//			gl.glEnable(GL.GL_TEXTURE_2D); // Enable Texture Mapping
//			gl.glShadeModel(GL.GL_SMOOTH); // Enable Smooth Shading
//			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
//			gl.glClearDepth(1.0f); // Depth Buffer Setup
//			gl.glEnable(GL.GL_DEPTH_TEST); // Enables Depth Testing
//			gl.glDepthFunc(GL.GL_LEQUAL); // The Type Of Depth Testing To Do
//			gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // Really
//																		// Nice
//																		// Perspective
//																		// Calculations
//
//			multitextureSupported = initMultitexture();
//			LoadGLTextures();
//			initLights();
//
//			loop = new Animator(glDrawable);
//			loop.start();
//		}
//
//		public void display(GLDrawable drawable) {
//			// Clear Color Buffer, Depth Buffer
//			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
//
//			if (!light)
//				gl.glDisable(GL.GL_LIGHTING);
//			else
//				gl.glEnable(GL.GL_LIGHTING);
//
//			if (bumps) {
//				if (useMultitexture && maxTexelUnits[0] > 1)
//					doMesh2TexelUnits();
//				else
//					doMesh1TexelUnits();
//			} else
//				doMeshNoBumps();
//			processKeyboard();
//		}
//
//		public void reshape(GLDrawable drawable, int xstart, int ystart,
//				int width, int height) {
//
//			height = (height == 0) ? 1 : height;
//
//			gl.glViewport(0, 0, width, height);
//			gl.glMatrixMode(GL.GL_PROJECTION);
//			gl.glLoadIdentity();
//
//			glu.gluPerspective(45, (float) width / height, 1, 1000);
//			gl.glMatrixMode(GL.GL_MODELVIEW);
//			gl.glLoadIdentity();
//		}
//
//		public void displayChanged(GLDrawable drawable, boolean modeChanged,
//				boolean deviceChanged) {
//		}
//	}
//
//	// isMultitextureSupported() Checks At Run-Time If Multitexturing Is
//	// Supported
//	boolean initMultitexture() {
//
//		String extensions;
//		extensions = gl.glGetString(GL.GL_EXTENSIONS); // Fetch Extension String
//
//		if (extensions.indexOf("GL_ARB_multitexture") != -1 // Is Multitexturing
//															// Supported?
//				&& __ARB_ENABLE // Override-Flag
//				&& extensions.indexOf("GL_EXT_texture_env_combine") != -1) {// Is
//																			// texture_env_combining
//																			// Supported?
//			gl.glGetIntegerv(GL.GL_MAX_TEXTURE_UNITS_ARB, maxTexelUnits);
//			return true;
//		}
//		useMultitexture = false; // We Can't Use It If It Isn't Supported!
//		return false;
//	}
//
//	void initLights() {
//
//		gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, LightAmbient); // Load
//																	// Light-Parameters
//																	// Into
//																	// GL_LIGHT1
//		gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, LightDiffuse);
//		gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, LightPosition);
//		gl.glEnable(GL.GL_LIGHT1);
//	}
//
//	void LoadGLTextures() { // Load PNGs And Convert To Textures
//
//		LoadImage image = new LoadImage(); // Create Storage Space For The
//											// Texture
//		byte[] alpha = null;
//
//		image.generateTextureInfo("Data/Base.bmp", false); // Load The
//															// Tile-Bitmap For
//															// Base-Texture
//
//		gl.glGenTextures(3, texture); // Create Three Textures
//
//		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[0]); // Create Nearest
//														// Filtered Texture
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
//				GL.GL_NEAREST);
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
//				GL.GL_NEAREST);
//		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, image.width,
//				image.height, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, image.data);
//
//		// ========
//		// Use GL_RGB8 Instead Of "3" In glTexImage2D. Also Defined By GL:
//		// GL_RGBA8 Etc.
//		// NEW: Now Creating GL_RGBA8 Textures, Alpha Is 1.0f Where Not
//		// Specified By Format.
//
//		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[1]); // Create Linear
//														// Filtered Texture
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
//				GL.GL_LINEAR);
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
//				GL.GL_LINEAR);
//		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, image.width,
//				image.height, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, image.data);
//
//		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[2]); // Create MipMapped
//														// Texture
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
//				GL.GL_LINEAR);
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
//				GL.GL_LINEAR_MIPMAP_NEAREST);
//		glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGB8, image.width,
//				image.height, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, image.data);
//
//		image.destroy();
//
//		image.generateTextureInfo("Data/Bump.bmp", false); // Load The Bumpmaps
//		gl.glPixelTransferf(GL.GL_RED_SCALE, 0.5f); // Scale RGB By 50%, So That
//													// We Have Only
//		gl.glPixelTransferf(GL.GL_GREEN_SCALE, 0.5f); // Half Intenstity
//		gl.glPixelTransferf(GL.GL_BLUE_SCALE, 0.5f);
//
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP); // No
//																					// Wrapping,
//																					// Please!
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
//		gl.glTexParameterfv(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_BORDER_COLOR, Gray);
//
//		gl.glGenTextures(3, bump); // Create Three Textures
//
//		gl.glBindTexture(GL.GL_TEXTURE_2D, bump[0]); // Create Nearest Filtered
//														// Texture
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
//				GL.GL_NEAREST);
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
//				GL.GL_NEAREST);
//		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, image.width,
//				image.height, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, image.data);
//
//		gl.glBindTexture(GL.GL_TEXTURE_2D, bump[1]); // Create Linear Filtered
//														// Texture
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
//				GL.GL_LINEAR);
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
//				GL.GL_LINEAR);
//		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, image.width,
//				image.height, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, image.data);
//
//		gl.glBindTexture(GL.GL_TEXTURE_2D, bump[2]); // Create MipMapped Texture
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
//				GL.GL_LINEAR);
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
//				GL.GL_LINEAR_MIPMAP_NEAREST);
//		glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGB8, image.width,
//				image.height, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, image.data);
//
//		for (int i = 0; i < image.data.length; i++)
//			// Invert The Bumpmap
//			image.data[i] = (byte) (255 - image.data[i]);
//
//		gl.glGenTextures(3, invbump); // Create Three Textures
//
//		gl.glBindTexture(GL.GL_TEXTURE_2D, invbump[0]); // Create Nearest
//														// Filtered Texture
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
//				GL.GL_NEAREST);
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
//				GL.GL_NEAREST);
//		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, image.width,
//				image.height, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, image.data);
//
//		gl.glBindTexture(GL.GL_TEXTURE_2D, invbump[1]); // Create Linear
//														// Filtered Texture
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
//				GL.GL_LINEAR);
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
//				GL.GL_LINEAR);
//		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, image.width,
//				image.height, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, image.data);
//
//		gl.glBindTexture(GL.GL_TEXTURE_2D, invbump[2]); // Create MipMapped
//														// Texture
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
//				GL.GL_LINEAR);
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
//				GL.GL_LINEAR_MIPMAP_NEAREST);
//		glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGB8, image.width,
//				image.height, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, image.data);
//
//		gl.glPixelTransferf(GL.GL_RED_SCALE, 1.0f); // Scale RGB Back To 100%
//													// Again
//		gl.glPixelTransferf(GL.GL_GREEN_SCALE, 1.0f);
//		gl.glPixelTransferf(GL.GL_BLUE_SCALE, 1.0f);
//
//		image.destroy();
//
//		image.generateTextureInfo("Data/OpenGL_Alpha.bmp", true); // Load The
//																	// Logo-Pngs
//		alpha = new byte[image.data.length]; // Create Memory For RGBA8-Texture
//
//		for (int a = 0; a < alpha.length; a += 4)
//			alpha[a + 3] = image.data[a]; // Pick Only Red Value As Alpha!
//
//		image.destroy();
//		image.generateTextureInfo("Data/OpenGL.bmp", true);
//
//		for (int a = 0; a < image.data.length; a += 4) {
//			alpha[a] = image.data[a]; // R
//			alpha[a + 1] = image.data[a + 1]; // G
//			alpha[a + 2] = image.data[a + 2]; // B
//		}
//
//		gl.glGenTextures(1, glLogo); // Create One Textures
//
//		gl.glBindTexture(GL.GL_TEXTURE_2D, glLogo[0]); // Create Linear Filtered
//														// RGBA8-Texture
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
//				GL.GL_LINEAR);
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
//				GL.GL_LINEAR);
//		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA8, image.width,
//				image.height, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, alpha);
//
//		alpha = null;
//		image.destroy();
//
//		image.generateTextureInfo("Data/Multi_On_Alpha.bmp", true);// Load The
//																	// "Extension Enabled"-Logo
//		alpha = new byte[image.data.length]; // Create Memory For RGBA8-Texture
//
//		for (int a = 0; a < alpha.length; a += 4)
//			alpha[a + 3] = image.data[a]; // Pick Only Red Value As Alpha!
//
//		image.destroy();
//		image.generateTextureInfo("Data/Multi_On.bmp", true);
//
//		for (int a = 0; a < image.data.length; a += 4) {
//			alpha[a] = image.data[a]; // R
//			alpha[a + 1] = image.data[a + 1]; // G
//			alpha[a + 2] = image.data[a + 2]; // B
//		}
//
//		gl.glGenTextures(1, multiLogo); // Create One Textures
//		gl.glBindTexture(GL.GL_TEXTURE_2D, multiLogo[0]); // Create Linear
//															// Filtered
//															// RGBA8-Texture
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
//				GL.GL_LINEAR);
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
//				GL.GL_LINEAR);
//		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA8, image.width,
//				image.height, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, alpha);
//
//		alpha = null;
//		image.destroy();
//	}
//
//	void doCube() {
//
//		int i;
//		gl.glBegin(GL.GL_QUADS);
//
//		// Front Face
//		gl.glNormal3f(0f, 0f, 1f);
//		for (i = 0; i < 4; i++) {
//			gl.glTexCoord2f(data[5 * i], data[5 * i + 1]);
//			gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
//		}
//		// Back Face
//		gl.glNormal3f(0f, 0f, -1f);
//		for (i = 4; i < 8; i++) {
//			gl.glTexCoord2f(data[5 * i], data[5 * i + 1]);
//			gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
//		}
//		// Top Face
//		gl.glNormal3f(0f, 1f, 0f);
//		for (i = 8; i < 12; i++) {
//			gl.glTexCoord2f(data[5 * i], data[5 * i + 1]);
//			gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
//		}
//		// Bottom Face
//		gl.glNormal3f(0f, -1f, 0f);
//		for (i = 12; i < 16; i++) {
//			gl.glTexCoord2f(data[5 * i], data[5 * i + 1]);
//			gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
//		}
//		// Right face
//		gl.glNormal3f(1f, 0f, 0f);
//		for (i = 16; i < 20; i++) {
//			gl.glTexCoord2f(data[5 * i], data[5 * i + 1]);
//			gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
//		}
//		// Left Face
//		gl.glNormal3f(-1.0f, 0.0f, 0.0f);
//		for (i = 20; i < 24; i++) {
//			gl.glTexCoord2f(data[5 * i], data[5 * i + 1]);
//			gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
//		}
//		gl.glEnd();
//	}
//
//	void VMatMult(float[] M, float[] v) {
//
//		float res[] = new float[3];
//		;
//
//		res[0] = M[0] * v[0] + M[1] * v[1] + M[2] * v[2] + M[3] * v[3];
//		res[1] = M[4] * v[0] + M[5] * v[1] + M[6] * v[2] + M[7] * v[3];
//		res[2] = M[8] * v[0] + M[9] * v[1] + M[10] * v[2] + M[11] * v[3];
//		;
//		v[0] = res[0];
//		v[1] = res[1];
//		v[2] = res[2];
//		v[3] = M[15]; // Homogenous Coordinate
//	}
//
//	void SetUpBumps(float[] n, float[] c, float[] l, float[] s, float[] t) {
//
//		float v[] = new float[3], // Vertex From Current Position To Light
//		lenQ; // Used To Normalize
//
//		// Calculate v From Current Vector c To Lightposition And Normalize v
//		v[0] = l[0] - c[0];
//		v[1] = l[1] - c[1];
//		v[2] = l[2] - c[2];
//
//		lenQ = (float) Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
//		v[0] /= lenQ;
//		v[1] /= lenQ;
//		v[2] /= lenQ;
//
//		// Project v Such That We Get Two Values Along Each Texture-Coordinat
//		// Axis.
//		c[0] = (s[0] * v[0] + s[1] * v[1] + s[2] * v[2]) * MAX_EMBOSS;
//		c[1] = (t[0] * v[0] + t[1] * v[1] + t[2] * v[2]) * MAX_EMBOSS;
//	}
//
//	void doLogo() {// MUST CALL THIS LAST!!!, Billboards The Two Logos.
//
//		gl.glDepthFunc(GL.GL_ALWAYS);
//		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
//		gl.glEnable(GL.GL_BLEND);
//		gl.glDisable(GL.GL_LIGHTING);
//		gl.glLoadIdentity();
//		gl.glBindTexture(GL.GL_TEXTURE_2D, glLogo[0]);
//
//		gl.glBegin(GL.GL_QUADS);
//		gl.glTexCoord2f(0.0f, 0.0f);
//		gl.glVertex3f(0.23f, -0.4f, -1.0f);
//		gl.glTexCoord2f(1.0f, 0.0f);
//		gl.glVertex3f(0.53f, -0.4f, -1.0f);
//		gl.glTexCoord2f(1.0f, 1.0f);
//		gl.glVertex3f(0.53f, -0.25f, -1.0f);
//		gl.glTexCoord2f(0.0f, 1.0f);
//		gl.glVertex3f(0.23f, -0.25f, -1.0f);
//		gl.glEnd();
//
//		if (useMultitexture) {
//			gl.glBindTexture(GL.GL_TEXTURE_2D, multiLogo[0]);
//			gl.glBegin(GL.GL_QUADS);
//			gl.glTexCoord2f(0.0f, 0.0f);
//			gl.glVertex3f(-0.53f, -0.4f, -1.0f);
//			gl.glTexCoord2f(1.0f, 0.0f);
//			gl.glVertex3f(-0.33f, -0.4f, -1.0f);
//			gl.glTexCoord2f(1.0f, 1.0f);
//			gl.glVertex3f(-0.33f, -0.3f, -1.0f);
//			gl.glTexCoord2f(0.0f, 1.0f);
//			gl.glVertex3f(-0.53f, -0.3f, -1.0f);
//			gl.glEnd();
//		}
//		gl.glDepthFunc(GL.GL_LEQUAL);
//	}
//
//	void doMesh1TexelUnits() {
//
//		float c[] = { 0f, 0f, 0f, 1f }, // Holds Current Vertex
//		n[] = { 0f, 0f, 0f, 1f }, // Normalized Normal Of Current Surface
//		s[] = { 0f, 0f, 0f, 1f }, // s-Texture Coordinate Direction, Normalized
//		t[] = { 0f, 0f, 0f, 1f }, // t-Texture Coordinate Direction, Normalized
//		l[] = { 0f, 0f, 0f, 0f }, // Holds Our Lightposition To Be Transformed
//									// Into Object Space
//		Minv[] = new float[16]; // Holds The Inverted Modelview Matrix To Do So.
//		int i;
//
//		// Build Inverse Modelview Matrix First. This Substitutes One Push/Pop
//		// With One glLoadIdentity();
//		// Simply Build It By Doing All Transformations Negated And In Reverse
//		// Order.
//		gl.glLoadIdentity();
//		gl.glRotatef(-yrot, 0.0f, 1.0f, 0.0f);
//		gl.glRotatef(-xrot, 1.0f, 0.0f, 0.0f);
//		gl.glTranslatef(0.0f, 0.0f, -z);
//		gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, Minv);
//		gl.glLoadIdentity();
//		gl.glTranslatef(0.0f, 0.0f, z);
//
//		gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
//		gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
//
//		// Transform The Lightposition Into Object Coordinates:
//		l[0] = LightPosition[0];
//		l[1] = LightPosition[1];
//		l[2] = LightPosition[2];
//		l[3] = 1.0f; // Homogenous Coordinate
//		VMatMult(Minv, l);
//
//		/*
//		 * PASS#1: Use Texture "Bump" No Blend No Lighting No Offset
//		 * Texture-Coordinates
//		 */
//
//		gl.glBindTexture(GL.GL_TEXTURE_2D, bump[filter]);
//		gl.glDisable(GL.GL_BLEND);
//		gl.glDisable(GL.GL_LIGHTING);
//		doCube();
//
//		/*
//		 * PASS#2: Use Texture "Invbump" Blend GL_ONE To GL_ONE No Lighting
//		 * Offset Texture Coordinates
//		 */
//
//		gl.glBindTexture(GL.GL_TEXTURE_2D, invbump[filter]);
//		gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
//		gl.glDepthFunc(GL.GL_LEQUAL);
//		gl.glEnable(GL.GL_BLEND);
//
//		gl.glBegin(GL.GL_QUADS);
//		// Front Face
//		n[0] = 0.0f;
//		n[1] = 0.0f;
//		n[2] = 1.0f;
//		s[0] = 1.0f;
//		s[1] = 0.0f;
//		s[2] = 0.0f;
//		t[0] = 0.0f;
//		t[1] = 1.0f;
//		t[2] = 0.0f;
//
//		for (i = 0; i < 4; i++) {
//			c[0] = data[5 * i + 2];
//			c[1] = data[5 * i + 3];
//			c[2] = data[5 * i + 4];
//			SetUpBumps(n, c, l, s, t);
//			gl.glTexCoord2f(data[5 * i] + c[0], data[5 * i + 1] + c[1]);
//			gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
//		}
//
//		// Back Face
//		n[0] = 0.0f;
//		n[1] = 0.0f;
//		n[2] = -1.0f;
//		s[0] = -1f;
//		s[1] = 0.0f;
//		s[2] = 0.0f;
//		t[0] = 0.0f;
//		t[1] = 1.0f;
//		t[2] = 0.0f;
//
//		for (i = 4; i < 8; i++) {
//			c[0] = data[5 * i + 2];
//			c[1] = data[5 * i + 3];
//			c[2] = data[5 * i + 4];
//			SetUpBumps(n, c, l, s, t);
//			gl.glTexCoord2f(data[5 * i] + c[0], data[5 * i + 1] + c[1]);
//			gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
//		}
//
//		// Top Face
//		n[0] = 0.0f;
//		n[1] = 1.0f;
//		n[2] = 0.0f;
//		s[0] = 1.0f;
//		s[1] = 0.0f;
//		s[2] = 0.0f;
//		t[0] = 0.0f;
//		t[1] = 0.0f;
//		t[2] = -1.0f;
//
//		for (i = 8; i < 12; i++) {
//			c[0] = data[5 * i + 2];
//			c[1] = data[5 * i + 3];
//			c[2] = data[5 * i + 4];
//			SetUpBumps(n, c, l, s, t);
//			gl.glTexCoord2f(data[5 * i] + c[0], data[5 * i + 1] + c[1]);
//			gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
//		}
//		// Bottom Face
//		n[0] = 0f;
//		n[1] = -1f;
//		n[2] = 0f;
//		s[0] = -1f;
//		s[1] = 0f;
//		s[2] = 0f;
//		t[0] = 0f;
//		t[1] = 0f;
//		t[2] = -1f;
//
//		for (i = 12; i < 16; i++) {
//			c[0] = data[5 * i + 2];
//			c[1] = data[5 * i + 3];
//			c[2] = data[5 * i + 4];
//			SetUpBumps(n, c, l, s, t);
//			gl.glTexCoord2f(data[5 * i] + c[0], data[5 * i + 1] + c[1]);
//			gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
//		}
//		// Right Face
//		n[0] = 1.0f;
//		n[1] = 0.0f;
//		n[2] = 0.0f;
//		s[0] = 0.0f;
//		s[1] = 0.0f;
//		s[2] = -1.0f;
//		t[0] = 0.0f;
//		t[1] = 1.0f;
//		t[2] = 0.0f;
//
//		for (i = 16; i < 20; i++) {
//			c[0] = data[5 * i + 2];
//			c[1] = data[5 * i + 3];
//			c[2] = data[5 * i + 4];
//			SetUpBumps(n, c, l, s, t);
//			gl.glTexCoord2f(data[5 * i] + c[0], data[5 * i + 1] + c[1]);
//			gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
//		}
//		// Left Face
//		n[0] = -1.0f;
//		n[1] = 0.0f;
//		n[2] = 0.0f;
//		s[0] = 0.0f;
//		s[1] = 0.0f;
//		s[2] = 1.0f;
//		t[0] = 0.0f;
//		t[1] = 1.0f;
//		t[2] = 0.0f;
//
//		for (i = 20; i < 24; i++) {
//			c[0] = data[5 * i + 2];
//			c[1] = data[5 * i + 3];
//			c[2] = data[5 * i + 4];
//			SetUpBumps(n, c, l, s, t);
//			gl.glTexCoord2f(data[5 * i] + c[0], data[5 * i + 1] + c[1]);
//			gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
//		}
//		gl.glEnd();
//
//		/*
//		 * PASS#3: Use Texture "Base" Blend GL_DST_COLOR To GL_SRC_COLOR
//		 * (Multiplies By 2) Lighting Enabled No Offset Texture-Coordinates
//		 */
//
//		if (!emboss) {
//			gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE,
//					GL.GL_MODULATE);
//			gl.glBindTexture(GL.GL_TEXTURE_2D, texture[filter]);
//			gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_SRC_COLOR);
//			gl.glEnable(GL.GL_LIGHTING);
//			doCube();
//		}
//
//		xrot += xspeed;
//		yrot += yspeed;
//		if (xrot > 360.0f)
//			xrot -= 360.0f;
//		if (xrot < 0.0f)
//			xrot += 360.0f;
//		if (yrot > 360.0f)
//			yrot -= 360.0f;
//		if (yrot < 0.0f)
//			yrot += 360.0f;
//
//		doLogo();
//	}
//
//	void doMesh2TexelUnits() {
//
//		float c[] = { 0f, 0f, 0f, 1f }, // Holds Current Vertex
//		n[] = { 0f, 0f, 0f, 1f }, // Normalized Normal Of Current Surface
//		s[] = { 0f, 0f, 0f, 1f }, // s-Texture Coordinate Direction, Normalized
//		t[] = { 0f, 0f, 0f, 1f }, // t-Texture Coordinate Direction, Normalized
//		l[] = { 0f, 0f, 0f, 0f }, // Holds Our Lightposition To Be Transformed
//									// Into Object Space
//		Minv[] = new float[16]; // Holds The Inverted Modelview Matrix To Do So.
//		int i;
//
//		// Build Inverse Modelview Matrix First. This Substitutes One Push/Pop
//		// With One glLoadIdentity();
//		// Simply Build It By Doing All Transformations Negated And In Reverse
//		// Order.
//		gl.glLoadIdentity();
//		gl.glRotatef(-yrot, 0.0f, 1.0f, 0.0f);
//		gl.glRotatef(-xrot, 1.0f, 0.0f, 0.0f);
//		gl.glTranslatef(0.0f, 0.0f, -z);
//		gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, Minv);
//		gl.glLoadIdentity();
//		gl.glTranslatef(0.0f, 0.0f, z);
//
//		gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
//		gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
//
//		// Transform The Lightposition Into Object Coordinates:
//		l[0] = LightPosition[0];
//		l[1] = LightPosition[1];
//		l[2] = LightPosition[2];
//		l[3] = 1.0f; // Homogenous Coordinate
//		VMatMult(Minv, l);
//
//		/*
//		 * PASS#1: Texel-Unit 0: Use Texture "Bump" No Blend No Lighting No
//		 * Offset Texture-Coordinates Texture-Operation "Replace" Texel-Unit 1:
//		 * Use Texture "Invbump" No Lighting Offset Texture Coordinates
//		 * Texture-Operation "Replace"
//		 */
//
//		// TEXTURE-UNIT #0
//		gl.glActiveTextureARB(GL.GL_TEXTURE0_ARB);
//		gl.glEnable(GL.GL_TEXTURE_2D);
//		gl.glBindTexture(GL.GL_TEXTURE_2D, bump[filter]);
//		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE,
//				GL.GL_COMBINE_EXT);
//		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_COMBINE_RGB_EXT, GL.GL_REPLACE);
//		// TEXTURE-UNIT #1:
//		gl.glActiveTextureARB(GL.GL_TEXTURE1_ARB);
//		gl.glEnable(GL.GL_TEXTURE_2D);
//		gl.glBindTexture(GL.GL_TEXTURE_2D, invbump[filter]);
//		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE,
//				GL.GL_COMBINE_EXT);
//		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_COMBINE_RGB_EXT, GL.GL_ADD);
//		// General Switches:
//		gl.glDisable(GL.GL_BLEND);
//		gl.glDisable(GL.GL_LIGHTING);
//		gl.glBegin(GL.GL_QUADS);
//
//		// Front Face
//		// Front Face
//		n[0] = 0.0f;
//		n[1] = 0.0f;
//		n[2] = 1.0f;
//		s[0] = 1.0f;
//		s[1] = 0.0f;
//		s[2] = 0.0f;
//		t[0] = 0.0f;
//		t[1] = 1.0f;
//		t[2] = 0.0f;
//
//		for (i = 0; i < 4; i++) {
//			c[0] = data[5 * i + 2];
//			c[1] = data[5 * i + 3];
//			c[2] = data[5 * i + 4];
//			SetUpBumps(n, c, l, s, t);
//			gl.glMultiTexCoord2fARB(GL.GL_TEXTURE0_ARB, data[5 * i],
//					data[5 * i + 1]);
//			gl.glMultiTexCoord2fARB(GL.GL_TEXTURE1_ARB, data[5 * i] + c[0],
//					data[5 * i + 1] + c[1]);
//			gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
//		}
//
//		// Back Face
//		n[0] = 0.0f;
//		n[1] = 0.0f;
//		n[2] = -1.0f;
//		s[0] = -1f;
//		s[1] = 0.0f;
//		s[2] = 0.0f;
//		t[0] = 0.0f;
//		t[1] = 1.0f;
//		t[2] = 0.0f;
//
//		for (i = 4; i < 8; i++) {
//			c[0] = data[5 * i + 2];
//			c[1] = data[5 * i + 3];
//			c[2] = data[5 * i + 4];
//			SetUpBumps(n, c, l, s, t);
//			gl.glMultiTexCoord2fARB(GL.GL_TEXTURE0_ARB, data[5 * i],
//					data[5 * i + 1]);
//			gl.glMultiTexCoord2fARB(GL.GL_TEXTURE1_ARB, data[5 * i] + c[0],
//					data[5 * i + 1] + c[1]);
//			gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
//		}
//
//		// Top Face
//		n[0] = 0.0f;
//		n[1] = 1.0f;
//		n[2] = 0.0f;
//		s[0] = 1.0f;
//		s[1] = 0.0f;
//		s[2] = 0.0f;
//		t[0] = 0.0f;
//		t[1] = 0.0f;
//		t[2] = -1.0f;
//
//		for (i = 8; i < 12; i++) {
//			c[0] = data[5 * i + 2];
//			c[1] = data[5 * i + 3];
//			c[2] = data[5 * i + 4];
//			SetUpBumps(n, c, l, s, t);
//			gl.glMultiTexCoord2fARB(GL.GL_TEXTURE0_ARB, data[5 * i],
//					data[5 * i + 1]);
//			gl.glMultiTexCoord2fARB(GL.GL_TEXTURE1_ARB, data[5 * i] + c[0],
//					data[5 * i + 1] + c[1]);
//			gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
//		}
//
//		// Bottom Face
//		n[0] = 0f;
//		n[1] = -1f;
//		n[2] = 0f;
//		s[0] = -1f;
//		s[1] = 0f;
//		s[2] = 0f;
//		t[0] = 0f;
//		t[1] = 0f;
//		t[2] = -1f;
//
//		for (i = 12; i < 16; i++) {
//			c[0] = data[5 * i + 2];
//			c[1] = data[5 * i + 3];
//			c[2] = data[5 * i + 4];
//			SetUpBumps(n, c, l, s, t);
//			gl.glMultiTexCoord2fARB(GL.GL_TEXTURE0_ARB, data[5 * i],
//					data[5 * i + 1]);
//			gl.glMultiTexCoord2fARB(GL.GL_TEXTURE1_ARB, data[5 * i] + c[0],
//					data[5 * i + 1] + c[1]);
//			gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
//		}
//
//		// Right Face
//		n[0] = 1.0f;
//		n[1] = 0.0f;
//		n[2] = 0.0f;
//		s[0] = 0.0f;
//		s[1] = 0.0f;
//		s[2] = -1.0f;
//		t[0] = 0.0f;
//		t[1] = 1.0f;
//		t[2] = 0.0f;
//
//		for (i = 16; i < 20; i++) {
//			c[0] = data[5 * i + 2];
//			c[1] = data[5 * i + 3];
//			c[2] = data[5 * i + 4];
//			SetUpBumps(n, c, l, s, t);
//			gl.glMultiTexCoord2fARB(GL.GL_TEXTURE0_ARB, data[5 * i],
//					data[5 * i + 1]);
//			gl.glMultiTexCoord2fARB(GL.GL_TEXTURE1_ARB, data[5 * i] + c[0],
//					data[5 * i + 1] + c[1]);
//			gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
//		}
//
//		// Left Face
//		n[0] = -1.0f;
//		n[1] = 0.0f;
//		n[2] = 0.0f;
//		s[0] = 0.0f;
//		s[1] = 0.0f;
//		s[2] = 1.0f;
//		t[0] = 0.0f;
//		t[1] = 1.0f;
//		t[2] = 0.0f;
//
//		for (i = 20; i < 24; i++) {
//			c[0] = data[5 * i + 2];
//			c[1] = data[5 * i + 3];
//			c[2] = data[5 * i + 4];
//			SetUpBumps(n, c, l, s, t);
//			gl.glMultiTexCoord2fARB(GL.GL_TEXTURE0_ARB, data[5 * i],
//					data[5 * i + 1]);
//			gl.glMultiTexCoord2fARB(GL.GL_TEXTURE1_ARB, data[5 * i] + c[0],
//					data[5 * i + 1] + c[1]);
//			gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
//		}
//		gl.glEnd();
//
//		/*
//		 * PASS#2 Use Texture "Base" Blend GL_DST_COLOR To GL_SRC_COLOR
//		 * (Multiplies By 2) Lighting Enabled No Offset Texture-Coordinates
//		 */
//
//		gl.glActiveTextureARB(GL.GL_TEXTURE1_ARB);
//		gl.glDisable(GL.GL_TEXTURE_2D);
//		gl.glActiveTextureARB(GL.GL_TEXTURE0_ARB);
//
//		if (!emboss) {
//			gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE,
//					GL.GL_MODULATE);
//			gl.glBindTexture(GL.GL_TEXTURE_2D, texture[filter]);
//			gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_SRC_COLOR);
//			gl.glEnable(GL.GL_BLEND);
//			gl.glEnable(GL.GL_LIGHTING);
//			doCube();
//		}
//
//		xrot += xspeed;
//		yrot += yspeed;
//		if (xrot > 360.0f)
//			xrot -= 360.0f;
//		if (xrot < 0.0f)
//			xrot += 360.0f;
//		if (yrot > 360.0f)
//			yrot -= 360.0f;
//		if (yrot < 0.0f)
//			yrot += 360.0f;
//
//		/* LAST PASS: Do The Logos! */
//		doLogo();
//	}
//
//	void doMeshNoBumps() {
//
//		gl.glLoadIdentity(); // Reset The View
//		gl.glTranslatef(0.0f, 0.0f, z);
//
//		gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
//		gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
//
//		gl.glActiveTextureARB(GL.GL_TEXTURE1_ARB);
//		gl.glDisable(GL.GL_TEXTURE_2D);
//		gl.glActiveTextureARB(GL.GL_TEXTURE0_ARB);
//
//		gl.glDisable(GL.GL_BLEND);
//		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[filter]);
//		gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_SRC_COLOR);
//		gl.glEnable(GL.GL_LIGHTING);
//		doCube();
//
//		xrot += xspeed;
//		yrot += yspeed;
//		if (xrot > 360.0f)
//			xrot -= 360.0f;
//		if (xrot < 0.0f)
//			xrot += 360.0f;
//		if (yrot > 360.0f)
//			yrot -= 360.0f;
//		if (yrot < 0.0f)
//			yrot += 360.0f;
//
//		/* LAST PASS: Do The Logos! */
//		doLogo();
//	}
//
//	public void processKeyboard() {
//		if (keys[KeyEvent.VK_PAGE_UP])
//			z -= 0.02f;
//
//		if (keys[KeyEvent.VK_PAGE_DOWN])
//			z += 0.02f;
//
//		if (keys[KeyEvent.VK_LEFT])
//			yspeed -= 0.01f;
//
//		if (keys[KeyEvent.VK_RIGHT])
//			yspeed += 0.01f;
//
//		if (keys[KeyEvent.VK_UP])
//			xspeed -= 0.01f;
//
//		if (keys[KeyEvent.VK_DOWN])
//			xspeed += 0.01f;
//	}
//
//	public void keyReleased(KeyEvent evt) {
//		keys[evt.getKeyCode()] = false;
//	}
//
//	public void keyPressed(KeyEvent evt) {
//
//		keys[evt.getKeyCode()] = true;
//
//		if (keys[KeyEvent.VK_ESCAPE]) {
//			loop.stop();
//			System.exit(0);
//		}
//
//		if (keys[KeyEvent.VK_M])
//			useMultitexture = ((!useMultitexture) && multitextureSupported);
//
//		if (keys[KeyEvent.VK_L])
//			light = !light;
//
//		if (keys[KeyEvent.VK_F]) {
//			filter += 1;
//			if (filter > 2)
//				filter = 0;
//		}
//
//		if (keys[KeyEvent.VK_B])
//			bumps = !bumps;
//
//		if (keys[KeyEvent.VK_E])
//			emboss = !emboss;
//	}
//
//	public void keyTyped(KeyEvent evt) {
//	}
//
//	public class shutDownWindow extends WindowAdapter {
//		public void windowClosing(WindowEvent e) {
//			loop.stop();
//		}
//	}
//}
