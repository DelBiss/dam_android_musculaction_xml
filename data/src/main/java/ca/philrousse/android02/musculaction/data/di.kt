package ca.philrousse.android02.musculaction.data

import android.content.Context
import ca.philrousse.android02.musculaction.data.local.MusculactionDAO
import ca.philrousse.android02.musculaction.data.local.MusculactionRoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): MusculactionRoomDB {
        return MusculactionRoomDB.getInstance(context)
    }

    @Provides
    fun provideMusculactionDAO(appDatabase: MusculactionRoomDB): MusculactionDAO {
        return appDatabase.dao()
    }
}