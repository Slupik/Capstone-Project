/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.tests;

import io.github.slupik.savepass.app.MyApplication;
import io.github.slupik.savepass.data.password.PasswordRepository;
import io.github.slupik.savepass.data.password.room.EntityPassword;

public final class PassDatabaseTestUtils {

    //Just random

    public static EntityPassword generateReallyOldPass(MyApplication application){
        EntityPassword pass = generatePass(application);
        pass.setLastRemindTime(1000);
        pass.setLastUpdate(1000);
        return pass;
    }

    public static void generateAndSaveReallyOldPass(MyApplication application){
        save(application, generateReallyOldPass(application));
    }

    //Really old

    public static EntityPassword generatePass(MyApplication application){
        return PasswordRepository.getExampleEntity(application);
    }

    public static void generateAndSavePass(MyApplication application){
        save(application, generatePass(application));
    }

    //Utils

    private static void save(MyApplication application, EntityPassword entityPassword) {
        PasswordRepository.getInstance(application).insert(entityPassword);
    }
}
