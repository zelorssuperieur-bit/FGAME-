package com.example.data.repository

import com.example.R
import com.example.data.local.FGameDao
import com.example.data.models.ChatMessageEntity
import com.example.data.models.CommentEntity
import com.example.data.models.CommunityGroupEntity
import com.example.data.models.PayoutTransactionEntity
import com.example.data.models.PostEntity
import com.example.data.models.ShortVideoEntity
import com.example.data.models.TournamentEntity
import com.example.data.models.UserEntity
import kotlinx.coroutines.flow.Flow

class FGameRepository(private val dao: FGameDao) {

    val currentUser: Flow<UserEntity?> = dao.getUserFlow("me")
    val allUsers: Flow<List<UserEntity>> = dao.getAllUsersFlow()
    val allPosts: Flow<List<PostEntity>> = dao.getAllPosts()
    val allShorts: Flow<List<ShortVideoEntity>> = dao.getAllShortVideos()
    val allGroups: Flow<List<CommunityGroupEntity>> = dao.getAllGroups()
    val allTournaments: Flow<List<TournamentEntity>> = dao.getAllTournaments()
    val allPayouts: Flow<List<PayoutTransactionEntity>> = dao.getAllPayouts()

    fun getUserFlow(userId: String): Flow<UserEntity?> = dao.getUserFlow(userId)
    fun getComments(postId: Long): Flow<List<CommentEntity>> = dao.getCommentsForPost(postId)
    fun getChatMessages(roomId: String): Flow<List<ChatMessageEntity>> = dao.getMessagesForRoom(roomId)

