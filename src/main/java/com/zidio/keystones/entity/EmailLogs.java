package com.zidio.keystones.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_logs")
public class EmailLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientMail;
    private String subject;

    @Column(length = 2000)
    private String body;

    private boolean sentStatus;
    private LocalDateTime sentAt = LocalDateTime.now();

    public EmailLogs() {}

    public EmailLogs(Long id, String recipientMail, String subject, String body, boolean sentStatus, LocalDateTime sentAt) {
        this.id = id;
        this.recipientMail = recipientMail;
        this.subject = subject;
        this.body = body;
        this.sentStatus = sentStatus;
        this.sentAt = sentAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRecipientMail() { return recipientMail; }
    public void setRecipientMail(String recipientMail) { this.recipientMail = recipientMail; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public boolean isSentStatus() { return sentStatus; }
    public void setSentStatus(boolean sentStatus) { this.sentStatus = sentStatus; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String recipientMail;
        private String subject;
        private String body;
        private boolean sentStatus;
        private LocalDateTime sentAt = LocalDateTime.now();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder recipientMail(String recipientMail) { this.recipientMail = recipientMail; return this; }
        public Builder subject(String subject) { this.subject = subject; return this; }
        public Builder body(String body) { this.body = body; return this; }
        public Builder sentStatus(boolean sentStatus) { this.sentStatus = sentStatus; return this; }
        public Builder sentAt(LocalDateTime sentAt) { this.sentAt = sentAt; return this; }

        public EmailLogs build() {
            return new EmailLogs(id, recipientMail, subject, body, sentStatus, sentAt);
        }
    }
}
