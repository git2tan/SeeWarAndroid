package com.example.seawar;

import android.os.Handler;

/**
 * Created by Artem on 27.05.2017.
 */

public interface IRefreshable {
    public void refresh();
    public Handler getHandler();
    IModel.ModelState getRelevantState();
}
