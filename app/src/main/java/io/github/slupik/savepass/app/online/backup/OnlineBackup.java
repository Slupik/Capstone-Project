/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.online.backup;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.github.slupik.savepass.data.password.PasswordRepository;
import io.github.slupik.savepass.data.password.room.EntityPassword;
import io.github.slupik.savepass.data.settings.ServerSettings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class OnlineBackup {
    private static Retrofit retrofit;

    public static void sendData(Context context) {
        List<EntityPassword> passwords = PasswordRepository.getInstance(context).getPasswords();
        Call<List<EntityPassword>> call = getService(context).send(passwords);
        call.enqueue(new Callback<List<EntityPassword>>() {
            @Override
            public void onResponse(@NonNull Call<List<EntityPassword>> call, @NonNull Response<List<EntityPassword>> response) {}
            @Override
            public void onFailure(@NonNull Call<List<EntityPassword>> call, @NonNull Throwable t) {}
        });
    }

    public static void saveData(final Context context) {
        Call<List<EntityPassword>> call = getService(context).download();
        call.enqueue(new Callback<List<EntityPassword>>() {
            @Override
            public void onResponse(@NonNull Call<List<EntityPassword>> call, @NonNull Response<List<EntityPassword>> response) {
                List<EntityPassword> passList = response.body();
                if(null!=passList) {
                    updateFromServer(context, passList);
                    deleteFromServer(context, passList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<EntityPassword>> call, @NonNull Throwable t) {}
        });
    }

    private static void deleteFromServer(Context context, List<EntityPassword> downloadedList) {
        List<EntityPassword> savedList = PasswordRepository.getInstance(context).getPasswords();

        List<EntityPassword> toDelete = new ArrayList<>();
        for(EntityPassword saved:savedList) {
            EntityPassword downloaded = getIdenticalPasswordEntity(downloadedList, saved);
            if(null==downloaded && saved.isToSyncWithServer()) {
                toDelete.add(saved);
            }
        }

        PasswordRepository.getInstance(context).delete(toDelete.toArray(new EntityPassword[toDelete.size()]));
    }

    private static void updateFromServer(Context context, List<EntityPassword> downloadedList) {
        List<EntityPassword> savedList = PasswordRepository.getInstance(context).getPasswords();

        List<EntityPassword> toUpdate = new ArrayList<>();
        for(EntityPassword downloaded:downloadedList) {
            EntityPassword saved = getIdenticalPasswordEntity(savedList, downloaded);
            if(isToSave(saved, downloaded)) {
                toUpdate.add(downloaded);
            }
        }

        PasswordRepository.getInstance(context).update(toUpdate.toArray(new EntityPassword[toUpdate.size()]));
    }

    private static boolean isToSave(EntityPassword saved, EntityPassword downloaded) {
        return null == saved || (downloaded.getLastUpdate() > saved.getLastUpdate() && saved.isToSyncWithServer());
    }

    private static EntityPassword getIdenticalPasswordEntity(List<EntityPassword> list, EntityPassword entity) {
        for(EntityPassword item:list) {
            if(item.getId()==entity.getId()) {
                return item;
            }
        }
        return null;
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
}
