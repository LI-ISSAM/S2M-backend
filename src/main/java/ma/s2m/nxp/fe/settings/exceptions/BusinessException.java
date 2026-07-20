package ma.s2m.nxp.fe.settings.exceptions;

import org.springframework.http.HttpStatus;

public class BusinessException extends Exception {

    private final HttpStatus status;
    private final String code;

    public BusinessException(String code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}