package com.example.gramasuvidha.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.gramasuvidha.data.AppDatabase
import com.example.gramasuvidha.data.ProjectRepository
import com.example.gramasuvidha.network.NetworkResult
import com.example.gramasuvidha.network.RetrofitClient

/**
 * Phase 3: Background Sync Worker
 * Uses WorkManager to periodically sync data from the API
 * Ensures data stays fresh even when app is in background
 */
class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    
    companion object {
        private const val TAG = "SyncWorker"
        const val WORK_NAME = "ProjectSyncWorker"
    }
    
    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "Starting background sync...")
            
            // Initialize repository
            val database = AppDatabase.getDatabase(applicationContext)
            val repository = ProjectRepository(
                projectDao = database.projectDao(),
                context = applicationContext
            )
            
            // Perform sync
            val result = repository.refreshProjects()
            
            when (result) {
                is NetworkResult.Success -> {
                    Log.d(TAG, "Background sync successful: ${result.data.size} projects")
                    Result.success()
                }
                is NetworkResult.Error -> {
                    Log.w(TAG, "Background sync failed: ${result.message}")
                    // Return retry so WorkManager will try again later
                    Result.retry()
                }
                is NetworkResult.Loading -> {
                    Log.d(TAG, "Background sync still in progress")
                    Result.retry()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error during background sync", e)
            Result.failure()
        }
    }
}
