package nehe.lesson27;

import java.util.StringTokenizer;
//import net.java.games.jogl.GL;
import java.io.*;

import javax.media.opengl.GL;

public class object3D {
	GL gl;

	object3D(GL gl) {
		this.gl = gl;
	}

	// vertex in 3d-coordinate system
	static class sPoint {
		float x, y, z;
	}

	// plane equation
	static class sPlaneEq {
		float a, b, c, d;
	}

	// structure describing an object's face
	static class sPlane {
		int p[] = new int[3], neigh[] = new int[3];
		sPoint normals[] = new sPoint[3];
		boolean visible;
		sPlaneEq PlaneEq;

		sPlane() {
			PlaneEq = new sPlaneEq();
			for (int i = 0; i < 3; i++)
				normals[i] = new sPoint();
		}
	}

	// object structure
	static class glObject {
		int nPlanes, nPoints;
		sPoint points[] = new sPoint[100];
		sPlane planes[] = new sPlane[100];

		glObject() {
			for (int i = 0; i < 100; i++) {
				points[i] = new sPoint();
				planes[i] = new sPlane();
			}
		}
	}

	// load object
	int ReadObject(String st, glObject o) {

		FileReader file;
		String data = "";
		int i, info;
		try {
			file = new FileReader(st);
			while ((info = file.read()) != -1)
				data += (char) info;
			file.close();
		} catch (IOException e) {
			return 0;
		}

		// points
		StringTokenizer tokenizer = new StringTokenizer(data);
		o.nPoints = Integer.parseInt(tokenizer.nextToken());

		for (i = 1; i <= o.nPoints; i++) {
			o.points[i].x = Float.parseFloat(tokenizer.nextToken());
			o.points[i].y = Float.parseFloat(tokenizer.nextToken());
			o.points[i].z = Float.parseFloat(tokenizer.nextToken());
		}

		// planes
		o.nPlanes = Integer.parseInt(tokenizer.nextToken());

		for (i = 0; i < o.nPlanes; i++) {
			o.planes[i].p[0] = Integer.parseInt(tokenizer.nextToken());
			o.planes[i].p[1] = Integer.parseInt(tokenizer.nextToken());
			o.planes[i].p[2] = Integer.parseInt(tokenizer.nextToken());

			o.planes[i].normals[0].x = Float.parseFloat(tokenizer.nextToken());
			o.planes[i].normals[0].y = Float.parseFloat(tokenizer.nextToken());
			o.planes[i].normals[0].z = Float.parseFloat(tokenizer.nextToken());
			o.planes[i].normals[1].x = Float.parseFloat(tokenizer.nextToken());
			o.planes[i].normals[1].y = Float.parseFloat(tokenizer.nextToken());
			o.planes[i].normals[1].z = Float.parseFloat(tokenizer.nextToken());
			o.planes[i].normals[2].x = Float.parseFloat(tokenizer.nextToken());
			o.planes[i].normals[2].y = Float.parseFloat(tokenizer.nextToken());
			o.planes[i].normals[2].z = Float.parseFloat(tokenizer.nextToken());
		}
		return 1;
	}

	// connectivity procedure - based on Gamasutra's article
	// hard to explain here
	void SetConnectivity(glObject o) {

		int p1i, p2i, p1j, p2j;
		int P1i, P2i, P1j, P2j;
		int i, j, ki, kj;

		for (i = 0; i < o.nPlanes - 1; i++)
			for (j = i + 1; j < o.nPlanes; j++)
				for (ki = 0; ki < 3; ki++)
					if (o.planes[i].neigh[ki] == 0) {
						for (kj = 0; kj < 3; kj++) {
							p1i = ki;
							p1j = kj;
							p2i = (ki + 1) % 3;
							p2j = (kj + 1) % 3;

							p1i = o.planes[i].p[p1i];
							p2i = o.planes[i].p[p2i];
							p1j = o.planes[j].p[p1j];
							p2j = o.planes[j].p[p2j];

							P1i = ((p1i + p2i) - abs(p1i - p2i)) / 2;
							P2i = ((p1i + p2i) + abs(p1i - p2i)) / 2;
							P1j = ((p1j + p2j) - abs(p1j - p2j)) / 2;
							P2j = ((p1j + p2j) + abs(p1j - p2j)) / 2;

							if ((P1i == P1j) && (P2i == P2j)) { // they are
																// neighbours
								o.planes[i].neigh[ki] = j + 1;
								o.planes[j].neigh[kj] = i + 1;
							}
						}
					}
	}

