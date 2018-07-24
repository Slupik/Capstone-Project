/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.data.password.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {EntityPassword.class}, version = 1)
public abstract class PasswordRoomDatabase extends RoomDatabase {
    public abstract PasswordDAO passwordDAO();

    //Singleton
    private static PasswordRoomDatabase INSTANCE;
    public static PasswordRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PasswordRoomDatabase.class) {
                if (INSTANCE == null) {
                    createDatabase(context);
                }
            }
        }
        return INSTANCE;
    }

    private static void createDatabase(final Context context) {
        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                PasswordRoomDatabase.class, "password_database")
                .build();
    }
}
