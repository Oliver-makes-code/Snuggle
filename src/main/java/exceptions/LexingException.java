package exceptions;

import lexing.Loc;

public class LexingException extends CompilationException {
    public LexingException(String message, Loc loc) {
        super(message, loc);
    }
}
