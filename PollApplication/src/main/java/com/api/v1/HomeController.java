package com.api.v1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	public static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private int uniqueModeratorID = 1000;
	private int uniquePollID = 1;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
	private HashMap<Integer, Moderator> moderatorDataMap = new HashMap<Integer, Moderator>();
	private HashMap<String, Poll> pollDataMap = new HashMap<String, Poll>();
	private HashMap<Integer, ArrayList<Poll>> moderatorPollDataMap = new HashMap<Integer, ArrayList<Poll>>();
	
	private int getUniqueModeratorID(){
		return uniqueModeratorID++;
	}
	
	private String getUniquqPollID(){
		return String.valueOf(uniquePollID++);
	}
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String home(Locale locale, Model model){
		logger.info("This is the home", locale);
		System.out.println("This is the syso home");
		return "HOME";
	}
	
	/**
	 * Create Moderator
	 * Resource: /moderators
	 * Description: Add a moderator to the system.
	 * 
	 * Request: 
	 * POST /moderators (with the following payload in the request body)
	 * 
	 * HTTP Headers:
	 * Content-type: application/json
	 * 
	 * Response:
	 * HTTP Code: 201
	 * 
	 */
	@RequestMapping(value=URIConstant.CREATE_MODERATOR, produces= {"application/json"}, method = RequestMethod.POST)
	public ResponseEntity<Moderator> createModerator(@RequestBody @Valid Moderator moderator){
		logger.info("In create Moderator");
		//create a unique Moderator id
		int moderatorID = getUniqueModeratorID();
		moderator.setId(moderatorID);
		moderator.setCreatedAt(dateFormat.format(new Date()));
		moderatorDataMap.put(moderatorID, moderator);
		//create response entity and return it
		ResponseEntity<Moderator> responseModerator = new ResponseEntity<Moderator>(moderator, HttpStatus.CREATED);
		return responseModerator;
	}
	
	
