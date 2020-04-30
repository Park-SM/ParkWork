package com.smparkworld.parkwork;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

public class ParkWork {

    public static final int WORK_STRING = 100;
    public static final int WORK_IMAGE = 101;

    public static final int METHOD_POST = 0;
    public static final int METHOD_GET = 1;

    private static ExecutingTaskManager exeTaskManager;

    private Context mContext;
    private String mUri;
    private int mMethod = METHOD_GET;
    private int mWorkType = WORK_STRING;
    private HashMap<String, String> mData;
    private OnResponseListener mListener;

    private ParkWork setArgument(Context context, String uri) {
        mContext = context;
        mUri = uri;
        return this;
    }

    public static ParkWork create(Context context, String uri) {
        return new ParkWork().setArgument(context, uri);
    }

    public ParkWork setMethod(int method) {
        mMethod = method;
        return this;
    }

    public ParkWork setType(int workType) {
        mWorkType = workType;
        return this;
    }

    public ParkWork setData(HashMap<String, String> data) {
        mData = data;
        return this;
    }

    public ParkWork setListener(OnResponseListener listener) {
        mListener = listener;
        return this;
    }

    public void start() {
        if (mUri == null) {
            Log.v("ParkWork error!", "The URI is empty.");
            return;
        }

        if (exeTaskManager == null)
            exeTaskManager = new ExecutingTaskManager();

        exeTaskManager.execute(mContext, mUri, mWorkType, mMethod, mData, mListener);
    }

    public interface OnResponseListener {
        void onResponse(String response);
        void onError(String errorMessage);
    }

}
