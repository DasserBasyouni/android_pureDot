package com.g7.soft.pureDot.util

import android.Manifest
import android.content.Context
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

object PermissionsHelper {
    /*private var locationToken: PermissionToken? = null
    private var reRequestLocationPermission = false*/


    fun requestLocationPermission(
        context: Context,
        grantedRunnable: Runnable = Runnable { /* todo default runner if needed*/ },
        deniedRunnable: Runnable = Runnable { /* todo default runner if needed*/ }
    ) {
        Dexter.withContext(context)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted())
                        grantedRunnable.run()
                    else if (report.isAnyPermissionPermanentlyDenied || report.areAllPermissionsGranted())
                        deniedRunnable.run()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    //token.continuePermissionRequest()
                }
            }).check()


        /*reRequestLocationPermission = true
        if (locationToken != null) locationToken!!.continuePermissionRequest()
        requestLocationPermissionCore(activity, grantedRunnable, deniedRunnable, permanentDeniedRunnable)*/
    }

    /*fun requestStoragePermission(
        context: Context,
        grantedRunnable: Runnable = Runnable { /* todo default runner if needed*/ },
        deniedRunnable: Runnable = Runnable { /* todo default runner if needed*/ }
    ) {
        Dexter.withContext(context)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted())
                        grantedRunnable.run()
                    else if (report.isAnyPermissionPermanentlyDenied || report.areAllPermissionsGranted())
                        deniedRunnable.run()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    //token.continuePermissionRequest()
                }
            }).check()
    }*/

    /*private fun requestLocationPermissionCore(
        activity: LocaleAwareCompatActivity, grantedRunnable: Runnable,
        deniedRunnable: Runnable? = null, permanentDeniedRunnable: Runnable? = null
    ) {
        val finalDeniedRunnable = deniedRunnable ?: Runnable {
            ProjectDialogUtils.showAskingDialog(
                activity = activity,
                titleResId = R.string.app_hint_msg_location_is_needed,
                successfulRunnable = {
                    requestLocationPermission(
                        activity,
                        grantedRunnable,
                        finalDeniedRunnable,
                        finalPermanentDeniedRunnable
                    )
                }
            )
        }
        val finalPermanentDeniedRunnable = permanentDeniedRunnable ?: Runnable {
            ProjectDialogUtils
                .showAppWarningDialog(activity, activity.getString(R.string.error_msg_location_permentant_denied))
        }

        Dexter.withActivity(activity)
            .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.isAnyPermissionPermanentlyDenied) finalPermanentDeniedRunnable.run() else if (report.areAllPermissionsGranted()) {
                        val manager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            activity.startActivityForResult(
                                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                ProjectConstant.RESULTS_CODE_GPS_PERMISSION
                            )
                        } else grantedRunnable.run()
                    } else finalDeniedRunnable.run()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    locationToken = token
                    finalDeniedRunnable.run()
                    if (reRequestLocationPermission) {
                        locationToken!!.continuePermissionRequest()
                        reRequestLocationPermission = false
                    }
                }
            }).check()
    }*/
}