	int abs(int integer) {
		return (integer > 0) ? integer : -integer;
	}

	// function for computing a plane equation given 3 points
	void CalcPlane(glObject o, sPlane plane) {

		sPoint v[] = new sPoint[4];

		int i;

		for (i = 0; i < 3; i++) {
			v[i + 1] = new sPoint();
			v[i + 1].x = o.points[plane.p[i]].x;
			v[i + 1].y = o.points[plane.p[i]].y;
			v[i + 1].z = o.points[plane.p[i]].z;
		}
		plane.PlaneEq.a = v[1].y * (v[2].z - v[3].z) + v[2].y
				* (v[3].z - v[1].z) + v[3].y * (v[1].z - v[2].z);
		plane.PlaneEq.b = v[1].z * (v[2].x - v[3].x) + v[2].z
				* (v[3].x - v[1].x) + v[3].z * (v[1].x - v[2].x);
		plane.PlaneEq.c = v[1].x * (v[2].y - v[3].y) + v[2].x
				* (v[3].y - v[1].y) + v[3].x * (v[1].y - v[2].y);
		plane.PlaneEq.d = -(v[1].x * (v[2].y * v[3].z - v[3].y * v[2].z)
				+ v[2].x * (v[3].y * v[1].z - v[1].y * v[3].z) + v[3].x
				* (v[1].y * v[2].z - v[2].y * v[1].z));
	}

	// procedure for drawing the object - very simple
	void DrawGLObject(glObject o) {
		int i, j;

		gl.glBegin(gl.GL_TRIANGLES);
		for (i = 0; i < o.nPlanes; i++) {
			for (j = 0; j < 3; j++) {
				gl.glNormal3f(o.planes[i].normals[j].x,
						o.planes[i].normals[j].y, o.planes[i].normals[j].z);
				gl.glVertex3f(o.points[o.planes[i].p[j]].x,
						o.points[o.planes[i].p[j]].y,
						o.points[o.planes[i].p[j]].z);
			}
		}
		gl.glEnd();
	}

