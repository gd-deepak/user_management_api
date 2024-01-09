package project.doc.dmc_security_api.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class JwtValidationException extends Exception {
    private static final long serialVersionUID = -6537307700289159134L;

    public JwtValidationException() {
    }

    public JwtValidationException(String message) {
        super(message);
    }
}

