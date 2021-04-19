package com.kaedin.fietsapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.spacex.googlemaps.MapViewHolder
import com.kaedin.spacex.googlemaps.MapViewListItemView
import kotlinx.android.synthetic.main.list_item.view.*

class AdapterVehicles(private val context: Context, private val vehicle: List<Vehicle>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val mapViewListItemView = MapViewListItemView(context)
        mapViewListItemView.mapViewOnCreate(null)
        return MapViewHolder(mapViewListItemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mapViewHolder = holder as MapViewHolder
        mapViewHolder.mapViewListItemViewOnResume(vehicle[position])
    }

    override fun getItemCount() = vehicle.size
}

