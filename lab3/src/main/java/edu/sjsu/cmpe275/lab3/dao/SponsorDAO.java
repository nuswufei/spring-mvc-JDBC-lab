package edu.sjsu.cmpe275.lab3.dao;

import edu.sjsu.cmpe275.lab3.Player;
import edu.sjsu.cmpe275.lab3.Sponsor;

public interface SponsorDAO {
	public Sponsor insert(Sponsor sponsor);
	public Sponsor findByID(long id);
	public Sponsor deleteByID(long id);
	public Sponsor update(Sponsor sponsor);
}
