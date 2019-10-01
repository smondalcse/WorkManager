package nitolmotors.sales.com.workmanager;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.Person;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.work.Configuration;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_TEXT = "text";
    public static final String EXTRA_OUTPUT_MESSAGE = "output_message";

    private TextView mTextView;
    private long repeatInterval = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.textView);

        Data data = new Data.Builder()
                .putString(MyWorker.EXTRA_TITLE, "Message from Activity!")
                .putString(MyWorker.EXTRA_TEXT, "Hi! I have come from activity.")
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresBatteryNotLow(false)
                .build();

        final OneTimeWorkRequest simpleRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInputData(data)
                .setConstraints(constraints)
                .addTag("simple_work")
                .build();

        final PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES)
                .addTag("periodic_work")
                .build();

        // location worker
        final PeriodicWorkRequest periodicLocationWork = new PeriodicWorkRequest.Builder(MyLocationWorker.class, repeatInterval, TimeUnit.MINUTES)
                .addTag("Location-work-manager")
                .build();

        final UUID workId = simpleRequest.getId();

        findViewById(R.id.simpleWorkButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstance().enqueue(simpleRequest);
            }
        });

        findViewById(R.id.cancelWorkButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //WorkManager.getInstance().cancelAllWorkByTag("simple_work");
                WorkManager.getInstance().cancelWorkById(workId);
            }
        });

        findViewById(R.id.periodicWorkButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstance().enqueue(periodicWorkRequest);
            }
        });

        findViewById(R.id.cancelPeriodicWorkButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstance().cancelWorkById(periodicWorkRequest.getId());
            }
        });

        findViewById(R.id.periodicLocationWorkButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  WorkManager.getInstance().enqueue(periodicLocationWork);
                WorkManager.getInstance().enqueueUniquePeriodicWork("Location", ExistingPeriodicWorkPolicy.REPLACE, periodicLocationWork);
            }
        });

        findViewById(R.id.cancelPeriodicLocationWorkButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstance().cancelWorkById(periodicLocationWork.getId());
            }
        });

        WorkManager.getInstance().getWorkInfoByIdLiveData(simpleRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {

                        if (workInfo != null) {

                            if (workInfo.getState().isFinished()) {

                                Data data = workInfo.getOutputData();

                                String output = data.getString(MyWorker.KEY_TASK_OUTPUT);

                             //   textView.append(output + "\n");
                            }

                            String status = workInfo.getState().name();
                         //   textView.append(status + "\n");
                        }
                    }
                });
    }


    /*
    PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(MyWorker.class, repeatInterval, TimeUnit.MINUTES)
            .addTag("Location")
            .build();

     */
}