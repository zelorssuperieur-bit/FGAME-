package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.models.ChatMessageEntity
import com.example.data.models.CommunityGroupEntity
import com.example.data.models.PayoutTransactionEntity
import com.example.data.models.PostEntity
import com.example.data.models.ShortVideoEntity
import com.example.data.models.TournamentEntity
import com.example.data.models.UserEntity
import com.example.data.repository.FGameRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FGameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FGameRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = FGameRepository(db.fGameDao())
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
    }

    // Active User ID & Multi-Account System
    private val _activeUserId = MutableStateFlow("me")
    val activeUserId: StateFlow<String> = _activeUserId.asStateFlow()

    private val _isAuthScreenVisible = MutableStateFlow(false)
    val isAuthScreenVisible: StateFlow<Boolean> = _isAuthScreenVisible.asStateFlow()

    val allSavedAccounts: StateFlow<List<UserEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentUser: StateFlow<UserEntity?> = activeUserId
        .flatMapLatest { id -> repository.getUserFlow(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val posts: StateFlow<List<PostEntity>> = repository.allPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val shorts: StateFlow<List<ShortVideoEntity>> = repository.allShorts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val groups: StateFlow<List<CommunityGroupEntity>> = repository.allGroups
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tournaments: StateFlow<List<TournamentEntity>> = repository.allTournaments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val payouts: StateFlow<List<PayoutTransactionEntity>> = repository.allPayouts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Multi-Account Operations (Facebook System Style)
    fun switchAccount(userId: String) {
        _activeUserId.value = userId
        _isAuthScreenVisible.value = false
        viewModelScope.launch {
            val user = allSavedAccounts.value.find { it.id == userId }
            if (user != null) {
                repository.updateUser(user.copy(lastLoginTimestamp = "À l'instant"))
            }
        }
    }

    fun logout() {
        _isAuthScreenVisible.value = true
    }

    fun closeAuthScreen() {
        _isAuthScreenVisible.value = false
    }

    fun registerNewSystemUser(
        username: String,
        handle: String,
        phone: String,
        email: String,
        bio: String,
        pinCode: String
    ) {
        viewModelScope.launch {
            val newId = "user_" + System.currentTimeMillis()
            val formattedHandle = if (handle.startsWith("@")) handle else "@$handle"
            val newUser = UserEntity(
                id = newId,
                username = username,
                userHandle = formattedHandle,
                bio = if (bio.isBlank()) "🎮 Membre FGAME Network | Passionné Esports & Gaming" else bio,
                followersCount = 1500, // Starting encouragement
                followingCount = 12,
                pointsBalance = 5000L, // 5,000 Pts = 12,500 FCFA Welcome Bonus!
                isVerified = true,
                badges = "NOUVEAU_NEMBRE,FGAME_CREATOR",
                phoneNumber = phone,
                email = email,
                pinCode = if (pinCode.isBlank()) "1234" else pinCode,
                isTwoFactorEnabled = true,
                lastLoginTimestamp = "À l'instant"
            )
            repository.updateUser(newUser)
            _activeUserId.value = newId
            _isAuthScreenVisible.value = false
        }
    }

    // Active Chat Room ID (Defined first before chatMessages)
    private val _activeChatRoom = MutableStateFlow<String?>("group_1")
    val activeChatRoom: StateFlow<String?> = _activeChatRoom.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val chatMessages: StateFlow<List<ChatMessageEntity>> = activeChatRoom
        .flatMapLatest { roomId ->
            repository.getChatMessages(roomId ?: "group_1")
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Navigation Tab
    private val _currentTab = MutableStateFlow(0) // 0: Accueil, 1: Shorts, 2: Messages, 3: Groupes, 4: Tournois, 5: Monétisation, 6: Profil
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    fun selectTab(tabIndex: Int) {
        _currentTab.value = tabIndex
    }

    fun selectChatRoom(roomId: String) {
        _activeChatRoom.value = roomId
    }

    // Active Call Simulation
    private val _activeCallType = MutableStateFlow<String?>(null) // "AUDIO" or "VIDEO" or null
    val activeCallType: StateFlow<String?> = _activeCallType.asStateFlow()

    fun startCall(type: String) {
        _activeCallType.value = type
    }
    fun endCall() {
        _activeCallType.value = null
    }

    // Interactive Mini-Game Arcade Dialog
    private val _showMiniGameDialog = MutableStateFlow(false)
    val showMiniGameDialog: StateFlow<Boolean> = _showMiniGameDialog.asStateFlow()

    fun openMiniGame() { _showMiniGameDialog.value = true }
    fun closeMiniGame() { _showMiniGameDialog.value = false }

    fun addPointsFromMiniGame(points: Long) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            repository.addPointsFromGame(user.id, points)
        }
    }

    // Post Creation State
    fun createNewPost(content: String, postType: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            repository.createPost(
                PostEntity(
                    authorName = user.username,
                    authorHandle = user.userHandle,
                    isAuthorVerified = user.isVerified,
                    content = content,
                    postType = postType,
                    timestamp = "À l'instant"
                )
            )
        }
    }

    fun toggleLikePost(post: PostEntity) {
        viewModelScope.launch { repository.toggleLikePost(post) }
    }

    fun toggleSavePost(post: PostEntity) {
        viewModelScope.launch { repository.toggleSavePost(post) }
    }

    fun addComment(postId: Long, text: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            repository.addComment(postId, user.username, text)
        }
    }

    fun toggleLikeShort(short: ShortVideoEntity) {
        viewModelScope.launch { repository.toggleLikeShort(short) }
    }

    fun sendChatMessage(text: String, type: String = "TEXT", voiceDuration: Int = 0) {
        val roomId = activeChatRoom.value ?: "group_1"
        val user = currentUser.value ?: return
        viewModelScope.launch {
            repository.sendMessage(roomId, text, user.username, isMine = true, type = type, voiceDuration = voiceDuration)
        }
    }

    fun toggleGroupJoin(group: CommunityGroupEntity) {
        viewModelScope.launch { repository.toggleJoinGroup(group) }
    }

    fun createGroup(name: String, desc: String, cat: String, isPrivate: Boolean) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            repository.createGroup(
                CommunityGroupEntity(
                    groupName = name,
                    description = desc,
                    category = cat,
                    memberCount = 1,
                    isPrivate = isPrivate,
                    isJoined = true,
                    adminName = user.username
                )
            )
        }
    }

    fun toggleTournamentJoin(tournament: TournamentEntity) {
        viewModelScope.launch { repository.toggleJoinTournament(tournament) }
    }

    fun createTournament(title: String, gameName: String, isPaid: Boolean, feePts: Int, prizePts: Long) {
        viewModelScope.launch {
            val fcfa = (prizePts / 1000L) * 2500L
            repository.createTournament(
                TournamentEntity(
                    title = title,
                    gameName = gameName,
                    isPaid = isPaid,
                    entryFeePoints = feePts,
                    prizePoolPoints = prizePts,
                    prizePoolFcfa = fcfa,
                    participantsCount = 1,
                    maxParticipants = 64,
                    startDate = "Prochainement",
                    status = "UPCOMING",
                    isJoined = true
                )
            )
        }
    }

    // Payout Request
    private val _payoutMessage = MutableStateFlow<String?>(null)
    val payoutMessage: StateFlow<String?> = _payoutMessage.asStateFlow()

    fun requestPayout(points: Long, method: String, account: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val success = repository.requestPayout(user.id, points, method, account)
            if (success) {
                val fcfa = (points / 1000L) * 2500L
                _payoutMessage.value = "✅ Retrait de $points Pts ($fcfa FCFA) vers $method ($account) réussi !"
            } else {
                _payoutMessage.value = "❌ Solde de points insuffisant."
            }
        }
    }

    fun clearPayoutMessage() {
        _payoutMessage.value = null
    }

    // Auth Simulation & Profile Update
    fun updateUserProfile(name: String, bio: String, phone: String, email: String, twoFactor: Boolean) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            repository.updateUser(
                user.copy(
                    username = name,
                    bio = bio,
                    phoneNumber = phone,
                    email = email,
                    isTwoFactorEnabled = twoFactor
                )
            )
        }
    }
}
