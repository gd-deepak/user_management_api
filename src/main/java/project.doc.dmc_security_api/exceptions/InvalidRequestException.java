package project.doc.dmc_security_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends Throwable {
    public InvalidRequestException(){
    }

    public InvalidRequestException(String message){
        super(message);
    }

    public InvalidRequestException(String message, Throwable cause){
        super(message, cause);
    }

    public InvalidRequestException(Throwable cause){
        super(cause);
    }
}
