package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AirtelMoneyColor
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.MtnMoneyColor
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.OrangeMoneyColor
import com.example.ui.theme.WaveMoneyColor

@Composable
fun WithdrawalDialog(
    currentPoints: Long,
    onDismiss: () -> Unit,
    onRequestPayout: (points: Long, method: String, account: String) -> Unit
) {
    var pointsInput by remember { mutableStateOf("10000") }
    var selectedMethod by remember { mutableStateOf("Orange Money") }
    var accountInput by remember { mutableStateOf("+225 07 89 45 12 34") }

    val methods = listOf(
        Pair("Orange Money", OrangeMoneyColor),
        Pair("MTN Mobile Money", MtnMoneyColor),
        Pair("Wave", WaveMoneyColor),
        Pair("Airtel Money", AirtelMoneyColor),
        Pair("Carte Bancaire", NeonPurple)
    )

    val pointsToWithdraw = pointsInput.toLongOrNull() ?: 0L
    val calculatedFcfa = (pointsToWithdraw / 1000L) * 2500L

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = GoldYellow)
                Spacer(modifier = Modifier.width(8.dp))
                Text("💳 Demande de Retrait FCFA", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column {
                Card(
                    colors = CardDefaults.cardColors(containerColor = GoldYellow.copy(alpha = 0.15f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("💡 Règle de conversion :", fontWeight = FontWeight.Bold, color = GoldYellow, fontSize = 12.sp)
                        Text("1 000 Points = 2 500 FCFA", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = GoldYellow)
                        Text("Solde actuel : $currentPoints Pts", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Méthode de Paiement :", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(6.dp))

                Column {
                    methods.chunked(2).forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            row.forEach { (method, color) ->
                                val isSelected = selectedMethod == method
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            if (isSelected) color.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            width = if (isSelected) 2.dp else 0.dp,
                                            color = if (isSelected) color else Color.Transparent,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { selectedMethod = method }
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = method,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) color else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = pointsInput,
                    onValueChange = { pointsInput = it },
                    label = { Text("Nombre de Points à retirer") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("payout_points_input")
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "= $calculatedFcfa FCFA",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = accountInput,
                    onValueChange = { accountInput = it },
                    label = { Text("Numéro Mobile / Compte Récepteur") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("payout_account_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (pointsToWithdraw > 0 && accountInput.isNotBlank()) {
                        onRequestPayout(pointsToWithdraw, selectedMethod, accountInput)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                modifier = Modifier.testTag("submit_payout_button")
            ) {
                Text("Confirmer le Retrait", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}
