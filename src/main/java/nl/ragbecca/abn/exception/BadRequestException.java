package nl.ragbecca.abn.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    private final String code;

    public BadRequestException(String message, String code) {
        super(message);
        this.code = code;
    }
}
