/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.model.server.backup;

import java.util.List;

import io.github.slupik.savepass.data.password.room.EntityPassword;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static io.github.slupik.savepass.model.server.backup.ServerDefaultSettings.DOWNLOADING_URL;
import static io.github.slupik.savepass.model.server.backup.ServerDefaultSettings.SENDING_URL;

public interface BackupService {

    @POST(SENDING_URL)
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<EntityPassword>> send(@Body List<EntityPassword> passwords);

    @GET(DOWNLOADING_URL)
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<EntityPassword>> download();
}
