package com.ynov.groupe1.entities;

public class Tweet {
	
	private String tweetAuthor;
	private String tweetText;
	private String tweetDate;
	private String tweetAlias;
	private String tweetAvatar;
	
	public Tweet(String tweetAuthor, String tweetText, String tweetDate, String tweetAlias, String tweetAvatar)
	{
		this.tweetAuthor = tweetAuthor;
		this.tweetText = tweetText;
		this.tweetDate = tweetDate;
		this.tweetAlias = tweetAlias;
		this.tweetAvatar = tweetAvatar;
	}
	
	public String getTweetAuthor() {
		return tweetAuthor;
	}

	public void setTweetAuthor(String tweetAuthor) {
		this.tweetAuthor = tweetAuthor;
	}

	public String getTweetText() {
		return tweetText;
	}

	public void setTweetText(String tweetText) {
		this.tweetText = tweetText;
	}

	public String getTweetDate() {
		return tweetDate;
	}

	public void setTweetDate(String tweetDate) {
		this.tweetDate = tweetDate;
	}

	public String getTweetAlias() {
		return tweetAlias;
	}

	public void setTweetAlias(String tweetAlias) {
		this.tweetAlias = tweetAlias;
	}

	public String getTweetAvatar() {
		return tweetAvatar;
	}

	public void setTweetAvatar(String tweetAvatar) {
		this.tweetAvatar = tweetAvatar;
	}

}
