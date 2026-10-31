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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.model.Persona
import com.example.ui.components.SarvamIndicVoiceSheet
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.FinSenseCyan
import com.example.ui.theme.FinSenseNavy
import com.example.ui.theme.FinSensePrimary
import com.example.ui.theme.FinSenseSurface
import com.example.ui.theme.FinSenseSurfaceHigh
import com.example.ui.theme.FinSenseSurfaceLow
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessBg
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
fun FinSenseCopilotScreen(
    currentPersona: Persona = Persona.PERSONAL,
    onViewPolicies: () -> Unit,
    onApplyLoan: () -> Unit = {},
    onExploreSavings: () -> Unit = {},
    onViewMerchantSupport: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }
    var isBotThinking by remember { mutableStateOf(false) }
    var showSarvamVoiceSheet by remember { mutableStateOf(false) }
    var textForSarvamTts by remember { mutableStateOf<String?>(null) }

    val isBusiness = currentPersona == Persona.BUSINESS

    val messages = remember(currentPersona) {
        mutableStateListOf(
            if (isBusiness) {
                ChatMessageItem(
                    id = "b_intro",
                    text = "Hi Rajesh! I am your Merchant Business AI Copilot. Today's collections stand at ₹18,450 with 99.4% payment success. Batch #SET-88291 (₹1,450) is being verified with HDFC Bank. How can I help your business grow or resolve issues?",
                    isUser = false,
                    time = "10:41 AM",
                    cardType = ChatCardType.SETTLEMENT_STATUS,
                    quickReplies = listOf(
                        "Why is settlement delayed?",
                        "Explain daily ₹83 loan debit",
                        "Pre-approved ₹2.5L Loan",
                        "Soundbox device test"
                    )
                )
            } else {
                ChatMessageItem(
                    id = "p_intro",
                    text = "Hi Rajesh! I noticed your recent ₹6,450 SpeedX service payment. Your Honda City insurance expires in 28 days. How can I help simplify your financial journey today?",
                    isUser = false,
                    time = "10:41 AM",
                    cardType = ChatCardType.INSURANCE_QUOTE,
                    quickReplies = listOf(
                        "Find vehicle insurance",
                        "Decode IDV & Zero Dep",
                        "Explain 1.0% Flat vs APR",
                        "Section 80D Tax savings"
                    )
                )
            }
        )
    }

    val promptChips = if (isBusiness) {
        listOf(
            Pair("Why is settlement delayed?", Icons.Default.Warning),
            Pair("Explain daily ₹83 loan debit", Icons.Default.Percent),
            Pair("Pre-approved ₹2.5L Loan", Icons.Default.Storefront),
            Pair("Soundbox device test", Icons.Default.Security),
            Pair("Store collection trends", Icons.Default.PieChart)
        )
    } else {
        listOf(
            Pair("Find vehicle insurance", Icons.Default.DirectionsCar),
            Pair("Decode IDV & Zero Dep", Icons.Default.Security),
            Pair("Explain 1.0% Flat vs APR", Icons.Default.Percent),
            Pair("Section 80D Tax savings", Icons.Default.HealthAndSafety),
            Pair("Auto-Sweep 7.4% returns", Icons.Default.Savings),
            Pair("SpeedX bill audit", Icons.Default.ReceiptLong)
        )
    }

    fun handleSend(text: String) {
        if (text.isBlank()) return
        val timeNow = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
        val userMsg = ChatMessageItem(
            id = System.currentTimeMillis().toString(),
            text = text.trim(),
            isUser = true,
            time = timeNow
        )
        messages.add(userMsg)
        scope.launch {
            com.example.backend.SupabaseRepository.addChatMessage(userMsg)
        }
        inputText = ""

        scope.launch {
            delay(60)
            listState.animateScrollToItem(messages.size - 1)
            isBotThinking = true
            delay(650)
            isBotThinking = false

            val query = text.lowercase()
            val replyText: String
            var cardType = ChatCardType.NONE
            var cardData: String? = null
            var quickReplies = listOf<String>()

            when {
                // Vehicle Insurance / SpeedX
                query.contains("insurance") || query.contains("vehicle") || query.contains("honda") || query.contains("speedx") || query.contains("car") -> {
                    replyText = "Based on your verified ₹6,450 SpeedX service invoice on 16 Oct, your 2021 Honda City (KA-01-MJ-4029) policy renewal is due in 28 days. Here are 3 pre-negotiated quotes with Zero Depreciation and 4,200+ cashless garages. Switching saves you ~₹2,200/yr."
                    cardType = ChatCardType.INSURANCE_QUOTE
                    quickReplies = listOf("Compare Quotes", "Decode IDV", "What is Zero Dep?")
                }

                // IDV Term Decoding
                query.contains("idv") || query.contains("declared value") -> {
                    replyText = "Insured Declared Value (IDV) is the maximum legal compensation your insurer will pay if your vehicle is stolen or suffers a total loss. It represents current market value (e.g. ₹7,20,000 for your car), not showroom price. Choosing the optimal IDV keeps your premium low while ensuring 100% adequate recovery."
                    cardType = ChatCardType.TERMS_DECODER
                    cardData = "IDV"
                    quickReplies = listOf("Compare Quotes", "Explain Zero Dep", "What is NCB?")
                }

                // Zero Depreciation
                query.contains("zero dep") || query.contains("bumper to bumper") || query.contains("depreciation") -> {
                    replyText = "Standard insurance only reimburses 50% for plastics/rubber and depreciated metal parts during repairs. With Zero Depreciation (Bumper-to-Bumper), your insurer pays 100% of parts and labor costs, saving you ₹12,000+ out-of-pocket per accidental repair."
                    cardType = ChatCardType.TERMS_DECODER
                    cardData = "ZERO_DEP"
                    quickReplies = listOf("Compare Quotes", "What is IDV?", "View Coverage Details")
                }

                // Flat vs Reducing APR
                query.contains("flat") || query.contains("apr") || query.contains("interest") || query.contains("reducing") || query.contains("rate") -> {
                    replyText = "A 1.0% per month 'Flat Rate' looks like 12% p.a., but because your loan principal reduces with every daily payment, the true Effective APR is ~18.2%. At FinSense, our Working Capital loan charges zero pre-closure fees and applies your daily ₹83 settlement debits directly to reducing principal."
                    cardType = ChatCardType.LOAN_OFFER
                    cardData = "FLAT_VS_REDUCING"
                    quickReplies = listOf("Apply for ₹2.5L Loan", "Explain Daily Debit", "Calculate EMI")
                }

                // Loans & Working Capital
                query.contains("loan") || query.contains("credit") || query.contains("working capital") || query.contains("borrow") -> {
                    replyText = "You have a pre-approved Merchant Working Capital limit of ₹2,50,000 based on your consistent ₹18,450/day UPI QR turnover. Repayment is friction-free with automatic daily deductions of ₹83 directly from morning settlements."
                    cardType = ChatCardType.LOAN_OFFER
                    quickReplies = listOf("Explain Daily Debit", "Explain 1.0% Flat vs APR", "Apply for ₹2.5L Loan")
                }

                // Daily settlement debit
                query.contains("daily debit") || query.contains("83") || query.contains("deduct") -> {
                    replyText = "Daily auto-debit takes a micro-amount (₹83) from morning customer QR collections so you never face a massive month-end EMI lump sum. If collections on any day are zero, no penalty is assessed and the cycle rolls over."
                    cardType = ChatCardType.LOAN_OFFER
                    quickReplies = listOf("Apply for ₹2.5L Loan", "Check Outstanding Balance")
                }

                // Section 80D Tax / Health
                query.contains("80d") || query.contains("health") || query.contains("tax") || query.contains("medical") -> {
                    replyText = "Under Section 80D, premiums paid for health cover provide up to ₹25,000 tax deduction for self and family, plus an extra ₹50,000 if covering senior citizen parents. Our cashless network covers 8,500+ top hospitals with zero co-pay."
                    cardType = ChatCardType.TERMS_DECODER
                    cardData = "80D"
                    quickReplies = listOf("Explore Health Plans", "Auto-Sweep 7.4% returns")
                }

                // Smart Savings Auto-Sweep
                query.contains("sweep") || query.contains("savings") || query.contains("fd") || query.contains("7.4") -> {
                    replyText = "Smart Savings Auto-Sweep automatically sweeps your idle bank balance above ₹10,000 into multi-partner Fixed Deposits yielding 7.4% p.a. vs 2.7% normal savings interest. If you spend via UPI or ATM, the FD breaks seamlessly with zero premature withdrawal penalty."
                    quickReplies = listOf("Activate Auto-Sweep", "Find vehicle insurance")
                }

                // Settlement delay & merchant issues
                query.contains("settlement") || query.contains("delay") || query.contains("bank") || query.contains("1,450") || query.contains("batch") -> {
                    replyText = "Settlement Batch #SET-88291 (₹1,450) is delayed due to an HDFC RTGS/NEFT gateway queue. UTR reference UTR90284719283 is confirmed. Expected clearance is 4:30 PM today, or you can request an instant advance to your Paytm wallet."
                    cardType = ChatCardType.SETTLEMENT_STATUS
                    cardData = "UTR90284719283"
                    quickReplies = listOf("Track Resolution", "Test Soundbox Device", "Explain Daily Loan Debit")
                }

                // Soundbox diagnostics
                query.contains("soundbox") || query.contains("speaker") || query.contains("device") || query.contains("voice") -> {
                    replyText = "Diagnostic for Soundbox SB-9921 shows battery at 92%, volume at 80%, and Airtel 4G signal strong. Last payment announcement was verified at 11:30 AM today."
                    cardType = ChatCardType.SOUNDBOX_DIAGNOSTIC
                    quickReplies = listOf("Play Test Announcement", "Report Payment Issue")
                }

                // Spending analysis
                query.contains("spending") || query.contains("expense") || query.contains("budget") || query.contains("audit") -> {
                    replyText = "This month you have spent ₹48,920 against your ₹65,000 budget. Vehicle expenses surged by +18% due to the SpeedX service (₹6,450) and fuel bills. Your overall savings rate is healthy at 24.8%."
                    quickReplies = listOf("Find vehicle insurance", "Auto-Sweep 7.4% returns")
                }

                else -> {
                    replyText = if (isBusiness) {
                        "I've cross-referenced your merchant profile: Store QR is active, daily collection is ₹18,450, and you qualify for ₹2,50,000 pre-approved working capital. What would you like to explore?"
                    } else {
                        "I have audited your account ledgers: Pre-approved vehicle insurance quotes are ready for Honda City KA-01-MJ-4029, and you can save ₹2,200 this year with Zero Dep cover. What can I clarify for you?"
                    }
                    quickReplies = if (isBusiness) {
                        listOf("Why is settlement delayed?", "Pre-approved ₹2.5L Loan", "Explain daily ₹83 loan debit")
                    } else {
                        listOf("Find vehicle insurance", "Decode IDV & Zero Dep", "Explain 1.0% Flat vs APR")
                    }
                }
            }

            val replyTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
            val botMsg = ChatMessageItem(
                id = (System.currentTimeMillis() + 1).toString(),
                text = replyText,
                isUser = false,
                time = replyTime,
                cardType = cardType,
                cardData = cardData,
                quickReplies = quickReplies
            )
            messages.add(botMsg)
            com.example.backend.SupabaseRepository.addChatMessage(botMsg)
            delay(60)
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        // Title Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = if (isBusiness) "FinSense Business AI" else "FinSense AI",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFC0E8FF))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(FinSenseCyan)
                        )
                        Text(
                            text = if (isBusiness) "Merchant Partner" else "Live",
                            style = MaterialTheme.typography.labelSmall,
                            color = FinSenseNavy,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = if (isBusiness) "Your 24/7 financial copilot for store growth" else "Your intelligent financial copilot & terms decoder",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }

            IconButton(
                onClick = {
                    messages.clear()
                    messages.add(
                        ChatMessageItem(
                            id = "reset",
                            text = if (isBusiness) {
                                "Hello Rajesh! FinSense Business AI is ready. How can I assist with settlements, loans, or store operations?"
                            } else {
                                "Hi Rajesh! Ready to help with insurance renewal, terms decoding, or spending insights."
                            },
                            isUser = false,
                            time = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date()),
                            quickReplies = if (isBusiness) {
                                listOf("Why is settlement delayed?", "Pre-approved ₹2.5L Loan", "Soundbox device test")
                            } else {
                                listOf("Find vehicle insurance", "Decode IDV & Zero Dep", "Explain 1.0% Flat vs APR")
                            }
                        )
                    )
                },
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(FinSenseSurfaceHigh)
                    .testTag("clear_chat_button")
            ) {
                Icon(
                    imageVector = Icons.Default.RestartAlt,
                    contentDescription = "Restart chat",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Sarvam AI Indic Voice Studio Banner
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F7FF)),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFD2E8FD), RoundedCornerShape(12.dp))
                .clickable {
                    textForSarvamTts = null
                    showSarvamVoiceSheet = true
                }
                .testTag("sarvam_banner_card")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(FinSenseNavy)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = null,
                            tint = FinSenseCyan,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Sarvam AI Indic Voice Studio",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = FinSenseNavy,
                                fontSize = 12.sp
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(StatusSuccessBg)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "Bulbul v3",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusSuccess
                                )
                            }
                        }
                        Text(
                            text = "10 Languages • Soundbox Audio & Vernacular TTS",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Open Studio",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = FinSenseCyan,
                        fontSize = 11.sp
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = FinSenseCyan,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }

        // Horizontal Prompt Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            items(promptChips) { (chipText, icon) ->
                PromptChipItem(
                    label = chipText,
                    icon = icon,
                    onClick = { handleSend(chipText) }
                )
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                if (message.isUser) {
                    UserMessageBubble(message = message)
                } else {
                    BotMessageBubble(
                        message = message,
                        onViewPolicies = onViewPolicies,
                        onApplyLoan = onApplyLoan,
                        onViewSettlement = onViewMerchantSupport,
                        onQuickReply = { handleSend(it) },
                        onListenWithSarvam = {
                            textForSarvamTts = it
                            showSarvamVoiceSheet = true
                        }
                    )
                }
            }

            if (isBotThinking) {
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
                            text = "FinSense is analyzing your financial context...",
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

        // Input Field Bar
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
                            text = if (isBusiness) "Ask about settlements, loans, QR..." else "Ask FinSense anything (e.g. IDV, loans, tax)...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(20.dp)
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
                        .testTag("chat_input_field")
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(FinSenseNavy)
                        .clickable {
                            textForSarvamTts = inputText.ifBlank { null }
                            showSarvamVoiceSheet = true
                        }
                        .testTag("sarvam_voice_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.RecordVoiceOver,
                        contentDescription = "Sarvam AI Voice",
                        tint = FinSenseCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(FinSenseCyan)
                        .clickable { handleSend(inputText) }
                        .testTag("chat_send_button")
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

    if (showSarvamVoiceSheet) {
        SarvamIndicVoiceSheet(
            initialText = textForSarvamTts,
            onDismiss = {
                showSarvamVoiceSheet = false
                textForSarvamTts = null
            }
        )
    }
}

@Composable
private fun PromptChipItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = FinSenseSurface,
        shadowElevation = 1.dp,
        modifier = Modifier
            .border(1.dp, BorderHairline, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .testTag("prompt_chip_${label.replace(" ", "_").lowercase()}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = FinSenseCyan,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun UserMessageBubble(message: ChatMessageItem) {
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
private fun BotMessageBubble(
    message: ChatMessageItem,
    onViewPolicies: () -> Unit,
    onApplyLoan: () -> Unit,
    onViewSettlement: () -> Unit,
    onQuickReply: (String) -> Unit,
    onListenWithSarvam: (String) -> Unit = {}
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
                imageVector = Icons.Default.AutoAwesome,
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

            // Listen in Indic Voice Action Chip
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFE8F6FE))
                    .border(1.dp, Color(0xFFD2E8FD), RoundedCornerShape(6.dp))
                    .clickable { onListenWithSarvam(message.text) }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Listen with Sarvam AI",
                    tint = FinSenseCyan,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = "Listen in Indic Voice (Sarvam Bulbul)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = FinSenseNavy
                )
            }

            // Embedded Interactive Card based on cardType
            when (message.cardType) {
                ChatCardType.INSURANCE_QUOTE -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderHairline, RoundedCornerShape(14.dp))
                            .testTag("interactive_insurance_recommendation_card")
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DirectionsCar,
                                        contentDescription = null,
                                        tint = FinSenseCyan,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Honda City Insurance Renewal",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = TextPrimary,
                                        fontSize = 15.sp
                                    )
                                }
                                Text(
                                    text = "DL 01 AA 2024",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(FinSenseSurfaceHigh)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            // Savings strip
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(FinSenseSurfaceLow)
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Savings,
                                        contentDescription = null,
                                        tint = FinSenseNavy,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Save ₹2,200/yr • Zero Dep",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                }
                                Text(
                                    text = "Best Match",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FinSenseNavy,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Action Link
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onViewPolicies() }
                                    .padding(top = 2.dp)
                                    .testTag("copilot_view_policies_action"),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "View 3 Quotes & Compare",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = FinSenseNavy
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = FinSenseNavy,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                ChatCardType.LOAN_OFFER -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderHairline, RoundedCornerShape(14.dp))
                            .testTag("interactive_loan_recommendation_card")
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Storefront,
                                        contentDescription = null,
                                        tint = FinSenseNavy,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "₹2,50,000 Working Capital",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = TextPrimary,
                                        fontSize = 15.sp
                                    )
                                }
                                Text(
                                    text = "Pre-Approved",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FinSenseNavy,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFC0E8FF))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            Text(
                                text = "Daily debit: ₹83 from morning QR • 0 Foreclosure fee • Disbursed in 30 mins",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onApplyLoan() }
                                    .padding(top = 2.dp)
                                    .testTag("copilot_apply_loan_action"),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Review Terms & Apply",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = FinSenseNavy
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = FinSenseNavy,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                ChatCardType.SETTLEMENT_STATUS -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = FinSenseSurfaceLow),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderHairline, RoundedCornerShape(12.dp))
                            .testTag("copilot_settlement_status_card")
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Batch #SET-88291 • ₹1,450",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Delayed",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF8B5000),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "HDFC Bank Clearing cycle in progress. UTR UTR90284719283 verified.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onViewSettlement() }
                                    .padding(top = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Open Resolution Hub",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = FinSenseNavy
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = FinSenseNavy,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }

                ChatCardType.TERMS_DECODER -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F7FF)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFBCE0FD), RoundedCornerShape(12.dp))
                            .testTag("copilot_terms_decoder_card")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = FinSenseNavy,
                                modifier = Modifier.size(18.dp)
                            )
                            Column {
                                Text(
                                    text = "FinSense Plain-Language Guarantee",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = FinSenseNavy
                                )
                                Text(
                                    text = "Zero hidden exclusions. Tap any highlighted term to see real consumer impact.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                else -> {}
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
                                .testTag("copilot_quick_reply_${chip.replace(" ", "_").lowercase()}")
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
                text = "FinSense • ${message.time}",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                fontSize = 10.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}
