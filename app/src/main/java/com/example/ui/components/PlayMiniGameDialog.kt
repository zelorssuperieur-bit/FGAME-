package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple

@Composable
fun PlayMiniGameDialog(
    onDismiss: () -> Unit,
    onRewardPoints: (points: Long) -> Unit
) {
    var gameMode by remember { mutableStateOf("MENU") } // MENU, TAP_RUSH, QUIZ, RESULT
    var tapScore by remember { mutableIntStateOf(0) }
    var pointsWon by remember { mutableLongStateOf(0L) }
    var quizStep by remember { mutableIntStateOf(0) }

    val quizQuestions = remember {
        listOf(
            Triple("Quel est le taux de conversion sur FGAME ?", listOf("1 000 Pts = 2 500 FCFA", "1 000 Pts = 1 000 FCFA", "5 000 Pts = 2 500 FCFA"), 0),
            Triple("A partir de combien d'abonnés la monétisation débute-t-elle ?", listOf("5 000", "10 000", "15 000"), 2),
            Triple("Quel moyen de paiement n'est PAS supporté sur FGAME ?", listOf("Orange Money & Wave", "Chèque bancaire physique", "Airtel Money & MTN"), 1)
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.VideogameAsset, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.size(8.dp))
                Text("🕹️ FGAME Arcade & Points", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (gameMode) {
                    "MENU" -> {
                        Text(
                            text = "Jouez aux mini-jeux pour gagner des Points FGAME convertibles en FCFA !",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            onClick = {
                                tapScore = 0
                                gameMode = "TAP_RUSH"
                            },
                            colors = CardDefaults.cardColors(containerColor = NeonPurple.copy(alpha = 0.2f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("start_tap_rush_game")
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.FlashOn, contentDescription = null, tint = GoldYellow, modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.size(12.dp))
                                Column {
                                    Text("⚡ Tap Speed Rush (10s)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    Text("Cliquez le plus vite possible ! Jusqu'à 1000 Pts", style = MaterialTheme.typography.bodySmall, color = NeonCyan)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            onClick = {
                                quizStep = 0
                                gameMode = "QUIZ"
                            },
                            colors = CardDefaults.cardColors(containerColor = NeonCyan.copy(alpha = 0.15f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("start_quiz_game")
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = GoldYellow, modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.size(12.dp))
                                Column {
                                    Text("🧠 Quiz Cultur'Game FGAME", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    Text("3 questions = 1 500 Pts (3 750 FCFA)", style = MaterialTheme.typography.bodySmall, color = GoldYellow)
                                }
                            }
                        }
                    }

                    "TAP_RUSH" -> {
                        Text("Cliquez sur la boule néon le plus vite possible !", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Score : $tapScore Taps", fontSize = 22.sp, fontWeight = FontWeight.Black, color = GoldYellow)

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(NeonCyan)
                                .clickable {
                                    tapScore++
                                }
                                .testTag("tap_game_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("TAP!", color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                pointsWon = tapScore.toLong() * 30L
                                onRewardPoints(pointsWon)
                                gameMode = "RESULT"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                        ) {
                            Text("Terminer & Récolter")
                        }
                    }

                    "QUIZ" -> {
                        val q = quizQuestions[quizStep]
                        Text("Question ${quizStep + 1}/3", fontWeight = FontWeight.Bold, color = NeonCyan)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(q.first, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)

                        Spacer(modifier = Modifier.height(12.dp))

                        q.second.forEachIndexed { index, option ->
                            Button(
                                onClick = {
                                    if (quizStep < quizQuestions.size - 1) {
                                        quizStep++
                                    } else {
                                        pointsWon = 1500L
                                        onRewardPoints(pointsWon)
                                        gameMode = "RESULT"
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(option, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }

                    "RESULT" -> {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = GoldYellow, modifier = Modifier.size(56.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Félicitations ! 🎉", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Vous avez gagné +$pointsWon Points FGAME !",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldYellow
                        )
                        val fcfaGained = (pointsWon / 1000L) * 2500L
                        Text(
                            text = "Valeur estimée : ~$fcfaGained FCFA",
                            style = MaterialTheme.typography.bodySmall,
                            color = NeonCyan
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
            ) {
                Text("Fermer")
            }
        }
    )
}
