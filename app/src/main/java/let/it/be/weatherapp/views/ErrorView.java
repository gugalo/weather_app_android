package let.it.be.weatherapp.views;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import let.it.be.weatherapp.R;

/**
 * Really simple view wich doesnt contain any settings or attr of it's own since it has no need
 * for at least at the moment. Used only to give fast access to single layout with programmatic
 * reference to some of it's elements
 */

public class ErrorView extends FrameLayout {

    private View retryButton;
    private View retryProgress;
    private TextView errorText;

    public ErrorView(@NonNull Context context) {
        this(context, null);
    }

    public ErrorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ErrorView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        removeAllViews();
        inflate(getContext(), R.layout.error_screen, this);
        retryButton = findViewById(R.id.errorRetryButton);
        retryProgress = findViewById(R.id.errorRetryProgress);
        errorText = (TextView) findViewById(R.id.errorMessageText);
    }

    public void setRertyButtonListener(OnClickListener listener) {
        retryButton.setOnClickListener(listener);
    }

    public void setErrorMessage(@StringRes int stringId) {
        errorText.setText(stringId);
    }

    public void setErrorMessage(String message) {
        errorText.setText(message);
    }

    public void showProgress(){
        retryButton.setVisibility(GONE);
        retryProgress.setVisibility(VISIBLE);
    }

    public void hideProgress(){
        retryButton.setVisibility(VISIBLE);
        retryProgress.setVisibility(GONE);
    }

}
