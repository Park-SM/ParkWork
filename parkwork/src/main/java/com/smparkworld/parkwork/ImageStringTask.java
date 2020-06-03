package com.smparkworld.parkwork;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;

public class ImageStringTask extends Thread {

    private final int MAX_BUF = 1024;
    private final int PRG_ONCE;

    private Context mContext;
    private String mUri;
    private HashMap<String, String> mImgData;
    private HashMap<String, String> mStrData;
    private ParkWork.OnResponseListener mRspListener;
    private ParkWork.OnProgressUpdateListener mPrgListener;
    private String mErrorMessage;
    private Handler mHandler;

    public ImageStringTask(Context context,
                           String uri,
                           HashMap<String, String> imgData,
                           HashMap<String, String> strData,
                           ParkWork.OnResponseListener rspListener,
                           ParkWork.OnProgressUpdateListener prgListener,
                           Handler handler) {
        mContext = context;
        mUri = uri;
        mImgData = imgData;
        mStrData = strData;
        mRspListener = rspListener;
        mPrgListener = prgListener;
        mHandler = handler;

        // "1 +" is for string request.
        PRG_ONCE = 100 / (1 + mImgData.keySet().size());
    }

    @Override
    public void run() {
        try {
            String lineEnd = "\n";
            String twoHyphens = "--";
            String boundary = "ParkWorkBoundary";

            int i = 0;
            int size = mStrData.keySet().size();
            StringBuffer data = new StringBuffer();
            for (String key : mStrData.keySet()) {
                if (i == 0) data.append("?");
                data.append(URLDecoder.decode(key, "UTF-8") + "=" + URLDecoder.decode(mStrData.get(key), "UTF-8"));
                if (++i < size) data.append("&");
            }
            if (mPrgListener != null)
                mPrgListener.onProgressUpdate(PRG_ONCE);

            URL url = new URL(mUri + data.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            int available = 0;
            int bufferSize = 0;
            InputStream fis;
            byte[] buffer = new byte[MAX_BUF];
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            for (String key : mImgData.keySet()) {
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + key + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                fis = checkUriType(mImgData.get(key));
                if (fis == null) continue;

                while ((available = fis.available()) != 0) {
                    bufferSize = (available < MAX_BUF) ? available : MAX_BUF;
                    fis.read(buffer, 0, bufferSize);
                    dos.write(buffer, 0, bufferSize);

                    Log.v("ParkWork upload result", "Uploading.." + bufferSize + "byte");
                }
                fis.close();

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                if (mPrgListener != null)
                    mPrgListener.onProgressUpdate(PRG_ONCE);
            }
            dos.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();

            onPostExecute(sb.toString());
        } catch (Exception e) {
            mErrorMessage = e.getMessage();
            onPostExecute(null);
            return;
        }
    }

    private InputStream checkUriType(String uri) throws IOException {
        if (uri == null || uri.length() == 0) return null;

        InputStream iStream;
        if (uri.contains("content:")) {
            iStream = mContext.getContentResolver().openInputStream(Uri.parse(uri));
        } else {
            iStream = new FileInputStream(uri);
        }

        return iStream;
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
