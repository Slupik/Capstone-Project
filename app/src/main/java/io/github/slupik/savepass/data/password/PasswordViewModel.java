/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.data.password;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import io.github.slupik.savepass.data.password.room.EntityPassword;

public class PasswordViewModel extends AndroidViewModel {

    private PasswordRepository mRepository;

    private LiveData<List<EntityPassword>> livePasswords;

    public PasswordViewModel (Application application) {
        super(application);
        mRepository = PasswordRepository.getInstance(application);
        livePasswords = mRepository.getLivePasswords();
    }

    public LiveData<List<EntityPassword>> getLivePasswords() { return livePasswords; }

    public void insert(EntityPassword word) { mRepository.insert(word); }
}
