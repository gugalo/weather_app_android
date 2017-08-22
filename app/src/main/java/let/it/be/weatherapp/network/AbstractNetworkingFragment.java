package let.it.be.weatherapp.network;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import let.it.be.weatherapp.models.exceptions.NetworkException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Headless fragment for loading data in background thread
 */
public abstract class AbstractNetworkingFragment<T> extends Fragment {

    private T savedResult;
    private ResultListener<T> resultListener;
    private NetworkException savedError;
    private Call<T> currentNetworkCall;
    private boolean hasPendingResult = false;

    /**
     * You might say that this is mistake and that fragment must be instantiated using empty
     * constructor. But that is not true for all cases. Main idea for empty fragment constructor
     * is for system to gracefully handle fragment re-creation. This is from docs : "fragment must
     * have an empty constructor, so it can be instantiated when restoring its activity's state".
     * But this fragment main idea is to maintain instance during configuration changes. But if
     * activity is recreated because of some other cases as low on memory, then this should be
     * handle by activity it self, since all information in this case should be loaded again
     * from internet.
     */
    public AbstractNetworkingFragment(Activity activity, String tag) {
        activity.getFragmentManager().beginTransaction().add(this, tag).commit();
    }

    public void setResultListener(ResultListener<T> resultListener) {
        setResultListener(resultListener, true);
    }

    public void setResultListener(ResultListener<T> resultListener, boolean notifyOfPendingResult) {
        this.resultListener = resultListener;
        if (notifyOfPendingResult && hasPendingResult) {
            if (savedResult != null) {
                onSuccess(savedResult);
            }
            if (savedError != null) {
                onError(savedError);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public static AbstractNetworkingFragment<?> findFragment(Activity activity, String tag) {
        return (AbstractNetworkingFragment<?>) activity.getFragmentManager().findFragmentByTag(tag);
    }

    public void startDataLoading() {
        if (currentNetworkCall == null) {
            runNetworkTask();
        } else {
            // this case means that thread is currently in progress already
            // do nothing
        }
    }

    public void destroy() {
        stopBackgroundProcess();
        if (getActivity() != null) {
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    public void stopBackgroundProcess() {
        if (currentNetworkCall != null) {
            currentNetworkCall.cancel();
            currentNetworkCall = null;
        }
    }

    @Override
    public void onDestroy() {
        stopBackgroundProcess();
        super.onDestroy();
    }

    public void retry() {
        stopBackgroundProcess();
        savedResult = null;
        savedError = null;
        startDataLoading();
    }

    private void runNetworkTask() {
        if (currentNetworkCall != null) {
            // do not start new loading process if it is already loading
            return;
        }
        savedResult = null;
        savedError = null;
        currentNetworkCall = getNetworkCall();
        Log.d("Networking", currentNetworkCall.request().toString());
        currentNetworkCall.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                currentNetworkCall = null;

                if (!response.isSuccessful()) {
                    onError(new NetworkException("Response code: " + response.code()));
                    return;
                }

                onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                currentNetworkCall = null;
                onError(new NetworkException(t));
            }
        });
    }

    public void onSuccess(T data) {
        savedResult = data;
        if (resultListener != null) {
            resultListener.onSuccess(data);
            hasPendingResult = false;
        } else {
            hasPendingResult = true;
        }
    }

    public void onError(NetworkException error) {
        savedError = error;
        if (resultListener != null) {
            resultListener.onFailed(error);
            hasPendingResult = false;
        } else {
            hasPendingResult = true;
        }
    }

    protected abstract Call<T> getNetworkCall();
}
