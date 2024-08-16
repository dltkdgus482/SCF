package com.scf.user.member.global.exception;

public class NotEnoughExperienceException extends BusinessException {
    public NotEnoughExperienceException(String message) {
        super(message);
    }

    public NotEnoughExperienceException(String message, Throwable cause) {
        super(message, cause);
    }
}