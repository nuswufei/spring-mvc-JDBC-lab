package edu.sjsu.cmpe275.lab3.dao;

import java.util.List;

import edu.sjsu.cmpe275.lab3.Opponents;
import edu.sjsu.cmpe275.lab3.Player;

public interface PlayerDAO {
	public Player insert(Player player);
	public Player findByID(long id);
	public Player deleteByID(long id);
	public Player update(Player player);
	public int findBySponsor(long id);
}
