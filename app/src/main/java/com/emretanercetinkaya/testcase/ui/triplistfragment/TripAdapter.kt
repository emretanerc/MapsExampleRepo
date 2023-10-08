package com.emretanercetinkaya.testcase.ui.triplistfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emretanercetinkaya.testcase.R
import com.emretanercetinkaya.testcase.model.Trip


class TripAdapter(val trips: List<Trip>, private val onItemClicked: (Trip) -> Unit) : RecyclerView.Adapter<TripAdapter.SavedAnimalModel>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedAnimalModel {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.trips_row_item, parent, false)
        return SavedAnimalModel(v)
    }

    override fun getItemCount(): Int {
        return trips.size
    }


    override fun onBindViewHolder(holder: SavedAnimalModel, position: Int) {
        val tripModel = trips[position]

        holder.busTimeTV.setText(tripModel.time)
        holder.busNameTV.setText(tripModel.bus_name)

        holder.bookBT.setOnClickListener {
            onItemClicked(tripModel)
        }
    }


    class SavedAnimalModel(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val busNameTV: TextView = itemView.findViewById(R.id.busNameTextView)
        val busTimeTV: TextView = itemView.findViewById(R.id.timeTextView)
        val bookBT: Button = itemView.findViewById(R.id.bookButton)
    }
}