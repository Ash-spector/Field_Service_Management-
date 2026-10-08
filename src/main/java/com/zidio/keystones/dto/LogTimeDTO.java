package com.zidio.keystones.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class LogTimeDTO {
    @NotNull(message = "Minutes worked is required")
    @Min(value = 1, message = "Minutes must be at least 1")
    private Integer minutes;

    private String note;

    public LogTimeDTO() {}

    public LogTimeDTO(Integer minutes, String note) {
        this.minutes = minutes;
        this.note = note;
    }

    public Integer getMinutes() { return minutes; }
    public void setMinutes(Integer minutes) { this.minutes = minutes; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
