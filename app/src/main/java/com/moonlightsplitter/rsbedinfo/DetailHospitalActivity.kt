package com.moonlightsplitter.rsbedinfo

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.moonlightsplitter.rsbedinfo.api.Client
import com.moonlightsplitter.rsbedinfo.models.DataHospital
import com.moonlightsplitter.rsbedinfo.models.ModelMaps
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailHospitalActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_HOSPITAL = "extra_hospital"
    }

    lateinit var context: Context
    lateinit var nameHospital: TextView
    lateinit var addressHospital: TextView
    lateinit var phoneHospital: TextView
    lateinit var btnCall: ImageButton
    lateinit var btnMap: ImageButton
    lateinit var toolbar: Toolbar
    lateinit var viewMap: RelativeLayout

    private var idHospital: String? = null
    private var phoneNumber: String? = null
    private var lat: String? = null
    private var long: String? = null
    private var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_hospital)
        context = this

        val hospital = intent.getSerializableExtra(EXTRA_HOSPITAL) as DataHospital

        initComponents()
        initToolbar()
        loadData(hospital)
        initClick()
    }

    private fun initComponents() {
        nameHospital = findViewById(R.id.nameHospital)
        addressHospital = findViewById(R.id.addressHospital)
        phoneHospital = findViewById(R.id.phoneHospital)
        btnCall = findViewById(R.id.btnCall)
        btnMap = findViewById(R.id.btnMap)
        toolbar = findViewById(R.id.toolbar)
        viewMap = findViewById(R.id.viewMap)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.title = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadData(hospital: DataHospital) {
        nameHospital.text = hospital.name?: "-"
        addressHospital.text = hospital.address?: "-"
        phoneHospital.text = hospital.phone?: "-"

        idHospital = hospital.id
        if (hospital.phone != null || !hospital.equals("")) {
            phoneNumber = hospital.phone
        }

        getMaps()
    }

    private fun getMaps() {
        Client.instance.getHospitalMaps(idHospital!!).enqueue(object: Callback<ModelMaps>{
            override fun onResponse(call: Call<ModelMaps>, response: Response<ModelMaps>) {
                if (response.code() == 200) {
                    viewMap.visibility = View.VISIBLE
                    lat = response.body()?.data?.lat
                    long = response.body()?.data?.long
                    title = response.body()?.data?.name
                }
            }

            override fun onFailure(call: Call<ModelMaps>, t: Throwable) {
                //
            }
        })
    }

    private fun initClick() {
        btnCall.setOnClickListener {
            if (phoneNumber != null) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                startActivity(intent)
            } else {
                Toast.makeText(context, getString(R.string.no_phone_number), Toast.LENGTH_LONG).show()
            }
        }

        btnMap.setOnClickListener {
            if (lat != null && long != null) {
                val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$long&mode=d&title=$title")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            } else {
                Toast.makeText(context, getString(R.string.no_place), Toast.LENGTH_LONG).show()
            }
        }
    }
}