package edu.sjsu.cmpe275.lab3;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;



import edu.sjsu.cmpe275.lab3.dao.impl.OpponentsDAOImpl;
import edu.sjsu.cmpe275.lab3.dao.impl.PlayerDAOImpl;
import edu.sjsu.cmpe275.lab3.dao.impl.SponsorDAOImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * Created by WU on 14/3/2015.
 */
@Controller
@RequestMapping("/")
public class MvcController {
	@ResponseBody
    @RequestMapping(value="player", method = RequestMethod.POST)
    public ResponseEntity<String> insertPlayer(Model model, @RequestParam(value = "firstname", required = false) String first,
    		@RequestParam(value = "lastname", required = false) String last,
    		@RequestParam(value = "email", required = false) String email,
    		@RequestParam(value = "description", required = false) String des,
    		@RequestParam(value = "sponsor", required = false) Integer sponsorID,
    		@RequestParam(value = "street", required = false) String street,
    		@RequestParam(value = "zip", required = false) String zip,
    		@RequestParam(value = "state", required = false) String state,
    		@RequestParam(value = "city", required = false) String city) {
    	ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    	PlayerDAOImpl playerDAO = (PlayerDAOImpl) context.getBean("playerDAO");
    	Player player = new Player();
    	Address playerAddress = new Address();
    	try {player.setFirstname(first);} catch(Exception e){}
    	try {player.setLastname(last);} catch(Exception e){}
    	try {player.setEmail(email);} catch(Exception e){}
    	try {player.setSponsorID(sponsorID);} catch(Exception e){}
    	try {player.setDescription(des);} catch(Exception e){}
    	try {playerAddress.setCity(city);} catch(Exception e){}
    	try {playerAddress.setState(state);} catch(Exception e){}
    	try {playerAddress.setStreet(street);} catch(Exception e){}
    	try {playerAddress.setZip(zip);} catch(Exception e){}
	    player.setAddress(playerAddress);
    	
    	Player playerInTable  = playerDAO.insert(player);
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

    	if(playerInTable.getId() != 0) {
	    	try {
	            ObjectMapper objMapper = new ObjectMapper();
	            String jsonString = objMapper.writeValueAsString(playerInTable);
	            return new ResponseEntity<String>(jsonString, headers, HttpStatus.OK);
	        }
	        catch (JsonMappingException e) {
	            e.printStackTrace();
	        } catch (JsonGenerationException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
    	}
    	return new ResponseEntity<String>("Unkown Error happened in Server", headers, HttpStatus.BAD_REQUEST);
    }
	
	@ResponseBody
    @RequestMapping(value = "player/{user}", method = RequestMethod.GET)
    public ResponseEntity<String> getPlayer(@PathVariable("user") Long user) {
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    	PlayerDAOImpl playerDAO = (PlayerDAOImpl) context.getBean("playerDAO");
    	OpponentsDAOImpl opponentsDAO = (OpponentsDAOImpl) context.getBean("opponentsDAO");
		SponsorDAOImpl sponsorDAO = (SponsorDAOImpl) context.getBean("sponsorDAO");
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

    	Player player = playerDAO.findByID(user);
    	if(player.getId() == 0) {
    		String temp = "USER DO NOT EXIST: ";
            temp += user;
            return new ResponseEntity<String>(temp, headers, HttpStatus.NOT_FOUND);
    	}
    	try {
             ObjectMapper objMapper = new ObjectMapper();
             String jsonString = objMapper.writeValueAsString(player);
             if(player.getSponsorID() != 0) {
            	 Sponsor sponsor = sponsorDAO.findByID(player.getSponsorID());
            	 jsonString += objMapper.writeValueAsString(sponsor);
             }
             List<Opponents> opponentsList = opponentsDAO.findList(player.getId());
             if(opponentsList.size()!= 0) {
            	 jsonString += objMapper.writeValueAsString(opponentsList);
             }
             return new ResponseEntity<String>(jsonString, headers, HttpStatus.OK);
         }
         catch (JsonMappingException e) {
             e.printStackTrace();
         } catch (JsonGenerationException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
    	return null;
	}
	
	@ResponseBody
    @RequestMapping(value="player/{user}", method = RequestMethod.POST)
    public ResponseEntity<String> updatePlayer(@PathVariable("user") Long user, @RequestParam(value = "firstname", required = false) String first,
    		@RequestParam(value = "lastname", required = false) String last,
    		@RequestParam(value = "email", required = false) String email,
    		@RequestParam(value = "description", required = false) String des,
    		@RequestParam(value = "sponsor", required = false) Integer sponsorID,
    		@RequestParam(value = "street", required = false) String street,
    		@RequestParam(value = "zip", required = false) String zip,
    		@RequestParam(value = "state", required = false) String state,
    		@RequestParam(value = "city", required = false) String city) {
    	ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    	PlayerDAOImpl playerDAO = (PlayerDAOImpl) context.getBean("playerDAO");
    	HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    Player currentPlayer = new Player();
    	try {
    		currentPlayer = playerDAO.findByID(user);
    	}catch(Exception e){}
    	if(currentPlayer.getId() == 0) {
    		String temp = "USER DO NOT EXIST: ";
            temp += user;
            return new ResponseEntity<String>(temp, headers, HttpStatus.NOT_FOUND);
    	}
    	
    	Player player = new Player();
    	Address playerAddress = new Address();
    	player.setId(user);
    	try {player.setFirstname(first);} catch(Exception e){}
    	try {player.setLastname(last);} catch(Exception e){}
    	try {player.setEmail(email);} catch(Exception e){}
    	try {player.setSponsorID(sponsorID);} catch(Exception e){}
    	try {player.setDescription(des);} catch(Exception e){}
    	try {playerAddress.setCity(city);} catch(Exception e){}
    	try {playerAddress.setState(state);} catch(Exception e){}
    	try {playerAddress.setStreet(street);} catch(Exception e){}
    	try {playerAddress.setZip(zip);} catch(Exception e){}
	    player.setAddress(playerAddress);
    	
    	Player playerInTable  = playerDAO.update(player);

    	if(playerInTable.getId() != 0) {
	    	try {
	            ObjectMapper objMapper = new ObjectMapper();
	            String jsonString = objMapper.writeValueAsString(playerInTable);
	            return new ResponseEntity<String>(jsonString, headers, HttpStatus.OK);
	        }
	        catch (JsonMappingException e) {
	            e.printStackTrace();
	        } catch (JsonGenerationException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
    	}
    	return new ResponseEntity<String>("Unkown Error happened in Server", headers, HttpStatus.BAD_REQUEST);
    }
	
	
	@ResponseBody
    @RequestMapping(value = "player/{user}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deletePlayer(@PathVariable("user") Long user) {
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    	PlayerDAOImpl playerDAO = (PlayerDAOImpl) context.getBean("playerDAO");
    	OpponentsDAOImpl opponentsDAO = (OpponentsDAOImpl) context.getBean("opponentsDAO");
    	
    	HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    Player player = new Player();
	    try {
	    	player = playerDAO.findByID(user);
	    } catch(Exception e){}
	    if(player.getId() == 0) {
	    	String temp = "USER DO NOT EXIST: ";
            temp += user;
            return new ResponseEntity<String>(temp, headers, HttpStatus.NOT_FOUND);
	    }
	    try {
            ObjectMapper objMapper = new ObjectMapper();
            player = playerDAO.deleteByID(player.getId());
            opponentsDAO.deleteList(player.getSponsorID());
            String jsonString = objMapper.writeValueAsString(player);
            return new ResponseEntity<String>(jsonString, headers, HttpStatus.OK);
        }
        catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	    return null;
	}
	
	@ResponseBody
    @RequestMapping(value="sponsor", method = RequestMethod.POST)
    public ResponseEntity<String> insertSponsor(Model model, @RequestParam(value = "name", required = false) String name,
    		@RequestParam(value = "description", required = false) String des,
    		@RequestParam(value = "street", required = false) String street,
    		@RequestParam(value = "zip", required = false) String zip,
    		@RequestParam(value = "state", required = false) String state,
    		@RequestParam(value = "city", required = false) String city) {
    	ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    	SponsorDAOImpl sponsorDAO = (SponsorDAOImpl) context.getBean("sponsorDAO");
    	Sponsor sponsor = new Sponsor();
    	Address sponsorAddress = new Address();
    	try {sponsor.setName(name);} catch(Exception e){}
    	try {sponsor.setDescription(des);} catch(Exception e){}
    	try {sponsorAddress.setCity(city);} catch(Exception e){}
    	try {sponsorAddress.setState(state);} catch(Exception e){}
    	try {sponsorAddress.setStreet(street);} catch(Exception e){}
    	try {sponsorAddress.setZip(zip);} catch(Exception e){}
    	sponsor.setAddress(sponsorAddress);
    	
    	Sponsor sponsorInTable  = sponsorDAO.insert(sponsor);
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

    	if(sponsorInTable.getId() != 0) {
	    	try {
	            ObjectMapper objMapper = new ObjectMapper();
	            String jsonString = objMapper.writeValueAsString(sponsorInTable);
	            return new ResponseEntity<String>(jsonString, headers, HttpStatus.OK);
	        }
	        catch (JsonMappingException e) {
	            e.printStackTrace();
	        } catch (JsonGenerationException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
    	}
    	return new ResponseEntity<String>("Unkown Error happened in Server", headers, HttpStatus.BAD_REQUEST);
    }
	
	@ResponseBody
    @RequestMapping(value = "sponsor/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getSponsor(@PathVariable("id") Long id) {
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		SponsorDAOImpl sponsorDAO = (SponsorDAOImpl) context.getBean("sponsorDAO");
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    Sponsor sponsor = sponsorDAO.findByID(id);
    	if(sponsor.getId() == 0) {
    		String temp = "USER DO NOT EXIST: ";
            temp += id;
            return new ResponseEntity<String>(temp, headers, HttpStatus.NOT_FOUND);
    	}
    	try {
             ObjectMapper objMapper = new ObjectMapper();
             String jsonString = objMapper.writeValueAsString(sponsor);
             return new ResponseEntity<String>(jsonString, headers, HttpStatus.OK);
         }
         catch (JsonMappingException e) {
             e.printStackTrace();
         } catch (JsonGenerationException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
    	return null;
	}
	
	@ResponseBody
    @RequestMapping(value="sponsor/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> updateSponsor(@PathVariable("id") Long id, @RequestParam(value = "name", required = false) String name,
    		@RequestParam(value = "description", required = false) String des,
    		@RequestParam(value = "street", required = false) String street,
    		@RequestParam(value = "zip", required = false) String zip,
    		@RequestParam(value = "state", required = false) String state,
    		@RequestParam(value = "city", required = false) String city) {
    	ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    	SponsorDAOImpl sponsorDAO = (SponsorDAOImpl) context.getBean("sponsorDAO");
    	HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    
	    Sponsor currentSponsor = new Sponsor();
    	try {
    		currentSponsor = sponsorDAO.findByID(id);
    	}catch(Exception e){}
    	if(currentSponsor.getId() == 0) {
    		String temp = "SPONSOR DO NOT EXIST: ";
            temp += id;
            return new ResponseEntity<String>(temp, headers, HttpStatus.NOT_FOUND);
    	}
    	
    	Sponsor sponsor = new Sponsor();
    	sponsor.setId(id);
    	Address sponsorAddress = new Address();
    	try {sponsor.setName(name);} catch(Exception e){}
    	try {sponsor.setDescription(des);} catch(Exception e){}
    	try {sponsorAddress.setCity(city);} catch(Exception e){}
    	try {sponsorAddress.setState(state);} catch(Exception e){}
    	try {sponsorAddress.setStreet(street);} catch(Exception e){}
    	try {sponsorAddress.setZip(zip);} catch(Exception e){}
    	sponsor.setAddress(sponsorAddress);
    	
    	Sponsor sponosorInTable  = sponsorDAO.update(sponsor);

    	if(sponosorInTable.getId() != 0) {
	    	try {
	            ObjectMapper objMapper = new ObjectMapper();
	            String jsonString = objMapper.writeValueAsString(sponosorInTable);
	            return new ResponseEntity<String>(jsonString, headers, HttpStatus.OK);
	        }
	        catch (JsonMappingException e) {
	            e.printStackTrace();
	        } catch (JsonGenerationException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
    	}
    	return new ResponseEntity<String>("Unkown Error happened in Server", headers, HttpStatus.BAD_REQUEST);
    }
	
	@ResponseBody
    @RequestMapping(value = "sponsor/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteSponsor(@PathVariable("id") Long id) {
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    	PlayerDAOImpl playerDAO = (PlayerDAOImpl) context.getBean("playerDAO");
    	SponsorDAOImpl sponsorDAO = (SponsorDAOImpl) context.getBean("sponsorDAO");
    	
    	HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    
	    Sponsor sponsor = new Sponsor();
	    try {
	    	sponsor = sponsorDAO.findByID(id);
	    } catch(Exception e){}
	    if(sponsor.getId() == 0) {
	    	String temp = "SPONSOR DO NOT EXIST: ";
            temp += id;
            return new ResponseEntity<String>(temp, headers, HttpStatus.NOT_FOUND);
	    }
	    int playerOfSponsor = playerDAO.findBySponsor(id);
	    if(playerOfSponsor > 0) {
	    	String temp = "THERE ARE STILL PLAYER UNDER THIS SPONSOR ";
            temp += id;
            return new ResponseEntity<String>(temp, headers, HttpStatus.BAD_REQUEST);
	    }
	    try {
	    	sponsorDAO.deleteByID(sponsor.getId());
            ObjectMapper objMapper = new ObjectMapper();
            String jsonString = objMapper.writeValueAsString(sponsor);
            return new ResponseEntity<String>(jsonString, headers, HttpStatus.OK);
        }
        catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	    return null;
	}
	
	@ResponseBody
    @RequestMapping(value = "opponents/{id1}/{id2}", method = RequestMethod.PUT)
    public ResponseEntity<String> addOpponents(@PathVariable("id1") Long id1, @PathVariable("id2") Long id2) {
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    	OpponentsDAOImpl opponentsDAO = (OpponentsDAOImpl) context.getBean("opponentsDAO");

    	HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    
    	int insertResult = opponentsDAO.insert(id1, id2);
    	if(insertResult == 1){
    		String temp = "THESE TWO PLAYERS ARE NOW OPPONENTS ";
            temp += (id1 + " " + id2);
            return new ResponseEntity<String>(temp, headers, HttpStatus.OK);
    	}
    	if(insertResult == 0){
    		String temp = "THESE TWO PLAYERS ARE ALREADY OPPONENTS";
            return new ResponseEntity<String>(temp, headers, HttpStatus.OK);
    	}
    	else {
    		String temp = "PLAYERS ARE NOT FOUND";
            return new ResponseEntity<String>(temp, headers, HttpStatus.NOT_FOUND);
    	}
	}
	
	@ResponseBody
    @RequestMapping(value = "opponents/{id1}/{id2}", method = RequestMethod.DELETE)
    public ResponseEntity<String> removeOpponents(@PathVariable("id1") Long id1, @PathVariable("id2") Long id2) {
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    	OpponentsDAOImpl opponentsDAO = (OpponentsDAOImpl) context.getBean("opponentsDAO");

    	HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    
	    boolean result = opponentsDAO.delete(id1, id2);
    	if(result){
    		String temp = "THESE TWO PLAYERS ARE NOW NOT OPPONENTS ";
            temp += (id1 + " " + id2);
            return new ResponseEntity<String>(temp, headers, HttpStatus.OK);
    	}
    	else {
    		String temp = "PLAYERS ARE NOT OPPONENTS";
            return new ResponseEntity<String>(temp, headers, HttpStatus.NOT_FOUND);
    	}
	}
}

