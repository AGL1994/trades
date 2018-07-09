package exception;

public class ReadFileDataException extends Exception {
    public ReadFileDataException(String message, Exception e){
        super(message, e);
    }
}
