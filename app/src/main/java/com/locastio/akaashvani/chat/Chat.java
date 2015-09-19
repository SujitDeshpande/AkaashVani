package com.locastio.akaashvani.chat;

/**
 * @author ketan
 * @since 6/21/13
 */
public class Chat {

    private String message;
    private String author;
    private long date;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Chat() {
    }

    public Chat(String message, String author, long date) {
        this.message = message;
        this.author = author;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public long getDate() {
        return date;
    }
}
