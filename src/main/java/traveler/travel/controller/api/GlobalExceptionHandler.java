package traveler.travel.controller.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import traveler.travel.exception.EmailDuplicateException;
import traveler.travel.exception.ErrorCode;
import traveler.travel.exception.ErrorResponse;

@ControllerAdvice
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailDuplicateException.class)
    public ResponseEntity<ErrorResponse>handleEmailDuplicateException(EmailDuplicateException ex){
      log.error("handleEmailDuplicateException", ex);
      ErrorResponse response = new ErrorResponse(ex.getErrorcode());
      return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorcode().getStatus()));
    }
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleArgumentException(Exception ex){
        log.error("handleException", ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
