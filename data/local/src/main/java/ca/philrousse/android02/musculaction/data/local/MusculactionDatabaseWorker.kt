package ca.philrousse.android02.musculaction.data.local

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ca.philrousse.android02.musculaction.data.entity.HierarchicCategory
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
                        val hierarchicCategoryType = object : TypeToken<List<HierarchicCategory>>() {}.type
                        val exerciseJson: List<HierarchicCategory> = Gson().fromJson(jsonReader, hierarchicCategoryType)

                        val database = MusculactionRoomDB.getInstance(applicationContext)
                        exerciseJson.forEach {
                            database.dao().insert(it)
                        }


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