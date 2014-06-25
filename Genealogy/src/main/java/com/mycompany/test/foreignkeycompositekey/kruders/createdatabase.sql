CREATE TABLE club (
  clubId INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  PRIMARY KEY (ClubId)
);
   
CREATE TABLE  team (
  teamid INT(10) UNSIGNED NOT NULL,
  clubid INT(10) UNSIGNED NOT NULL,
  teamname VARCHAR(10) NOT NULL,
  PRIMARY KEY (teamid, clubid),
  FOREIGN KEY (clubid) REFERENCES club (clubid));
  
--truncate table club;
--truncate table team;