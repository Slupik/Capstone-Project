/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.model.cryptography;

import org.junit.Assert;
import org.junit.Test;

public class AESencTest {

    @Test
    public void testEncryption() throws Exception {
        doAllTestsForKey("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        doAllTestsForKey("\uD83C\uDDF5\uD83C\uDDF1");
        doAllTestsForKey("\uD83C\uDF4C");
        doAllTestsForKey("ઊઠઊ");
        doAllTestsForKey("Ùąý̛̿Έω\u052A\u058D֏ؕٶజ్ఞ\u200Cా♠");
    }

    private void doAllTestsForKey(String key) throws Exception {
        checkKey(key);
        checkEncryption(key);

    }

    private void checkKey(String key) {
        byte[] seed = AESenc.getSeed(key);
        Assert.assertEquals(16, seed.length);
    }

    private static final String[] EXAMPLE_PASSWORDS = new String[]{
            "I really like polish signs like ąóęł and UTF-8 ex ♠ they are awesome! \u052A\u058Dؽض׆טٍؿὉỪﺳ  ﷺ¶çǅ abజ్ఞ\u200Cాc",
            "abc",
            "\uD83C\uDDF5\uD83C\uDDF1",
            "\uD83C\uDF4C",
            "a",
            "ઊઠઊ"
    };
    private void checkEncryption(String key) throws Exception {
        for(String pass:EXAMPLE_PASSWORDS){
            Assert.assertEquals(
                    "Invalid password encryption or decryption",
                    pass,
                    AESenc.decrypt(key, AESenc.encrypt(key, pass)));
        }
    }
}