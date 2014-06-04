package com.mycompany.store;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Constants {

	private static HashMap<String, String> translateProperties = null;

	private static void loadTranslateProperties() {
		translateProperties = new HashMap<String, String>();

		ResourceBundle rb = ResourceBundle
				.getBundle("com.mycompany.store.translate");
		Enumeration<String> keys = rb.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = rb.getString(key);
			translateProperties.put(key, value);
		}
	}

	public static String getTranslateProperties(String name) {
		if (translateProperties == null) {
			loadTranslateProperties();
		}
		return translateProperties.get(name);
	}

	public static String getEditLink(int id) {
		return "/tuesday/rest/hello/edit/id/" + id;
	}

	public static String getListLink() {
		return "/tuesday/rest/hello/list";
	}

	public static String getSubmitLink() {
		return "/tuesday/rest/hello/edit/submit";
	}
}
