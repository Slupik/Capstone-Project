/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.externalapi;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;

public class FaviconGrabberTest {

    @Test
    public void getAddressForAPI() throws MalformedURLException {
        Assert.assertEquals("github.com", FaviconGrabber.getAddressForAPI("https://github.com/"));
    }
}