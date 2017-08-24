package let.it.be.weatherapp.models.exceptions;

public class NetworkException extends Exception {

    public static final String TAG = NetworkException.class.getSimpleName();

    public NetworkException() {
    }

    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkException(Throwable cause) {
        super(cause);
    }
}
