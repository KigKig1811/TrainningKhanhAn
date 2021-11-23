package com.vnpay.anlmk.data.repository

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.MutableLiveData
//import com.vnpay.base.R
import com.vnpay.anlmk.data.constants.Tags
import com.vnpay.anlmk.data.repository.impl.CommonRepo
import com.vnpay.anlmk.data.repository.impl.LoginRepo
import com.vnpay.anlmk.di.ResourceProvider
import com.vnpay.anlmk.networks.AppData
import com.vnpay.anlmk.ui.bases.BaseViewModel
import com.vnpay.anlmk.utils.SingleLiveEvent
import com.vnpay.anlmk.walks.GPSTracker
import com.vnpay.supersecure.Storage
import com.theartofdev.edmodo.cropper.CropImage
import com.vnpay.anlmk.R
import com.vnpay.anlmk.ui.entites.FunctionEntity


class HomeModel(val repo: CommonRepo, val loginRepo: LoginRepo, val resource: ResourceProvider) :
    BaseViewModel() {
    var functionLive = MutableLiveData<List<FunctionEntity>>()
    var listFunction = ArrayList<FunctionEntity>()
    var functionBankingLive = MutableLiveData<List<FunctionEntity>>()
    var listFunctionBanking = ArrayList<FunctionEntity>()

    fun addFunc() {
        listFunction.add(
            FunctionEntity(
                1,
                resource.getString(R.string.setting_otp),
                R.drawable.ic_otp,
                R.color.white_filled
            )
        )
        listFunction.add(
            FunctionEntity(
                2,
                resource.getString(R.string.finger),
                R.drawable.fingerprintred_2,
                R.color.white_filled
            )
        )
        listFunction.add(
            FunctionEntity(
                3,
                resource.getString(R.string.change_password),
                R.drawable.doimk,
                R.color.white_filled
            )
        )
        listFunction.add(
            FunctionEntity(
                4,
                resource.getString(R.string.manage_contacts),
                R.drawable.qldanhba,
                R.color.white_filled
            )
        )
        listFunction.add(
            FunctionEntity(
                5,
                resource.getString(R.string.price_lookup),
                R.drawable.tracuutygia,
                R.color.white_filled
            )
        )
        listFunction.add(
            FunctionEntity(
                6,
                resource.getString(R.string.Q_A),
                R.drawable.hoidap,
                R.color.white_filled
            )
        )
        listFunction.add(
            FunctionEntity(
                7,
                resource.getString(R.string.info_app),
                R.drawable.thongtinungdung,
                R.color.white_filled
            )
        )

        functionLive.value = listFunction

    }

    fun addFunctionBank() {
        listFunctionBanking.add(
            FunctionEntity(
                1,
                resource.getString(R.string.account),
                R.drawable.ic_account,
                R.color.white_filled
            )
        )
        listFunctionBanking.add(
            FunctionEntity(
                2,
                resource.getString(R.string.titleTransferActivity),
                R.drawable.ic_transfer,
                R.color.white_filled
            )
        )
        listFunctionBanking.add(
            FunctionEntity(
                3,
                resource.getString(R.string.titleApprove),
                R.drawable.ic_approve,
                R.color.white_filled
            )
        )
        listFunctionBanking.add(
            FunctionEntity(
                4,
                resource.getString(R.string.titleSearch),
                R.drawable.ic_search,
                R.color.white_filled
            )
        )
        functionBankingLive.value = listFunctionBanking
    }

//    fun getFunctionBank(): LiveData<List<String>> {
//
//        return functionBankingLive
//    }


//    fun changeAvatar(act: Activity, code: String) {
//
//        if (code.equals("camera")) {
//            if (!checkCameraPermission(act)!!) {
//                requestCameraPermission(act)
//            } else {
//                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                try {
//                    act.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//                } catch (e: ActivityNotFoundException) {
//                    // display error state to the user
//                }
//            }
//        } else {
//            if (!checkStoragePermission(act)!!) {
//                requestStoragePermission(act)
//            } else {
//
//            }
//        }
//
//
////
//    }
//
//
//    fun pickFromGallery(act: Activity) {
//        CropImage.activity().start(act)
//    }
//
//    // checking camera permissions
//    fun checkCameraPermission(act: Activity): Boolean? {
//        val result = checkSelfPermission(
//            act,
//            Manifest.permission.CAMERA
//        ) == PackageManager.PERMISSION_GRANTED
//        val result1 = checkSelfPermission(
//            act,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED
//        return result && result1
//    }
//
//    // Requesting camera permission
//    fun requestCameraPermission(act: Activity) {
//        requestPermissions(
//            act,
//            cameraPermission,
//            CAMERA_REQUEST
//        )
//    }
//
//    // checking storage permissions
//    fun checkStoragePermission(act: Activity): Boolean? {
//        return checkSelfPermission(
//            act,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    // Requesting  gallery permission
//    fun requestStoragePermission(act: Activity) {
//        requestPermissions(
//            act,
//            storagePermission,
//            STORAGE_REQUEST
//        )
//    }


    //
    annotation class State(
        val ACTIVE: Int = 1,
        val LOGIN: Int = 2
    )

    fun isActiveMB(): Boolean {
        return Storage.g().getLong(Tags.CLIENT_ID, 0L) != 0L
    }

    fun isLoginedMB(): Boolean {
        return !TextUtils.isEmpty(AppData.g().sessionId)
    }


    fun openGPS(act: Activity) {
        if (ActivityCompat.checkSelfPermission(
                act,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                act,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1005
            )
        } else {
            GPSTracker.run(act)
        }
    }

}


