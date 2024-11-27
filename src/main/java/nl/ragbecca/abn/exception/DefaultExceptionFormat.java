package nl.ragbecca.abn.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DefaultExceptionFormat {
    private String code;
    private String message;
    private int status;
}
