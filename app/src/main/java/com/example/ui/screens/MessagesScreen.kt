package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.ChatMessageEntity
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.OnlineGreen

@Composable
fun MessagesScreen(
    activeRoomId: String?,
    onSelectRoom: (String) -> Unit,
    messages: List<ChatMessageEntity>,
    onSendMessage: (text: String, type: String, voiceSec: Int) -> Unit,
    onStartCall: (type: String) -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    var isRecordingVoice by remember { mutableStateOf(false) }

    val chatRooms = listOf(
        Pair("group_1", "🌍 Squad FGAME Africa"),
        Pair("private_sita", "👤 Sita Gamer"),
        Pair("private_mohamed", "👤 Mohamed FIFA")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Chat Room Header Bar
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                // Room Selector Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    chatRooms.forEach { (roomId, label) ->
                        val isSelected = (activeRoomId ?: "group_1") == roomId
                        Surface(
                            onClick = { onSelectRoom(roomId) },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) NeonPurple else MaterialTheme.colorScheme.surface,
                            modifier = Modifier.testTag("chat_room_tab_$roomId")
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Active Conversation Info & Call Launchers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(NeonPurple),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                            }
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(OnlineGreen)
                                    .align(Alignment.BottomEnd)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            val activeName = chatRooms.find { it.first == activeRoomId }?.second ?: "Discussion Chat"
                            Text(activeName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                            Text("En ligne • Mode chiffré FGAME", fontSize = 10.sp, color = OnlineGreen)
                        }
                    }

                    Row {
                        // Audio Call Button
                        IconButton(
                            onClick = { onStartCall("AUDIO") },
                            modifier = Modifier.testTag("audio_call_button")
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Appel Audio", tint = NeonCyan)
                        }
                        // Video Call Button
                        IconButton(
                            onClick = { onStartCall("VIDEO") },
                            modifier = Modifier.testTag("video_call_button")
                        ) {
                            Icon(Icons.Default.Videocam, contentDescription = "Appel Vidéo", tint = NeonPurple)
                        }
                    }
                }
            }
        }

        // Messages Conversation Stream
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                ChatMessageItem(message = msg)
            }
        }

        // Input Dock
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mic Button for simulated Voice Recording
                IconButton(
                    onClick = {
                        isRecordingVoice = !isRecordingVoice
                        if (!isRecordingVoice) {
                            onSendMessage("🎙️ Message Vocal (00:08)", "VOICE", 8)
                        }
                    },
                    modifier = Modifier.testTag("voice_record_button")
                ) {
                    Icon(
                        imageVector = if (isRecordingVoice) Icons.Default.GraphicEq else Icons.Default.Mic,
                        contentDescription = "Vocal",
                        tint = if (isRecordingVoice) Color.Red else NeonPurple
                    )
                }

                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = {
                        Text(if (isRecordingVoice) "Enregistrement vocal en cours..." else "Écrivez un message privé...")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field"),
                    shape = RoundedCornerShape(24.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            onSendMessage(messageText, "TEXT", 0)
                            messageText = ""
                        }
                    },
                    modifier = Modifier.testTag("send_chat_message_button")
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Envoyer", tint = NeonPurple)
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessageEntity) {
    val isMine = message.isMine
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isMine) NeonPurple else MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isMine) 16.dp else 4.dp,
                bottomEnd = if (isMine) 4.dp else 16.dp
            ),
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                if (!isMine) {
                    Text(
                        text = message.senderName,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }

                if (message.messageType == "VOICE") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.GraphicEq, contentDescription = null, tint = NeonCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${message.voiceDurationSec}s",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    Text(
                        text = message.text,
                        color = if (isMine) Color.White else MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = message.timestamp,
                    fontSize = 9.sp,
                    color = if (isMine) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
