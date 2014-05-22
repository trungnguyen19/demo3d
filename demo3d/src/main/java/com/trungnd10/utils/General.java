package com.trungnd10.utils;

import java.net.URL;

public class General {
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

}
