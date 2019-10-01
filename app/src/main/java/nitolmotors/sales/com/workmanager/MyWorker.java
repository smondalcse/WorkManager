package nitolmotors.sales.com.workmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyWorker extends Worker {

    private static final String TAG = "MyWorker";

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_TEXT = "text";
    public static final String KEY_TASK_OUTPUT = "output_message";

    public static Boolean isRunning = false;
    public static final int FOUR_MINUTES = 10000; // 1 min = 60000 milisec

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {


        //Executed on different thread
/*
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/

/*
        Data data = getInputData();
        String title = data.getString(MainActivity.EXTRA_TITLE);
        String text = data.getString(MainActivity.EXTRA_TEXT);

        Data output = new Data.Builder()
                .putString(data.getString(MainActivity.EXTRA_OUTPUT_MESSAGE), "I have come from MyWorker!")
                .build();

        setOutputData(output);
*/

        //sendNotification("Simple Work Manager", "I have been send by WorkManager!");
        sendNotification("NCallLog", "I am from NCallLog WM");

        return Result.SUCCESS;
    }


//    Handler mHandler = new Handler();
//    Runnable mHandlerTask = new Runnable() {
//        @Override
//        public void run() {
//            if (!isRunning) {
//                DoIntervalWork();
//            }
//            mHandler.postDelayed(mHandlerTask, FOUR_MINUTES);
//        }
//    };

//    private void DoIntervalWork() {
//        Log.d(TAG, "DoIntervalWork: ");
//        Toast.makeText(getApplicationContext(), "I am a work manager", Toast.LENGTH_LONG).show();
//    }


    public void sendNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1, notification.build());

    }
}
