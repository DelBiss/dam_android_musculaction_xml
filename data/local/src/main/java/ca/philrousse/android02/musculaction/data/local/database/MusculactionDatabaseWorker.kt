package ca.philrousse.android02.musculaction.data.local.database

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ca.philrousse.android02.musculaction.data.local.json.MAJson
import ca.philrousse.android02.musculaction.data.local.json.MAJsonCategory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MusculactionDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val filename = inputData.getString(KEY_FILENAME)
            if (filename != null) {
                @Suppress("BlockingMethodInNonBlockingContext")
                applicationContext.assets.open(filename).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val jsonCategoryType = object : TypeToken<List<MAJsonCategory>>() {}.type
                        val exerciseJson: List<MAJsonCategory> = Gson().fromJson(jsonReader, jsonCategoryType)

                        val database = MusculactionRoomDB.getInstance(applicationContext)

                        MAJson(exerciseJson).insert(database.dao())

                        Result.success()
                    }
                }
            } else {
                Log.e(TAG, "Error seeding database - no valid filename")
                Result.failure()
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "MusculactionDbWorker"
        const val KEY_FILENAME = "MUSCULACTION_DATA_FILENAME"
    }
}