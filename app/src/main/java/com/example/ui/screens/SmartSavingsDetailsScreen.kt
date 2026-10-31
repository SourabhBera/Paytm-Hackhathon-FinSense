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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.FinSenseCyan
import com.example.ui.theme.FinSenseNavy
import com.example.ui.theme.FinSenseSurface
import com.example.ui.theme.FinSenseSurfaceHigh
import com.example.ui.theme.FinSenseSurfaceLow
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.components.AssessmentMetric
import com.example.ui.components.RecommendationRationaleDropdown
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SmartSavingsDetailsScreen(
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var depositAmount by remember { mutableFloatStateOf(25000f) }
    var selectedBankIndex by remember { mutableIntStateOf(0) }
    var isFaqExpanded by remember { mutableStateOf(false) }
    var isActivated by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val formattedAmount = "₹" + NumberFormat.getNumberInstance(Locale("en", "IN")).format(depositAmount.toInt())
    val normalYield = (depositAmount * 0.027f).toInt()
    val smartYield = (depositAmount * 0.074f).toInt()
    val extraGain = smartYield - normalYield

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
                            .testTag("savings_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "Smart Auto-Sweep Savings",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }
        }

        // Why this recommendation is shown (Transaction History & Financial Assessment)
        item {
            RecommendationRationaleDropdown(
                title = "Why is this Smart Savings recommendation shown?",
                collapsedSummary = "Average idle balance ₹24,850 • Earning 2.7% • Auto-Sweep boosts to 7.4% p.a.",
                transactionHistoryTitle = "₹24,850 average wallet & linked bank balance with idle cash > ₹10,000",
                transactionHistoryDetail = "Continuous analysis of past 30 days indicates surplus funds above your daily spending velocity. Idle cash has remained in low-yield savings accounts across 18 consecutive days.",
                financialAssessmentTitle = "Personal Liquidity & Interest Loss Assessment",
                financialAssessmentDetail = "Keeping idle cash in standard savings yields only 2.7% (~₹675/yr on ₹25,000). By auto-sweeping excess funds into partner fixed deposits, your annual yield increases to 7.4% (~₹1,850/yr), generating +₹1,175 extra gain with 100% instant UPI liquidity.",
                assessmentMetrics = listOf(
                    AssessmentMetric("Current Yield", "2.7% p.a."),
                    AssessmentMetric("Auto-Sweep Yield", "7.4% p.a."),
                    AssessmentMetric("Net Extra Gain", "+₹1,175/yr"),
                    AssessmentMetric("Liquidity Lock", "0 Days (Instant)")
                ),
                aiRecommendationRationale = "Activating Auto-Sweep multiplies your passive returns with zero penalty on premature breaking whenever you make payments.",
                initiallyExpanded = true,
                testTagPrefix = "savings_rationale"
            )
        }

        // Hero Context Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F8F1)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, StatusSuccess.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(StatusSuccess)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "WEALTH INTELLIGENCE",
                                style = MaterialTheme.typography.labelSmall,
                                color = StatusSuccess,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Zero Lock-in",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                color = FinSenseNavy,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFE0E8FF))
                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Earn up to 7.4% p.a. on idle cash",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "You currently maintain ~₹24,850 in regular savings earning only 2.7%. Auto-sweep funds above ₹10,000 for 2.7x higher returns.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // 3-Step Inference Timeline
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "INFERENCE TIMELINE",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Automated",
                            style = MaterialTheme.typography.labelMedium,
                            color = FinSenseNavy,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    SavingsTimelineStep(
                        isCompleted = true,
                        isActive = false,
                        title = "Idle balance analyzed",
                        subtitle = "₹24,850 maintained across 45+ days",
                        showLineBelow = true
                    )
                    SavingsTimelineStep(
                        isCompleted = true,
                        isActive = false,
                        title = "Zero-penalty liquidity verified",
                        subtitle = "Instant spend & withdraw anytime",
                        showLineBelow = true
                    )
                    SavingsTimelineStep(
                        isCompleted = false,
                        isActive = true,
                        title = "7.4% FD rate pre-locked",
                        subtitle = "DICGC insured up to ₹5 Lakhs",
                        showLineBelow = false
                    )
                }
            }
        }

        // Interactive Yield Calculator Card
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
                            text = "Auto-Sweep Balance",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary
                        )
                        Text(
                            text = formattedAmount,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = FinSenseNavy
                        )
                    }

                    Slider(
                        value = depositAmount,
                        onValueChange = { depositAmount = it },
                        valueRange = 10000f..100000f,
                        steps = 8,
                        colors = SliderDefaults.colors(
                            thumbColor = FinSenseCyan,
                            activeTrackColor = FinSenseCyan,
                            inactiveTrackColor = FinSenseSurfaceHigh
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Yield Comparison
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(FinSenseSurfaceLow)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Regular Savings (2.7%)", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Text(text = "₹$normalYield/yr", style = MaterialTheme.typography.headlineSmall, color = TextSecondary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Smart Auto-Sweep (7.4%)", style = MaterialTheme.typography.labelSmall, color = StatusSuccess, fontWeight = FontWeight.Bold)
                            Text(text = "₹$smartYield/yr", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = StatusSuccess)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Extra earnings: +₹$extraGain / year with zero effort",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = FinSenseNavy
                        )
                    }
                }
            }
        }

        // Partner Bank Options
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "SELECT PARTNER BANK",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    fontWeight = FontWeight.Bold
                )

                BankOptionCard(
                    bankName = "Paytm Payments Bank Fixed Deposit",
                    rate = "7.4% p.a.",
                    tenure = "1 Year • Instant Auto-break",
                    isSelected = selectedBankIndex == 0,
                    onClick = { selectedBankIndex = 0 }
                )

                BankOptionCard(
                    bankName = "IndusInd Bank Smart Sweep",
                    rate = "7.5% p.a.",
                    tenure = "400 Days • DICGC Insured",
                    isSelected = selectedBankIndex == 1,
                    onClick = { selectedBankIndex = 1 }
                )
            }
        }

        // Accordion: How does Auto-Sweep work?
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderHairline, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isFaqExpanded = !isFaqExpanded }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = null,
                                tint = FinSenseNavy,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "How does Auto-Sweep work?",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Icon(
                            imageVector = if (isFaqExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    AnimatedVisibility(visible = isFaqExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SweepPoint("Threshold Limit: ₹10,000 stays in your primary bank for daily UPI bills.")
                            SweepPoint("Automatic Conversion: Any excess amount automatically sweeps into high-yield fixed deposits.")
                            SweepPoint("Zero-Penalty Liquidity: If you spend via QR or transfer, funds reverse-sweep immediately without penalty fees.")
                        }
                    }
                }
            }
        }

        // Trust badge
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = FinSenseNavy,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Regulated by RBI • Guaranteed by DICGC up to ₹5,00,000",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }

        // CTA Button
        item {
            Button(
                onClick = {
                    isActivated = true
                    scope.launch {
                        snackbarHostState.showSnackbar("Auto-Sweep activated! Excess funds will now earn 7.4% p.a.")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = if (isActivated) StatusSuccess else FinSenseCyan),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("activate_auto_sweep_button")
            ) {
                Text(
                    text = if (isActivated) "Auto-Sweep Activated ✓" else "Activate Auto-Sweep in 1 Tap",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = if (isActivated) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SavingsTimelineStep(
    isCompleted: Boolean,
    isActive: Boolean,
    title: String,
    subtitle: String,
    showLineBelow: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(22.dp)
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = StatusSuccess,
                    modifier = Modifier.size(20.dp)
                )
            } else if (isActive) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(FinSenseCyan.copy(alpha = 0.25f))
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(FinSenseCyan)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(TextMuted)
                )
            }

            if (showLineBelow) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(24.dp)
                        .background(BorderHairline)
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.SemiBold,
                color = if (isActive) FinSenseNavy else TextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
        }
    }
}

@Composable
private fun BankOptionCard(
    bankName: String,
    rate: String,
    tenure: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) FinSenseSurfaceLow else FinSenseSurface
        ),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) FinSenseCyan else BorderHairline,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
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
                        .clip(CircleShape)
                        .background(FinSenseSurfaceHigh)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = null,
                        tint = FinSenseNavy,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = bankName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = tenure,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
            }

            Text(
                text = rate,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = StatusSuccess
            )
        }
    }
}

@Composable
private fun SweepPoint(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = FinSenseNavy,
            modifier = Modifier
                .size(16.dp)
                .padding(top = 2.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}
