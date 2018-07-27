/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.model.cryptography;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

class AESenc {
    private static final String SALT_FOR_PASS = "$2a$10$8Kc1lCgog/8KOTIs7/mYHO";
    private static final String ALGO = "AES";

    public static String encrypt(String key, String data) throws Exception {
        return encrypt(getSeed(key), data);
    }

    public static String encrypt(byte[] seed, String data) throws Exception {
        Key key = generateKey(seed);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());

        return Base64.encode(encVal);
    }

    public static String decrypt(String key, String encryptedData) throws Exception {
        return decrypt(getSeed(key), encryptedData);
    }

    public static String decrypt(byte[] seed, String encryptedData) throws Exception {
        Key key = generateKey(seed);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.decode(encryptedData);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    private static final int MAX_KEY_SIZE = 16;
    public static byte[] getSeed(String key) {
        String keyHash = BCrypt.hashpw(key, SALT_FOR_PASS);
        String saltedHash = keyHash.substring(0, MAX_KEY_SIZE);
        int saltLength = SALT_FOR_PASS.length();
        if(keyHash.length()>saltLength+MAX_KEY_SIZE){
            saltedHash = keyHash.substring(saltLength, saltLength+MAX_KEY_SIZE);
        }
        return saltedHash.getBytes();
    }

    /**
     * Generate a new encryption key.
     */
    private static Key generateKey(byte[] seed) throws Exception {
        return new SecretKeySpec(seed, ALGO);
    }
}