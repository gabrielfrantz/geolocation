package com.example.geolocation.classes.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.geolocation.R
import com.example.geolocation.classes.spinner.SpinnerItem
import kotlinx.android.synthetic.main.spinner_layout.view.*


class CustomAdapter(context: Context, spinnerItems: List<SpinnerItem>):
    ArrayAdapter<SpinnerItem>(context, 0, spinnerItems) {

    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }

    override fun getDropDownView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }

    private fun createView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.spinner_layout,
            parent,
            false
        )
        view.image_spinner.setImageResource(item?.icon!!)
        view.tv_spinner.text = context.getResources().getString(item.label)
        return view
    }
}