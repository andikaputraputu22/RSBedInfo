package com.moonlightsplitter.rsbedinfo

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.moonlightsplitter.rsbedinfo.adapter.AdapterBed
import com.moonlightsplitter.rsbedinfo.api.Client
import com.moonlightsplitter.rsbedinfo.models.BedDetail
import com.moonlightsplitter.rsbedinfo.models.DataHospital
import com.moonlightsplitter.rsbedinfo.models.ModelBedDetail
import com.moonlightsplitter.rsbedinfo.models.ModelMaps
import com.moonlightsplitter.rsbedinfo.utils.CustomLoading
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
    lateinit var spinnerType: Spinner
    lateinit var noData: TextView
    private lateinit var listBed: RecyclerView
    private lateinit var bed: ArrayList<BedDetail>
    private val loading = CustomLoading(this)

    private var idHospital: String? = null
    private var phoneNumber: String? = null
    private var lat: String? = null
    private var long: String? = null
    private var title: String? = null
    private var type: String? = "1"

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

    data class Type(
            var id: String,
            var name: String
    )

    private fun initComponents() {
        nameHospital = findViewById(R.id.nameHospital)
        addressHospital = findViewById(R.id.addressHospital)
        phoneHospital = findViewById(R.id.phoneHospital)
        btnCall = findViewById(R.id.btnCall)
        btnMap = findViewById(R.id.btnMap)
        toolbar = findViewById(R.id.toolbar)
        viewMap = findViewById(R.id.viewMap)
        spinnerType = findViewById(R.id.spinnerType)
        listBed = findViewById(R.id.listBed)
        noData = findViewById(R.id.noData)
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

    private fun getType() {
        val dataType = arrayOf(
                Type("1", "Covid"),
                Type("2", "Non-Covid")
        )
        val listType: ArrayList<String> = ArrayList()
        for (i in 0 until dataType.count()) {
            listType.addAll(setOf(dataType[i].name))
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(context, R.layout.spinner_item, listType)
        spinnerType.adapter = adapter
        spinnerType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                type = dataType[position].id
                getBed()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }
    }

    private fun loadData(hospital: DataHospital) {
        nameHospital.text = hospital.name?: "-"
        addressHospital.text = hospital.address?: "-"
        phoneHospital.text = hospital.phone?: "-"

        idHospital = hospital.id
        if (hospital.phone != null || !hospital.equals("")) {
            phoneNumber = hospital.phone
        }

        getType()
        getMaps()
    }

    private fun getBed() {
        loading.startLoading()
        val adapterBed = AdapterBed()
        listBed.layoutManager = LinearLayoutManager(context)
        listBed.setHasFixedSize(true)
        listBed.adapter = adapterBed
        Client.instance.getBedDetail(idHospital!!, type!!).enqueue(object: Callback<ModelBedDetail>{
            override fun onResponse(call: Call<ModelBedDetail>, response: Response<ModelBedDetail>) {
                loading.isDismiss()
                if (response.code() == 200) {
                    if (response.body()!!.data.bedDetail!!.count() != 0) {
                        isEmptyData(false)
                        bed = response.body()!!.data.bedDetail!!
                        adapterBed.setData(bed)
                    } else {
                        isEmptyData(true)
                    }
                } else {
                    isEmptyData(true)
                }
            }

            override fun onFailure(call: Call<ModelBedDetail>, t: Throwable) {
                loading.isDismiss()
                isEmptyData(true)
            }
        })
    }

    private fun isEmptyData(status: Boolean) {
        if (status) {
            listBed.visibility = View.GONE
            noData.visibility = View.VISIBLE
        } else {
            listBed.visibility = View.VISIBLE
            noData.visibility = View.GONE
        }
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