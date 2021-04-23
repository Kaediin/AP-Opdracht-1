package com.kaedin.fietsapp.recyclerview

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.fietsapp.models.Vehicle
import com.kaedin.fietsapp.googlemaps.MapViewHolder
import com.kaedin.fietsapp.googlemaps.MapViewListItemView

/**
 * This adapter class sets up the recyclerview list. It matches each list item to the recyclerview and fills the layout in the list item
 * @param context context is needed for the adapter. It needs to know where it came from and where it is going to
 * @param vehicle is a list of vehicles being shows
 * @see RecyclerView.Adapter
 */
class AdapterVehicles(private val context: Context, private val vehicle: List<Vehicle>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * Create the viewholder which, as it says, holds the view.
     * @see RecyclerView.ViewHolder
     * (when using mulitple recyclerviews is can be usefull to use custom viewholders)
     *
     * Instead of usually returning the parentlayout of the listitem we return [MapViewHolder(mapViewListItemView)] because we need the GoogleMapsView in the list item
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val mapViewListItemView = MapViewListItemView(context)
        mapViewListItemView.mapViewOnCreate(null)
        return MapViewHolder(mapViewListItemView)
    }

    /**
     * Binds the content to the viewholder
     * @param holder the viewholder itself. Here we can for example change the text, img, Maps-location etc
     * @param position gets the position of the current vehicle in the list. list-item 1 --> pos 0 --> [vehicle[position]]
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mapViewHolder = holder as MapViewHolder
        mapViewHolder.mapViewListItemViewOnResume(vehicle[position])
    }

    /**
     * Determines the amount of list items in the view. We only want the same amount of list items as we have vehicles to prevent Nullpointers
     */
    override fun getItemCount() = vehicle.size
}

