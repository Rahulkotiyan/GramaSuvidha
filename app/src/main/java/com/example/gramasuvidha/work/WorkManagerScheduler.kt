package com.example.gramasuvidha.work

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Phase 3: WorkManager Scheduler
 * Configures and schedules periodic background sync tasks
 */
object WorkManagerScheduler {
    
    private const val TAG = "WorkManagerScheduler"
    
    /**
     * Schedule periodic sync work
     * Syncs every 6 hours when network is available
     */
    fun schedulePeriodicSync(context: Context) {
        try {
            // Define constraints for the work
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // Only run when network is available
                .setRequiresBatteryNotLow(true) // Only run when battery is not low
                .build()
            
            // Create periodic work request (every 6 hours)
            val periodicSyncRequest = PeriodicWorkRequestBuilder<SyncWorker>(
                6, TimeUnit.HOURS // Repeat interval
            )
                .setConstraints(constraints)
                .addTag(SyncWorker.WORK_NAME)
                .build()
            
            // Schedule the work
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                SyncWorker.WORK_NAME,
                androidx.work.ExistingPeriodicWorkPolicy.UPDATE, // Replace existing work
                periodicSyncRequest
            )
            
            Log.d(TAG, "Periodic sync scheduled successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error scheduling periodic sync", e)
        }
    }
    
    /**
     * Cancel all scheduled sync work
     */
    fun cancelPeriodicSync(context: Context) {
        try {
            WorkManager.getInstance(context).cancelAllWorkByTag(SyncWorker.WORK_NAME)
            Log.d(TAG, "Periodic sync cancelled")
        } catch (e: Exception) {
            Log.e(TAG, "Error cancelling periodic sync", e)
        }
    }
    
    /**
     * Check if sync work is already scheduled
     */
    fun isSyncScheduled(context: Context): Boolean {
        return try {
            val workInfos = WorkManager.getInstance(context)
                .getWorkInfosByTag(SyncWorker.WORK_NAME)
                .get()
            workInfos.isNotEmpty()
        } catch (e: Exception) {
            Log.e(TAG, "Error checking sync status", e)
            false
        }
    }
}
