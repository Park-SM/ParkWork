package com.smparkworld.parkwork;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;

public abstract class StringTask extends AsyncTask<Void, Void, String> {

    private String mUri;
    private int mMethod;
    private HashMap<String, String> mData;
    private String mErrorMessage;

    public StringTask(String uri, int method, HashMap<String, String> data) {
        mUri = uri;
        mMethod = method;
        mData = data;
    }

    @Override
    protected String doInBackground(Void... args) {
        try {

            StringBuffer data = new StringBuffer();

            if (mData != null) {
                int i = 0;
                int count = mData.keySet().size();
                if (mMethod == ParkWork.METHOD_GET) data.append("?");

                for (String key : mData.keySet()) {
                    data.append(URLDecoder.decode(key, "UTF-8") + "=" + URLDecoder.decode(mData.get(key), "UTF-8"));
                    if (++i < count) data.append("&");
                }
                if (mMethod == ParkWork.METHOD_GET) mUri += data.toString();
            }

            URL url = new URL(mUri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod((mMethod == ParkWork.METHOD_GET) ? "GET" : "POST");

            if (mData != null) {
                conn.setDoOutput(true);
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(data.toString());
                osw.close();
            }

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

    protected String getErrorMessage() { return mErrorMessage; }

    @Override
    protected abstract void onPostExecute(String result);
}
