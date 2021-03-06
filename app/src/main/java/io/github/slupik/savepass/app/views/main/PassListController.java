/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.views.main;

import android.support.v7.widget.RecyclerView;

public interface PassListController {
    void setAdapter(RecyclerView.Adapter<PassListAdapter.ViewHolder> adapter);
}
