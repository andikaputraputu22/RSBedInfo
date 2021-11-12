package com.moonlightsplitter.rsbedinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.moonlightsplitter.rsbedinfo.R
import com.moonlightsplitter.rsbedinfo.models.BedDetail
import com.moonlightsplitter.rsbedinfo.utils.StaticController

class AdapterBed : RecyclerView.Adapter<AdapterBed.DataBedViewHolder>() {

    private val listBed = ArrayList<BedDetail>()

    inner class DataBedViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val nameBed = view.findViewById<TextView>(R.id.nameBed)
        private val availableBed = view.findViewById<TextView>(R.id.availableBed)
        private val emptyBed = view.findViewById<TextView>(R.id.emptyBed)
        private val queueNumber = view.findViewById<TextView>(R.id.queueNumber)
        private val time = view.findViewById<TextView>(R.id.time)

        fun bindView(bed: BedDetail) {
            nameBed.text = bed.stats?.title?: "-"
            val bedAvailable = "${bed.stats?.bed_available?: 0} Units"
            val bedEmpty = "${bed.stats?.bed_empty?: 0} Units"
            availableBed.text = bedAvailable
            emptyBed.text = bedEmpty
            queueNumber.text = "${bed.stats?.queue?: 0}"
            time.text = StaticController().dateFormatted(bed.time)
        }
    }

    fun setData(itemData: ArrayList<BedDetail>) {
        listBed.clear()
        listBed.addAll(itemData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterBed.DataBedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bed, parent, false)
        return DataBedViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterBed.DataBedViewHolder, position: Int) {
        holder.bindView(listBed[position])
    }

    override fun getItemCount(): Int {
        return listBed.size
    }
}