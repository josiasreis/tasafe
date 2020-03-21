package br.com.tasafe.model.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.tasafe.model.data.Site
import br.com.tasafe.model.data.User

@Database(entities = [User::class, Site::class], version = 6,exportSchema = false)
abstract class TaSafeDataBase : RoomDatabase() {

    abstract fun userDAO(): UserDAO
    abstract fun siteDAO(): SiteDAO

    companion object {
        @Volatile
        private var INSTANCE: TaSafeDataBase? = null

        private class TaSafeDatabaseCallback : RoomDatabase.Callback()

        fun getDatabase(context: Context): TaSafeDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaSafeDataBase::class.java,
                    "tasafe_database"
                ) .addCallback(TaSafeDatabaseCallback())
                  .fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}