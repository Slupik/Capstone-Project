/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.model.server.backup;

import java.util.List;

import io.github.slupik.savepass.data.password.room.EntityPassword;
import io.github.slupik.savepass.model.server.backup.object.BodyDownloadBackup;
import io.github.slupik.savepass.model.server.backup.object.BodySendBackup;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static io.github.slupik.savepass.model.server.backup.ServerDefaultSettings.DOWNLOADING_URL;
import static io.github.slupik.savepass.model.server.backup.ServerDefaultSettings.SENDING_URL;

public interface BackupService {

    @POST(SENDING_URL)
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ResponseBody> send(@Body BodySendBackup body);

    @POST(DOWNLOADING_URL)
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<List<EntityPassword>> download(@Body BodyDownloadBackup body);
}
