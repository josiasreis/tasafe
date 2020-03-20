package br.com.tasafe.model.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.tasafe.model.data.Site
import br.com.tasafe.model.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(User::class, Site::class), version = 6,exportSchema = false)
public abstract class TaSafeDataBase : RoomDatabase() {

    abstract fun userDAO(): UserDAO
    abstract fun siteDAO(): SiteDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: TaSafeDataBase? = null


        private class TaSafeDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
            }
        }

        fun getDatabase(context: Context, scope: CoroutineScope): TaSafeDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaSafeDataBase::class.java,
                    "tasafe_database"
                ) .addCallback(TaSafeDatabaseCallback(scope))
                  .fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}