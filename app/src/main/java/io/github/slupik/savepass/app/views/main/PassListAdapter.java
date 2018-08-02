/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.views.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.slupik.savepass.R;
import io.github.slupik.savepass.app.MyApplication;
import io.github.slupik.savepass.app.externalapi.FaviconGrabber;
import io.github.slupik.savepass.data.password.PasswordRepository;
import io.github.slupik.savepass.data.password.room.EntityPassword;

public class PassListAdapter extends RecyclerView.Adapter<PassListAdapter.ViewHolder> {

    private final List<EntityPassword> mValues = new ArrayList<>();
    private final PassListListener mListener;

    public PassListAdapter(MyApplication app, PassListListener listener) {
        mValues.add(PasswordRepository.getExampleEntity(app));
        mValues.add(PasswordRepository.getExampleEntity(app));
        mValues.add(PasswordRepository.getExampleEntity(app));
        mListener = listener;
    }

    public void setNewData(List<EntityPassword> data) {
        mValues.clear();
        if(data!=null) {
            mValues.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pass_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.loadData(mValues.get(position));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;

        @BindView(R.id.itemFavicon)
        ImageView favicon;
        @BindView(R.id.itemPassHeader)
        TextView header;
        @BindView(R.id.itemPassLogin)
        TextView login;
        @BindView(R.id.itemPassDesc) @Nullable
        TextView desc;

        EntityPassword mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }

        public void loadData(EntityPassword password) {
            mItem = password;

            login.setText(password.getLogin());
            header.setText(password.getPasswordName());
            favicon.setImageDrawable(null);
            if(null != desc) {
                if(TextUtils.isEmpty(password.getShortDesc())) {
                    desc.setVisibility(View.GONE);
                } else {
                    desc.setVisibility(View.VISIBLE);
                    desc.setText(password.getShortDesc());
                }
            }

            FaviconGrabber.getDataForUrl(password.getWebAddress(), new FaviconGrabber.GrabberCallback() {
                @Override
                public void onUrlReturn(String url) {
                    Picasso.with(favicon.getContext()).load(url).fit().into(favicon);
                }

                @Override
                public void onError() {
                }
            });
        }
    }
}
