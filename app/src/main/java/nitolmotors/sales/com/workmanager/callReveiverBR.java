package nitolmotors.sales.com.workmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.UUID;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class callReveiverBR extends BroadcastReceiver {
    private static final String TAG = "callReveiverBR";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ===========>>>>>>>>>>>>");
        Toast.makeText(context, "I am from NCallLog. You got a call. Data to database", Toast.LENGTH_SHORT).show();

        callWorkmanager(context);
    }

    private void callWorkmanager(Context context) {
        Log.i(TAG, "callWorkmanager: ");
/*
        Data data = new Data.Builder()
                .putString(MyWorker.EXTRA_TITLE, "Message from Activity!")
                .putString(MyWorker.EXTRA_TEXT, "Hi! I have come from activity.")
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresBatteryNotLow(false)
                .build();
        */
        final OneTimeWorkRequest simpleRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
              //  .setInputData(data)
             //   .setConstraints(constraints)
                .addTag("simple_work")
                .build();

        final UUID workId = simpleRequest.getId();
        Toast.makeText(context, "workId:" + workId, Toast.LENGTH_SHORT).show();
        WorkManager.getInstance().enqueue(simpleRequest);

    }


}
