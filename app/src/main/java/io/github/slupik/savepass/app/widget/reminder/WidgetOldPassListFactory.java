/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.widget.reminder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import io.github.slupik.savepass.R;
import io.github.slupik.savepass.app.externalapi.FaviconGrabber;
import io.github.slupik.savepass.app.views.viewpass.ShowPassActivity;
import io.github.slupik.savepass.data.password.room.EntityPassword;

class WidgetOldPassListFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private final List<EntityPassword> mPasswords = new ArrayList<>();

    WidgetOldPassListFactory(Context context) {
        mContext = context;
        mPasswords.clear();
        mPasswords.addAll(OldPassWidgetProvider.mPasswordList);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mPasswords.clear();
        mPasswords.addAll(OldPassWidgetProvider.mPasswordList);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mPasswords.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mPasswords == null || position>= mPasswords.size()) return null;
        return getItemView(mPasswords.get(position));
    }

    private RemoteViews getItemView(final EntityPassword bean) {
        final RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_old_pass_item);
        views.setTextViewText(R.id.wop_pass_header, bean.getPasswordName());
        views.setTextViewText(R.id.wop_pass_login, bean.getLogin());
        FaviconGrabber.getDataForUrl(bean.getWebAddress(), new FaviconGrabber.GrabberCallback() {
            @Override
            public void onUrlReturn(String url) {
                Picasso.with(getContext()).load(url).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        views.setImageViewBitmap(R.id.wop_favicon, bitmap);
                    }
                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {}
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {}
                });
            }
            @Override
            public void onError() {}
        });

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(ShowPassActivity.ARG_DATA, new Gson().toJson(bean));
        views.setOnClickFillInIntent(R.id.wop_pass_container, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private Context getContext() {
        return mContext;
    }
}