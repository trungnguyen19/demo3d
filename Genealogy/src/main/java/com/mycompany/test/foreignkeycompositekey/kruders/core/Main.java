package com.mycompany.test.foreignkeycompositekey.kruders.core;

import org.hibernate.Session;

import com.mycompany.test.foreignkeycompositekey.kruders.model.bean.Club;
import com.mycompany.test.foreignkeycompositekey.kruders.model.bean.Team;
import com.mycompany.test.foreignkeycompositekey.kruders.model.bean.TeamId;
import com.mycompany.test.foreignkeycompositekey.kruders.util.HibernateUtil;

public class Main {
	public static void main(String[] args) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		session.beginTransaction();

		Club club = new Club();
		club.setClubId(1);
		club.setName("Arsenal");

		TeamId teamId = new TeamId();
		teamId.setTeamid(1);
		teamId.setClubid(club.getClubId());

		Team team = new Team();
		team.setId(teamId);
		team.setClub(club);

		team.setTeamname("Team A");

		club.getTeams().add(team);
		session.save(team);
		session.save(club);
		session.getTransaction().commit();
	}
}
