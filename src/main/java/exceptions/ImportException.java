package exceptions;

import lexing.Loc;

public class ImportException extends CompilationException {
    public ImportException(String message, Loc loc) {
        super(message, loc);
    }
}
