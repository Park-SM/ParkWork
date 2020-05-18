package com.smparkworld.parkwork;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class ReadOnlyTask extends Thread {

    private String mUri;
    private ParkWork.OnProgressUpdateListener mListener;
    private String mErrorMessage;

    public ReadOnlyTask(String uri,
                        ParkWork.OnProgressUpdateListener listener) {
        mUri = uri;
        mListener = listener;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(mUri);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;

            while ((line = br.readLine()) != null) sb.append(line);
            br.close();

            if (mListener != null)
                mListener.onProgressUpdate(100);

            onPostExecute(sb.toString());
        } catch(Exception e) {
            onPostExecute(null);
            mErrorMessage = e.getMessage();
        }
    }

    public String getErrorMessage() { return mErrorMessage; }

    protected abstract void onPostExecute(String result);
}
