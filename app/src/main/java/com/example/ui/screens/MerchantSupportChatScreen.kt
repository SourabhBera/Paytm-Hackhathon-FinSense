package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatCardType
import com.example.model.ChatMessageItem
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.FinSenseCyan
import com.example.ui.theme.FinSenseNavy
import com.example.ui.theme.FinSenseSurface
import com.example.ui.theme.FinSenseSurfaceHigh
import com.example.ui.theme.FinSenseSurfaceLow
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MerchantSupportChatScreen(
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }
    var isBotTyping by remember { mutableStateOf(false) }

    val messages = remember {
        mutableStateListOf(
            ChatMessageItem(
                id = "m1",
                text = "Hello Rajesh! I am FinSense Merchant AI. I've detected that your settlement batch #SET-88291 (₹1,450) is delayed due to high traffic at HDFC Bank's clearing node.",
                isUser = false,
                time = "11:35 AM",
                cardType = ChatCardType.SETTLEMENT_STATUS,
                quickReplies = listOf(
                    "Check Bank UTR Status",
                    "Test Soundbox Device",
                    "Explain Daily Loan Debit",
                    "Request Instant Release"
                )
            )
        )
    }

    fun sendMerchantMessage(userText: String) {
        if (userText.isBlank()) return
        val timeNow = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
        messages.add(
            ChatMessageItem(
                id = System.currentTimeMillis().toString(),
                text = userText.trim(),
                isUser = true,
                time = timeNow
            )
        )
        inputText = ""

        scope.launch {
            delay(80)
            listState.animateScrollToItem(messages.size - 1)
            isBotTyping = true
            delay(700)
            isBotTyping = false

            val query = userText.lowercase()
            val replyText: String
            var cardType = ChatCardType.NONE
            var cardData: String? = null
            var quickReplies = listOf<String>()

            when {
                query.contains("utr") || query.contains("status") || query.contains("delay") || query.contains("bank") -> {
                    replyText = "I queried HDFC Bank Gateway directly with reference UTR90284719283. The bank has acknowledged the batch. Clearing cycle cycle-3 is estimated to credit by 4:30 PM today."
                    cardType = ChatCardType.SETTLEMENT_STATUS
                    cardData = "UTR90284719283"
                    quickReplies = listOf("Request Instant Release", "Download Settlement Slip")
                }
                query.contains("soundbox") || query.contains("speaker") || query.contains("voice") || query.contains("device") -> {
                    replyText = "Running real-time diagnostics on your Soundbox (Device ID: SB-9921-HDFC). Battery is at 92% and 4G Airtel signal is strong. Last audio broadcast was verified at 11:30 AM."
                    cardType = ChatCardType.SOUNDBOX_DIAGNOSTIC
                    cardData = "SB-9921"
                    quickReplies = listOf("Play Test Announcement", "Report Payment Missed")
                }
                query.contains("loan") || query.contains("debit") || query.contains("working capital") || query.contains("83") -> {
                    replyText = "Your daily working capital auto-debit of ₹83 is automatically withheld from morning QR settlements. Today's collections were ₹18,450, leaving ₹18,367 net queued for payout. No late fees will be charged."
                    cardType = ChatCardType.LOAN_OFFER
                    quickReplies = listOf("Check Loan Outstanding", "Increase Credit Limit")
                }
                query.contains("instant") || query.contains("release") || query.contains("advance") -> {
                    replyText = "I can immediately reroute ₹1,450 to your Paytm Payments Bank Merchant Account with zero processing fee while HDFC clearing finishes."
                    cardType = ChatCardType.SETTLEMENT_STATUS
                    quickReplies = listOf("Confirm Instant Reroute", "Wait for HDFC Batch")
                }
                query.contains("qr") || query.contains("payment") || query.contains("customer") -> {
                    replyText = "Your store QR has received 14 customer UPI payments today with 100% success rate. The highest single payment was ₹4,200 from customer Ankit S."
                    quickReplies = listOf("View QR Transaction List", "Check Soundbox Device")
                }
                else -> {
                    replyText = "I have checked your merchant ledger. Everything is synchronized. If you need assistance with settlements, QR sound alerts, or working capital debits, let me know!"
                    quickReplies = listOf("Check Bank UTR Status", "Test Soundbox Device", "Explain Daily Loan Debit")
                }
            }

            val replyTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
            messages.add(
                ChatMessageItem(
                    id = (System.currentTimeMillis() + 1).toString(),
                    text = replyText,
                    isUser = false,
                    time = replyTime,
                    cardType = cardType,
                    cardData = cardData,
                    quickReplies = quickReplies
                )
            )
            delay(80)
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(FinSenseSurfaceHigh)
                        .testTag("merchant_chat_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Merchant AI Support",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextPrimary
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFC0E8FF))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(FinSenseCyan)
                            )
                            Text(
                                text = "Live Ops",
                                style = MaterialTheme.typography.labelSmall,
                                color = FinSenseNavy,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "Rajesh Electronics • Ticket #9942",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
            }

            IconButton(
                onClick = {
                    messages.clear()
                    messages.add(
                        ChatMessageItem(
                            id = "m_reset",
                            text = "Hello Rajesh! Ready to assist you with store settlements, Soundbox hardware, and loan auto-debits.",
                            isUser = false,
                            time = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date()),
                            quickReplies = listOf(
                                "Check Bank UTR Status",
                                "Test Soundbox Device",
                                "Explain Daily Loan Debit"
                            )
                        )
                    )
                },
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(FinSenseSurfaceHigh)
                    .testTag("reset_merchant_chat_button")
            ) {
                Icon(
                    imageVector = Icons.Default.RestartAlt,
                    contentDescription = "Reset chat",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Active Ticket Context Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderHairline, RoundedCornerShape(12.dp))
                .padding(vertical = 4.dp)
                .testTag("merchant_chat_context_banner")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = StatusWarning,
                        modifier = Modifier.size(16.dp)
                    )
                    Column {
                        Text(
                            text = "Batch #SET-88291 • ₹1,450",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "HDFC Bank Clearing Delay Detected",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                Text(
                    text = "Investigating",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF8B5000),
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(StatusWarningBg)
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Chat Message List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                if (msg.isUser) {
                    MerchantUserBubble(msg)
                } else {
                    MerchantBotBubble(
                        message = msg,
                        onQuickReply = { sendMerchantMessage(it) },
                        onActionClicked = { actionLabel ->
                            scope.launch {
                                snackbarHostState.showSnackbar(actionLabel)
                            }
                        }
                    )
                }
            }

            if (isBotTyping) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(start = 36.dp, top = 4.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            color = FinSenseCyan,
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "FinSense Merchant AI is querying banking node...",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        // Bottom Input Area
        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, top = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = "Ask Merchant Support anything...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = FinSenseSurface,
                        unfocusedContainerColor = FinSenseSurface,
                        focusedBorderColor = FinSenseCyan,
                        unfocusedBorderColor = BorderHairline
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("merchant_chat_input_field")
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(FinSenseCyan)
                        .clickable { sendMerchantMessage(inputText) }
                        .testTag("merchant_chat_send_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MerchantUserBubble(message: ChatMessageItem) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 2.dp))
                .background(FinSenseCyan)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(top = 2.dp, end = 2.dp)
        ) {
            Text(
                text = message.time,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                fontSize = 10.sp
            )
            Icon(
                imageVector = Icons.Default.DoneAll,
                contentDescription = "Delivered",
                tint = FinSenseCyan,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun MerchantBotBubble(
    message: ChatMessageItem,
    onQuickReply: (String) -> Unit,
    onActionClicked: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(0.96f),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(FinSenseNavy)
        ) {
            Icon(
                imageVector = Icons.Default.Storefront,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(15.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Text bubble
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(FinSenseSurface)
                    .border(1.dp, BorderHairline, RoundedCornerShape(topStart = 2.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
            }

            // Interactive Settlement Status Card
            if (message.cardType == ChatCardType.SETTLEMENT_STATUS) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = FinSenseSurfaceLow),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderHairline, RoundedCornerShape(12.dp))
                        .testTag("merchant_settlement_status_card")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalance,
                                    contentDescription = null,
                                    tint = FinSenseNavy,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Bank Reference: UTR90284719283",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                            Text(
                                text = "Queue #14",
                                style = MaterialTheme.typography.labelSmall,
                                color = FinSenseNavy
                            )
                        }

                        Text(
                            text = "Batch Amount: ₹1,450.00 • Destination: HDFC Bank (A/C **9012)",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onActionClicked("Direct bank query sent. Server latency 118ms: Batch confirmed in cycle 3.") },
                                colors = ButtonDefaults.buttonColors(containerColor = FinSenseNavy),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp)
                                    .testTag("re_query_bank_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Re-Query Bank",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Button(
                                onClick = { onActionClicked("Instant Reroute Requested: Credited ₹1,450 to Paytm Payments Bank Wallet.") },
                                colors = ButtonDefaults.buttonColors(containerColor = FinSenseCyan),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp)
                                    .testTag("instant_release_button")
                            ) {
                                Text(
                                    text = "Instant Release",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Interactive Soundbox Diagnostic Card
            if (message.cardType == ChatCardType.SOUNDBOX_DIAGNOSTIC) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = FinSenseSurfaceLow),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderHairline, RoundedCornerShape(12.dp))
                        .testTag("merchant_soundbox_diagnostic_card")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = null,
                                    tint = FinSenseNavy,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Paytm Soundbox 4G (SB-9921)",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SignalCellularAlt,
                                    contentDescription = null,
                                    tint = FinSenseCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Airtel 4G",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FinSenseCyan,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Text(
                            text = "Battery: 92% • Volume: High (Level 8) • Last Sync: 2 mins ago",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )

                        Button(
                            onClick = { onActionClicked("Playing test sound: 'Paytm par ₹1 prapt hue!' on device SB-9921") },
                            colors = ButtonDefaults.buttonColors(containerColor = FinSenseNavy),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(36.dp)
                                .testTag("play_test_chime_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Play Test Voice Announcement",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Quick reply chips
            if (message.quickReplies.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(message.quickReplies) { chip ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(FinSenseSurface)
                                .border(1.dp, BorderHairline, RoundedCornerShape(16.dp))
                                .clickable { onQuickReply(chip) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("quick_reply_${chip.replace(" ", "_").lowercase()}")
                        ) {
                            Text(
                                text = chip,
                                style = MaterialTheme.typography.labelSmall,
                                color = FinSenseNavy,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Text(
                text = "FinSense Merchant AI • ${message.time}",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                fontSize = 10.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}
