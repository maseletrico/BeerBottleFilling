package com.maseletrico.beerbottlefilling.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maseletrico.beerbottlefilling.BlueToothInfo
import com.maseletrico.beerbottlefilling.R
import kotlinx.android.synthetic.main.recycler_view_row.view.*

class PairedAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PairedAdapter.PairedViewHolder>() {

    private var dataList = mutableListOf<BlueToothInfo>()
    fun setListData(data: MutableList<BlueToothInfo>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairedViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_view_row, parent, false)
        return PairedViewHolder(view)
    }

    override fun onBindViewHolder(holder: PairedViewHolder, position: Int) {
        val blueToothInfo: BlueToothInfo = dataList[position]
        holder.bindView(blueToothInfo)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) {
            dataList.size
        } else {
            0
        }
    }

    inner class PairedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        fun bindView(blueToothInfo: BlueToothInfo) {

            itemView.tvBlueToothName.text = blueToothInfo.name
            itemView.tvBlueToothAddress.text = blueToothInfo.Address
        }

        init {
            itemView.clBlueToothRow.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if(position !=  RecyclerView.NO_POSITION) {
                listener.onItemClick(
                    itemView.tvBlueToothName.text.toString(),
                    itemView.tvBlueToothAddress.text.toString()
                )
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(bluToothName: String, blueToothAddress: String) {}

    }
}