/**
 * 	View Moderator
 * 	Resource: /moderators/{moderator_id}
 *  Description: View a moderator resource
 *  
 *  Request:
 *  GET /moderators/123456
 *  Accept: application/json
 *  
 *  Response:
 *  HTTP Code: 200
 *  
 */	
	@RequestMapping(value=URIConstant.VIEW_MODERATOR, produces = {"application/json"}, method = RequestMethod.GET)
	public ResponseEntity<Moderator> viewModerator(@PathVariable("moderator_id") int moderatorID, HttpServletRequest request){
		//check for authorization 
		//not implemented yet
		Moderator moderatorObject;
		ResponseEntity<Moderator> responseModerator = new ResponseEntity<Moderator>(HttpStatus.NO_CONTENT);;
		if(moderatorID > 0){
			if(moderatorDataMap != null && moderatorDataMap.size() > 0){
				//check if the value exists in the moderatorMap
				if(moderatorDataMap.containsKey(moderatorID)){
					//check if the moderator id is there in the moderatorData Map
					//get the value from the hashmap
					moderatorObject = moderatorDataMap.get(moderatorID);
					responseModerator = new ResponseEntity<Moderator>(moderatorObject, HttpStatus.OK);
				}
			}
		}		
		return responseModerator;
	}
	
	/**
	 * Update Moderator
	 * Resource: /moderators/{moderator_id}
	 * Description: Update an existing moderator information.
	 * 
	 * Request: 
	 * PUT /moderators/123456 (with the following payload in the request body)
	 * 
	 * HTTP Headers:
	 * Content-type: application/json
	 * 
	 * Response:
	 * HTTP Code: 200
	 */
	@RequestMapping(value=URIConstant.UPDATE_MODERATOR, produces = {"application/json"}, method = RequestMethod.PUT)
	public ResponseEntity<Moderator> updateModerator(@RequestBody @Valid Moderator moderatorUpdate,  @PathVariable("moderator_id") int moderatorID, HttpServletRequest request){
		//check if the moderatorID is there in the ModeratorDataMap
		ResponseEntity<Moderator> responseModerator = new ResponseEntity<Moderator>(HttpStatus.NO_CONTENT);
		if(moderatorID > 0){
			if(moderatorUpdate != null){
				if(moderatorDataMap.containsKey(moderatorID)){
					Moderator moderator = moderatorDataMap.get(moderatorID);
					if(moderatorUpdate.getCreatedAt() != null){
						moderator.setCreatedAt(moderatorUpdate.getCreatedAt());
					}else if(moderatorUpdate.getEmail() != null){
						moderator.setEmail(moderatorUpdate.getEmail());
					}else if(moderatorUpdate.getName() != null){
						moderator.setName(moderatorUpdate.getName());
					}else if(moderatorUpdate.getPassword() != null){
						moderator.setPassword(moderatorUpdate.getPassword());
					}
					//update the existing moderator object
					moderatorDataMap.put(moderator.getId(), moderator);
					responseModerator = new ResponseEntity<Moderator>(moderator, HttpStatus.OK);
				}
			}
		}
		
		return responseModerator;
	}
	
	/**
	 * Create a Poll
	 * 
	 * Resource: /moderators/{moderator_id}/polls
	 * Description: Create a new poll
	 * 
	 * Request: 
	 * POST /moderators/12345/polls (with the following payload in the request body)
	 * 
	 * HTTP Headers:
	 * Content-type: application/json
	 * 
	 * Response:
	 * HTTP Code: 201
	 */
	
	@RequestMapping(value=URIConstant.CREATE_POLL, produces={"application/json"}, method = RequestMethod.POST)
	public ResponseEntity<Poll> createPoll(@RequestBody @Valid Poll poll, @PathVariable("moderator_id") int moderatorID , HttpServletRequest request){
		ResponseEntity<Poll> responsePoll = new ResponseEntity<Poll>(HttpStatus.NO_CONTENT);
		//create a poll for the existing moderator only
		if(moderatorID > 0){
			if(moderatorDataMap.containsKey(moderatorID)){
				//Moderator moderator = moderatorDataMap.get(moderatorID);
				//create a Poll object
				Poll pollNew = new Poll();
				String pollID = getUniquqPollID();
				pollNew.setId(pollID);
				pollNew.setQuestion(poll.getQuestion());
				pollNew.setStartedAt(poll.getStartedAt());
				pollNew.setExpiredAt(poll.getExpiredAt());
				pollNew.setChoice(poll.getChoice());
				pollNew.setResults(new int[2]);
				
				//save this poll object
				pollDataMap.put(pollID, pollNew);
				
				//keep track of the moderator and the polls generated
				ArrayList<Poll> moderatorPoll = null; 
				moderatorPoll = moderatorPollDataMap.get(moderatorID);
				if(moderatorPoll == null){
					//create an ArrayList for Poll and add it to the moderatorPoll Data map
					moderatorPoll = new ArrayList<Poll>(); 
				}
				moderatorPoll.add(pollNew);
				moderatorPollDataMap.put(moderatorID, moderatorPoll);
				responsePoll = new ResponseEntity<Poll>(pollNew, HttpStatus.CREATED);
			}
		}
		
		return responsePoll;
	}
	
	
	/**
	 * View a Poll Without Result
	 * Resource: /polls/{poll_id}
	 * Description: View a poll.
	 * 
	 * Request: 
	 * GET /polls/1ADC2FZ
	 * 
	 * HTTP Headers:
	 * Content-type: application/json
	 * 
	 */

	public ResponseEntity<Poll> viewPollWithoutResult(@PathVariable("poll_id") String pollID, HttpServletRequest request){
		
		ResponseEntity<Poll> responsePoll = new ResponseEntity<Poll>(HttpStatus.NO_CONTENT);
		if(pollID != null){
			//check if the poll exists in the pollDataMap
			if(pollDataMap.containsKey(pollID)){
				Poll poll = pollDataMap.get(pollID);
				
			}
		}
		return responsePoll;
	}
	
	
	

}
