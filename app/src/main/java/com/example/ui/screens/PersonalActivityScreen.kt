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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
fun PersonalActivityScreen(
    onNavigate: (AppScreen) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    var selectedFilterIndex by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    val filterChips = listOf("All", "Paid", "Received", "Cashback", "Auto-debited")

    val personalRecommendations = listOf(
        RecommendationItem(
            id = "rec_car_insurance",
            title = "Vehicle insurance renewal due in 28 days",
            badge = "MOTOR CARE",
            savingsTag = "Save ₹2,200",
            description = "SpeedX service detected on 16 Oct. CareGuard & ShieldPrime quotes ready.",
            targetScreen = AppScreen.RECOMMENDATION_DETAILS,
            iconType = "CAR",
            ctaText = "Review Policies"
        ),
        RecommendationItem(
            id = "rec_smart_savings",
            title = "7.4% p.a. Auto-Sweep on idle funds",
            badge = "SMART WEALTH",
            savingsTag = "Earn +₹4,150",
            description = "Auto-sweep funds exceeding ₹10,000 threshold into partner bank FD.",
            targetScreen = AppScreen.SMART_SAVINGS_DETAILS,
            iconType = "SAVINGS",
            ctaText = "Activate Sweep"
        ),
        RecommendationItem(
            id = "rec_health_tax",
            title = "₹10 Lakhs Family Health & Tax Exemption",
            badge = "80D TAX BENEFIT",
            savingsTag = "Save ₹3,600 Tax",
            description = "Comprehensive cashless hospitalization at 10,000+ network hospitals.",
            targetScreen = AppScreen.INSURANCE_DISCOVERY,
            iconType = "HEALTH",
            ctaText = "Compare Covers"
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Passbook & Activity",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Real-time records for Paytm Wallet & Linked Banks",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFE8EDFF),
                    modifier = Modifier
                        .clickable {
                            scope.launch {
                                snackbarHostState.showSnackbar("Downloading monthly statement PDF...")
                            }
                        }
                        .testTag("download_statement_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FileDownload,
                            contentDescription = "Download Statement",
                            tint = FinSenseNavy,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "PDF",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = FinSenseNavy
                        )
                    }
                }
            }
        }

        // Account Summary Card
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
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TOTAL AVAILABLE BALANCE",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "2 Accounts Active",
                            style = MaterialTheme.typography.labelSmall,
                            color = FinSenseNavy,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "₹24,850",
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = ".00",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    HorizontalDivider(color = BorderHairline, thickness = 0.8.dp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(StatusSuccess)
                            )
                            Text(
                                text = "Wallet: ₹4,850 • SBI Acc: ₹20,000",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }

                        Text(
                            text = "Auto-Sweep Eligible",
                            style = MaterialTheme.typography.labelSmall,
                            color = FinSenseCyan,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                onNavigate(AppScreen.SMART_SAVINGS_DETAILS)
                            }
                        )
                    }
                }
            }
        }

        // 3 Personal Contextual Recommendations Header
        item {
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
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = FinSenseNavy,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Smart FinSense Insights (3)",
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

        // 3 Personal Recommendations Carousel
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(personalRecommendations) { rec ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F4FD)),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .width(300.dp)
                            .border(1.dp, Color(0xFF71D2FF).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = rec.badge,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = FinSenseNavy
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
                                onClick = { onNavigate(rec.targetScreen) },
                                colors = ButtonDefaults.buttonColors(containerColor = FinSenseCyan),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                            ) {
                                Text(
                                    text = rec.ctaText,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Filter Chips Row
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filterChips.indices.toList()) { index ->
                    val isSelected = selectedFilterIndex == index
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) FinSenseNavy else FinSenseSurface,
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = if (isSelected) FinSenseNavy else BorderHairline
                        ),
                        modifier = Modifier
                            .clickable { selectedFilterIndex = index }
                            .testTag("filter_chip_$index")
                    ) {
                        Text(
                            text = filterChips[index],
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextPrimary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // Transactions List Card
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
                    Text(
                        text = "October 2024",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    PersonalActivityItem(
                        icon = Icons.Default.LocalCafe,
                        iconBg = Color(0xFFE0E8FF),
                        iconTint = FinSenseNavy,
                        title = "Starbucks Coffee",
                        category = "Shopping • UPI Paid",
                        time = "Today, 14:10",
                        amount = "-₹340",
                        isCredit = false,
                        onClick = {
                            scope.launch { snackbarHostState.showSnackbar("Starbucks ₹340 paid via UPI") }
                        }
                    )

                    HorizontalDivider(color = BorderHairline, thickness = 0.8.dp)

                    PersonalActivityItem(
                        icon = Icons.Default.Restaurant,
                        iconBg = Color(0xFFFFDCBE),
                        iconTint = Color(0xFF8B5000),
                        title = "Swiggy Online Food",
                        category = "Food • Paytm Wallet",
                        time = "Today, 12:45",
                        amount = "-₹480",
                        isCredit = false,
                        onClick = {
                            scope.launch { snackbarHostState.showSnackbar("Swiggy ₹480 paid via Wallet") }
                        }
                    )

                    HorizontalDivider(color = BorderHairline, thickness = 0.8.dp)

                    // Contextual Highlighted Item
                    PersonalActivityItem(
                        icon = Icons.Default.Build,
                        iconBg = Color(0xFFDAE1FF),
                        iconTint = FinSenseNavy,
                        title = "SpeedX Motor Works",
                        category = "Auto Service • Bank Direct",
                        time = "16 Oct, 11:20",
                        amount = "-₹6,450",
                        isCredit = false,
                        badge = "Context Engine Insight",
                        onClick = { onNavigate(AppScreen.RECOMMENDATION_DETAILS) }
                    )

                    HorizontalDivider(color = BorderHairline, thickness = 0.8.dp)

                    PersonalActivityItem(
                        icon = Icons.Default.Savings,
                        iconBg = Color(0xFFE8F8F1),
                        iconTint = StatusSuccess,
                        title = "FinSense Cash Back Received",
                        category = "Promotional • Paytm Balance",
                        time = "14 Oct, 18:30",
                        amount = "+₹50",
                        isCredit = true,
                        onClick = {
                            scope.launch { snackbarHostState.showSnackbar("Promo cashback ₹50 credited") }
                        }
                    )

                    HorizontalDivider(color = BorderHairline, thickness = 0.8.dp)

                    PersonalActivityItem(
                        icon = Icons.Default.ElectricBolt,
                        iconBg = Color(0xFFFFDCBE),
                        iconTint = Color(0xFF8B5000),
                        title = "BESCOM Electricity Bill",
                        category = "Utilities • Auto-debited",
                        time = "12 Oct, 09:15",
                        amount = "-₹1,820",
                        isCredit = false,
                        onClick = {
                            scope.launch { snackbarHostState.showSnackbar("BESCOM bill ₹1,820 auto-debited") }
                        }
                    )

                    HorizontalDivider(color = BorderHairline, thickness = 0.8.dp)

                    PersonalActivityItem(
                        icon = Icons.Default.AccountBalance,
                        iconBg = Color(0xFFE8F8F1),
                        iconTint = StatusSuccess,
                        title = "Salary Credit - ACME Corp",
                        category = "Inward Transfer • SBI Account",
                        time = "10 Oct, 06:00",
                        amount = "+₹75,000",
                        isCredit = true,
                        onClick = {
                            scope.launch { snackbarHostState.showSnackbar("Salary credit of ₹75,000 recorded") }
                        }
                    )

                    HorizontalDivider(color = BorderHairline, thickness = 0.8.dp)

                    PersonalActivityItem(
                        icon = Icons.Default.Movie,
                        iconBg = Color(0xFFF3E5F5),
                        iconTint = Color(0xFF7B1FA2),
                        title = "Netflix India Subscription",
                        category = "Entertainment • Auto-Mandate",
                        time = "08 Oct, 02:00",
                        amount = "-₹649",
                        isCredit = false,
                        onClick = {
                            scope.launch { snackbarHostState.showSnackbar("Netflix ₹649 auto-mandate executed") }
                        }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PersonalActivityItem(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    category: String,
    time: String,
    amount: String,
    isCredit: Boolean,
    badge: String? = null,
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
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    if (badge != null) {
                        Text(
                            text = badge,
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
                    text = "$category • $time",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }

        Text(
            text = amount,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = if (isCredit) StatusSuccess else TextPrimary
        )
    }
}
