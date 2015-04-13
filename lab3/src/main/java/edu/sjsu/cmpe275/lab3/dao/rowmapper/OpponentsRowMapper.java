package edu.sjsu.cmpe275.lab3.dao.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.sjsu.cmpe275.lab3.Address;
import edu.sjsu.cmpe275.lab3.Opponents;
import edu.sjsu.cmpe275.lab3.Sponsor;

public class OpponentsRowMapper implements RowMapper<Opponents>{
	@Override
	public Opponents mapRow(ResultSet rs, int rowNum) throws SQLException {
		Opponents opponents = new Opponents();
		Address address = new Address();
		opponents.setPlay1((Integer)rs.getInt("firstplayerID "));
		opponents.setPlay2((Integer)rs.getInt("secondplayerID"));
		return opponents;
	}

}
