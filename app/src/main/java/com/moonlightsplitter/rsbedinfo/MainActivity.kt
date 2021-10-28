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
import com.moonlightsplitter.rsbedinfo.models.ModelProvince
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var context: Context
    lateinit var spinnerProvince: Spinner

    private var id_province: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this

        initComponents()
        getProvinces()
    }

    private fun initComponents() {
        spinnerProvince = findViewById(R.id.spinnerProvince)
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
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            id_province = data[position].id
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }

                    }
                }
            }

            override fun onFailure(call: Call<ModelProvince>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}