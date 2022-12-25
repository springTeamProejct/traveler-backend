package traveler.travel.global.exception;

import lombok.Data;

@Data
public class ErrorResponse {

    private int status;
    private String code;
    private String message;

    public ErrorResponse(ErrorCode errorCode){
        this.status = errorCode.getStatus();
        this.code = errorCode.getErrorCode();
        this.message = errorCode.getMessage();
    }
}
