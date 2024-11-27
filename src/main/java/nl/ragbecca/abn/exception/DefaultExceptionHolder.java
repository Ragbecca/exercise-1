package nl.ragbecca.abn.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DefaultExceptionHolder {
    private List<DefaultExceptionFormat> errors;
}
