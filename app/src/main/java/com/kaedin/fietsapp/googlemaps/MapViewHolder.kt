package com.kaedin.fietsapp.googlemaps

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.fietsapp.models.Vehicle

class MapViewHolder(private val mMapViewListItemView: MapViewListItemView?) :
    RecyclerView.ViewHolder(
        mMapViewListItemView!!
    ) {
    /**
     * Recyclerviewholder for the MapsView. This class is used to link a GoogleMaps widget with a list item from a recyclerview
     * @see RecyclerView.ViewHolder
     */
    fun mapViewListItemViewOnCreate(savedInstanceState: Bundle?) {
        mMapViewListItemView?.mapViewOnCreate(savedInstanceState)
    }

    /** Pass vehicle to the listItemView */
    fun mapViewListItemViewOnResume(vehicle: Vehicle) {
        mMapViewListItemView?.mapViewOnResume(vehicle)
    }

    /** Call the pause method */
    fun mapViewListItemViewOnPause() {
        mMapViewListItemView?.mapViewOnPause()
    }

    /** Call the destroy method */
    fun mapViewListItemViewOnDestroy() {
        mMapViewListItemView?.mapViewOnDestroy()
    }

    /** Call the low memory method */
    fun mapViewListItemViewOnLowMemory() {
        mMapViewListItemView?.mapViewOnLowMemory()
    }

    fun mapViewListItemViewOnSaveInstanceState(outState: Bundle?) {
        mMapViewListItemView?.mapViewOnSaveInstanceState(outState)
    }
}