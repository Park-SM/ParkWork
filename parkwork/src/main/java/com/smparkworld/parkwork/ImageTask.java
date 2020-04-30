package com.smparkworld.parkwork;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public abstract class ImageTask extends Thread {

    private final int MAX_BUF = 1024;

    private Context mContext;
    private String mUri;
    private HashMap<String, String> mImageUri;
    private String mErrorMessage;

    public ImageTask(Context context, String uri, HashMap<String, String> imageUri) {
        mContext = context;
        mUri = uri;
        mImageUri = imageUri;
    }

    @Override
    public void run() {
        try {
            String lineEnd = "\n";
            String twoHyphens = "--";
            String boundary = "ParkWorkBoundary";

            URL url = new URL(mUri);
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

            for (String key : mImageUri.keySet()) {
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + key + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                fis = checkUriType(mImageUri.get(key));
                if (fis == null) continue;

                while((available = fis.available()) != 0) {
                    bufferSize = (available < MAX_BUF) ? available : MAX_BUF;
                    fis.read(buffer, 0, bufferSize);
                    dos.write(buffer, 0, bufferSize);

                    Log.v("ParkWork upload result", "Uploading.." + bufferSize + "byte");
                }
                fis.close();

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
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

    public String getErrorMessage() { return mErrorMessage; }

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

    protected abstract void onPostExecute(String result);
}
