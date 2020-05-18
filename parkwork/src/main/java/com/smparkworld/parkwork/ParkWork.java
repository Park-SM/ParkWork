package com.smparkworld.parkwork;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

public class ParkWork {

    private static TaskManager exeTaskManager;

    private Context mContext;
    private String mUri;
    private HashMap<String, String> mImgData;
    private HashMap<String, String> mStrData;
    private OnResponseListener mRspListener;
    private OnProgressUpdateListener mPrgListener;

    private ParkWork setArgument(Context context, String uri) {
        mContext = context;
        mUri = uri;
        return this;
    }

    public static ParkWork create(Context context, String uri) {
        return new ParkWork().setArgument(context, uri);
    }

    public ParkWork setImageData(HashMap<String, String> data) {
        mImgData = data;
        return this;
    }

    public ParkWork setStringData(HashMap<String, String> data) {
        mStrData = data;
        return this;
    }

    public ParkWork setOnResponseListener(OnResponseListener listener) {
        mRspListener = listener;
        return this;
    }

    public ParkWork setOnProgressUpdateListener(OnProgressUpdateListener listener) {
        mPrgListener = listener;
        return this;
    }

    public void start() {
        if (mUri == null) {
            Log.v("ParkWork error!", "The URI is empty.");
            return;
        }

        if (exeTaskManager == null)
            exeTaskManager = new TaskManager();

        exeTaskManager.execute(mContext, mUri, mImgData, mStrData, mRspListener, mPrgListener);
    }

    public interface OnResponseListener {
        void onResponse(String response);
        void onError(String errorMessage);
    }

    public interface OnProgressUpdateListener {
        void onProgressUpdate(int progress);
    }

}
