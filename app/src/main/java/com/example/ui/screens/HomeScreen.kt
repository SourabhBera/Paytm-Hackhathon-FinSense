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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speaker
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.model.AppScreen
import com.example.model.Persona
import com.example.model.RecommendationItem
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.FinSenseCyan
import com.example.ui.theme.FinSenseNavy
import com.example.ui.theme.FinSenseSurface
import com.example.ui.theme.FinSenseSurfaceHigh
import com.example.ui.theme.FinSenseSurfaceLow
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    currentPersona: Persona,
    onNavigate: (AppScreen) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var isBalanceVisible by remember { mutableStateOf(true) }
    var selectedRecTab by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    val personalRecommendations = listOf(
        RecommendationItem(
            id = "rec_car_insurance",
            title = "Your vehicle insurance deserves a review",
            badge = "FINSENSE MOTOR",
            savingsTag = "Save ₹2,200/yr",
            description = "Based on recent SpeedX service charges. Renewal starting at ₹2,199/yr for Honda City.",
            targetScreen = AppScreen.RECOMMENDATION_DETAILS,
            iconType = "CAR",
            ctaText = "Explore Quotes"
        ),
        RecommendationItem(
            id = "rec_smart_savings",
            title = "Earn 7.4% p.a. on idle bank funds",
            badge = "AUTO-SWEEP",
            savingsTag = "Earn +₹4,150/yr",
            description = "You hold ~₹24,850 in regular savings. Auto-sweep funds above ₹10,000 for 2.7x higher returns.",
            targetScreen = AppScreen.SMART_SAVINGS_DETAILS,
            iconType = "SAVINGS",
            ctaText = "Activate Auto-Sweep"
        ),
        RecommendationItem(
            id = "rec_health_cover",
            title = "₹10 Lakhs Family Health & Tax Cover",
            badge = "TAX BENEFIT",
            savingsTag = "Save ₹3,600 Tax",
            description = "Comprehensive cashless hospitalization across 10,000+ hospitals. 100% paperless claims.",
            targetScreen = AppScreen.INSURANCE_DISCOVERY,
            iconType = "HEALTH",
            ctaText = "Compare Plans"
        )
    )

    val businessRecommendations = listOf(
        RecommendationItem(
            id = "rec_loan",
            title = "Pre-Approved Working Capital Loan",
            badge = "CAPITAL EXPANSION",
            savingsTag = "Unlock ₹1,50,000",
            description = "Zero collateral required. 1.0%/mo rate with daily automated deduction from QR settlements.",
            targetScreen = AppScreen.WORKING_CAPITAL_LOANS,
            iconType = "LOAN",
            ctaText = "Apply in 2 Min"
        ),
        RecommendationItem(
            id = "rec_shop_insurance",
            title = "Rajesh Electronics Stock & Fire Cover",
            badge = "MERCHANT SECURITY",
            savingsTag = "Save ₹1,800/yr",
            description = "Protect ₹25 Lakhs inventory from fire, burglary, and monsoon damages for just ₹499/yr.",
            targetScreen = AppScreen.SHOP_INSURANCE_DETAILS,
            iconType = "SHOP",
            ctaText = "Insure Store"
        ),
        RecommendationItem(
            id = "rec_settlement",
            title = "Settlement Delay Detected (#SET-88291)",
            badge = "TICKET #9942",
            savingsTag = "₹1,450 Pending",
            description = "Bank clearing cycle ongoing for ₹1,450. Track instant credit status and merchant resolution.",
            targetScreen = AppScreen.MERCHANT_ISSUE_RESOLUTION,
            iconType = "SETTLEMENT",
            ctaText = "Track Status"
        )
    )

    val activeRecommendations = if (currentPersona == Persona.PERSONAL) personalRecommendations else businessRecommendations

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Greeting row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (currentPersona == Persona.PERSONAL) "Hi Rajesh!" else "Rajesh Electronics & Co.",
                        style = MaterialTheme.typography.headlineLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = if (currentPersona == Persona.PERSONAL) "Ready for your day?" else "Verified Merchant Store • Soundbox Active",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        if (currentPersona == Persona.PERSONAL) {
                            Text(text = "✨", fontSize = 13.sp)
                        }
                    }
                }

                // Persona status badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFE0E8FF))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(FinSenseCyan)
                    )
                    Text(
                        text = if (currentPersona == Persona.PERSONAL) "UPI Active" else "Store Live",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = FinSenseNavy
                    )
                }
            }
        }

        // Balance Card (Adapts to Personal vs Business)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderHairline, RoundedCornerShape(16.dp))
                    .testTag("balance_card")
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = if (currentPersona == Persona.PERSONAL) "PAYTM BALANCE" else "MERCHANT SETTLEMENT BALANCE",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted,
                                letterSpacing = 0.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = if (!isBalanceVisible) "₹ ••••••••" else if (currentPersona == Persona.PERSONAL) "₹24,850.00" else "₹1,84,500.00",
                                    style = MaterialTheme.typography.displayLarge,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(
                                    onClick = { isBalanceVisible = !isBalanceVisible },
                                    modifier = Modifier
                                        .size(28.dp)
                                        .testTag("toggle_balance_button")
                                ) {
                                    Icon(
                                        imageVector = if (isBalanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle balance visibility",
                                        tint = TextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        // Action Button
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFE8EDFF),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            if (currentPersona == Persona.PERSONAL) "Add Money drawer opened" else "Settlement transfer to HDFC initiated"
                                        )
                                    }
                                }
                                .testTag("balance_primary_action")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = if (currentPersona == Persona.PERSONAL) Icons.Default.Add else Icons.Default.Payments,
                                    contentDescription = null,
                                    tint = FinSenseNavy,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = if (currentPersona == Persona.PERSONAL) "Add Money" else "Settle Now",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = FinSenseNavy,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Linked & Passbook row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = if (currentPersona == Persona.PERSONAL) "Wallet & Bank Linked" else "Today's Collections: ₹18,450 (+12%)",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (currentPersona == Persona.PERSONAL) TextSecondary else StatusSuccess,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (currentPersona == Persona.PERSONAL) FinSenseSurfaceLow else Color(0xFFE8F8F1))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                        Text(text = "•", color = TextMuted)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onNavigate(if (currentPersona == Persona.PERSONAL) AppScreen.FINSENSE else AppScreen.ACTIVITY)
                                }
                                .testTag("passbook_link")
                        ) {
                            Text(
                                text = if (currentPersona == Persona.PERSONAL) "Passbook" else "Settlements Ledger",
                                style = MaterialTheme.typography.bodySmall,
                                color = FinSenseNavy,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = FinSenseNavy,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Quick Shortcuts (5 Buttons, adapting to Persona)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderHairline, RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp, horizontal = 6.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    if (currentPersona == Persona.PERSONAL) {
                        QuickActionItem(Icons.Default.QrCodeScanner, "Scan & Pay", hasRadar = true, tag = "action_scan_pay") {
                            scope.launch { snackbarHostState.showSnackbar("Opening QR Scanner...") }
                        }
                        QuickActionItem(Icons.Default.PhoneAndroid, "Recharge", tag = "action_recharge") {
                            scope.launch { snackbarHostState.showSnackbar("Mobile Recharge selected") }
                        }
                        QuickActionItem(Icons.Default.Bolt, "Electricity", tag = "action_electricity") {
                            scope.launch { snackbarHostState.showSnackbar("Electricity Bill payment selected") }
                        }
                        QuickActionItem(Icons.Default.AccountBalance, "To Bank", tag = "action_bank_transfer") {
                            scope.launch { snackbarHostState.showSnackbar("Bank Transfer selected") }
                        }
                        QuickActionItem(Icons.Default.GridView, "More", isSecondary = true, tag = "action_more") {
                            scope.launch { snackbarHostState.showSnackbar("Showing all 24 services") }
                        }
                    } else {
                        QuickActionItem(Icons.Default.Speaker, "Soundbox", hasRadar = true, tag = "action_soundbox") {
                            scope.launch { snackbarHostState.showSnackbar("Soundbox audio replay triggered") }
                        }
                        QuickActionItem(Icons.Default.ReceiptLong, "Settlements", tag = "action_settlements") {
                            onNavigate(AppScreen.MERCHANT_ISSUE_RESOLUTION)
                        }
                        QuickActionItem(Icons.Default.AccountBalanceWallet, "Loans", tag = "action_business_loan") {
                            onNavigate(AppScreen.WORKING_CAPITAL_LOANS)
                        }
                        QuickActionItem(Icons.Default.Description, "Invoicing", tag = "action_gst_invoice") {
                            scope.launch { snackbarHostState.showSnackbar("GST Invoicing & bill creator opened") }
                        }
                        QuickActionItem(Icons.Default.GridView, "More", isSecondary = true, tag = "action_merchant_more") {
                            scope.launch { snackbarHostState.showSnackbar("Showing all merchant business tools") }
                        }
                    }
                }
            }
        }

        // Section Title for Recommendations
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = FinSenseNavy,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = if (currentPersona == Persona.PERSONAL) "Personal FinSense Insights (3)" else "Merchant Business Intelligence (3)",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Text(
                    text = "Swipe to view",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }

        // 2-3 Rich Spot-on Recommendations Carousel
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(activeRecommendations) { rec ->
                    RecommendationCarouselCard(
                        rec = rec,
                        onAction = { onNavigate(rec.targetScreen) }
                    )
                }
            }
        }

        // AI Copilot Quick Launcher banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = FinSenseNavy),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(AppScreen.COPILOT) }
                    .testTag("copilot_launcher_banner")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(FinSenseCyan)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (currentPersona == Persona.PERSONAL) "Ask FinSense AI Copilot" else "Ask FinSense Merchant AI",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (currentPersona == Persona.PERSONAL) "Instant spending answers, bill audits & quotes" else "Settlement delay checks & working capital questions",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = FinSenseCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Recent Transactions / Payments Section
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderHairline, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
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
                            Text(
                                text = if (currentPersona == Persona.PERSONAL) "Recent Transactions" else "Recent Customer Payments",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(FinSenseCyan)
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                onNavigate(if (currentPersona == Persona.PERSONAL) AppScreen.FINSENSE else AppScreen.ACTIVITY)
                            }
                        ) {
                            Text(
                                text = "View All",
                                style = MaterialTheme.typography.labelMedium,
                                color = FinSenseNavy,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = FinSenseNavy,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    if (currentPersona == Persona.PERSONAL) {
                        // Personal transactions
                        TransactionRow(
                            icon = Icons.Default.LocalCafe,
                            iconBg = Color(0xFFE0E8FF),
                            iconTint = FinSenseNavy,
                            title = "Starbucks Coffee",
                            subtitle = "18 Oct • Shopping",
                            amount = "-₹340",
                            method = "UPI Paid",
                            onClick = {
                                scope.launch { snackbarHostState.showSnackbar("Starbucks transaction: ₹340 via UPI") }
                            }
                        )
                        HorizontalDivider(color = BorderHairline, thickness = 0.8.dp)
                        TransactionRow(
                            icon = Icons.Default.Build,
                            iconBg = Color(0xFFDAE1FF),
                            iconTint = FinSenseNavy,
                            title = "SpeedX Motor Works",
                            subtitle = "16 Oct • Auto Service",
                            amount = "-₹6,450",
                            method = "Bank Direct",
                            highlighted = true,
                            onClick = { onNavigate(AppScreen.RECOMMENDATION_DETAILS) }
                        )
                        HorizontalDivider(color = BorderHairline, thickness = 0.8.dp)
                        TransactionRow(
                            icon = Icons.Default.ElectricBolt,
                            iconBg = Color(0xFFFFDCBE),
                            iconTint = Color(0xFF8B5000),
                            title = "BESCOM Electricity",
                            subtitle = "12 Oct • Utilities",
                            amount = "-₹1,820",
                            method = "Auto-debited",
                            onClick = {
                                scope.launch { snackbarHostState.showSnackbar("BESCOM electricity bill: ₹1,820 auto-debited") }
                            }
                        )
                    } else {
                        // Merchant business transactions
                        TransactionRow(
                            icon = Icons.Default.CurrencyRupee,
                            iconBg = Color(0xFFE8F8F1),
                            iconTint = StatusSuccess,
                            title = "UPI Payment Inflow",
                            subtitle = "18 Oct 18:24 • Customer App",
                            amount = "+₹1,250",
                            method = "Settled",
                            highlighted = true,
                            onClick = { onNavigate(AppScreen.MERCHANT_ISSUE_RESOLUTION) }
                        )
                        HorizontalDivider(color = BorderHairline, thickness = 0.8.dp)
                        TransactionRow(
                            icon = Icons.Default.Speaker,
                            iconBg = Color(0xFFE0E8FF),
                            iconTint = FinSenseNavy,
                            title = "QR Soundbox Audio Payment",
                            subtitle = "18 Oct 17:15 • Counter Soundbox",
                            amount = "+₹3,800",
                            method = "Settled",
                            onClick = { onNavigate(AppScreen.MERCHANT_ISSUE_RESOLUTION) }
                        )
                        HorizontalDivider(color = BorderHairline, thickness = 0.8.dp)
                        TransactionRow(
                            icon = Icons.Default.CreditCard,
                            iconBg = Color(0xFFFFF0F0),
                            iconTint = Color(0xFFBA1A1A),
                            title = "Card Swipe Settlement Delay",
                            subtitle = "18 Oct 15:40 • Batch #SET-88291",
                            amount = "+₹1,450",
                            method = "Under Review",
                            highlighted = true,
                            onClick = { onNavigate(AppScreen.MERCHANT_ISSUE_RESOLUTION) }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun RecommendationCarouselCard(
    rec: RecommendationItem,
    onAction: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F4FD)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .width(310.dp)
            .border(1.dp, Color(0xFF71D2FF).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0xFF71D2FF).copy(alpha = 0.5f), CircleShape)
                    ) {
                        val icon = when (rec.iconType) {
                            "CAR" -> Icons.Default.DirectionsCar
                            "SAVINGS" -> Icons.Default.Savings
                            "HEALTH" -> Icons.Default.Security
                            "LOAN" -> Icons.Default.AccountBalanceWallet
                            "SHOP" -> Icons.Default.Storefront
                            else -> Icons.Default.ReceiptLong
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = FinSenseNavy,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = rec.badge,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = FinSenseNavy,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = rec.savingsTag,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF603600),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFFDCBE))
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            Text(
                text = rec.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 2
            )

            Text(
                text = rec.description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                maxLines = 2
            )

            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(containerColor = FinSenseCyan),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Text(
                    text = rec.ctaText,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun QuickActionItem(
    icon: ImageVector,
    label: String,
    isSecondary: Boolean = false,
    hasRadar: Boolean = false,
    tag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .clickable { onClick() }
            .testTag(tag)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(if (isSecondary) FinSenseSurfaceLow else Color(0xFFE6F8FE))
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSecondary) TextSecondary else FinSenseNavy,
                modifier = Modifier.size(24.dp)
            )
            if (hasRadar) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, end = 4.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(FinSenseCyan)
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun TransactionRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    amount: String,
    method: String,
    highlighted: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBg)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    if (highlighted) {
                        Text(
                            text = "Insight",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = FinSenseNavy,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFE0E8FF))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = amount,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = method,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
    }
}
