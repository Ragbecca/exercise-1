package nl.ragbecca.abn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class AbnExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public DefaultExceptionHolder handleBadRequestException(BadRequestException ex) {
        return new DefaultExceptionHolder(List.of(new DefaultExceptionFormat(ex.getCode(), ex.getMessage(), HttpStatus.BAD_REQUEST.value())));
    }
}
