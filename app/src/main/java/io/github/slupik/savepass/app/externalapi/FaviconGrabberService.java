/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.externalapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

interface FaviconGrabberService {
    @GET("{webpage}")
    Call<FaviconGrabberData> getFaviconData(@Path("webpage") String webAddress);
}
