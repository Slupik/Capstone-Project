/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.data.password.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PasswordDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EntityPassword... word);

    @Update
    void updatePasswords(EntityPassword... users);


    @Query("SELECT * from passwords_table ORDER BY pass_name ASC")
    List<EntityPassword> getAllPasswords();

    @Query("SELECT * from passwords_table ORDER BY pass_name ASC")
    LiveData<List<EntityPassword>> getLiveDataOfAllPasswords();

    @Query("SELECT * from passwords_table WHERE (:nowInMillis-last_remind)>(remind_time) ORDER BY (:nowInMillis-last_remind) DESC")
    List<EntityPassword> getPasswordsToRemind(long nowInMillis);


    @Delete
    void deletePasswords(EntityPassword... users);

    @Query("DELETE FROM passwords_table")
    void deleteAll();
}
