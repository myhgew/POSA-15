package vandy.mooc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

/**
 * An Activity that downloads an image, stores it in a local file on
 * the local device, and returns a Uri to the image file.
 */
public class DownloadImageActivity extends Activity {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();

    private Uri uri;
    private ProgressBar spinner;


    /**
     * Hook method called when a new instance of Activity is created.
     * One time initialization code goes here, e.g., UI layout and
     * some class scope variable initialization.
     *
     * @param savedInstanceState object that contains saved state information.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        // @@ TODO -- you fill in here.
        super.onCreate(savedInstanceState);

        setContentView(R.layout.download_image_activity);

        spinner = (ProgressBar) findViewById(R.id.progressBar1);

        // Get the URL associated with the Intent data.
        // @@ TODO -- you fill in here.
        Intent intent = getIntent();
        uri = Uri.parse(intent.getStringExtra("Url"));
        Log.i(TAG, uri.toString());

        // Download the image in the background, create an Intent that
        // contains the path to the image file, and set this as the
        // result of the Activity.

        // @@ TODO -- you fill in here using the Android "HaMeR"
        // concurrency framework.  Note that the finish() method
        // should be called in the UI thread, whereas the other
        // methods should be called in the background thread.

        spinner.setVisibility(View.VISIBLE);
        startDownloadingOnNewThread();
    }

    private void finishActivity(Uri pathUri) {
        if (pathUri != null) {
            String path = pathUri.toString();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("PATH", path);
            setResult(RESULT_OK, resultIntent);
        } else {
            setResult(RESULT_CANCELED);
        }

        updateProgressBarOnUIThread();

        finish();
    }

    private void startDownloadingOnNewThread() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DownloadUtils downloadUtils = new DownloadUtils();
                Uri pathUri = downloadUtils.downloadImage(DownloadImageActivity.this, uri);
                finishActivity(pathUri);
            }
        };

        new Thread(runnable).start();
    }

    private void updateProgressBarOnUIThread() {
        // Get a handler that can be used to post to the main thread
        Handler mainHandler = new Handler(this.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                spinner.setVisibility(View.GONE);

            }
        };
        mainHandler.post(myRunnable);
    }
}
