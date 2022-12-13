package traveler.travel.exception;

import lombok.Data;

@Data
public class EmailDuplicateException extends RuntimeException{

    private ErrorCode errorcode;

    public EmailDuplicateException(String message, ErrorCode errorCode){
        super(message);
        this.errorcode = errorCode;
    }
}
