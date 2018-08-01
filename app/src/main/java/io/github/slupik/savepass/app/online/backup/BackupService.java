/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.online.backup;

import java.util.List;

import io.github.slupik.savepass.data.password.room.EntityPassword;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BackupService {

    @FormUrlEncoded
    @POST("backup/send")
    Call<List<EntityPassword>> send(@Body List<EntityPassword> passwords);

    @GET("backup/download")
    Call<List<EntityPassword>> download();
}
