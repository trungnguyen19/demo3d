package com.mycompany.test.foreignkeycompositekey.kruders.model.bean;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TeamId implements java.io.Serializable {

	private int teamid;
	private int clubid;

	public TeamId() {
	}

	public TeamId(int teamid, int clubid) {
		this.teamid = teamid;
		this.clubid = clubid;
	}
	
	
	@Column(name = "teamid", nullable = false)
	public int getTeamid() {
		return this.teamid;
	}

	public void setTeamid(int teamid) {
		this.teamid = teamid;
	}

	@Column(name = "clubid", nullable = false)
	public int getClubid() {
		return this.clubid;
	}

	public void setClubid(int clubid) {
		this.clubid = clubid;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TeamId))
			return false;
		TeamId castOther = (TeamId) other;

		return (this.getTeamid() == castOther.getTeamid())
				&& (this.getClubid() == castOther.getClubid());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getTeamid();
		result = 37 * result + this.getClubid();
		return result;
	}

}
