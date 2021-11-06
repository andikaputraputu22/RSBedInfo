package com.moonlightsplitter.rsbedinfo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moonlightsplitter.rsbedinfo.DetailHospitalActivity.Companion.EXTRA_HOSPITAL
import com.moonlightsplitter.rsbedinfo.adapter.AdapterHospital
import com.moonlightsplitter.rsbedinfo.api.Client
import com.moonlightsplitter.rsbedinfo.models.DataHospital
import com.moonlightsplitter.rsbedinfo.models.ModelCity
import com.moonlightsplitter.rsbedinfo.models.ModelHospital
import com.moonlightsplitter.rsbedinfo.models.ModelProvince
import com.moonlightsplitter.rsbedinfo.utils.CustomLoading
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var context: Context
    lateinit var spinnerProvince: Spinner
    lateinit var spinnerCity: Spinner
    lateinit var spinnerType: Spinner
    lateinit var noData: TextView
    private lateinit var listHospital: RecyclerView
    private lateinit var hospital: ArrayList<DataHospital>
    private val loading = CustomLoading(this)

    private var idProvince: String? = null
    private var idCity: String? = null
    private var type: String? = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this

        initComponents()
        getProvinces()
    }

    data class Type(
            var id: String,
            var name: String
    )

    private fun initComponents() {
        spinnerProvince = findViewById(R.id.spinnerProvince)
        spinnerCity = findViewById(R.id.spinnerCity)
        spinnerType = findViewById(R.id.spinnerType)
        listHospital = findViewById(R.id.listHospital)
        noData = findViewById(R.id.noData)
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
                getHospital()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }
    }

    private fun getProvinces() {
        Client.instance.getProvinces().enqueue(object: Callback<ModelProvince>{
            override fun onResponse(call: Call<ModelProvince>, response: Response<ModelProvince>) {
                if (response.code() == 200) {
                    val data = response.body()!!.provinces
                    val listProvince: ArrayList<String> = ArrayList()
                    for (i in 0 until data.size) {
                        listProvince.addAll(setOf(data[i].name!!))
                    }

                    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(context, R.layout.spinner_item, listProvince)
                    spinnerProvince.adapter = adapter
                    spinnerProvince.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            idProvince = data[position].id
                            getCities(idProvince)
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            //
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ModelProvince>, t: Throwable) {
                isEmptyData(true)
                Log.e("ERROR GET PROVINCES", t.localizedMessage)
            }
        })
    }

    private fun getCities(idProvince: String?) {
        Client.instance.getCities(idProvince!!).enqueue(object: Callback<ModelCity>{
            override fun onResponse(call: Call<ModelCity>, response: Response<ModelCity>) {
                if (response.code() == 200) {
                    val data = response.body()!!.cities
                    val listCity: ArrayList<String> = ArrayList()
                    for (i in 0 until data.size) {
                        listCity.addAll(setOf(data[i].name!!))
                    }

                    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(context, R.layout.spinner_item, listCity)
                    spinnerCity.adapter = adapter
                    spinnerCity.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            idCity = data[position].id
                            getType()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            //
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ModelCity>, t: Throwable) {
                isEmptyData(true)
                Log.e("ERROR GET CITIES", t.localizedMessage)
            }
        })
    }

    private fun getHospital() {
        loading.startLoading()
        val adapterHospital = AdapterHospital(type!!)
        listHospital.layoutManager = LinearLayoutManager(context)
        listHospital.setHasFixedSize(true)
        listHospital.adapter = adapterHospital
        Client.instance.getHospitals(idProvince!!, idCity!!, type!!).enqueue(object: Callback<ModelHospital>{
            override fun onResponse(call: Call<ModelHospital>, response: Response<ModelHospital>) {
                loading.isDismiss()
                if (response.code() == 200) {
                    if (response.body()!!.hospitals.count() != 0) {
                        isEmptyData(false)
                        hospital = response.body()!!.hospitals
                        adapterHospital.setData(hospital)
                        listAction(adapterHospital)
                    } else {
                        isEmptyData(true)
                    }
                } else {
                    isEmptyData(true)
                }
            }

            override fun onFailure(call: Call<ModelHospital>, t: Throwable) {
                loading.isDismiss()
                isEmptyData(true)
            }
        })
    }

    private fun isEmptyData(status: Boolean) {
        if (status) {
            listHospital.visibility = View.GONE
            noData.visibility = View.VISIBLE
        } else {
            listHospital.visibility = View.VISIBLE
            noData.visibility = View.GONE
        }
    }

    private fun listAction(adapterHospital: AdapterHospital) {
        adapterHospital.setOnItemClickListener(object: AdapterHospital.OnItemClickListener {
            override fun onItemClicked(hospital: DataHospital) {
                val intent = Intent(context, DetailHospitalActivity::class.java)
                intent.putExtra(EXTRA_HOSPITAL, hospital)
                startActivity(intent)
            }
        })
    }
}