package ca.philrousse.android02.musculaction.data

import ca.philrousse.android02.musculaction.data.local.MusculactionDAO
import ca.philrousse.android02.musculaction.data.local.MusculactionRoomDB
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusculactionRepository @Inject constructor(
    private val dao:MusculactionDAO
    ){



    fun getCategories() = dao.getCategories()
    fun get() = dao.getCategories()


    companion object{
        @Volatile
        private var instance: MusculactionRepository? = null
        fun getInstance(
            dao:MusculactionDAO
        )= instance ?: synchronized(this) {
                instance ?: MusculactionRepository(dao).also { instance = it }
            }

        }


}
