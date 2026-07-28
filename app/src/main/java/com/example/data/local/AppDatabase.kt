package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.models.ChatMessageEntity
import com.example.data.models.CommentEntity
import com.example.data.models.CommunityGroupEntity
import com.example.data.models.PayoutTransactionEntity
import com.example.data.models.PostEntity
import com.example.data.models.ShortVideoEntity
import com.example.data.models.TournamentEntity
import com.example.data.models.UserEntity

@Database(
    entities = [
        UserEntity::class,
        PostEntity::class,
        CommentEntity::class,
        ShortVideoEntity::class,
        ChatMessageEntity::class,
        CommunityGroupEntity::class,
        TournamentEntity::class,
        PayoutTransactionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fGameDao(): FGameDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fgame_social_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
