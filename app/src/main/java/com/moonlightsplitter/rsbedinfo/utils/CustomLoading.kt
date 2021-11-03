package com.moonlightsplitter.rsbedinfo.utils

import android.app.Activity
import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import com.moonlightsplitter.rsbedinfo.R

class CustomLoading(val mActivity: Activity) {

    private lateinit var dialog: AlertDialog

    fun startLoading() {
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.loading, null)
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()
        if (dialog.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        dialog.show()
    }

    fun isDismiss() {
        dialog.dismiss()
    }
}