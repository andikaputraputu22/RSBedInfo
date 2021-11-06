package com.moonlightsplitter.rsbedinfo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.moonlightsplitter.rsbedinfo.models.DataHospital

class DetailHospitalActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_HOSPITAL = "extra_hospital"
    }

    lateinit var context: Context
    lateinit var nameHospital: TextView
    lateinit var addressHospital: TextView
    lateinit var phoneHospital: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_hospital)
        context = this

        val hospital = intent.getSerializableExtra(EXTRA_HOSPITAL) as DataHospital

        initComponents()
        loadData(hospital)
    }

    private fun initComponents() {
        nameHospital = findViewById(R.id.nameHospital)
        addressHospital = findViewById(R.id.addressHospital)
        phoneHospital = findViewById(R.id.phoneHospital)
    }

    private fun loadData(hospital: DataHospital) {
        nameHospital.text = hospital.name?: "-"
        addressHospital.text = hospital.address?: "-"
        phoneHospital.text = hospital.phone?: "-"
    }
}