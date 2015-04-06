package com.api.v1;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class HomeController {
	
	public static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private int uniqueModeratorID = 1000;
	private int uniquePollID = 100;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
	private HashMap<Integer, Moderator> moderatorDataMap = new HashMap<Integer, Moderator>();
	private HashMap<String, Poll> pollDataMap = new HashMap<String, Poll>();
	private HashMap<Integer, ArrayList<Poll>> moderatorPollDataMap = new HashMap<Integer, ArrayList<Poll>>();
	private final SecureRandom random = new SecureRandom();

	
	private int getUniqueModeratorID(){
		return uniqueModeratorID++;
	}
	
	private String getUniquqPollID(){
		return String.valueOf(new BigInteger(30, random).toString(36).toUpperCase());
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
		moderator.setCreatedAt(getCurrentDate());
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
	public ResponseEntity<Moderator> updateModerator(@RequestBody Moderator moderatorUpdate,  @PathVariable("moderator_id") int moderatorID, HttpServletRequest request){
		logger.info("In update Moderator");
		//check if the moderatorID is there in the ModeratorDataMap
		ResponseEntity<Moderator> responseModerator = new ResponseEntity<Moderator>(HttpStatus.NO_CONTENT);
		if(moderatorID > 0){
			if(moderatorUpdate != null){
				if(moderatorDataMap.containsKey(moderatorID)){
					Moderator moderator = moderatorDataMap.get(moderatorID);
					logger.info("In update Moderator : " + moderator.getName());
					if(moderatorUpdate.getCreatedAt() != null){
						moderator.setCreatedAt(moderatorUpdate.getCreatedAt());
					}
					if(moderatorUpdate.getEmail() != null){
						moderator.setEmail(moderatorUpdate.getEmail());
					}
					if(moderatorUpdate.getName() != null){
						moderator.setName(moderatorUpdate.getName());
					}
					if(moderatorUpdate.getPassword() != null){
						logger.info("Updated Moderator Password: " + moderatorUpdate.getPassword());
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
	public ResponseEntity<PollDAO> createPoll(@RequestBody @Valid Poll poll, @PathVariable("moderator_id") int moderatorID , HttpServletRequest request){
		ResponseEntity<PollDAO> responsePoll = new ResponseEntity<PollDAO>(HttpStatus.NO_CONTENT);
		//create a poll for the existing moderator only
		if(moderatorID > 0){
			if(moderatorDataMap.containsKey(moderatorID)){
				//Moderator moderator = moderatorDataMap.get(moderatorID);
				//create a Poll object
				Poll pollNew = new Poll();
				String pollID = getUniquqPollID();
				pollNew.setId(pollID);
				pollNew.setQuestion(poll.getQuestion());
				pollNew.setStarted_at(poll.getStarted_at());
				pollNew.setExpired_at(poll.getExpired_at());
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
				PollDAO pollDAO = new PollDAO(pollNew);
				moderatorPollDataMap.put(moderatorID, moderatorPoll);
				responsePoll = new ResponseEntity<PollDAO>(pollDAO, HttpStatus.CREATED);
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
	@RequestMapping(value=URIConstant.VIEW_POLL_WITHOUT_RESULT, produces={"application/json"}, method=RequestMethod.GET)
	public ResponseEntity<PollDAO> viewPollWithoutResult(@PathVariable("poll_id") String pollID, HttpServletRequest request){
		
		ResponseEntity<PollDAO> responsePoll = new ResponseEntity<PollDAO>(HttpStatus.NO_CONTENT);
		if(pollID != null){
			//check if the poll exists in the pollDataMap
			if(pollDataMap.containsKey(pollID)){
				Poll pollTemp = new Poll();
				Poll poll = pollDataMap.get(pollID);
				
				pollTemp.setId(poll.getId());
				pollTemp.setQuestion(poll.getQuestion());
				pollTemp.setStarted_at(poll.getStarted_at());
				pollTemp.setExpired_at(poll.getExpired_at());
				pollTemp.setChoice(poll.getChoice());
				PollDAO pollDAO = new PollDAO(pollTemp);
				responsePoll = new ResponseEntity<PollDAO>(pollDAO, HttpStatus.OK);
			}
		}
		return responsePoll;
	}
	
	/**
	 * View a Poll With Result
	 * Resource: /moderators/{moderator_id}/polls/{poll_id}
	 * Description: View a poll with current result.
	 * 
	 * Request: 
	 * GET /moderators/12345/polls/1ADC2FZ
	 * 
	 * HTTP Headers:
	 * Content-type: application/json
	 * 
	 */

	@RequestMapping(value=URIConstant.VIEW_POLL_WITH_RESULT, produces={"application/json"}, method=RequestMethod.GET)
	public ResponseEntity<Poll> viewPollWithResult(@PathVariable("poll_id") String pollID, @PathVariable("moderator_id") int moderatorID ,HttpServletRequest request){
		
		ResponseEntity<Poll> responsePoll = new ResponseEntity<Poll>(HttpStatus.NO_CONTENT);
		Poll poll = null;
		if(pollID != null && moderatorID > 0){
			//check if the poll exists in the pollDataMap
			if(moderatorPollDataMap.containsKey(moderatorID)){
				
				ArrayList<Poll> pollList = moderatorPollDataMap.get(moderatorID);
				if(pollList != null && pollList.size() > 0){
					Iterator<Poll> iterator = pollList.iterator();
					while(iterator.hasNext()){
						Poll tempPoll = iterator.next();
						if(tempPoll.getId().equals(pollID)){
							poll = tempPoll;
							break;
						}
					}
					responsePoll = new ResponseEntity<Poll>(poll, HttpStatus.OK);
				}
			}
		}
		return responsePoll;
	}
	
	
	/**
	 * List All Polls
	 * Resource: /moderators/{moderator_id}/polls
	 * Description: List all polls created by the given moderator.
	 * 
	 * Request: 
	 * GET /moderators/12345/polls
	 * 
	 * HTTP Headers:
	 * Accept-type: application/json
	 * 
	 * Response:
	 * HTTP Code: 200
	 * 
	 */
	@RequestMapping(value=URIConstant.VIEW_ALL_POLL, produces={"application/json"},method=RequestMethod.GET)
	private ResponseEntity<ArrayList<Poll>> getAllModeratorPolls(@PathVariable("moderator_id") int moderatorID, HttpServletRequest request){
		ResponseEntity<ArrayList<Poll>> responsePolls = new ResponseEntity<ArrayList<Poll>>(HttpStatus.NO_CONTENT);
		//get the moderator object from the moderatorPollDataMap
		if(moderatorID > 0){
			if(moderatorPollDataMap.containsKey(moderatorID)){
				ArrayList<Poll> pollList = moderatorPollDataMap.get(moderatorID);
				if(pollList != null && pollList.size() > 0){
					responsePolls = new ResponseEntity<ArrayList<Poll>>(pollList, HttpStatus.OK);
				}
			}
		}
		return responsePolls;
	}
	
	
	/**
	 * Delete a Poll
	 * 
	 * Resource: /moderators/{moderator_id}/polls/{poll_id}
	 * Description: Delete a poll
	 * 
	 * Request: 
	 * DELETE /moderators/12345/polls/2BZE91C
	 * 
	 * Response:
	 * HTTP Code: 204
	 */
	@RequestMapping(value=URIConstant.DELETE_POLL, produces={"application/json"}, method=RequestMethod.DELETE)
	public ResponseEntity<Poll> deletePoll(@PathVariable("poll_id") String pollID, @PathVariable("moderator_id") int moderatorID ,HttpServletRequest request){
		
		ResponseEntity<Poll> responsePoll = new ResponseEntity<Poll>(HttpStatus.NO_CONTENT);
		Poll poll = null;
		if(pollID != null && moderatorID > 0){
			//check if the poll exists in the pollDataMap
			if(moderatorPollDataMap.containsKey(moderatorID)){
				
				ArrayList<Poll> pollList = moderatorPollDataMap.get(moderatorID);
				if(pollList != null && pollList.size() > 0){
					int index = 0;
					for(Poll pollTemp:pollList){
						if(pollTemp != null){
							if(pollTemp.getId().equals(pollID)){
								break;
							}else{
								index++;
							}
						}
					}
					//Remove the poll object from the pollList
					moderatorPollDataMap.get(moderatorID).remove(index);
					responsePoll = new ResponseEntity<Poll>(HttpStatus.NO_CONTENT);
				}
			}
		}
		return responsePoll;
	}
	
	
	/**
	 * Vote a Poll
	 * 
	 * Resource: /polls/{poll_id}?choice={choice_index}
	 * Description: Vote a poll
	 * 
	 * Request: 
	 * # User's choice for the poll was "Yes" which is index 0.
	 * PUT /polls/2BZE91C?choice=0     
	 * 
	 * Response:
	 * HTTP Code: 204
	 * 
	 */
	
	@RequestMapping(value=URIConstant.VOTE_POLL , method=RequestMethod.PUT)
	public @ResponseBody ResponseEntity getVote(@PathVariable("poll_id") String pollID, @RequestParam("choice") int choice){				
		System.out.println("in vote a poll function");
		//int choice = Integer.valueOf(request.getParameter("choice"));
		ResponseEntity<Poll> responsePoll = new ResponseEntity<Poll>(HttpStatus.NO_CONTENT);

		System.out.println("POLLID: " +pollID + "   " + "choice Index: " + choice);
		if(pollID != null){
			//check if the poll exists in the pollDataMap
			if(pollDataMap.containsKey(pollID)){
				Poll poll = pollDataMap.get(pollID);
				String choices[] = poll.getChoice();
				if(choice < choices.length){
					//consider it as a valid choice
					//increment the value at index for the choice
					int result = poll.getResults()[choice];
					int resultArray[] = poll.getResults();
					resultArray[choice]++;
					poll.setResults(resultArray);
					//choices[choice] = String.valueOf(result++);
					//poll.setChoice(choices);
					//update the poll object in the pollDataMap
					pollDataMap.put(pollID, poll);
					//update the poll object in the moderatorPollDataMap
					Iterator<Integer> iteratorModerator = moderatorPollDataMap.keySet().iterator();
					Integer moderatorID = null;
					int index = -1;
					boolean found = false;
					while(iteratorModerator.hasNext()){
						moderatorID = iteratorModerator.next();
						System.out.println("Moderator ID = " + moderatorID);
						ArrayList<Poll> pollList = moderatorPollDataMap.get(moderatorID);
						if(pollList != null && pollList.size() > 0){
							index = 0;
							for(Poll pollTemp:pollList){
								if(pollTemp != null){
									if(pollTemp.getId().equals(pollID)){
										found = true;
										break;
									}else{
										index++;
									}
								}
							}
						}
					}
					
					//Remove the poll object from the pollList
					if(index > -1 && found){
						Poll p = moderatorPollDataMap.get(moderatorID).get(index);
						p.setChoice(choices);
						moderatorPollDataMap.get(moderatorID).add(index, p);
					}
					responsePoll = new ResponseEntity<Poll>(poll, HttpStatus.NO_CONTENT);

				}
				
			}
		}
		return responsePoll;
	}

	
	public String getCurrentDate() {
		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		df.setTimeZone(timeZone);
		return (df.format(new Date()));
	}

	
	
	
	
}
