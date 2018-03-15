package com.parkbobo.utils.http;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;

/**
 * Created by free on 16-6-16.
 */
public interface HttpFileCallBack {
    void onError(Call call,Exception e);

    void onSuccess(Call call,File downloadPath);

    void onProgress(long progressByte, long allByte);
}
