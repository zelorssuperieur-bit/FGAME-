package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.models.TournamentEntity
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple

@Composable
fun TournamentsScreen(
    tournaments: List<TournamentEntity>,
    onToggleJoinTournament: (TournamentEntity) -> Unit,
    onCreateTournament: (title: String, gameName: String, isPaid: Boolean, feePts: Int, prizePts: Long) -> Unit,
    onOpenMiniGame: () -> Unit
) {
    var showCreateTournamentDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            // Hero Esports Banner
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.img_tournament_banner_1785266855461),
                            contentDescription = "Tournaments Banner",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.55f))
                        )
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterStart)
                        ) {
                            Text("🏆 ARENA TOURNOIS FGAME", color = GoldYellow, fontWeight = FontWeight.Black, fontSize = 18.sp)
                            Text("Participez gratuitement ou à frais d'entrée et gagnez des FCFA !", color = Color.White, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                onClick = onOpenMiniGame,
                                shape = RoundedCornerShape(20.dp),
                                color = NeonPurple,
                                modifier = Modifier.testTag("arcade_banner_button")
                            ) {
                                Text("🕹️ Arcade Speedrun (Gagnez des Pts)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Tournament Items
            items(tournaments, key = { it.id }) { tournament ->
                TournamentCardItem(
                    tournament = tournament,
                    onToggleJoin = { onToggleJoinTournament(tournament) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }

        FloatingActionButton(
            onClick = { showCreateTournamentDialog = true },
            containerColor = GoldYellow,
            contentColor = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)
                .testTag("create_tournament_fab")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Organiser un Tournoi")
        }

        if (showCreateTournamentDialog) {
            CreateTournamentDialog(
                onDismiss = { showCreateTournamentDialog = false },
                onCreate = { title, gameName, isPaid, feePts, prizePts ->
                    onCreateTournament(title, gameName, isPaid, feePts, prizePts)
                    showCreateTournamentDialog = false
                }
            )
        }
    }
}

@Composable
fun TournamentCardItem(
    tournament: TournamentEntity,
    onToggleJoin: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("tournament_card_${tournament.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = if (tournament.status == "LIVE") Color.Red.copy(alpha = 0.2f) else NeonPurple.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (tournament.status == "LIVE") "🔴 EN DIRECT" else "📅 À VENIR",
                        color = if (tournament.status == "LIVE") Color.Red else NeonPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    color = if (tournament.isPaid) GoldYellow.copy(alpha = 0.2f) else NeonCyan.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (tournament.isPaid) "Payant (${tournament.entryFeePoints} Pts)" else "GRATUIT",
                        color = if (tournament.isPaid) GoldYellow else NeonCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(tournament.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text("Jeu : ${tournament.gameName}", style = MaterialTheme.typography.bodySmall, color = NeonCyan)

            Spacer(modifier = Modifier.height(12.dp))

            // Cashprize & Stats Box
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Cashprize Récompense :", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                        Text(
                            text = "${tournament.prizePoolPoints} Pts (${tournament.prizePoolFcfa} FCFA)",
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldYellow,
                            fontSize = 14.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.People, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${tournament.participantsCount}/${tournament.maxParticipants}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Début : ${tournament.startDate}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Button(
                    onClick = onToggleJoin,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (tournament.isJoined) MaterialTheme.colorScheme.surfaceVariant else NeonPurple
                    ),
                    modifier = Modifier.testTag("join_tournament_button_${tournament.id}")
                ) {
                    Text(
                        text = if (tournament.isJoined) "Inscrit ✓" else "S'inscrire",
                        color = if (tournament.isJoined) MaterialTheme.colorScheme.onSurface else Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CreateTournamentDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, gameName: String, isPaid: Boolean, feePts: Int, prizePts: Long) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var gameName by remember { mutableStateOf("Free Fire") }
    var isPaid by remember { mutableStateOf(false) }
    var prizePtsText by remember { mutableStateOf("50000") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("🏆 Créer un Tournoi", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Titre du Tournoi") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = gameName,
                    onValueChange = { gameName = it },
                    label = { Text("Jeu (Ex: Free Fire, FIFA, PUBG, Mobile Legends)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = prizePtsText,
                    onValueChange = { prizePtsText = it },
                    label = { Text("Cashprize (Points FGAME)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val prize = prizePtsText.toLongOrNull() ?: 50000L
                        onCreate(title, gameName, isPaid, 0, prize)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
            ) {
                Text("Publier le Tournoi", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Annuler") }
        }
    )
}
