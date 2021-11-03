package com.g7.soft.pureDot.worker

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.repo.OrderRepository
import com.g7.soft.pureDot.repo.UserRepository
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import java.io.IOException

class LocationUploadWorker(
    private val context: Context,
    params: WorkerParameters
) :
    CoroutineWorker(context, params) {

    companion object {
        private const val ACTION = "com.g7.soft.pureDot.location"
        private const val TIMEOUT = 5_000L // 5 sec
        private const val REQUEST = 1000
    }

    @RequiresPermission(allOf = [ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION])
    override suspend fun doWork(): Result {
        val location = obtain() ?: return Result.retry()
        return when (upload(location)) {
            true -> Result.success()
            false -> Result.failure()
        }
    }

    @RequiresPermission(allOf = [ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION])
    private suspend fun obtain(): Location? =
        // coroutines which will cancel all child coroutines if time out reached
        withTimeoutOrNull(TIMEOUT) {
            // this block will suspend until continuation won't be invoked
            suspendCancellableCoroutine { continuation ->
                // location client and request
                val client = LocationServices.getFusedLocationProviderClient(context)
                val request =
                    LocationRequest().apply { priority = LocationRequest.PRIORITY_HIGH_ACCURACY }
                // init intent and receiver
                val intent = PendingIntent.getBroadcast(context, REQUEST, Intent(ACTION), 0)
                val receiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context, data: Intent) {
                        val location = LocationResult.extractResult(data)?.lastLocation
                        // stop listening
                        client.removeLocationUpdates(intent)
                        context.unregisterReceiver(this)
                        // resume suspended continuation which basically means return location as function
                        continuation.resume(location) {
                            Timber.e(it)
                        }
                    }
                }
                // start listening
                context.registerReceiver(receiver, IntentFilter(ACTION))
                client.requestLocationUpdates(request, intent)

                // stop listening if cancelled by timeout or other reason
                continuation.invokeOnCancellation {
                    client.removeLocationUpdates(intent)
                    context.unregisterReceiver(receiver)
                }
            }
        }

    private suspend fun upload(location: Location): Boolean {
        Log.e("Z_", "location: $location")
        //lifecycleScope.launch {
        val tokenId = UserRepository("").getTokenId(context)

        var areaName: String? = null
        val geocoder = Geocoder(context, context.currentLocale)
        try {
            val listAddresses: List<Address>? =
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (null != listAddresses && listAddresses.isNotEmpty()) {
                areaName = listAddresses[0].adminArea
            }
        } catch (e: IOException) {
            Timber.e(e)
            //e.printStackTrace()
        }

        // TODO clean up for performance if needed
        return OrderRepository(context.currentLocale.toLanguageTag()).setCurrentLocation(
            tokenId = tokenId,
            latitude = location.latitude,
            longitude = location.longitude,
            areaName = areaName,
        )?.status == ApiConstant.Status.SUCCESS.value

        //}
    }
}