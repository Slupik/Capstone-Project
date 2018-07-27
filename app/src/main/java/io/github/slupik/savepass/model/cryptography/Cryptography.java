/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.model.cryptography;

import android.content.Context;
import android.text.TextUtils;

import io.github.slupik.savepass.data.settings.MainPasswordSettings;

public class Cryptography {
    private static final String BCRYPT_SALT = "$2a$10$RJoAVj/HO3ASEbrTDMmIFO";

    private Context mContext;

    public Cryptography(Context context) {
        mContext = context;
    }

    public boolean isMainPasswordSaved(){
        String realHash = new MainPasswordSettings(mContext).getPasswordAppHash();
        return !TextUtils.isEmpty(realHash);
    }

    public boolean isValidMainPassword(String password){
        String realHash = new MainPasswordSettings(mContext).getPasswordAppHash();
        String checkingHash = getHashedString(password);
        return realHash.equals(checkingHash);
    }

    public void setAndEncryptMainPassword(String password) {
        String hash = getHashedString(password);
        new MainPasswordSettings(mContext).setPasswordAppHash(hash);
    }

    private static String getHashedString(String string){
        return BCrypt.hashpw(string, BCRYPT_SALT);
    }

    public static String getEncryptedPass(String key, String password) throws Exception {
        return AESenc.encrypt(key, password);
    }

    public static String getDecryptedPass(String key, String encryptedPassword) throws Exception {
        return AESenc.decrypt(key, encryptedPassword);
    }
}
