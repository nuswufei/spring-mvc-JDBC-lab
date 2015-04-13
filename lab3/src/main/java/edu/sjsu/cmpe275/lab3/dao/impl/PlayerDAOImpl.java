package edu.sjsu.cmpe275.lab3.dao.impl;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.*;

import com.mysql.jdbc.PreparedStatement;

import edu.sjsu.cmpe275.lab3.Opponents;
import edu.sjsu.cmpe275.lab3.Player;
import edu.sjsu.cmpe275.lab3.Sponsor;
import edu.sjsu.cmpe275.lab3.dao.PlayerDAO;
import edu.sjsu.cmpe275.lab3.dao.rowmapper.PlayerRowMapper;

public class PlayerDAOImpl implements PlayerDAO{
	private JdbcTemplate jdbcTemplate;
	private SponsorDAOImpl sponsorDAO;
	
	public SponsorDAOImpl getSponsorDAO() {
		return sponsorDAO;
	}

	public void setSponsorDAO(SponsorDAOImpl sponsorDAO) {
		this.sponsorDAO = sponsorDAO;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public Player insert(final Player player) {
		if(player.getSponsorID() != 0) {
			Sponsor sponsor = sponsorDAO.findByID(player.getSponsorID());
			if(sponsor.getId() == 0) return new Player();
		}
		final String sql = "INSERT IGNORE INTO player " +
				"(firstname, lastname, email, description, street, city, state, zip, sponsorID) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int row = 0;
		try {
			row= this.jdbcTemplate.update(new PreparedStatementCreator(){
	            @Override
				public java.sql.PreparedStatement createPreparedStatement(
						java.sql.Connection con) throws SQLException {
	            	
	            	java.sql.PreparedStatement ps =con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	            	
	                ps.setString(1, player.getFirstname());
	                ps.setString(2, player.getLastname());
	                ps.setString(3, player.getEmail());
	                ps.setString(4, player.getDescription());
	                ps.setString(5, player.getAddress().getStreet());
	                ps.setString(6, player.getAddress().getCity());
	                ps.setString(7, player.getAddress().getState());
	                ps.setString(8, player.getAddress().getZip());
	                ps.setLong(9, player.getSponsorID());
	                return ps;
				}
	        }, keyHolder);
		}catch(Exception e){}
		if(row > 0) return findByID((long) keyHolder.getKey());
		else return new Player();
		
	}

	@Override
	public Player findByID(long id) {
		String sql = "SELECT * FROM player WHERE id = ?";
		Player player = new Player();
		try {
			player = (Player) jdbcTemplate.queryForObject(
					sql, new Object[] { id }, new PlayerRowMapper());
		}
		catch(Exception e){}
		return player;
	}

	@Override
	public Player deleteByID(long id) {
		Player player = findByID(id);
		try {
			String sql = "DELETE FROM player WHERE id = ?";
			jdbcTemplate.update(sql, new Object[]{id});
		} catch(Exception e){}
		return player;
	}

	@Override
	public Player update(Player player) {
		Player updatedPlayer = new Player();
		if(player.getSponsorID() != 0) {
			Sponsor sponsor = sponsorDAO.findByID(player.getSponsorID());
			if(sponsor.getId() == 0) return new Player();
		}
		else {
			try {
				String sql = "UPDATE player SET firstname = ?, lastname = ?, email = ?, description = ?, street = ?, city = ?, state = ?, zip = ?, sponsorID = ? WHERE id = ?";
				jdbcTemplate.update(sql, new Object[]{player.getFirstname(), player.getLastname(),
						player.getEmail(), player.getDescription(), player.getAddress().getStreet(),
						player.getAddress().getCity(), player.getAddress().getState(),
						player.getAddress().getZip(), player.getSponsorID(), player.getId()});
				updatedPlayer = findByID(player.getId());
			}catch(Exception e){}
		}
		return updatedPlayer;
	}

	@Override
	public int findBySponsor(long id) {
		String sql = "SELECT * FROM player WHERE sponsorID  = ?";
		int count = 0;
		try {
			List<Map<String,Object>> res =jdbcTemplate.queryForList(sql, new Object[]{ id });
			count = res.size();
		} catch(Exception e){}
		return count;
	}
	

}
