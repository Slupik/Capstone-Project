/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.views.addpass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.slupik.savepass.model.utils.Randomizer;

class PasswordGenerator {
    private final List<Character> charsToUse = new ArrayList<>();

    PasswordGenerator(boolean incSymbols, boolean incNumbers, boolean incLowChar, boolean incUpChar) {
        if(incSymbols) {
            Collections.addAll(charsToUse, '!', '@', '#', '$', '%', '^', '&', '*', '(', ')');
        }
        if(incNumbers) {
            Collections.addAll(charsToUse, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
        }
        if(incLowChar) {
            Collections.addAll(charsToUse, 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z');
        }
        if(incUpChar) {
            Collections.addAll(charsToUse, 'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
        }
    }

    String generatePassword(int length) {
        StringBuilder password = new StringBuilder();
        for(int i=0;i<length;i++) {
            password.append(charsToUse.get(Randomizer.randomInteger(0, charsToUse.size()-1)));
        }
        return password.toString();
    }
}
