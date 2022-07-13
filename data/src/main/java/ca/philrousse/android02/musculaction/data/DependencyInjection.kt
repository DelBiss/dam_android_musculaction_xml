package ca.philrousse.android02.musculaction.data

import ca.philrousse.android02.musculaction.remote.MusculactionRemoteDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

//    @Singleton
//    @Provides
//    fun provideAppDatabase(@ApplicationContext context: Context): MusculactionRoomDB {
//        return MusculactionRoomDB.getInstance(context)
//    }
//
//    @Provides
//    fun provideMusculactionLocalDAO(appDatabase: MusculactionRoomDB): MusculactionLocalDAO {
//        return appDatabase.dao()
//    }

    @Provides
    fun provideMusculactionRemoteDAO(): MusculactionRemoteDAO {
        return MusculactionRemoteDAO.getInstance()
    }

    @Singleton
    @Provides
    fun provideIMusculactionRepository(dao:MusculactionRemoteDAO): IMusculactionRepository {
        return MusculactionRemoteRepository.getInstance(dao)
    }


}