	void CastShadow(glObject o, float[] lp) {
		int i, j, k, jj;
		int p1, p2;
		sPoint v1 = new sPoint(), v2 = new sPoint();
		float side;

		// set visual parameter
		for (i = 0; i < o.nPlanes; i++) {
			// chech to see if light is in front or behind the plane (face
			// plane)
			side = o.planes[i].PlaneEq.a * lp[0] + o.planes[i].PlaneEq.b
					* lp[1] + o.planes[i].PlaneEq.c * lp[2]
					+ o.planes[i].PlaneEq.d * lp[3];
			if (side > 0)
				o.planes[i].visible = true;
			else
				o.planes[i].visible = false;
		}

		gl.glDisable(gl.GL_LIGHTING);
		gl.glDepthMask(false);
		gl.glDepthFunc(gl.GL_LEQUAL);

		gl.glEnable(gl.GL_STENCIL_TEST);
		gl.glColorMask(false, false, false, false);
		gl.glStencilFunc(gl.GL_ALWAYS, 1, 0xffffffff);

		// first pass, stencil operation decreases stencil value
		gl.glFrontFace(gl.GL_CCW);
		gl.glStencilOp(gl.GL_KEEP, gl.GL_KEEP, gl.GL_INCR);

		for (i = 0; i < o.nPlanes; i++) {
			if (o.planes[i].visible)
				for (j = 0; j < 3; j++) {
					k = o.planes[i].neigh[j];
					if ((k == 0) || (!o.planes[k - 1].visible)) {
						// here we have an edge, we must draw a polygon
						p1 = o.planes[i].p[j];
						jj = (j + 1) % 3;
						p2 = o.planes[i].p[jj];

						// calculate the length of the vector
						v1.x = (o.points[p1].x - lp[0]) * 100;
						v1.y = (o.points[p1].y - lp[1]) * 100;
						v1.z = (o.points[p1].z - lp[2]) * 100;

						v2.x = (o.points[p2].x - lp[0]) * 100;
						v2.y = (o.points[p2].y - lp[1]) * 100;
						v2.z = (o.points[p2].z - lp[2]) * 100;

						// draw the polygon
						gl.glBegin(gl.GL_TRIANGLE_STRIP);
						gl.glVertex3f(o.points[p1].x, o.points[p1].y,
								o.points[p1].z);
						gl.glVertex3f(o.points[p1].x + v1.x, o.points[p1].y
								+ v1.y, o.points[p1].z + v1.z);

						gl.glVertex3f(o.points[p2].x, o.points[p2].y,
								o.points[p2].z);
						gl.glVertex3f(o.points[p2].x + v2.x, o.points[p2].y
								+ v2.y, o.points[p2].z + v2.z);
						gl.glEnd();
					}
				}
		}

		// second pass, stencil operation increases stencil value
		gl.glFrontFace(gl.GL_CW);
		gl.glStencilOp(gl.GL_KEEP, gl.GL_KEEP, gl.GL_DECR);

		for (i = 0; i < o.nPlanes; i++) {
			if (o.planes[i].visible)
				for (j = 0; j < 3; j++) {
					k = o.planes[i].neigh[j];
					if ((k == 0) || (!o.planes[k - 1].visible)) {
						// here we have an edge, we must draw a polygon
						p1 = o.planes[i].p[j];
						jj = (j + 1) % 3;
						p2 = o.planes[i].p[jj];

						// calculate the length of the vector
						v1.x = (o.points[p1].x - lp[0]) * 100;
						v1.y = (o.points[p1].y - lp[1]) * 100;
						v1.z = (o.points[p1].z - lp[2]) * 100;

						v2.x = (o.points[p2].x - lp[0]) * 100;
						v2.y = (o.points[p2].y - lp[1]) * 100;
						v2.z = (o.points[p2].z - lp[2]) * 100;

						// draw the polygon
						gl.glBegin(gl.GL_TRIANGLE_STRIP);
						gl.glVertex3f(o.points[p1].x, o.points[p1].y,
								o.points[p1].z);
						gl.glVertex3f(o.points[p1].x + v1.x, o.points[p1].y
								+ v1.y, o.points[p1].z + v1.z);

						gl.glVertex3f(o.points[p2].x, o.points[p2].y,
								o.points[p2].z);
						gl.glVertex3f(o.points[p2].x + v2.x, o.points[p2].y
								+ v2.y, o.points[p2].z + v2.z);
						gl.glEnd();
					}
				}
		}

		gl.glFrontFace(gl.GL_CCW);
		gl.glColorMask(true, true, true, true);

		// draw a shadowing rectangle covering the entire screen
		gl.glColor4f(0.0f, 0.0f, 0.0f, 0.4f);
		gl.glEnable(gl.GL_BLEND);
		gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
		gl.glStencilFunc(gl.GL_NOTEQUAL, 0, 0xffffffff);
		gl.glStencilOp(gl.GL_KEEP, gl.GL_KEEP, gl.GL_KEEP);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glBegin(gl.GL_TRIANGLE_STRIP);
		gl.glVertex3f(-0.1f, 0.1f, -0.10f);
		gl.glVertex3f(-0.1f, -0.1f, -0.10f);
		gl.glVertex3f(0.1f, 0.1f, -0.10f);
		gl.glVertex3f(0.1f, -0.1f, -0.10f);
		gl.glEnd();
		gl.glPopMatrix();
		gl.glDisable(gl.GL_BLEND);

		gl.glDepthFunc(gl.GL_LEQUAL);
		gl.glDepthMask(true);
		gl.glEnable(gl.GL_LIGHTING);
		gl.glDisable(gl.GL_STENCIL_TEST);
		gl.glShadeModel(gl.GL_SMOOTH);
	}
}