package com.smparkworld.parkwork;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;

public class StringTask extends AsyncTask<Void, Void, String> {

    private String mUri;
    private HashMap<String, String> mData;
    private ParkWork.OnResponseListener mRspListener;
    private ParkWork.OnProgressUpdateListener mPrgListener;
    private String mErrorMessage;

    public StringTask(String uri,
                      HashMap<String, String> data,
                      ParkWork.OnResponseListener rspListener,
                      ParkWork.OnProgressUpdateListener prgListener) {
        mUri = uri;
        mData = data;
        mRspListener = rspListener;
        mPrgListener = prgListener;
    }

    @Override
    protected String doInBackground(Void... args) {
        try {

            int i = 0;
            int count = mData.keySet().size();
            StringBuffer data = new StringBuffer();

            for (String key : mData.keySet()) {
                data.append(URLDecoder.decode(key, "UTF-8") + "=" + URLDecoder.decode(mData.get(key), "UTF-8"));
                if (++i < count) data.append("&");
            }

            URL url = new URL(mUri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (mData != null) {
                conn.setDoOutput(true);
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(data.toString());
                osw.close();
            }
            if (mPrgListener != null)
                mPrgListener.onProgressUpdate(100);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();

            return sb.toString();
        } catch(Exception e) {
            mErrorMessage = e.getMessage();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (mRspListener != null) {
            if (result != null)
                mRspListener.onResponse(result);
            else
                mRspListener.onError(mErrorMessage);

        }
    }
}
