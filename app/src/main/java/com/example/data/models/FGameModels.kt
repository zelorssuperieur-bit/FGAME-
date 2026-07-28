package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = "me",
    val username: String = "Alex_Gamer",
    val userHandle: String = "@alex_fgame",
    val bio: String = "🎮 Gamer Pro & Creator | FIFA & Free Fire | 📍 Abidjan",
    val avatarUrl: String = "",
    val followersCount: Int = 18450, // > 15,000 threshold for monetization!
    val followingCount: Int = 342,
    val pointsBalance: Long = 125000L, // 1000 pts = 2500 FCFA => 125k pts = 312,500 FCFA
    val isVerified: Boolean = true,
    val badges: String = "VERIFIED_CREATOR,TOURNAMENT_CHAMP,PRO_GAMER",
    val phoneNumber: String = "+225 07 89 45 12 34",
    val email: String = "alex.fgame@studio.ci",
    val isTwoFactorEnabled: Boolean = true,
    val pinCode: String = "1234",
    val isDeviceRegistered: Boolean = true,
    val deviceModel: String = "FGAME Mobile Device",
    val lastLoginTimestamp: String = "Aujourd'hui"
)

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorName: String,
    val authorHandle: String,
    val authorAvatar: String = "",
    val isAuthorVerified: Boolean = false,
    val content: String,
    val postType: String = "TEXT", // TEXT, PHOTO, VIDEO, SHORT
    val mediaUrl: String = "",
    val mediaDrawableRes: Int = 0,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val timestamp: String = "À l'instant"
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val postId: Long,
    val authorName: String,
    val authorAvatar: String = "",
    val content: String,
    val timestamp: String = "À l'instant"
)

@Entity(tableName = "short_videos")
data class ShortVideoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val creatorName: String,
    val creatorHandle: String,
    val creatorAvatar: String = "",
    val musicTrack: String,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val isLiked: Boolean = false,
    val thumbnailDrawableRes: Int = 0
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chatRoomId: String, // e.g. "group_1" or "private_user2"
    val senderName: String,
    val senderAvatar: String = "",
    val text: String,
    val messageType: String = "TEXT", // TEXT, VOICE, MEDIA
    val voiceDurationSec: Int = 0,
    val timestamp: String = "12:30",
    val isMine: Boolean = false
)

@Entity(tableName = "community_groups")
data class CommunityGroupEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val groupName: String,
    val description: String,
    val category: String, // e.g. "Gaming", "Vidéos", "Monétisation", "Esports"
    val memberCount: Int,
    val isPrivate: Boolean = false,
    val isJoined: Boolean = false,
    val adminName: String = "FGAME Admin"
)

@Entity(tableName = "tournaments")
data class TournamentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val gameName: String,
    val isPaid: Boolean = false,
    val entryFeePoints: Int = 0,
    val prizePoolPoints: Long = 50000L, // 50,000 pts = 125,000 FCFA
    val prizePoolFcfa: Long = 125000L,
    val participantsCount: Int = 12,
    val maxParticipants: Int = 64,
    val startDate: String = "Aujourd'hui 20h00",
    val status: String = "LIVE", // UPCOMING, LIVE, COMPLETED
    val winnerName: String = "-",
    val isJoined: Boolean = false
)

@Entity(tableName = "payout_transactions")
data class PayoutTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amountPoints: Long,
    val amountFcfa: Long,
    val paymentMethod: String, // "Orange Money", "MTN Mobile Money", "Wave", "Airtel Money", "Carte Bancaire"
    val accountDetails: String,
    val date: String,
    val status: String = "COMPLÉTÉ" // PENDING, COMPLETED
)
