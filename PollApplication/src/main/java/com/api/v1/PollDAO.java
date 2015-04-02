package com.api.v1;

public class PollDAO {
	private String id;
	private String question;
	private String startedAt;
	private String expiredAt;
	private String[] choice;
	
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
	public String getStartedAt() {
		return startedAt;
	}
	/**
	 * @param startedAt the startedAt to set
	 */
	public void setStartedAt(String startedAt) {
		this.startedAt = startedAt;
	}
	/**
	 * @return the expiredAt
	 */
	public String getExpiredAt() {
		return expiredAt;
	}
	/**
	 * @param expiredAt the expiredAt to set
	 */
	public void setExpiredAt(String expiredAt) {
		this.expiredAt = expiredAt;
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
