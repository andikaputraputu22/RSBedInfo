package com.moonlightsplitter.rsbedinfo.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.moonlightsplitter.rsbedinfo.R
import com.moonlightsplitter.rsbedinfo.models.DataHospital

class AdapterHospital(private val type: String) : RecyclerView.Adapter<AdapterHospital.DataHospitalViewHolder>() {

    private val listHospital = ArrayList<DataHospital>()
    private lateinit var onItemClickListener: OnItemClickListener

    inner class DataHospitalViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val nameHospital = view.findViewById<TextView>(R.id.nameHospital)
        private val locationHospital = view.findViewById<TextView>(R.id.locationHospital)
        private val infoHospital = view.findViewById<TextView>(R.id.infoHospital)
        private val itemClick = view.findViewById<MaterialCardView>(R.id.itemClick)

        fun bindView(hospital: DataHospital, type: String) {
            nameHospital.text = hospital.name?: ""
            locationHospital.text = (hospital.address?: "")
            if (type == "1") {
                val totalBed: String = "Available bed: ${hospital.bed_availability?: 0}"
                infoHospital.text = totalBed
            } else {
                var totalBed: String = ""
                totalBed = if (hospital.available_beds?.count() != 0) {
                    "Available bed: ${hospital.available_beds?.get(0)?.available?: 0}"
                } else {
                    "Available bed: 0"
                }
                infoHospital.text = totalBed
            }

            itemClick.setOnClickListener {
                onItemClickListener.onItemClicked(listHospital[adapterPosition])
            }
        }
    }

    fun setData(itemData: ArrayList<DataHospital>) {
        listHospital.clear()
        listHospital.addAll(itemData)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHospitalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hospital, parent, false)
        return DataHospitalViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataHospitalViewHolder, position: Int) {
        holder.bindView(listHospital[position], type)
    }

    override fun getItemCount(): Int {
        return listHospital.size
    }

    interface OnItemClickListener {
        fun onItemClicked(hospital: DataHospital)
    }
}