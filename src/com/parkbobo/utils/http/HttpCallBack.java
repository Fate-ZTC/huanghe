package com.parkbobo.utils.http;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by free on 16-5-20.
 */
public interface HttpCallBack {
    void onError(Call call, IOException e);

    void onSuccess(Call call, String body);
}
