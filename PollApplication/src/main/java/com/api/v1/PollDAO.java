package com.api.v1;

public class PollDAO {
	private String id;
	private String question;
	private String started_at;
	private String expired_at;
	private String[] choice;
	
	public PollDAO(Poll poll) {
		this.id = poll.getId();
		this.question = poll.getQuestion();
		this.started_at = poll.getStarted_at();
		this.expired_at = poll.getExpired_at();
		this.choice = poll.getChoice();
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}
	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	/**
	 * @return the startedAt
	 */
	public String getStarted_at() {
		return started_at;
	}
	/**
	 * @param started_at the startedAt to set
	 */
	public void setStarted_at(String started_at) {
		
		this.started_at = started_at;
	}
	/**
	 * @return the expiredAt
	 */
	public String getExpired_at() {
		return expired_at;
	}
	/**
	 * @param expiredAt the expiredAt to set
	 */
	public void setExpired_at(String expiredAt) {
		this.expired_at = expiredAt;
	}
	/**
	 * @return the choice
	 */
	public String[] getChoice() {
		return choice;
	}
	/**
	 * @param choice the choice to set
	 */
	public void setChoice(String[] choice) {
		this.choice = choice;
	}
}
