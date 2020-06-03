package com.smparkworld.parkwork;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import java.util.HashMap;

public class TaskManager {

    private Handler mHandler = new Handler();

    public void execute(Context mContext,
                        String uri,
                        HashMap<String, String> imgData,
                        HashMap<String, String> strData,
                        ParkWork.OnResponseListener rspListener,
                        ParkWork.OnProgressUpdateListener prgListener) {

        if (imgData != null && strData != null) {
            new ImageStringTask(mContext, uri, imgData, strData, rspListener, prgListener, mHandler).start();
        } else if (imgData != null) {
            new ImageTask(mContext, uri, imgData, rspListener, prgListener, mHandler).start();
        } else if (strData != null) {
            new StringTask(uri, strData, rspListener, prgListener)
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new ReadOnlyTask(uri, rspListener, prgListener, mHandler).start();
        }

    }



}
