package com.smparkworld.parkwork;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import java.util.HashMap;

public class TaskManager {

    private HashMap<String, String> mImgData;
    private HashMap<String, String> mStrData;
    private ParkWork.OnResponseListener mRspListener;
    private ParkWork.OnProgressUpdateListener mPrgListener;
    private Handler handler = new Handler();

    public void execute(Context mContext,
                        String uri,
                        HashMap<String, String> imgData,
                        HashMap<String, String> strData,
                        ParkWork.OnResponseListener rspListener,
                        ParkWork.OnProgressUpdateListener prgListener) {
        mImgData = imgData;
        mStrData = strData;
        mRspListener = rspListener;
        mPrgListener = prgListener;

        if (mImgData != null && mStrData != null) {
            new ImageStringTask(mContext, uri, mImgData, mStrData, mPrgListener) {
                @Override
                protected void onPostExecute(final String result) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mRspListener != null) {
                                if (result != null)
                                    mRspListener.onResponse(result);
                                else
                                    mRspListener.onError(getErrorMessage());
                            }
                        }
                    });
                }
            }.start();
        } else if (mImgData != null) {
            new ImageTask(mContext, uri, mImgData, mPrgListener) {
                @Override
                protected void onPostExecute(final String result) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mRspListener != null) {
                                if (result != null)
                                    mRspListener.onResponse(result);
                                else
                                    mRspListener.onError(getErrorMessage());
                            }
                        }
                    });
                }
            }.start();
        } else if (mStrData != null) {
            new StringTask(uri, mStrData, mPrgListener) {
                @Override
                protected void onPostExecute(String result) {
                    if (mRspListener != null) {
                        if (result != null)
                            mRspListener.onResponse(result);
                        else
                            mRspListener.onError(getErrorMessage());

                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new ReadOnlyTask(uri, mPrgListener) {
                @Override
                protected void onPostExecute(final String result) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mRspListener != null) {
                                if (result != null)
                                    mRspListener.onResponse(result);
                                else
                                    mRspListener.onError(getErrorMessage());
                            }
                        }
                    });
                }
            }.start();
        }

    }



}
