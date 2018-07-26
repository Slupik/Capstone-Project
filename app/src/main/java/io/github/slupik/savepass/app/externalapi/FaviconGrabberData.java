/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.externalapi;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FaviconGrabberData {
    @SerializedName("domain")
    private String domain;
    @SerializedName("icons")
    private List<Icon> icons;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<Icon> getIcons() {
        return icons;
    }

    public void setIcons(List<Icon> icons) {
        this.icons = icons;
    }

    @NonNull
    public String getUrlForBiggestIcon(){
        Icon icon = getBiggestIcon();
        if(icon==null) {
            return "";
        } else {
            return icon.getSrc();
        }
    }

    @Nullable
    public Icon getBiggestIcon(){
        Icon biggestIcon = null;
        int biggestSize = 0;
        for(Icon icon :icons) {
            if(icon.getSize()>=biggestSize) {
                biggestIcon = icon;
                biggestSize = icon.getSize();
            }
        }
        return biggestIcon;
    }

    public static FaviconGrabberData fromGson(String json) {
        return new Gson().fromJson(json, FaviconGrabberData.class);
    }


    public class Icon {
        @SerializedName("src")
        private String src;
        @SerializedName("sizes")
        private String sizes;

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getSizes() {
            return sizes;
        }

        public void setSizes(String sizes) {
            this.sizes = sizes;
        }

        public int getSize(){
            if(!isEmpty(sizes)) {
                String[] parts = sizes.split("x");
                int a = Integer.parseInt(parts[0]);
                int b = Integer.parseInt(parts[1]);
                return a*b;
            }
            return 0;
        }
    }

    private static boolean isEmpty(String data){
        return data == null || data.length()==0;
    }
}
