package com.vnpay.anlmk.ui.activities.home

import android.Manifest
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.theartofdev.edmodo.cropper.CropImage
import com.vnpay.anlmk.R
//import com.vnpay.base.R
import com.vnpay.anlmk.bind.FindViewer
import com.vnpay.anlmk.bind.Tap
import com.vnpay.anlmk.data.repository.HomeModel
import com.vnpay.anlmk.ui.activities.Account.AccountActivity
import com.vnpay.anlmk.ui.activities.transfer.TransferActivity
import com.vnpay.anlmk.ui.adapters.home.BankingFunctionAdapter
import com.vnpay.anlmk.ui.adapters.home.FinanceFunctionAdapter
import com.vnpay.anlmk.ui.bases.BaseActivity
import com.vnpay.anlmk.ui.entites.FunctionEntity
import com.vnpay.anlmk.ui.views.HomeBannerViewPager
import de.hdodenhof.circleimageview.CircleImageView
//import io.fabric.sdk.android.services.concurrency.AsyncTask
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_home_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import com.vnpay.anlmk.utils.MiddleDividerItemDecoration


class HomeActivity : BaseActivity(), BankingFunctionAdapter.Callback {
    override val titleId: Int = 0

    override val model: HomeModel by viewModel()

    @FindViewer(R.id.rcv_homefunction1)
    protected lateinit var rcvFunctionBank: RecyclerView

    @FindViewer(R.id.rcv_homefunction2)
    protected lateinit var rcvFunction: RecyclerView

    @FindViewer(R.id.imgavtar)
    protected lateinit var avatar: CircleImageView



    override val layoutId: Int = R.layout.activity_homepage


    private val CAMERA_REQUEST = 100
    private val STORAGE_REQUEST = 200
    lateinit var cameraPermission: Array<String>
    lateinit var storagePermission: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("AAA_Home","onCreate Home")
        cameraPermission =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        rcvFunction.layoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        rcvFunctionBank.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        rcvFunction.addItemDecoration(
            MiddleDividerItemDecoration(this!!, MiddleDividerItemDecoration.ALL)
        )
//        rcvFunction.addItemDecoration(
//            DividerItemDecoration(
//                rcvFunction.getContext(),
//                DividerItemDecoration.HORIZONTAL
//            )
//        )

