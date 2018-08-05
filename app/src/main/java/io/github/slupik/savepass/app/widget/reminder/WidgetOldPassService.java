/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.widget.reminder;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetOldPassService extends RemoteViewsService {
    public WidgetOldPassService() {
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetOldPassListFactory(this.getApplicationContext());
    }

}
