package xyz.victorolaitan.easyjson;

public class EasyJSONException extends Exception {

    static final int SAVE_ERROR = 0;
    static final int UNEXPECTED_TOKEN = 1;
    static final int LOAD_ERROR = 2;

    EasyJSONException(int error) {
        super(translateError(error));
    }

    EasyJSONException(int error, String details) {
        super(translateError(error) + " : " + details);
    }

    EasyJSONException(int error, Throwable cause) {
        super(translateError(error), cause);
    }

    static String translateError(int error) {
        switch (error) {
            case SAVE_ERROR:
                return "EasyJSON failed to save json structure!";
            case UNEXPECTED_TOKEN:
                return "EasyJSON encountered an incompatible token!";
            case LOAD_ERROR:
                return "A read error occurred when attempting to load the file!";
            default:
                return null;
        }
    }
}
