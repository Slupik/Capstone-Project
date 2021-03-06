/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.data.password.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import io.github.slupik.savepass.model.cryptography.Cryptography;
import io.github.slupik.savepass.model.utils.Randomizer;

@Entity(tableName = "passwords_table")
public class EntityPassword {

    @PrimaryKey(autoGenerate = true) @SerializedName("local_id")
    private int id;

    @ColumnInfo(name = "unique_code") @SerializedName("unique_code")
    private String uniqueCode = Randomizer.randomStandardString(32);

    @ColumnInfo(name = "web_address") @SerializedName("web_address")
    private String webAddress;

    @ColumnInfo(name = "pass_name") @SerializedName("pass_name")
    private String passwordName;

    @ColumnInfo(name = "login") @SerializedName("login")
    private String login;

    @ColumnInfo(name = "encrypted_password") @SerializedName("encrypted_password")
    private String encryptedPassword;

    @ColumnInfo(name = "notes") @SerializedName("notes")
    private String notes;

    @ColumnInfo(name = "short_desc") @SerializedName("short_desc")
    private String shortDesc;

    @ColumnInfo(name = "remind_time") @SerializedName("remind_time")
    private long remindTimeInMilis;

    @ColumnInfo(name = "last_update") @SerializedName("last_update")
    private long lastUpdate;

    @ColumnInfo(name = "last_remind") @SerializedName("last_remind")
    private long lastRemindTime;

    @ColumnInfo(name = "sync_server") @SerializedName("sync_server")
    private boolean isToSyncWithServer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public String getPasswordName() {
        return passwordName;
    }

    public void setPasswordName(String passwordName) {
        this.passwordName = passwordName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public void setAndEncryptPassword(String key, String password) throws Exception {
        String encrypted = Cryptography.getEncryptedPass(key, password);
        setEncryptedPassword(encrypted);
    }

    public String getDecryptedPassword(String key) throws Exception {
        return Cryptography.getDecryptedPass(key, getEncryptedPassword());
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public long getRemindTimeInMilis() {
        return remindTimeInMilis;
    }

    public void setRemindTimeInMilis(long remindTimeInMilis) {
        this.remindTimeInMilis = remindTimeInMilis;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public long getLastRemindTime() {
        return lastRemindTime;
    }

    public void setLastRemindTime(long lastRemindTime) {
        this.lastRemindTime = lastRemindTime;
    }

    public boolean isToSyncWithServer() {
        return isToSyncWithServer;
    }

    public void setToSyncWithServer(boolean toSyncWithServer) {
        isToSyncWithServer = toSyncWithServer;
    }
}
