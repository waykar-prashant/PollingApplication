package com.api.v1;

public class URIConstant {
	private static final String ACTIVE_API_VERSION = "v1";
	
	//1
	public static final String CREATE_MODERATOR = "api/"+ ACTIVE_API_VERSION +"/moderators";
	
	//2
	public static final String VIEW_MODERATOR = "api/"+ ACTIVE_API_VERSION +"/moderators/{moderator_id}";
	
	//3
	public static final String UPDATE_MODERATOR = "api/"+ ACTIVE_API_VERSION +"/moderators/{moderator_id}";
	
	//4
	public static final String CREATE_POLL = "api/"+ ACTIVE_API_VERSION +"/moderators/{moderator_id}/polls";
	
	//5
	public static final String VIEW_POLL_WITHOUT_RESULT = "api/"+ ACTIVE_API_VERSION +"/polls/{poll_id}";
	
	//6
	public static final String VIEW_POLL_WITH_RESULT = "api/"+ ACTIVE_API_VERSION +"/moderators/{moderator_id}/polls/{poll_id}";
	
	//7
	public static final String VIEW_ALL_POLL = "api/"+ ACTIVE_API_VERSION +"/moderators/{moderator_id}/polls";
	
	//8
	public static final String DELETE_POLL = "api/"+ ACTIVE_API_VERSION +"/moderators/{moderator_id}/polls/{poll_id}";
	
	//9
	public static final String VOTE_POLL = "api/"+ ACTIVE_API_VERSION +"/polls/{poll_id}?choice={choice_index}";

	

	
}
