package edu.sjsu.cmpe275.lab3.dao.impl;

import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import edu.sjsu.cmpe275.lab3.Player;
import edu.sjsu.cmpe275.lab3.Sponsor;
import edu.sjsu.cmpe275.lab3.dao.SponsorDAO;
import edu.sjsu.cmpe275.lab3.dao.rowmapper.PlayerRowMapper;
import edu.sjsu.cmpe275.lab3.dao.rowmapper.SponsorRowMapper;

public class SponsorDAOImpl implements SponsorDAO{
	private JdbcTemplate jdbcTemplate;
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Sponsor insert(final Sponsor sponsor) {
		final String sql = "INSERT IGNORE INTO sponsor " +
				"(name, description, street, city, state, zip) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int row = 0;
		try {
	        row= this.jdbcTemplate.update(new PreparedStatementCreator(){
	            @Override
				public java.sql.PreparedStatement createPreparedStatement(
						java.sql.Connection con) throws SQLException {
	            	
	            	java.sql.PreparedStatement ps =con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	            	
	                ps.setString(1, sponsor.getName());
	                ps.setString(2, sponsor.getDescription());
	                ps.setString(3, sponsor.getAddress().getStreet());
	                ps.setString(4, sponsor.getAddress().getCity());
	                ps.setString(5, sponsor.getAddress().getState());
	                ps.setString(6, sponsor.getAddress().getZip());
	                return ps;
				}
	        }, keyHolder);
		}catch(Exception e) {}
		if(row > 0) return findByID((long) keyHolder.getKey());
		else return new Sponsor();
	}

	@Override
	public Sponsor findByID(long id) {
		String sql = "SELECT * FROM sponsor WHERE id = ?";
		Sponsor sponsor = new Sponsor();
		try {
			sponsor = (Sponsor) jdbcTemplate.queryForObject(
					sql, new Object[] { id }, new SponsorRowMapper());
		}
		catch(Exception e){}
		return sponsor;
	}

	@Override
	public Sponsor deleteByID(long id) {
		Sponsor sponsor = findByID(id);
		try {
			String sql = "DELETE FROM sponsor WHERE id = ?";
			jdbcTemplate.update(sql, new Object[]{id});
		} catch(Exception e){}
		return sponsor;
	}

	@Override
	public Sponsor update(Sponsor sponsor) {
		Sponsor currentSponsor = findByID(sponsor.getId());
		if(currentSponsor.getId() == 0) {
			return currentSponsor;
		}
		else {
			try {
				String sql = "UPDATE player SET name = ?, description = ?, street = ?, city = ?, state = ?, zip = ?, WHERE id = ?";
				jdbcTemplate.update(sql, new Object[]{sponsor.getName(), sponsor.getDescription(),sponsor.getAddress().getStreet(),
						sponsor.getAddress().getCity(), sponsor.getAddress().getState(),
						sponsor.getAddress().getZip()});
				sponsor = findByID(sponsor.getId());
			}catch(Exception e){}
		}
		return sponsor;
	}

}
