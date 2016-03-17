package com.ynov.groupe1.projettwitter.classes;

/**
 * Created by Paul on 14/03/2016.
 */

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

    public String getTweetText() {
        return tweetText;
    }

    public String getTweetDate() {
        return tweetDate;
    }

    public String getTweetAlias() {
        return tweetAlias;
    }

    public String getTweetAvatar() {
        return tweetAvatar;
    }

}
