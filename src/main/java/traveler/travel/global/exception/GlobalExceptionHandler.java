package traveler.travel.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import traveler.travel.global.dto.ResponseDto;
import traveler.travel.global.exception.EmailDuplicateException;
import traveler.travel.global.exception.ErrorCode;
import traveler.travel.global.exception.ErrorResponse;

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

    @ExceptionHandler(BadRequestException.class)
    public ResponseDto handleBadRequestException(BadRequestException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), e.getErrorCode());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseDto handleNotFoundException(NotFoundException e) {
        return new ResponseDto<>(HttpStatus.NOT_FOUND.value(), e.getErrorCode());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseDto handleForbiddenException(ForbiddenException e) {
        return new ResponseDto<>(HttpStatus.FORBIDDEN.value(), e.getErrorCode());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleArgumentException(Exception ex) {

        log.error("handleException", ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
