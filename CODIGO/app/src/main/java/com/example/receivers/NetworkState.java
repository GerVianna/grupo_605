package com.example.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.services.ServiceManager;

public class NetworkState extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (!checkInternet(context)) {
            Toast.makeText(context, "Conexion perdida, verifique su conexion a internet", Toast.LENGTH_LONG).show();
        }
    }

    boolean checkInternet(Context context) {
        ServiceManager serviceManager = new ServiceManager(context);
        if (serviceManager.isNetworkAvailable()) {
            return true;
        } else {
            return false;
        }
    }
}
