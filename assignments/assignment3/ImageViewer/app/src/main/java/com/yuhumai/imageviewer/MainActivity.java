package com.yuhumai.imageviewer;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private ImageView imageView;
        private Button button;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            imageView = (ImageView) rootView.findViewById(R.id.imageView);

             button = (Button) rootView.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uriString = "http://blogs-images.forbes.com/thomasbrewster/files/2014/09/Android1.png";
                    Uri imageLinkUri = Uri.parse(uriString);

                    new DownloadImageTask().execute(imageLinkUri);

                    button.setEnabled(false);
                }
            });

            return rootView;
        }

        public class DownloadImageTask extends AsyncTask<Uri, Integer, Uri> {
            protected Uri doInBackground(Uri... uris) {
                return Utils.downloadImage(getActivity().getApplicationContext(), uris[0]);
            }

            protected void onProgressUpdate(Integer... progress) {

            }

            protected void onPostExecute(Uri result) {
                imageView.setImageURI(result);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                new GrayImageTask().execute(result);
            }
        }

        public class GrayImageTask extends AsyncTask<Uri, Integer, Uri> {
            protected Uri doInBackground(Uri... uris) {
                return Utils.grayScaleFilter(getActivity().getApplicationContext(), uris[0]);
            }

            protected void onProgressUpdate(Integer... progress) {

            }

            protected void onPostExecute(Uri result) {
                imageView.setImageURI(result);

                button.setEnabled(true);
            }
        }
    }
}
