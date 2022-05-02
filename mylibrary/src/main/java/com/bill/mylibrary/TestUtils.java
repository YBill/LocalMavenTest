package com.bill.mylibrary;

import android.content.Context;
import android.widget.Toast;

import me.drakeet.support.toast.ToastCompat;

/**
 * Created by Bill on 2022/5/2.
 */

public class TestUtils {

    public static void toast(Context context, String msg) {
//        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

        ToastCompat.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