    suspend fun seedInitialDataIfNeeded() {
        val user = dao.getUserOnce("me")
        if (user == null) {
            // Seed main account
            dao.insertOrUpdateUser(
                UserEntity(
                    id = "me",
                    username = "Alex_Gamer",
                    userHandle = "@alex_fgame",
                    bio = "🎮 Gamer Pro & Creator FGAME | Champion Free Fire 2026 | 📍 Abidjan",
                    followersCount = 18450, // Monétisation active !
                    followingCount = 380,
                    pointsBalance = 142000L, // 142k pts = 355,000 FCFA
                    isVerified = true,
                    badges = "CERTIFIÉ,CHAMPION,CREATEUR_TOP",
                    phoneNumber = "+225 07 89 45 12 34",
                    email = "alex.g@fgame.ci",
                    pinCode = "1234",
                    lastLoginTimestamp = "Aujourd'hui 14:10"
                )
            )
            // Seed secondary saved account on device
            dao.insertOrUpdateUser(
                UserEntity(
                    id = "user_sita",
                    username = "Sita_Pro_Gamer",
                    userHandle = "@sita_gaming",
                    bio = "🔥 Championne FIFA Mobile CI | Team Leader Esports | 📍 Yamoussoukro",
                    followersCount = 9400,
                    followingCount = 210,
                    pointsBalance = 45000L,
                    isVerified = true,
                    badges = "PRO_GAMER,VERIFIED",
                    phoneNumber = "+225 05 12 34 56 78",
                    email = "sita.p@fgame.ci",
                    pinCode = "5678",
                    lastLoginTimestamp = "Hier 18:30"
                )
            )

            // Seed Feed Posts
            dao.insertPost(
                PostEntity(
                    id = 1,
                    authorName = "FGAME Official",
                    authorHandle = "@fgame_app",
                    isAuthorVerified = true,
                    content = "🔥 Bienvenue sur FGAME - Le Réseau Social Nouvelle Génération ! Créez du contenu, participez aux tournois Esports et convertissez vos points en FCFA directement via Orange Money, MTN, Wave & Airtel ! 🚀💰",
                    postType = "PHOTO",
                    mediaDrawableRes = R.drawable.img_creator_hero_1785266866535,
                    likesCount = 4280,
                    commentsCount = 312,
                    sharesCount = 184,
                    timestamp = "Il y a 10 min"
                )
            )
            dao.insertPost(
                PostEntity(
                    id = 2,
                    authorName = "Esports Abidjan Arena",
                    authorHandle = "@esports_ci",
                    isAuthorVerified = true,
                    content = "🏆 Le Grand Tournoi Free Fire FGAME commence ce soir à 20h ! Cashprize : 100 000 Points FGAME (250 000 FCFA). Inscrivez-vous vite dans l'onglet Tournois ! 📱🎮",
                    postType = "PHOTO",
                    mediaDrawableRes = R.drawable.img_tournament_banner_1785266855461,
                    likesCount = 1950,
                    commentsCount = 142,
                    sharesCount = 89,
                    timestamp = "Il y a 1h"
                )
            )
            dao.insertPost(
                PostEntity(
                    id = 3,
                    authorName = "Kouassi Streamer",
                    authorHandle = "@kouassi_tv",
                    isAuthorVerified = false,
                    content = "Victoire Épique au dernier tournoi FIFA 2026 ! J'ai déjà retiré mes gains de 50 000 FCFA sur Wave en 2 minutes ! Merci FGAME 🔥🎯",
                    postType = "TEXT",
                    likesCount = 820,
                    commentsCount = 64,
                    sharesCount = 19,
                    timestamp = "Il y a 3h"
                )
            )

            // Seed Comments for post 1
            dao.insertComment(
                CommentEntity(
                    postId = 1,
                    authorName = "Sita Gamer",
                    content = "Incroyable l'application ! La vitesse des paiements Wave est au top 🔥",
                    timestamp = "Il y a 5 min"
                )
            )
            dao.insertComment(
                CommentEntity(
                    postId = 1,
                    authorName = "Mohamed FIFA",
                    content = "Qui est chaud pour créer un groupe d'entraînement ce soir ?",
                    timestamp = "Il y a 2 min"
                )
            )

            // Seed Short Videos (TikTok style)
            dao.insertShortVideo(
                ShortVideoEntity(
                    id = 1,
                    title = "💥 Highlight Clutch Free Fire 1v4 en Finale ! Abonne-toi pour plus !",
                    creatorName = "Alex_Gamer",
                    creatorHandle = "@alex_fgame",
                    musicTrack = "🎶 FGAME Beats - Afro Trap Gaming #1",
                    likesCount = 12400,
                    commentsCount = 850,
                    sharesCount = 420,
                    isLiked = true,
                    thumbnailDrawableRes = R.drawable.img_tournament_banner_1785266855461
                )
            )
            dao.insertShortVideo(
                ShortVideoEntity(
                    id = 2,
                    title = "⚡ Tuto: Comment monétiser ses vidéos dès 15 000 abonnés sur FGAME 💰",
                    creatorName = "FGAME Creator Hub",
                    creatorHandle = "@fgame_creators",
                    musicTrack = "🎶 FGAME Studio - Success Sound",
                    likesCount = 28900,
                    commentsCount = 1430,
                    sharesCount = 980,
                    isLiked = false,
                    thumbnailDrawableRes = R.drawable.img_creator_hero_1785266866535
                )
            )
            dao.insertShortVideo(
                ShortVideoEntity(
                    id = 3,
                    title = "🎮 Test du nouveau jeu rétro FGAME Arcade en direct !",
                    creatorName = "Yao Pixel",
                    creatorHandle = "@yao_pixel",
                    musicTrack = "🎶 Synthwave Gaming Mix 2026",
                    likesCount = 5600,
                    commentsCount = 210,
                    sharesCount = 95,
                    isLiked = false,
                    thumbnailDrawableRes = R.drawable.img_fgame_icon_1785266845474
                )
            )

            // Seed Groups
            dao.insertGroup(
                CommunityGroupEntity(
                    id = 1,
                    groupName = "🌍 FGAME Africa Gaming Community",
                    description = "La plus grande communauté de gamers en Afrique de l'Ouest. Conseils, lives et tournois !",
                    category = "Esports",
                    memberCount = 48200,
                    isPrivate = false,
                    isJoined = true
                )
            )
            dao.insertGroup(
                CommunityGroupEntity(
                    id = 2,
                    groupName = "💰 Créateurs & Monétisation FGAME",
                    description = "Espace d'entraide pour optimiser vos vues, vos abonnés et échanger des astuces de monétisation.",
                    category = "Monétisation",
                    memberCount = 21500,
                    isPrivate = false,
                    isJoined = true
                )
            )
            dao.insertGroup(
                CommunityGroupEntity(
                    id = 3,
                    groupName = "⚽ FIFA 2026 Mobile League",
                    description = "Ligue privée pour les passionnés du ballon rond virtuel. Tournois chaque week-end.",
                    category = "Gaming",
                    memberCount = 12900,
                    isPrivate = true,
                    isJoined = false
                )
            )

            // Seed Tournaments
            dao.insertTournament(
                TournamentEntity(
                    id = 1,
                    title = "🔥 FGAME Free Fire Battle Royale #12",
                    gameName = "Free Fire",
                    isPaid = false,
                    entryFeePoints = 0,
                    prizePoolPoints = 100000L, // 250 000 FCFA
                    prizePoolFcfa = 250000L,
                    participantsCount = 48,
                    maxParticipants = 50,
                    startDate = "Aujourd'hui 20h00",
                    status = "LIVE",
                    isJoined = true
                )
            )
            dao.insertTournament(
                TournamentEntity(
                    id = 2,
                    title = "⚽ Coupe d'Afrique FIFA Mobile 2026",
                    gameName = "FIFA 2026",
                    isPaid = true,
                    entryFeePoints = 1000, // 2500 FCFA
                    prizePoolPoints = 200000L, // 500 000 FCFA
                    prizePoolFcfa = 500000L,
                    participantsCount = 24,
                    maxParticipants = 32,
                    startDate = "Demain 18h00",
                    status = "UPCOMING",
                    isJoined = false
                )
            )
            dao.insertTournament(
                TournamentEntity(
                    id = 3,
                    title = "🎯 Quiz Speedrun FGAME Arcade",
                    gameName = "FGAME Mini-Game",
                    isPaid = false,
                    entryFeePoints = 0,
                    prizePoolPoints = 20000L, // 50 000 FCFA
                    prizePoolFcfa = 50000L,
                    participantsCount = 128,
                    maxParticipants = 200,
                    startDate = "En cours",
                    status = "LIVE",
                    isJoined = true
                )
            )

            // Seed Messages
            dao.insertMessage(
                ChatMessageEntity(
                    chatRoomId = "group_1",
                    senderName = "Sita Gamer",
                    text = "Salut tout le monde ! Prêts pour le tournoi Free Fire de 20h ?",
                    timestamp = "14:20",
                    isMine = false
                )
            )
            dao.insertMessage(
                ChatMessageEntity(
                    chatRoomId = "group_1",
                    senderName = "Alex_Gamer",
                    text = "Oui grave ! Je suis déjà inscrit, mon squad est au complet 💪",
                    timestamp = "14:22",
                    isMine = true
                )
            )
            dao.insertMessage(
                ChatMessageEntity(
                    chatRoomId = "private_sita",
                    senderName = "Sita Gamer",
                    text = "Hey Alex, tu veux faire une partie en duo avant le tournoi ?",
                    timestamp = "15:05",
                    isMine = false
                )
            )

            // Seed Payout Transactions
            dao.insertPayout(
                PayoutTransactionEntity(
                    id = 1,
                    amountPoints = 20000L,
                    amountFcfa = 50000L,
                    paymentMethod = "Wave",
                    accountDetails = "+225 07 89 ** ** 34",
                    date = "26 Juillet 2026",
                    status = "COMPLÉTÉ"
                )
            )
            dao.insertPayout(
                PayoutTransactionEntity(
                    id = 2,
                    amountPoints = 40000L,
                    amountFcfa = 100000L,
                    paymentMethod = "Orange Money",
                    accountDetails = "+225 07 89 ** ** 34",
                    date = "15 Juillet 2026",
                    status = "COMPLÉTÉ"
                )
            )
        }
    }

