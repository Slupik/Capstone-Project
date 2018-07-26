/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.externalapi;

import org.junit.Assert;
import org.junit.Test;

public class FaviconGrabberDataTest {
    private static final String GITHUB_DATA = "{\"domain\":\"github.com\",\"icons\":[{\"src\":\"https://assets-cdn.github.com/favicon.ico\",\"type\":\"image/x-icon\"},{\"src\":\"https://assets-cdn.github.com/pinned-octocat.svg\"},{\"src\":\"https://github.com/fluidicon.png\"},{\"src\":\"https://assets-cdn.github.com/apple-touch-icon-114x114.png\",\"sizes\":\"114x114\"},{\"src\":\"https://assets-cdn.github.com/apple-touch-icon-120x120.png\",\"sizes\":\"120x120\"},{\"src\":\"https://assets-cdn.github.com/apple-touch-icon-144x144.png\",\"sizes\":\"144x144\"},{\"src\":\"https://assets-cdn.github.com/apple-touch-icon-152x152.png\",\"sizes\":\"152x152\"},{\"src\":\"https://assets-cdn.github.com/apple-touch-icon-180x180.png\",\"sizes\":\"180x180\"},{\"src\":\"https://assets-cdn.github.com/apple-touch-icon-57x57.png\",\"sizes\":\"57x57\"},{\"src\":\"https://assets-cdn.github.com/apple-touch-icon-60x60.png\",\"sizes\":\"60x60\"},{\"src\":\"https://assets-cdn.github.com/apple-touch-icon-72x72.png\",\"sizes\":\"72x72\"},{\"src\":\"https://assets-cdn.github.com/apple-touch-icon-76x76.png\",\"sizes\":\"76x76\"},{\"src\":\"https://github.com/favicon.ico\",\"type\":\"image/x-icon\"}]}";

    @Test
    public void getBiggestIcon() {
        Assert.assertEquals("https://assets-cdn.github.com/apple-touch-icon-180x180.png", getParsedData().getBiggestIcon().getSrc());
    }

    @Test
    public void getUrlForBiggestIcon() {
        Assert.assertEquals("https://assets-cdn.github.com/apple-touch-icon-180x180.png", getParsedData().getUrlForBiggestIcon());
    }

    private static FaviconGrabberData getParsedData() {
        return FaviconGrabberData.fromGson(GITHUB_DATA);
    }
}