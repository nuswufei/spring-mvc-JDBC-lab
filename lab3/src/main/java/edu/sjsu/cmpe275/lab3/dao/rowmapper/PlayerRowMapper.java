package edu.sjsu.cmpe275.lab3.dao.rowmapper;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.sjsu.cmpe275.lab3.*;

public class PlayerRowMapper implements RowMapper<Player>{

	@Override
	public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
		Player player = new Player();
		Address address = new Address();
		player.setId(rs.getLong("id"));
		player.setFirstname(rs.getString("firstname"));
		player.setLastname(rs.getString("lastname"));
		player.setEmail(rs.getString("email"));
		player.setSponsorID(rs.getInt("sponsorID"));
		address.setCity(rs.getString("city"));
		address.setState(rs.getString("state"));
		address.setStreet(rs.getString("street"));
		address.setZip(rs.getString("zip"));
		player.setAddress(address);
		return player;
	}

}