    // User updates
    suspend fun updateUser(user: UserEntity) = dao.insertOrUpdateUser(user)

    // Posts actions
    suspend fun createPost(post: PostEntity) = dao.insertPost(post)
    suspend fun toggleLikePost(post: PostEntity) {
        val updated = post.copy(
            isLiked = !post.isLiked,
            likesCount = if (!post.isLiked) post.likesCount + 1 else post.likesCount - 1
        )
        dao.updatePost(updated)
    }
    suspend fun toggleSavePost(post: PostEntity) {
        val updated = post.copy(isSaved = !post.isSaved)
        dao.updatePost(updated)
    }

    // Comments
    suspend fun addComment(postId: Long, authorName: String, content: String) {
        dao.insertComment(CommentEntity(postId = postId, authorName = authorName, content = content))
    }

    // Short Videos actions
    suspend fun toggleLikeShort(short: ShortVideoEntity) {
        val updated = short.copy(
            isLiked = !short.isLiked,
            likesCount = if (!short.isLiked) short.likesCount + 1 else short.likesCount - 1
        )
        dao.updateShortVideo(updated)
    }

    // Chat actions
    suspend fun sendMessage(roomId: String, text: String, senderName: String, isMine: Boolean, type: String = "TEXT", voiceDuration: Int = 0) {
        dao.insertMessage(
            ChatMessageEntity(
                chatRoomId = roomId,
                senderName = senderName,
                text = text,
                messageType = type,
                voiceDurationSec = voiceDuration,
                timestamp = "Aujourd'hui",
                isMine = isMine
            )
        )
    }