        model.apply {
            addFunctionBank()
            addFunc()
            functionBankingLive.observe(this@HomeActivity, Observer {
                rcvFunctionBank.adapter = BankingFunctionAdapter(
                    model.listFunctionBanking,
                    this@HomeActivity,
                    this@HomeActivity
                )
                rcvFunctionBank.adapter?.notifyDataSetChanged()
            })
            functionLive.observe(this@HomeActivity, Observer {
                rcvFunction.adapter = FinanceFunctionAdapter(model.listFunction)
                rcvFunction.adapter?.notifyDataSetChanged()
            })
        }


    }

    @RequiresApi(Build.VERSION_CODES.M)
    @Tap(R.id.imgavtar)
    fun TapChange() {


        if (!checkCameraPermission()!!) {
            requestCameraPermission()
        } else {
            pickFromGallery()
        }

    }

    // checking camera permissions
    open fun checkCameraPermission(): Boolean? {
        val result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        val result1 = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        return result && result1
    }

    // Requesting camera permission
    @RequiresApi(Build.VERSION_CODES.M)
    fun requestCameraPermission() {
        requestPermissions(
            cameraPermission,
            CAMERA_REQUEST
        )
    }

    private fun pickFromGallery() {
        CropImage.activity().start(this@HomeActivity)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_REQUEST -> {
                if (grantResults.size > 0) {
                    val camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromGallery()
                    } else {
                        Toast.makeText(
                            this,
                            "Please Enable Camera and Storage Permissions",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            STORAGE_REQUEST -> {
                if (grantResults.size > 0) {
                    val writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (writeStorageaccepted) {
                        pickFromGallery()
                    } else {
                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri

                var bitmap: Bitmap? = null
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                avatar.setImageBitmap(bitmap)


            }
        }
    }

    override fun onClick(item: FunctionEntity) {

        when (item.getId()) {
            1 -> startActivity(
                Intent(
                    this,
                    AccountActivity::class.java
                )
            )
            2 -> {
                var intent = Intent(
                    this,
                    TransferActivity::class.java
                )
                intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
                startActivity(
                    intent

                )
            }
        }


    }


}


//
//    override fun onBackPressed() {
//        when {
//            drawer.isDrawerOpen(GravityCompat.END) -> drawer.closeDrawer(GravityCompat.END)
//            model.isLoginedMB() -> logout()
//            else -> super.onBackPressed()
//        }
//    }
//
//    fun logout() {
//        confirm.newBuild().setNotice(R.string.decide_logout)
//            .addButtonLeft(R.string.cancel)
//            .addButtonRight(R.string.agree) {
//                AppData.g().logout()
//                confirm.dismiss()
//            }
//    }
//
//    private fun initView() {
//        if (!model.isActiveMB()) {
//            login.text = getString(R.string.active)
//        }
//    }
//
//
//    override fun onResume() {
//        super.onResume()
//        updateStateLogin()
//        displayAvatar()
//        updateCounter(OTTData.g().counterNotification)
//
//    }
//
//    private fun updateStateLogin() {
//        drawer.apply {
//            setDrawerLockMode(if (model.isLoginedMB()) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
//        }
//        avatar_top.apply {
//            visibility = if (model.isLoginedMB()) View.VISIBLE else View.GONE
//        }
//        login.apply {
//            visibility = if (!model.isLoginedMB()) View.VISIBLE else View.GONE
//        }
//        text_side_bar.apply {
//            text = getString(R.string.halo, AppData.g().fullName)
//        }
//    }
//
//    fun updateCounter(c: Int) {
//        runOnUiThread {
//            counter.visibility =
//                if (OTTData.g().counterNotification == 0) View.GONE else View.VISIBLE
//
//            counter.text = UIModel.getInstance().getBeautyNumber(c)
//            if (side_menu.adapter != null)
//                side_menu.adapter!!.notifyDataSetChanged()
//        }
//    }
//
//    fun displayAvatar() {
//        runOnUiThread {
//            side_avatar.setImageResource(R.drawable.default_avt)
//            avatar_top.setImageResource(R.drawable.default_avt)
//        }
//
//        if (!TextUtils.isEmpty(OTTData.g().avatar)) {
//
//            AsyncTask.execute {
//                try {
//                    val avt = UIModel.getInstance().providePicasso(this@HomeActivity)
//                        .load(OTTData.g().avatar)
//                        .transform(CircleTransform(this@HomeActivity))
//                        .get()
//
//                    if (avt != null)
//                        runOnUiThread {
//                            side_avatar.setImageBitmap(avt)
//                            avatar_top.setImageBitmap(avt)
//                        }
//                } catch (e: Exception) {
//                    side_avatar.postDelayed({ displayAvatar() }, 5000)
//                }
//            }
//
//        } else {
//            side_avatar.setImageResource(R.drawable.default_avt)
//            avatar_top.setImageResource(R.drawable.default_avt)
//        }
//    }
//
//    @Tap(R.id.login)
//    protected fun TapLogin() {
//        if (!model.isActiveMB()) {
//            val counter = Storage.g().getInt(Tags.COUNTER_TRYPHONE, 0)
//            startActivity(
//                Intent(this@HomeActivity, LoginActivity::class.java)
//            )
//            Storage.g().setInt(Tags.COUNTER_TRYPHONE, counter + 1)
//
//        } else
//            startActivityForResult(Intent(this, LoginActivity::class.java), Tags.REQUEST_CODE)
//    }
//
//    @Tap(R.id.avatar_top)
//    protected fun TapAvatarTop() {
//        drawer.openDrawer(GravityCompat.END)
//    }
//
//    @Tap(R.id.side_avatar)
//    protected fun TapAvatar() {
//        OTTBuilder.openSceneAvatar(this)
//    }
//
//    @Tap(R.id.history)
//    protected fun TapHistory() {
//    }
//
//    @Tap(R.id.notification)
//    protected fun TapNotification() {
//        startActivity(Intent(this, BankNotificationActivity::class.java))
//    }
//
//    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
//        if (intent == null || intent.component == null || canNextScene(intent) || AppData.g()
//                .isLogined()
//        )
//            super.startActivityForResult(intent, requestCode)
//        else if (model.isActiveMB()) {
//            startActivityForResult(
//                Intent(this, LoginActivity::class.java).putExtra(
//                    Tags.DESTINY,
//                    intent.component!!.className
//                ), Tags.LOGIN_CODE
//            )
//        }
//    }
//
//    private fun canNextScene(i: Intent?): Boolean {
//        if (i?.component == null)
//            return true
//        val c = i.component!!.className
//
//        val canGoContinue = c == LoginActivity::class.java.name ||
//                c == BrowserActivity::class.java.name ||
//                c == BankNotificationActivity::class.java.name
//        if (!canGoContinue && !model.isActiveMB()) {
//            TapLogin()
//            return false
//        }
//        return canGoContinue
//    }
