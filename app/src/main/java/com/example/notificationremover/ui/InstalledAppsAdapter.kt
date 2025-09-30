package com.example.notificationremover.ui

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notificationremover.R

class InstalledAppsAdapter(
    private val context: Context,
    private val installedApps: List<ApplicationInfo>,
    private val onAppSelected: (String, String) -> Unit
) : RecyclerView.Adapter<InstalledAppsAdapter.AppViewHolder>() {

    private val packageManager = context.packageManager

    class AppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appIcon: ImageView = view.findViewById(R.id.appIcon)
        val appName: TextView = view.findViewById(R.id.appName)
        val packageName: TextView = view.findViewById(R.id.packageName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        // Reuse the same layout as app_setting_item but only use the icon and text parts
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app_setting, parent, false)
        
        // Hide the delete button and removal time
        itemView.findViewById<View>(R.id.deleteButton).visibility = View.GONE
        itemView.findViewById<View>(R.id.removalTime).visibility = View.GONE
        
        return AppViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val appInfo = installedApps[position]
        
        // Set app icon
        holder.appIcon.setImageDrawable(packageManager.getApplicationIcon(appInfo))
        
        // Set text
        val appName = packageManager.getApplicationLabel(appInfo).toString()
        holder.appName.text = appName
        holder.packageName.text = appInfo.packageName
        
        // Set click listener
        holder.itemView.setOnClickListener {
            onAppSelected(appInfo.packageName, appName)
        }
    }

    override fun getItemCount() = installedApps.size
}
