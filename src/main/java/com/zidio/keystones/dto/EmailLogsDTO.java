package com.zidio.keystones.dto;

public class EmailLogsDTO {
    private String recipientMail;
    private String subject;
    private String body;
    private boolean sentStatus;

    public EmailLogsDTO() {}

    public EmailLogsDTO(String recipientMail, String subject, String body, boolean sentStatus) {
        this.recipientMail = recipientMail;
        this.subject = subject;
        this.body = body;
        this.sentStatus = sentStatus;
    }

    public String getRecipientMail() { return recipientMail; }
    public void setRecipientMail(String recipientMail) { this.recipientMail = recipientMail; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public boolean isSentStatus() { return sentStatus; }
    public void setSentStatus(boolean sentStatus) { this.sentStatus = sentStatus; }
}
