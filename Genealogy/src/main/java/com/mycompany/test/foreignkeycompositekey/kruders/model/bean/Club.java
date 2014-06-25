package com.mycompany.test.foreignkeycompositekey.kruders.model.bean;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.mycompany.test.foreignkeycompositekey.kruders.MyClass;

@Entity
@Table(name = "club", catalog = MyClass.dbName)
public class Club implements java.io.Serializable {

	private Integer clubId;
	private String name;
	private Set<Team> teams = new HashSet<Team>(0);

	public Club() {
	}

	public Club(String name) {
		this.name = name;
	}

	public Club(String name, Set<Team> teams) {
		this.name = name;
		this.teams = teams;
	}

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "clubId", unique = true, nullable = false)
	public Integer getClubId() {
		return this.clubId;
	}

	public void setClubId(Integer clubId) {
		this.clubId = clubId;
	}

	@Column(name = "name", nullable = false, length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "club")
	public Set<Team> getTeams() {
		return this.teams;
	}

	public void setTeams(Set<Team> teams) {
		this.teams = teams;
	}

}
