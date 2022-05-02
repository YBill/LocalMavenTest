package com.bill.mylibrary;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Bill on 2022/5/2.
 */

public class TestUtils {

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
