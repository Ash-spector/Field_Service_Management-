package com.zidio.keystones.dto;

public class AuthResponseDTO {
    private String token;
    private String message;
    private UserDTO user;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String token, String message) {
        this.token = token;
        this.message = message;
    }

    public AuthResponseDTO(String token, String message, UserDTO user) {
        this.token = token;
        this.message = message;
        this.user = user;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String token;
        private String message;
        private UserDTO user;

        public Builder token(String token) { this.token = token; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder user(UserDTO user) { this.user = user; return this; }

        public AuthResponseDTO build() {
            return new AuthResponseDTO(token, message, user);
        }
    }
}
