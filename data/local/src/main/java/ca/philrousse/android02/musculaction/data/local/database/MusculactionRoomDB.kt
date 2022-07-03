package ca.philrousse.android02.musculaction.data.local.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import ca.philrousse.android02.musculaction.data.entity.*
import ca.philrousse.android02.musculaction.data.local.database.MusculactionDatabaseWorker.Companion.KEY_FILENAME

@Database(entities = [Category::class,Exercise::class,ExerciseDetail::class,ExerciseDetailVideo::class,Subcategory::class, Image::class], version = 3, exportSchema = false)
abstract class MusculactionRoomDB: RoomDatabase() {
    abstract fun dao(): MusculactionDAO

    companion object {
        @Volatile
        private var instance: MusculactionRoomDB? = null

        fun getInstance(context: Context): MusculactionRoomDB {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): MusculactionRoomDB {
            return Room.databaseBuilder(context, MusculactionRoomDB::class.java, "bdmusculation")
                .addCallback(
                    object : RoomDatabase.Callback() {

                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            populateDB()
                        }

                        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                            super.onDestructiveMigration(db)
                            populateDB()
                        }

                        private fun populateDB() {
                            val request = OneTimeWorkRequestBuilder<MusculactionDatabaseWorker>()
                                .setInputData(workDataOf(KEY_FILENAME to "muculaction_data.json"))
                                .build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
