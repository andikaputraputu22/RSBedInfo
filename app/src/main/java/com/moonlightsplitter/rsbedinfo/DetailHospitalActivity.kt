package com.moonlightsplitter.rsbedinfo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.moonlightsplitter.rsbedinfo.models.DataHospital

class DetailHospitalActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_HOSPITAL = "extra_hospital"
    }

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_hospital)
        context = this

        val hospital = intent.getSerializableExtra(EXTRA_HOSPITAL) as DataHospital
        Toast.makeText(context, hospital.name, Toast.LENGTH_LONG).show()
    }
}