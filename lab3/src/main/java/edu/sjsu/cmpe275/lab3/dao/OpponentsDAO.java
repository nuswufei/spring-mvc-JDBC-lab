package edu.sjsu.cmpe275.lab3.dao;

import java.util.List;

import edu.sjsu.cmpe275.lab3.Opponents;
import edu.sjsu.cmpe275.lab3.Player;

public interface OpponentsDAO {
	public Integer insert(long id1, long id2);
	public boolean delete(long id1, long id2);
	public boolean findbyID(long id1, long id2);
	public List<Opponents> findList(long id);
	public void deleteList(long id);
}
