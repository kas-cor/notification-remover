package com.example.notificationremover

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notificationremover.databinding.ActivityMainBinding
import com.example.notificationremover.service.NotificationListenerService
import com.example.notificationremover.ui.AppSettingsAdapter
import com.example.notificationremover.ui.AppSettingsViewModel
import com.example.notificationremover.ui.InstalledAppsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: AppSettingsViewModel
    private lateinit var adapter: AppSettingsAdapter
    
    companion object {
        private const val TAG = "MainActivity"
        private const val PERMISSION_REQUEST_CODE = 123
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            // Setup ViewModel
            viewModel = ViewModelProvider(this)[AppSettingsViewModel::class.java]
            
            // Setup UI
            setupServiceStatus()
            setupPermissionButton()
            setupDefaultTimeSettings()
            setupAppSettingsList()
            setupAddAppButton()
            
            // Observe app settings changes
            viewModel.allAppSettings.observe(this) { appSettings ->
                adapter.submitList(appSettings)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate", e)
            showErrorToast("Failed to initialize app: ${e.message}")
        }
    }
    
    override fun onResume() {
        super.onResume()
        try {
            // Update service status when activity resumes
            updateServiceStatus()
            
            // Check if permission was just granted
            if (isNotificationListenerEnabled()) {
                showPermissionRationale()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in onResume", e)
        }
    }
    
    private fun setupServiceStatus() {
        updateServiceStatus()
    }
    
    private fun updateServiceStatus() {
        try {
            val isEnabled = isNotificationListenerEnabled()
            binding.serviceStatusText.text = if (isEnabled) {
                getString(R.string.service_running)
            } else {
                getString(R.string.service_not_running)
            }
            
            // Show/hide permission card based on status
            binding.permissionCard.visibility = if (isEnabled) {
                android.view.View.GONE
            } else {
                android.view.View.VISIBLE
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating service status", e)
        }
    }
    
    private fun showPermissionRationale() {
        if (!isNotificationListenerEnabled()) {
            AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("This app needs notification access to automatically remove notifications. " +
                          "This permission is safe and the app only removes notifications according to your settings.")
                .setPositiveButton("Grant Permission") { _, _ ->
                    openNotificationSettings()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
    
    private fun setupPermissionButton() {
        binding.grantPermissionButton.setOnClickListener {
            openNotificationSettings()
        }
    }
    
    private fun openNotificationSettings() {
        try {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error opening notification settings", e)
            showErrorToast("Failed to open notification settings")
        }
    }
    
    private fun setupDefaultTimeSettings() {
        // Display current default time
        binding.defaultTimeInput.setText(viewModel.getDefaultRemovalTime().toString())
        
        // Setup save button
        binding.saveDefaultButton.setOnClickListener {
            val timeText = binding.defaultTimeInput.text.toString().trim()
            if (timeText.isEmpty()) {
                showErrorToast("Please enter a time value")
                return@setOnClickListener
            }
            
            val timeMinutes = timeText.toIntOrNull()
            if (timeMinutes == null) {
                showErrorToast("Please enter a valid number")
                return@setOnClickListener
            }
            
            when {
                timeMinutes < 1 -> {
                    showErrorToast("Time must be at least 1 minute")
                    return@setOnClickListener
                }
                timeMinutes > 1440 -> {
                    showErrorToast("Time cannot exceed 24 hours (1440 minutes)")
                    return@setOnClickListener
                }
                else -> {
                    viewModel.saveDefaultRemovalTime(timeMinutes)
                    showSuccessToast("Default time saved: $timeMinutes minutes")
                }
            }
        }
    }
    
    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    
    private fun showSuccessToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
    private fun setupAppSettingsList() {
        adapter = AppSettingsAdapter(this) { appSetting ->
            // Delete app setting when delete button is clicked
            viewModel.deleteAppSetting(appSetting)
        }
        
        binding.appSettingsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.appSettingsRecyclerView.adapter = adapter
    }
    
    private fun setupAddAppButton() {
        binding.addAppSettingButton.setOnClickListener {
            showAddAppDialog()
        }
    }
    
    private fun showAddAppDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_app_setting, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Add", null) // Set in show() to prevent auto-dismiss
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()
        
        // Load installed apps
        val installedApps = getInstalledApps()
        
        // Setup RecyclerView
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.installedAppsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // Get the time input field
        val timeInput = dialogView.findViewById<EditText>(R.id.removalTimeInput)
        
        // Fill with default value
        timeInput.setText(viewModel.getDefaultRemovalTime().toString())
        
        var selectedPackage: String? = null
        var selectedAppName: String? = null
        
        // Setup adapter
        val appsAdapter = InstalledAppsAdapter(this, installedApps) { packageName, appName ->
            selectedPackage = packageName
            selectedAppName = appName
            
            // Change the positive button text to indicate the selected app
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
            dialog.setTitle("Selected: $appName")
        }
        
        recyclerView.adapter = appsAdapter
        
        // Show dialog
        dialog.show()
        
        // Override positive button to prevent auto-dismiss
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
            isEnabled = false // Disable until an app is selected
            
            setOnClickListener {
                if (selectedPackage != null && selectedAppName != null) {
                    val timeText = timeInput.text.toString().trim()
                    
                    if (timeText.isEmpty()) {
                        showErrorToast("Please enter a time value")
                        return@setOnClickListener
                    }
                    
                    val timeMinutes = timeText.toIntOrNull()
                    if (timeMinutes == null) {
                        showErrorToast("Please enter a valid number")
                        return@setOnClickListener
                    }
                    
                    when {
                        timeMinutes < 1 -> {
                            showErrorToast("Time must be at least 1 minute")
                            return@setOnClickListener
                        }
                        timeMinutes > 1440 -> {
                            showErrorToast("Time cannot exceed 24 hours (1440 minutes)")
                            return@setOnClickListener
                        }
                        else -> {
                            viewModel.addAppSetting(selectedPackage!!, selectedAppName!!, timeMinutes)
                            dialog.dismiss()
                        }
                    }
                } else {
                    showErrorToast("Please select an app first")
                }
            }
        }
    }
    
    private fun getInstalledApps(): List<ApplicationInfo> {
        return try {
            val pm = packageManager
            val installedApps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            
            // Filter out system apps and sort by app name
            installedApps
                .filter { app -> 
                    app.packageName != packageName && // Filter out this app
                    (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 && // Filter out system apps
                    app.packageName.isNotEmpty() // Ensure package name is valid
                }
                .sortedBy { 
                    try {
                        pm.getApplicationLabel(it).toString()
                    } catch (e: Exception) {
                        Log.w(TAG, "Failed to get label for ${it.packageName}", e)
                        it.packageName
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting installed apps", e)
            showErrorToast("Failed to load installed apps")
            emptyList()
        }
    }
    
    private fun isNotificationListenerEnabled(): Boolean {
        return try {
            val packageName = packageName
            val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
            
            if (!TextUtils.isEmpty(flat)) {
                val names = flat.split(":")
                for (name in names) {
                    val componentName = ComponentName.unflattenFromString(name)
                    if (componentName != null && TextUtils.equals(packageName, componentName.packageName)) {
                        return true
                    }
                }
            }
            false
        } catch (e: Exception) {
            Log.e(TAG, "Error checking notification listener permission", e)
            false
        }
    }
}
