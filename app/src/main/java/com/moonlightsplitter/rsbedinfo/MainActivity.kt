package com.moonlightsplitter.rsbedinfo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.moonlightsplitter.rsbedinfo.api.Client
import com.moonlightsplitter.rsbedinfo.models.ModelCity
import com.moonlightsplitter.rsbedinfo.models.ModelProvince
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var context: Context
    lateinit var spinnerProvince: Spinner
    lateinit var spinnerCity: Spinner
    lateinit var spinnerType: Spinner

    private var idProvince: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this

        initComponents()
        getProvinces()
        getType()
    }

    data class Type(
            var id: String,
            var name: String
    )

    private fun initComponents() {
        spinnerProvince = findViewById(R.id.spinnerProvince)
        spinnerCity = findViewById(R.id.spinnerCity)
        spinnerType = findViewById(R.id.spinnerType)
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
                //
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
                            //
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            //
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ModelCity>, t: Throwable) {
                Log.e("ERROR GET CITIES", t.localizedMessage)
            }
        })
    }
}