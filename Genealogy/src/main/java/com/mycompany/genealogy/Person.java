package com.mycompany.genealogy;

import java.util.Vector;

public class Person implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4979985511209369244L;

	Integer personId;

	Person father;
	Person mother;

	private String name;

	Vector<Person> children;

	public Person() {
	}

	public Person(String name) {
		this.name = name;
		mother = father = null;
		children = new Vector<Person>();
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public Vector<Person> getChildren() {
		return children;
	}

	public void setChildren(Vector<Person> children) {
		this.children = children;
	}

	public void setFather(Person father) {
		this.father = father;
	}

	public void setMother(Person mother) {
		this.mother = mother;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Link together all members of a family.
	 * 
	 * @param pa
	 *            the father
	 * @param ma
	 *            the mother
	 * @param kids
	 *            the children
	 */
	public static void linkFamily(Person pa, Person ma, Person[] kids) {
		int len = kids.length;
		Person kid = null;
		for (int i = 0; i < len; i++) {
			kid = kids[i];
			pa.children.addElement(kid);
			ma.children.addElement(kid);
			kid.father = pa;
			kid.mother = ma;
		}
	}

	// / getter methods ///////////////////////////////////

	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public Person getFather() {
		return father;
	}

	public Person getMother() {
		return mother;
	}

	public int getChildCount() {
		return children.size();
	}

	public Person getChildAt(int i) {
		return (Person) children.elementAt(i);
	}

	public int getIndexOfChild(Person kid) {
		return children.indexOf(kid);
	}

}
