package let.it.be.weatherapp.network;

import let.it.be.weatherapp.models.exceptions.NetworkException;

public interface ResultListener<T> {
    void onSuccess(T result);

    void onFailed(NetworkException error);
}
