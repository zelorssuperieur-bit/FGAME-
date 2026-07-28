package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.PayoutTransactionEntity
import com.example.data.models.UserEntity
import com.example.ui.theme.AirtelMoneyColor
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.MtnMoneyColor
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.OrangeMoneyColor
import com.example.ui.theme.WaveMoneyColor

@Composable
fun MonetizationScreen(
    currentUser: UserEntity?,
    payouts: List<PayoutTransactionEntity>,
    onOpenWithdrawalDialog: () -> Unit,
    payoutMessage: String?,
    onClearPayoutMessage: () -> Unit
) {
    val user = currentUser ?: UserEntity()
    val points = user.pointsBalance
    val fcfaVal = (points / 1000L) * 2500L
    val subThreshold = 15000
    val progress = (user.followersCount.toFloat() / subThreshold.toFloat()).coerceAtMost(1.0f)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        // Top Header
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text("💰 Monétisation & Tableau de Bord", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Convertissez vos créations et victoires en revenus réels", style = MaterialTheme.typography.bodySmall, color = NeonCyan)
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Notification Banner
        if (payoutMessage != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = NeonPurple.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(payoutMessage, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Button(onClick = onClearPayoutMessage) {
                            Text("OK")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Wallet Balance Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("monetization_balance_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = GoldYellow, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Solde Disponible", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        }
                        Surface(
                            color = GoldYellow.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("1 000 Pts = 2 500 FCFA", color = GoldYellow, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "$points Points",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = GoldYellow
                    )
                    Text(
                        text = "≈ $fcfaVal FCFA",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onOpenWithdrawalDialog,
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("request_payout_button")
                    ) {
                        Icon(Icons.Default.Payment, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Demander un Retrait FCFA", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Eligibility Threshold Gauge
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (user.followersCount >= subThreshold) Icons.Default.CheckCircle else Icons.Default.Shield,
                                contentDescription = null,
                                tint = if (user.followersCount >= subThreshold) NeonCyan else GoldYellow
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Condition de Monétisation (15k Abonnés)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Text(
                            text = if (user.followersCount >= subThreshold) "ÉLIGIBLE ✓" else "${user.followersCount}/$subThreshold",
                            fontWeight = FontWeight.Bold,
                            color = if (user.followersCount >= subThreshold) NeonCyan else GoldYellow,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = NeonCyan,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Vos abonnés actuels : ${user.followersCount} abonnés.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Supported Payment Methods Badges
        item {
            Text("💳 Modes de Paiement Supportés", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PaymentMethodChip("Orange", OrangeMoneyColor)
                PaymentMethodChip("MTN", MtnMoneyColor)
                PaymentMethodChip("Wave", WaveMoneyColor)
                PaymentMethodChip("Airtel", AirtelMoneyColor)
                PaymentMethodChip("Carte", NeonPurple)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Payout Transaction History
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.History, contentDescription = null, tint = NeonCyan)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Historique des Retraits", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(payouts, key = { it.id }) { payout ->
            PayoutCardItem(payout = payout)
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
fun PaymentMethodChip(name: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = name,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun PayoutCardItem(payout: PayoutTransactionEntity) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("${payout.paymentMethod} • ${payout.accountDetails}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("Date : ${payout.date}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("+${payout.amountFcfa} FCFA", fontWeight = FontWeight.ExtraBold, color = NeonCyan, fontSize = 14.sp)
                Text("(${payout.amountPoints} Pts) • ${payout.status}", fontSize = 10.sp, color = GoldYellow, fontWeight = FontWeight.Bold)
            }
        }
    }
}
