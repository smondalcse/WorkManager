package nitolmotors.sales.com.workmanager;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyLocationWorker extends Worker {

    private static final String TAG = "MyWorker";
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    /**
     * The current location.
     */
    private Location mLocation;
    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    private Context mContext;

    private String fromRegRegCode, fromRegMobile, fromRegGUID, fromRegImei, clientIP;

    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;

    public MyLocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: Done");
        //mContext.startService(new Intent(mContext, LocationUpdatesService.class));
        Log.d(TAG, "onStartJob: STARTING JOB..");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
            }
        };

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        try {
            mFusedLocationClient
                    .getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                Log.d(TAG, "onComplete: calling: getLatitude" + String.valueOf(task.getResult().getLatitude()));
                                Log.d(TAG, "onComplete: calling: getLongitude" + String.valueOf(task.getResult().getLongitude()));
                                mLocation = task.getResult();
/*
                                String currentTime = CommonUses.getDateToStoreInLocation();
                                String mLatitude = String.valueOf(mLocation.getLatitude());
                                String mLongitude = String.valueOf(mLocation.getLongitude());

                                LocationHistoryTable table = new LocationHistoryTable();
                                table.setLatitude(mLatitude);
                                table.setLongitude(mLongitude);
                                table.setUpdateTime(currentTime);
                                table.setIsUploaded(CommonUses.PENDING);

                                LocationHistoryTableDao tableDao = SohamApplication.daoSession.getLocationHistoryTableDao();
                                tableDao.insert(table);
 */

                                Log.d(TAG, "Location : " + mLocation);
                                mFusedLocationClient.removeLocationUpdates(mLocationCallback);

                                /**
                                 * Upload on server if network available
                                 */
//                                if (Util.isNetworkAvailable(mContext)) {
//                                    checkForServerIsUP();
//                                }

                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }

        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    null);
        } catch (SecurityException unlikely) {
            //Utils.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
       // return Result.success();
        return Result.SUCCESS;
    }
}