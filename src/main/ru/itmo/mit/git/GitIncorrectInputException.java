package ru.itmo.mit.git;

public class GitIncorrectInputException extends GitException {
    public GitIncorrectInputException() {
    }
    public GitIncorrectInputException(String message) {
        super(message);
    }

    public GitIncorrectInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public GitIncorrectInputException(Throwable cause) {
        super(cause);
    }

    public GitIncorrectInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
