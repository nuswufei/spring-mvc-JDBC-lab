package edu.sjsu.cmpe275.lab3.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import edu.sjsu.cmpe275.lab3.Opponents;
import edu.sjsu.cmpe275.lab3.Player;
import edu.sjsu.cmpe275.lab3.dao.OpponentsDAO;
import edu.sjsu.cmpe275.lab3.dao.rowmapper.OpponentsRowMapper;
import edu.sjsu.cmpe275.lab3.dao.rowmapper.SponsorRowMapper;
public class OpponentsDAOImpl implements OpponentsDAO{
	private JdbcTemplate jdbcTemplate;
	private PlayerDAOImpl playerDao;
	public PlayerDAOImpl getPlayerDao() {
		return playerDao;
	}

	public void setPlayerDao(PlayerDAOImpl playerDao) {
		this.playerDao = playerDao;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Integer insert(long id1, long id2) { // -1 404error 0 already exist, 1 inserted
		Player p1 = playerDao.findByID(id1);
		Player p2 = playerDao.findByID(id2);
		if(p1.getId() == 0 || p2.getId() == 0) return -1;
		if(!findbyID(id1, id2)){
			String sql = "INSERT IGNORE INTO opponents " +
					"(firstplayerID, secondplayerID) "
					+ "VALUES (?, ?)";
			try {
				jdbcTemplate.update(sql, new Object[]{id1, id2});
				jdbcTemplate.update(sql, new Object[]{id2, id1});
			} catch(Exception e){}
			return 1;
		}
		return 0;
	}

	@Override
	public boolean delete(long id1, long id2) {
		if(!findbyID(id1, id2)) return false;
		try {
			String sql = "DELETE FROM opponents WHERE firstplayerID  = ? AND secondplayerID = ?";
			jdbcTemplate.update(sql, new Object[]{id1, id2});
			jdbcTemplate.update(sql, new Object[]{id2, id1});
		} catch(Exception e){}
		return true;
	}

	@Override
	public boolean findbyID(long id1, long id2) {
		String sql = "SELECT * FROM opponents WHERE firstplayerID  = ? AND secondplayerID = ?";
		Opponents opponents = new Opponents();
		try {
			opponents = (Opponents) jdbcTemplate.queryForObject(
					sql, new Object[] { id1, id2}, new OpponentsRowMapper());
		}
		catch(Exception e){}
		return opponents.getPlay1() != 0;
	}

	@Override
	public List<Opponents> findList(long id) {
		String sql = "SELECT * FROM opponents WHERE firstplayerID  = ?";
		List<Opponents> queryResult = new ArrayList<Opponents>();
		try {
			List<Map<String,Object>> res =jdbcTemplate.queryForList(sql, new Object[]{ id });
			for(Map m : res) {
				Opponents opponents = new Opponents();
				opponents.setPlay1((Integer) m.get("firstplayerID"));
				opponents.setPlay2((Integer) m.get("secondplayerID"));
				queryResult.add(opponents);
			}
		} catch(Exception e){}
		return queryResult;
	}

	@Override
	public void deleteList(long id) {
		String sql = "DELETE FROM opponents WHERE firstplayerID  = ? OR secondplayerID = ?";
		try {
			jdbcTemplate.update(sql, new Object[]{id, id});
		} catch(Exception e){}
	}

}
