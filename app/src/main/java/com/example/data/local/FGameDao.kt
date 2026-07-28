package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.models.ChatMessageEntity
import com.example.data.models.CommentEntity
import com.example.data.models.CommunityGroupEntity
import com.example.data.models.PayoutTransactionEntity
import com.example.data.models.PostEntity
import com.example.data.models.ShortVideoEntity
import com.example.data.models.TournamentEntity
import com.example.data.models.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FGameDao {

    // User Profile
    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserFlow(id: String = "me"): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserOnce(id: String = "me"): UserEntity?

    @Query("SELECT * FROM users")
    fun getAllUsersFlow(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUser(id: String)

    // Posts
    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)

    @Update
    suspend fun updatePost(post: PostEntity)

    @Query("DELETE FROM posts WHERE id = :postId")
    suspend fun deletePost(postId: Long)

    // Comments
    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY id ASC")
    fun getCommentsForPost(postId: Long): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)

    // Short Videos
    @Query("SELECT * FROM short_videos ORDER BY id DESC")
    fun getAllShortVideos(): Flow<List<ShortVideoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShortVideo(shortVideo: ShortVideoEntity)

    @Update
    suspend fun updateShortVideo(shortVideo: ShortVideoEntity)

    // Chat Messages
    @Query("SELECT * FROM chat_messages WHERE chatRoomId = :chatRoomId ORDER BY id ASC")
    fun getMessagesForRoom(chatRoomId: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    // Groups
    @Query("SELECT * FROM community_groups ORDER BY memberCount DESC")
    fun getAllGroups(): Flow<List<CommunityGroupEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: CommunityGroupEntity)

    @Update
    suspend fun updateGroup(group: CommunityGroupEntity)

    // Tournaments
    @Query("SELECT * FROM tournaments ORDER BY id DESC")
    fun getAllTournaments(): Flow<List<TournamentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTournament(tournament: TournamentEntity)

    @Update
    suspend fun updateTournament(tournament: TournamentEntity)

    // Payout Transactions
    @Query("SELECT * FROM payout_transactions ORDER BY id DESC")
    fun getAllPayouts(): Flow<List<PayoutTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayout(payout: PayoutTransactionEntity)
}
