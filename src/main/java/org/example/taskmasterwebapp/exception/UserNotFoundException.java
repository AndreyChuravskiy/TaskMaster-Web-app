package org.example.taskmasterwebapp.exception;

public class UserNotFoundException extends  RuntimeException{
    public UserNotFoundException(String message) {super(message); }
}