    // Group actions
    suspend fun toggleJoinGroup(group: CommunityGroupEntity) {
        val updated = group.copy(
            isJoined = !group.isJoined,
            memberCount = if (!group.isJoined) group.memberCount + 1 else group.memberCount - 1
        )
        dao.updateGroup(updated)
    }
    suspend fun createGroup(group: CommunityGroupEntity) = dao.insertGroup(group)

    // Tournament actions
    suspend fun toggleJoinTournament(tournament: TournamentEntity) {
        val updated = tournament.copy(
            isJoined = !tournament.isJoined,
            participantsCount = if (!tournament.isJoined) tournament.participantsCount + 1 else tournament.participantsCount - 1
        )
        dao.updateTournament(updated)
    }
    suspend fun createTournament(tournament: TournamentEntity) = dao.insertTournament(tournament)

    // Payout / Points
    suspend fun requestPayout(userId: String, amountPoints: Long, paymentMethod: String, accountDetails: String): Boolean {
        val user = dao.getUserOnce(userId) ?: return false
        if (user.pointsBalance < amountPoints) return false

        val fcfaAmount = (amountPoints / 1000L) * 2500L
        val updatedUser = user.copy(pointsBalance = user.pointsBalance - amountPoints)
        dao.insertOrUpdateUser(updatedUser)

        dao.insertPayout(
            PayoutTransactionEntity(
                amountPoints = amountPoints,
                amountFcfa = fcfaAmount,
                paymentMethod = paymentMethod,
                accountDetails = accountDetails,
                date = "Aujourd'hui",
                status = "COMPLÉTÉ"
            )
        )
        return true
    }

    suspend fun addPointsFromGame(userId: String, points: Long) {
        val user = dao.getUserOnce(userId) ?: return
        dao.insertOrUpdateUser(user.copy(pointsBalance = user.pointsBalance + points))
    }
}
