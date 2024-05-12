package exeception;

import java.io.IOException;

public class CompressException extends RuntimeException {
    public CompressException(String str, Exception e) {
        super(str,e);
    }
}
