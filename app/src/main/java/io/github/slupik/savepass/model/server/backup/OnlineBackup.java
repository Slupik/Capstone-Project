/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.model.server.backup;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import io.github.slupik.savepass.data.password.PasswordRepository;
import io.github.slupik.savepass.data.password.room.EntityPassword;
import io.github.slupik.savepass.data.settings.ServerSettings;
import io.github.slupik.savepass.model.server.backup.object.BodyDownloadBackup;
import io.github.slupik.savepass.model.server.backup.object.BodySendBackup;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class OnlineBackup {
    private static Retrofit retrofit;

    public static void sendData(Context context) {
        List<EntityPassword> passwords = PasswordRepository.getInstance(context).getPasswords();
        Call<ResponseBody> call = getService(context).send(new BodySendBackup(context, passwords));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {}
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }

    public static void saveData(final Context context) {
        Call<List<EntityPassword>> call = getService(context).download(new BodyDownloadBackup(context));
        call.enqueue(new Callback<List<EntityPassword>>() {
            @Override
            public void onResponse(@NonNull Call<List<EntityPassword>> call, @NonNull Response<List<EntityPassword>> response) {
                List<EntityPassword> passList = response.body();
                if(null!=passList) {
                    updateFromServer(context, passList);
                    deleteFromServer(context, passList);
                    addFromServer(context, passList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<EntityPassword>> call, @NonNull Throwable t) {}
        });
    }

    private static void addFromServer(Context context, List<EntityPassword> downloadedList) {
        new PasswordAdder(context).execute(downloadedList);
    }

    private static void deleteFromServer(Context context, List<EntityPassword> downloadedList) {
        new PasswordDeleter(context).execute(downloadedList);
    }

    private static void updateFromServer(Context context, List<EntityPassword> downloadedList) {
        new PasswordUpdater(context).execute(downloadedList);
    }

    private static BackupService getService(Context context) {
        return getRetrofit(context).create(BackupService.class);
    }

    private static Retrofit getRetrofit(Context context) {
        if(null==retrofit) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(new ServerSettings(context).getBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    @SuppressWarnings("unused")
    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
