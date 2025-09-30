package com.example.notificationremover.ui

import android.content.Context
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notificationremover.R
import com.example.notificationremover.data.AppSettingEntity

class AppSettingsAdapter(
    private val context: Context,
    private val onDeleteClick: (AppSettingEntity) -> Unit
) : ListAdapter<AppSettingEntity, AppSettingsAdapter.AppSettingViewHolder>(AppSettingDiffCallback()) {

    private val packageManager = context.packageManager

    class AppSettingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appIcon: ImageView = view.findViewById(R.id.appIcon)
        val appName: TextView = view.findViewById(R.id.appName)
        val packageName: TextView = view.findViewById(R.id.packageName)
        val removalTime: TextView = view.findViewById(R.id.removalTime)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppSettingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app_setting, parent, false)
        return AppSettingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AppSettingViewHolder, position: Int) {
        val appSetting = getItem(position)
        
        // Set app icon
        try {
            val appInfo = packageManager.getApplicationInfo(appSetting.packageName, 0)
            holder.appIcon.setImageDrawable(packageManager.getApplicationIcon(appInfo))
        } catch (e: PackageManager.NameNotFoundException) {
            // App not found, use default icon
            holder.appIcon.setImageResource(android.R.drawable.sym_def_app_icon)
        }
        
        // Set text
        holder.appName.text = appSetting.appName
        holder.packageName.text = appSetting.packageName
        holder.removalTime.text = "${appSetting.removalTimeMinutes} min"
        
        // Set delete button click listener
        holder.deleteButton.setOnClickListener {
            onDeleteClick(appSetting)
        }
    }

    private class AppSettingDiffCallback : DiffUtil.ItemCallback<AppSettingEntity>() {
        override fun areItemsTheSame(oldItem: AppSettingEntity, newItem: AppSettingEntity): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: AppSettingEntity, newItem: AppSettingEntity): Boolean {
            return oldItem == newItem
        }
    }
}
