package com.mycompany.test.foreignkeycompositekey.kruders.model.bean;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mycompany.test.foreignkeycompositekey.kruders.MyClass;

@Entity
@Table(name = "team", catalog = MyClass.dbName)
public class Team implements java.io.Serializable {

	private TeamId id;
	private Club club;
	private String teamname;

	public Team() {
	}

	public Team(TeamId id, Club club, String teamname) {
		this.id = id;
		this.club = club;
		this.teamname = teamname;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "teamid", column = @Column(name = "teamid", nullable = false)),
			@AttributeOverride(name = "clubid", column = @Column(name = "clubid", nullable = false)) })
	public TeamId getId() {
		return this.id;
	}

	public void setId(TeamId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clubid", nullable = false, insertable = false, updatable = false)
	public Club getClub() {
		return this.club;
	}

	public void setClub(Club club) {
		this.club = club;
	}

	@Column(name = "teamname", nullable = false, length = 10)
	public String getTeamname() {
		return this.teamname;
	}

	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

}
