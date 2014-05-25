package nehe.lesson24;

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
 � Program   :Nehe's 24th lesson port to JOGL                     �
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
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.ImageUtil;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import com.trungnd10.utils.General;

public class Lesson24 implements KeyListener

{

	initRenderer renderer;
	GLAutoDrawable glDrawable;
	GLCanvas canvas;
	Animator loop;
	JFrame frame;
	GLU glu;
	GL gl;

	Texture myTexture;

	int base, // Base Display List For The Font
			scroll, // Used For Scrolling The Screen
			swidth, // Scissor Width
			sheight, // Scissor Height
			maxtokens, // Keeps Track Of The Number Of Extensions Supported
			xLocation, yLocation, screenWidth, screenHeight,
			canvasHeight,
			canvasWidth;

	class TextureImage { // Create A Structure
		byte imageData[]; // Image Data (Up To 32 Bits)
		int bpp, // Image Color Depth In Bits Per Pixel.
				width, // Image Width
				height, // Image Height
				texID[] = new int[1]; // Texture ID Used To Select A Texture
	}

	boolean keys[] = new boolean[256]; // Array Used For The Keyboard Routine
	TextureImage textures[] = new TextureImage[1]; // Storage For One Texture

	boolean LoadTGA(TextureImage texture, String filename) { // Loads A TGA File
																// Into Memory

		byte TGAcompare[] = new byte[12], // Used To Compare TGA Header
		TGAheader[] = { 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // Uncompressed
																// TGA Header
		header[] = new byte[6], // First 6 Useful Bytes From The Header
		temp; // Temporary Variable

		int bytesPerPixel, // Holds Number Of Bytes Per Pixel Used In The TGA
							// File
		imageSize, // Used To Store The Image Size When Setting Aside Ram
		type = GL.GL_RGBA; // Set The Default GL Mode To RBGA (32 BPP)

		FileInputStream file = null;

		try {
			file = new FileInputStream(filename);
			file.read(TGAcompare, 0, TGAcompare.length);
			file.read(header, 0, header.length);
		} catch (IOException e) {
			try {
				file.close();
			} catch (IOException n) {
			}
			return false;
		} catch (NullPointerException e) {
			try {
				file.close();
			} catch (IOException n) {
			}
			return false;
		}

		for (int i = 0; i < TGAcompare.length; i++)
			// Does The Header Match What We Want?
			if (TGAcompare[i] != TGAheader[i]) {
				try {
					file.close();
				} catch (IOException e) {
				}
				return false;
			}

		texture.width = header[1] << 8 | header[0]; // Determine The TGA
													// Width(highbyte*256+lowbyte)
		texture.height = header[3] << 8 | header[2]; // Determine The TGA
														// Height(highbyte*256+lowbyte)

		if (texture.width <= 0 || // Is The Width Less Than Or Equal To Zero
				texture.height <= 0 || // Is The Height Less Than Or Equal To
										// Zero
				(header[4] != 24 && header[4] != 32)) { // Is The TGA 24 or 32
														// Bit?

			try {
				file.close();
			} catch (IOException n) {
			}
			return false; // Return False
		}

		texture.bpp = header[4]; // Grab The TGA's Bits Per Pixel (24 or 32)
		bytesPerPixel = texture.bpp / 8; // Divide By 8 To Get The Bytes Per
											// Pixel
		imageSize = texture.width * texture.height * bytesPerPixel; // Calculate
																	// The
																	// Memory
																	// Required
																	// For The
																	// TGA Data

		texture.imageData = new byte[imageSize]; // Reserve Memory To Hold The
													// TGA Data

		try {
			file.read(texture.imageData, 0, imageSize);
		} catch (IOException e) {
			try {
				file.close();
			} catch (IOException n) {
			}
			return false; // Return False
		}

		for (int i = 0; i < imageSize; i += bytesPerPixel) { // Loop Through The
																// Image Data
																// Swaps The 1st
																// And 3rd Bytes
																// ('R'ed and
																// 'B'lue)
			temp = texture.imageData[i]; // Temporarily Store The Value At Image
											// Data 'i'
			texture.imageData[i + 0] = texture.imageData[i + 2]; // Set The 1st
																	// Byte To
																	// The Value
																	// Of The
																	// 3rd Byte
			texture.imageData[i + 2] = temp; // Set The 3rd Byte To The Value In
												// 'temp' (1st Byte Value)
		}

		try {
			file.close();
		} catch (IOException n) {
		}

		// myTexture.enable();
		// myTexture.bind();

		// Build A Texture From The Data
		// gl.glGenTextures(1, texture.texID); // Generate OpenGL texture IDs
		// gl.glBindTexture(GL.GL_TEXTURE_2D, texture.texID[0]); // Bind Our
		// // Texture
		// gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
		// GL.GL_LINEAR); // Linear Filtered
		// gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
		// GL.GL_LINEAR); // Linear Filtered

		if (texture.bpp == 24) // Was The TGA 24 Bits
			type = GL.GL_RGB; // If So Set The 'type' To GL_RGB

		// gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, type, texture.width,
		// texture.height, 0, type, GL.GL_UNSIGNED_BYTE, texture.imageData);

		// myTexture.disable();

		return true; // Texture Building Went Ok, Return True
	}

	void BuildFont() { // Build Our Font Display List

		base = gl.glGenLists(256); // Creating 256 Display Lists

		myTexture.enable();
		myTexture.bind();

		// gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0].texID[0]); // Select
		// Our
		// // Font
		// // Texture
		for (int loop1 = 0; loop1 < 256; loop1++) { // Loop Through All 256
													// Lists

			float cx = (float) (loop1 % 16) / 16.0f; // X Position Of Current
														// Character
			float cy = (float) (loop1 / 16) / 16.0f; // Y Position Of Current
														// Character

			gl.glNewList(base + loop1, GL.GL_COMPILE); // Start Building A List
			gl.glBegin(GL.GL_QUADS); // Use A Quad For Each Character
			gl.glTexCoord2f(cx, 1.0f - cy - 0.0625f); // Texture Coord (Bottom
														// Left)
			gl.glVertex2d(0, 16); // Vertex Coord (Bottom Left)
			gl.glTexCoord2f(cx + 0.0625f, 1.0f - cy - 0.0625f); // Texture Coord
																// (Bottom
																// Right)
			gl.glVertex2i(16, 16); // Vertex Coord (Bottom Right)
			gl.glTexCoord2f(cx + 0.0625f, 1.0f - cy - 0.001f); // Texture Coord
																// (Top Right)
			gl.glVertex2i(16, 0); // Vertex Coord (Top Right)
			gl.glTexCoord2f(cx, 1.0f - cy - 0.001f); // Texture Coord (Top Left)
			gl.glVertex2i(0, 0); // Vertex Coord (Top Left)
			gl.glEnd(); // Done Building Our Quad (Character)
			gl.glTranslated(14, 0, 0); // Move To The Right Of The Character
			gl.glEndList(); // Done Building The Display List
		} // Loop Until All 256 Are Built

		// myTexture.disable();
	}

	void KillFont() { // Delete The Font From Memory
		gl.glDeleteLists(base, 256); // Delete All 256 Display Lists
	}

	void glPrint(int x, int y, int set, String fmt) { // Where The Printing
														// Happens

		if (fmt == null) // If There's No Text
			return;

		byte text[] = fmt.getBytes();

		if (set > 1) // Did User Choose An Invalid Character Set?
			set = 1; // If So, Select Set 1 (Italic)

		gl.glEnable(GL.GL_TEXTURE_2D); // Enable Texture Mapping
		gl.glLoadIdentity(); // Reset The Modelview Matrix
		gl.glTranslated(x, y, 0); // Position The Text (0,0 - Top Left)
		gl.glListBase(base - 32 + (128 * set)); // Choose The Font Set (0 or 1)
		gl.glScalef(1.0f, 2.0f, 1.0f); // Make The Text 2X Taller

		ByteBuffer bb = ByteBuffer.allocateDirect(text.length);
		bb.put(text);
		// gl.glCallLists(text.length, GL.GL_UNSIGNED_BYTE, bb); // Write The
		bb.rewind();
		gl.glCallLists(text.length, GL.GL_UNSIGNED_BYTE, bb); // Write The
																// Text To The
																// Screen

		gl.glDisable(GL.GL_TEXTURE_2D); // Disable Texture Mapping
	}

	public static void main(String[] args) {
		Lesson24 demo = new Lesson24();
	}

	Lesson24() {
		int fullScreen = JOptionPane.showConfirmDialog(null,
				"Would you like to run in fullscreen mode?", "Fullscreen",
				JOptionPane.YES_NO_OPTION);
		if (fullScreen != 0)
			JFrame.setDefaultLookAndFeelDecorated(true);

		frame = new JFrame(
				"NeHe's Token, Extensions, Scissoring & TGA Loading Tutorial");
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
					"src/main/java/nehe/lesson24/data/icon.png").getImage());
		}

		canvas = new GLCanvas();// GLDrawableFactory.getFactory().createGLCanvas(new
								// GLCapabilities());
		canvas.setSize(new Dimension(canvasWidth, canvasHeight));
		canvas.addGLEventListener((renderer = new initRenderer()));
		canvas.requestFocus();
		canvas.addKeyListener(this);

		frame.addKeyListener(this);
		frame.addWindowListener(new shutDownWindow());
		frame.getContentPane().add(canvas, BorderLayout.CENTER);

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
			try {
				String resourceName = "nehe/lesson24/data/font.bmp";
				URL url = General.getResource(resourceName);
				if (url == null) {
					throw new RuntimeException("Error reading resources");
				}
				BufferedImage bi = ImageIO.read(new File(url.getFile()));
				ImageUtil.flipImageVertically(bi);
				myTexture = TextureIO.newTexture(bi, true);
			} catch (IOException e) {
				e.printStackTrace();
			}

			gl = drawable.getGL();
			glu = new GLU();// drawable.getGLU();
			glDrawable = drawable;
			drawable.setGL(new DebugGL(drawable.getGL()));

			if (!LoadTGA((textures[0] = new TextureImage()),
					"src/main/java/nehe/lesson24/data/font.tga")) { // Load
				// The
				// Font
				// Texture
				JOptionPane.showMessageDialog(null, "Unable to load Font.tga!", // If
																				// Loading
																				// Failed,
																				// Exit
						"Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}

			BuildFont(); // Build The Font

			gl.glShadeModel(GL.GL_SMOOTH); // Enable Smooth Shading
			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
			gl.glClearDepth(1.0f); // Depth Buffer Setup
			// gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0].texID[0]); //
			// Select
			// // Our
			// // Font
			// // Texture

			myTexture.enable();
			myTexture.bind();

			loop = new Animator(glDrawable);
			loop.start();
		}

		public void display(GLAutoDrawable drawable) {
			// Clear Color Buffer, Depth Buffer, Stencil Buffer
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT
					| GL.GL_STENCIL_BUFFER_BIT);

			int cnt = 0; // Local Counter Variable

			gl.glColor3f(1.0f, 0.5f, 0.5f); // Set Color To Bright Red
			glPrint(50, 16, 1, "Renderer"); // Display Renderer
			glPrint(80, 48, 1, "Vendor"); // Display Vendor Name
			glPrint(66, 80, 1, "Version"); // Display Version

			gl.glColor3f(1.0f, 0.7f, 0.4f); // Set Color To Orange
			glPrint(200, 16, 1, gl.glGetString(GL.GL_RENDERER)); // Display
																	// Renderer
			glPrint(200, 48, 1, gl.glGetString(GL.GL_VENDOR)); // Display Vendor
																// Name
			glPrint(200, 80, 1, gl.glGetString(GL.GL_VERSION)); // Display
																// Version

			gl.glColor3f(0.5f, 0.5f, 1.0f); // Set Color To Bright Blue
			glPrint(192, 432, 1, "NeHe Productions"); // Write NeHe Productions
														// At The Bottom Of The
														// Screen

			gl.glLoadIdentity(); // Reset The ModelView Matrix
			gl.glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To White
			gl.glBegin(GL.GL_LINE_STRIP); // Start Drawing Line Strips
											// (Something New)
			gl.glVertex2d(639, 417); // Top Right Of Bottom Box
			gl.glVertex2d(0, 417); // Top Left Of Bottom Box
			gl.glVertex2d(0, 480); // Lower Left Of Bottom Box
			gl.glVertex2d(639, 480); // Lower Right Of Bottom Box
			gl.glVertex2d(639, 128); // Up To Bottom Right Of Top Box
			gl.glEnd(); // Done First Line Strip
			gl.glBegin(GL.GL_LINE_STRIP); // Start Drawing Another Line Strip
			gl.glVertex2d(0, 128); // Bottom Left Of Top Box
			gl.glVertex2d(639, 128); // Bottom Right Of Top Box
			gl.glVertex2d(639, 1); // Top Right Of Top Box
			gl.glVertex2d(0, 1); // Top Left Of Top Box
			gl.glVertex2d(0, 417); // Down To Top Left Of Bottom Box
			gl.glEnd(); // Done Second Line Strip

			gl.glScissor(1, (int) (0.135416f * sheight), swidth - 2,
					(int) (0.597916f * sheight)); // Define Scissor Region
			gl.glEnable(GL.GL_SCISSOR_TEST); // Enable Scissor Testing

			String text = gl.glGetString(GL.GL_EXTENSIONS); // Allocate Memory
															// For Our Extension
															// String
			StringTokenizer tokenizer = new StringTokenizer(text);

			while (tokenizer.hasMoreTokens()) { // While The Token Isn't NULL
				cnt++; // Increase The Counter
				if (cnt > maxtokens) { // Is 'maxtokens' Less Than 'cnt'
					maxtokens = cnt; // If So, Set 'maxtokens' Equal To 'cnt'
				}

				gl.glColor3f(0.5f, 1.0f, 0.5f); // Set Color To Bright Green
				glPrint(0, 96 + (cnt * 32) - scroll, 0, "" + cnt); // Print
																	// Current
																	// Extension
																	// Number

				gl.glColor3f(1.0f, 1.0f, 0.5f); // Set Color To Yellow
				String s = tokenizer.nextToken();
				glPrint(50, 96 + (cnt * 32) - scroll, 0, s); // Print
																// The
																// Current
																// Token
																// (Parsed
																// Extension
																// Name)
			}
			gl.glDisable(GL.GL_SCISSOR_TEST); // Disable Scissor Testing
			gl.glFlush(); // Flush The Rendering Pipeline
			processKeyboard();

			// try {
			// Thread.sleep(50);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}

		public void reshape(GLAutoDrawable drawable, int xstart, int ystart,
				int width, int height) {

			height = (height == 0) ? 1 : height;
			swidth = width; // Set Scissor Width To Window Width
			sheight = height; // Set Scissor Height To Window Height

			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL.GL_PROJECTION); // Select The Projection Matrix
			gl.glLoadIdentity(); // Reset The Projection Matrix
			gl.glOrtho(0.0f, 640, 480, 0.0f, -1.0f, 1.0f); // Create Ortho
															// 640x480 View (0,0
															// At Top Left)
			gl.glMatrixMode(GL.GL_MODELVIEW); // Select The Modelview Matrix
			gl.glLoadIdentity();
		}

		public void displayChanged(GLAutoDrawable drawable,
				boolean modeChanged, boolean deviceChanged) {
		}
	}

	public void processKeyboard() {

		if (keys[KeyEvent.VK_UP] && (scroll > 0)) // Is Up Arrow Being Pressed?
			scroll -= 2; // If So, Decrease 'scroll' Moving Screen Down
		if (keys[KeyEvent.VK_DOWN] && (scroll < 32 * (maxtokens - 9))) // Is
																		// Down
																		// Arrow
																		// Being
																		// Pressed?
			scroll += 2; // If So, Increase 'scroll' Moving Screen Up

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
