package com.smparkworld.parkwork;

import android.content.Context;
import android.os.Handler;

import java.util.HashMap;

public class ExecutingTaskManager {

    private HashMap<String, String> mData;
    private ParkWork.OnResponseListener mListener;
    private Handler handler = new Handler();

    public void execute(Context mContext,
                        String uri,
                        int workType,
                        int method,
                        HashMap<String, String> data,
                        ParkWork.OnResponseListener listener) {
        mData = data;
        mListener = listener;

        switch(workType) {
            case ParkWork.WORK_STRING:
                new StringTask(uri, method, mData) {
                    @Override
                    protected void onPostExecute(String result) {
                        if (mListener != null) {
                            if (result != null)
                                mListener.onResponse(result);
                            else
                                mListener.onError(getErrorMessage());

                        }
                    }
                }.execute();
                break;

            case ParkWork.WORK_IMAGE:
                new ImageTask(mContext, uri, mData) {
                    @Override
                    protected void onPostExecute(final String result) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mListener != null) {
                                    if (result != null)
                                        mListener.onResponse(result);
                                    else
                                        mListener.onError(getErrorMessage());
                                }
                            }
                        });
                    }
                }.start();
                break;

        }

    }



}
