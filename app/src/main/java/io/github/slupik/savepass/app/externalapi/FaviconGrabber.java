/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.externalapi;

import android.text.TextUtils;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * http://favicongrabber.com/service-api-reference
 */
public final class FaviconGrabber {
    private static final String BASE_URL = "http://favicongrabber.com/api/grab/";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private FaviconGrabber(){}

    public static void getDataForUrl(String webpage, final GrabberCallback callback) {
        String url;
        try {
            url = FaviconGrabber.getAddressForAPI(webpage);
        } catch (MalformedURLException e) {
            callback.onError();
            return;
        }
        Log.d("PICPHOTO", "getDataForUrl URL: "+url);

        FaviconGrabberService service = retrofit.create(FaviconGrabberService.class);
        Call<FaviconGrabberData> call = service.getFaviconData(url);
        call.enqueue(new Callback<FaviconGrabberData>() {
            @Override
            public void onResponse(Call<FaviconGrabberData> call, Response<FaviconGrabberData> response) {
                FaviconGrabberData data = response.body();
                if(data!=null) {
                    String url = data.getUrlForBiggestIcon();
                    Log.d("PICPHOTO", "getUrlForBiggestIcon URL: "+url);
                    if(!TextUtils.isEmpty(url)) {
                        callback.onUrlReturn(url);
                    } else {
                        callback.onError();
                    }
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<FaviconGrabberData> call, Throwable t) {
                callback.onError();
            }
        });
    }

    static String getAddressForAPI(String webpage) throws MalformedURLException {
        if(!webpage.startsWith("http") && !webpage.startsWith("https")) {
            webpage = "http://"+webpage;
        }
        URL webURL = new URL(webpage);
        return webURL.getHost();
    }

    public interface GrabberCallback {
        void onUrlReturn(String url);
        void onError();
    }
}
