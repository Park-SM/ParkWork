package com.smparkworld.parkwork;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadOnlyTask extends Thread {

    private String mUri;
    private ParkWork.OnResponseListener mRspListener;
    private ParkWork.OnProgressUpdateListener mPrgListener;
    private String mErrorMessage;
    private Handler mHandler;

    public ReadOnlyTask(String uri,
                        ParkWork.OnResponseListener rspListener,
                        ParkWork.OnProgressUpdateListener prgListener,
                        Handler handler) {
        mUri = uri;
        mRspListener = rspListener;
        mPrgListener = prgListener;
        mHandler = handler;
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

            if (mPrgListener != null)
                mPrgListener.onProgressUpdate(100);

            onPostExecute(sb.toString());
        } catch(Exception e) {
            onPostExecute(null);
            mErrorMessage = e.getMessage();
        }
    }

    protected void onPostExecute(final String result) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mRspListener != null) {
                    if (result != null)
                        mRspListener.onResponse(result);
                    else
                        mRspListener.onError(mErrorMessage);
                }
            }
        });
    }
}
