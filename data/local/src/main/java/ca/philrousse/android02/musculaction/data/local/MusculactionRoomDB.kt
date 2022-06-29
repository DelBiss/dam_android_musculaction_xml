package ca.philrousse.android02.musculaction.data.local

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import ca.philrousse.android02.musculaction.data.entity.table.*
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import ca.philrousse.android02.musculaction.data.local.MusculactionDatabaseWorker
import ca.philrousse.android02.musculaction.data.local.MusculactionDatabaseWorker.Companion.KEY_FILENAME
import ca.philrousse.android02.musculaction.data.entity.ExerciceDetailHierachy
import ca.philrousse.android02.musculaction.data.entity.CategoryHierachy
import ca.philrousse.android02.musculaction.data.entity.ExerciseHierachy
import ca.philrousse.android02.musculaction.data.entity.SubcategoryHierachy


import kotlinx.coroutines.launch

@Database(entities = [ExercisesCategory::class,Exercise::class,ExerciseDetail::class,ExerciseDetailVideo::class,ExercisesSubcategory::class], version = 1, exportSchema = false)
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

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): MusculactionRoomDB {
            return Room.databaseBuilder(context, MusculactionRoomDB::class.java, "bdmusculation")
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<MusculactionDatabaseWorker>()
                                .setInputData(workDataOf(KEY_FILENAME to "muculaction_data.json"))
                                .build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                .build()
        }
    }
}

class dumyData{
    companion object{
        var id_cat = 0
        var id_sub = 0
        var id_ex = 0
        var id_det = 0
        var id_vid = 0

        fun cat():ExercisesCategory{
            id_cat++
            return ExercisesCategory("Category $id_cat","Desc cat $id_cat", "Img cat $id_cat")
        }

        fun sub():ExercisesSubcategory{
            id_sub++
            return ExercisesSubcategory("Subcategory $id_sub")
        }

        fun ex():Exercise{
            id_ex++
            return Exercise("Exercise $id_ex","Desc Exercise $id_ex", "Img Exercise $id_ex")
        }

        fun det():ExerciseDetail{
            id_det++
            return ExerciseDetail("ExerciseDetail $id_det","Desc ExerciseDetail $id_det")
        }

        fun vid():ExerciseDetailVideo{
            id_vid++
            return ExerciseDetailVideo("ExerciseDetailVideo $id_vid")
        }

        fun vCat():CategoryHierachy{
            return CategoryHierachy(cat(), listOf(vsub(), vsub()))
        }
        fun vsub():SubcategoryHierachy{
            return SubcategoryHierachy(sub(), listOf(vex(), vex()))
        }
        fun vex():ExerciseHierachy{
            return ExerciseHierachy(ex(), listOf(vdet(), vdet()))
        }

        fun vdet():ExerciceDetailHierachy{
            return ExerciceDetailHierachy(det(), listOf(vid(), vid()))
        }

    }
}