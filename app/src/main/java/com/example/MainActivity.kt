package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.CallSimulationDialog
import com.example.ui.components.CreatePostDialog
import com.example.ui.components.FGameBottomBar
import com.example.ui.components.FGameTopBar
import com.example.ui.components.PlayMiniGameDialog
import com.example.ui.components.WithdrawalDialog
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.GroupsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MessagesScreen
import com.example.ui.screens.MonetizationScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ShortsScreen
import com.example.ui.screens.TournamentsScreen
import com.example.ui.theme.FGameTheme
import com.example.ui.viewmodel.FGameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FGameTheme {
                FGameApp()
            }
        }
    }
}

@Composable
fun FGameApp(viewModel: FGameViewModel = viewModel()) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val allSavedAccounts by viewModel.allSavedAccounts.collectAsStateWithLifecycle()
    val activeUserId by viewModel.activeUserId.collectAsStateWithLifecycle()
    val isAuthScreenVisible by viewModel.isAuthScreenVisible.collectAsStateWithLifecycle()

    val posts by viewModel.posts.collectAsStateWithLifecycle()
    val shorts by viewModel.shorts.collectAsStateWithLifecycle()
    val groups by viewModel.groups.collectAsStateWithLifecycle()
    val tournaments by viewModel.tournaments.collectAsStateWithLifecycle()
    val payouts by viewModel.payouts.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()

    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val activeChatRoom by viewModel.activeChatRoom.collectAsStateWithLifecycle()
    val activeCallType by viewModel.activeCallType.collectAsStateWithLifecycle()
    val showMiniGameDialog by viewModel.showMiniGameDialog.collectAsStateWithLifecycle()
    val payoutMessage by viewModel.payoutMessage.collectAsStateWithLifecycle()

    var showCreatePostDialog by remember { mutableStateOf(false) }
    var showWithdrawalDialog by remember { mutableStateOf(false) }

    if (isAuthScreenVisible) {
        AuthScreen(
            savedAccounts = allSavedAccounts,
            activeUserId = activeUserId,
            onSwitchAccount = { userId -> viewModel.switchAccount(userId) },
            onRegisterNewUser = { username, handle, phone, email, bio, pin ->
                viewModel.registerNewSystemUser(username, handle, phone, email, bio, pin)
            },
            onDismiss = { viewModel.closeAuthScreen() }
        )
    } else {
        Scaffold(
            topBar = {
                FGameTopBar(
                    currentUser = currentUser,
                    onOpenSearch = { viewModel.selectTab(0) },
                    onOpenMessages = { viewModel.selectTab(2) },
                    onOpenNotifications = { viewModel.selectTab(0) },
                    onOpenCreateDialog = { showCreatePostDialog = true },
                    onOpenMiniGame = { viewModel.openMiniGame() },
                    onOpenMonetization = { viewModel.selectTab(5) },
                    onOpenAuth = { viewModel.logout() }
                )
            },
            bottomBar = {
                FGameBottomBar(
                    selectedTab = currentTab,
                    onTabSelected = { viewModel.selectTab(it) }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    0 -> HomeScreen(
                        posts = posts,
                        onLikePost = { viewModel.toggleLikePost(it) },
                        onSavePost = { viewModel.toggleSavePost(it) },
                        onAddComment = { postId, text -> viewModel.addComment(postId, text) },
                        onOpenCreatePost = { showCreatePostDialog = true }
                    )
                    1 -> ShortsScreen(
                        shorts = shorts,
                        onLikeShort = { viewModel.toggleLikeShort(it) }
                    )
                    2 -> MessagesScreen(
                        activeRoomId = activeChatRoom,
                        onSelectRoom = { viewModel.selectChatRoom(it) },
                        messages = chatMessages,
                        onSendMessage = { text, type, sec -> viewModel.sendChatMessage(text, type, sec) },
                        onStartCall = { viewModel.startCall(it) }
                    )
                    3 -> GroupsScreen(
                        groups = groups,
                        onToggleJoinGroup = { viewModel.toggleGroupJoin(it) },
                        onCreateGroup = { name, desc, cat, isPrivate ->
                            viewModel.createGroup(name, desc, cat, isPrivate)
                        }
                    )
                    4 -> TournamentsScreen(
                        tournaments = tournaments,
                        onToggleJoinTournament = { viewModel.toggleTournamentJoin(it) },
                        onCreateTournament = { title, gameName, isPaid, fee, prize ->
                            viewModel.createTournament(title, gameName, isPaid, fee, prize)
                        },
                        onOpenMiniGame = { viewModel.openMiniGame() }
                    )
                    5 -> MonetizationScreen(
                        currentUser = currentUser,
                        payouts = payouts,
                        onOpenWithdrawalDialog = { showWithdrawalDialog = true },
                        payoutMessage = payoutMessage,
                        onClearPayoutMessage = { viewModel.clearPayoutMessage() }
                    )
                    6 -> ProfileScreen(
                        currentUser = currentUser,
                        onUpdateProfile = { name, bio, phone, email, twoFactor ->
                            viewModel.updateUserProfile(name, bio, phone, email, twoFactor)
                        },
                        onLogout = { viewModel.logout() }
                    )
                }
            }
        }
    }

    // Active Dialog Modals
    if (showCreatePostDialog) {
        CreatePostDialog(
            onDismiss = { showCreatePostDialog = false },
            onSubmitPost = { content, type ->
                viewModel.createNewPost(content, type)
            }
        )
    }

    if (showMiniGameDialog) {
        PlayMiniGameDialog(
            onDismiss = { viewModel.closeMiniGame() },
            onRewardPoints = { points -> viewModel.addPointsFromMiniGame(points) }
        )
    }

    if (showWithdrawalDialog) {
        WithdrawalDialog(
            currentPoints = currentUser?.pointsBalance ?: 0L,
            onDismiss = { showWithdrawalDialog = false },
            onRequestPayout = { pts, method, acct ->
                viewModel.requestPayout(pts, method, acct)
            }
        )
    }

    if (activeCallType != null) {
        CallSimulationDialog(
            callType = activeCallType!!,
            onEndCall = { viewModel.endCall() }
        )
    }
}
