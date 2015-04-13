package edu.sjsu.cmpe275.lab3.dao.rowmapper;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.sjsu.cmpe275.lab3.*;
public class SponsorRowMapper implements RowMapper<Sponsor>{

	@Override
	public Sponsor mapRow(ResultSet rs, int rowNum) throws SQLException {
		Sponsor sponsor = new Sponsor();
		Address address = new Address();
		sponsor.setId(rs.getLong("id"));
		sponsor.setName(rs.getString("name"));
		sponsor.setDescription(rs.getString("description"));
		address.setCity(rs.getString("city"));
		address.setState(rs.getString("state"));
		address.setStreet(rs.getString("street"));
		address.setZip(rs.getString("zip"));
		sponsor.setAddress(address);
		return sponsor;
	}

}
