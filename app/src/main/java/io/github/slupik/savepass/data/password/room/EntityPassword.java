/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.data.password.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "passwords_table")
public class EntityPassword {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "web_address")
    private String webAddress;

    @ColumnInfo(name = "pass_name")
    private String passwordName;

    @ColumnInfo(name = "login")
    private String login;

    @ColumnInfo(name = "encrypted_password")
    private String encryptedPassword;

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "short_desc")
    private String shortDesc;

    @ColumnInfo(name = "remind_time")
    private long remindTimeInMilis;

    @ColumnInfo(name = "last_update")
    private long lastUpdate;

    @ColumnInfo(name = "last_remind")
    private long lastRemindTime;

    @ColumnInfo(name = "sync_server")
    private boolean isToSyncWithServer;

}
