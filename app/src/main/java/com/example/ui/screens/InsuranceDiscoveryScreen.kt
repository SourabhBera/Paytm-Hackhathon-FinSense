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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.TireRepair
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.FinSenseSurfaceContainer
import com.example.ui.theme.FinSenseSurfaceHigh
import com.example.ui.theme.FinSenseSurfaceLow
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusErrorBg
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.components.AssessmentMetric
import com.example.ui.components.RecommendationRationaleDropdown
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun InsuranceDiscoveryScreen(
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var selectedQuoteIndex by remember { mutableStateOf(0) }
    var isCoverageExpanded by remember { mutableStateOf(false) }
    var isFleetMode by remember { mutableStateOf(false) }
    var activeJargonDefinition by remember { mutableStateOf<com.example.ui.components.JargonDefinition?>(null) }
    var issuedPolicyNumber by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

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
                            .testTag("insurance_discovery_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "Vehicle Insurance",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary
                    )
                }

                // Personal / Fleet Switcher Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(FinSenseSurfaceHigh)
                        .padding(2.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (!isFleetMode) FinSenseSurface else Color.Transparent,
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { isFleetMode = false }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = FinSenseNavy,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Personal",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = FinSenseNavy
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isFleetMode) FinSenseSurface else Color.Transparent,
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                isFleetMode = true
                                scope.launch {
                                    snackbarHostState.showSnackbar("Switched to Commercial Fleet policies")
                                }
                            }
                    ) {
                        Text(
                            text = "Fleet",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Why this recommendation is shown (Transaction History & Financial Assessment)
        item {
            RecommendationRationaleDropdown(
                title = "Why is this insurance recommendation shown?",
                collapsedSummary = "SpeedX service ₹6,450 • 28 days to policy expiry • Save ~₹2,200/yr",
                transactionHistoryTitle = "₹6,450 paid to SpeedX Motor Works on 16 Oct (Auto Service)",
                transactionHistoryDetail = "Transaction #TXN-88219 confirmed routine vehicle maintenance milestone. Auto repair and fuel expenditures rose by +18% this month, reaching the optimal policy renewal window.",
                financialAssessmentTitle = "Personal Budget Capacity & Vehicle Asset Risk Profile",
                financialAssessmentDetail = "Monthly budget is ₹65,000 (spent ₹48,920, healthy cash buffer). Your 2021 Honda City (IDV ₹7,20,000) policy expires on 18 Nov. An accident claim without Zero Depreciation would require ~₹12,000+ out-of-pocket for plastic and rubber parts.",
                assessmentMetrics = listOf(
                    AssessmentMetric("Asset IDV", "₹7,20,000"),
                    AssessmentMetric("Policy Expiry", "28 Days"),
                    AssessmentMetric("Accrued NCB", "35% (Save ₹2,200)"),
                    AssessmentMetric("Suitability Score", "94% High")
                ),
                aiRecommendationRationale = "Renewing through FinSense locks in your 35% No-Claim Bonus before expiry and includes 100% Zero-Depreciation cover, saving ₹2,200/yr vs manual dealer renewal.",
                initiallyExpanded = true,
                testTagPrefix = "insurance_rationale"
            )
        }

        // Vehicle Information Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderHairline, RoundedCornerShape(16.dp))
                    .testTag("vehicle_info_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(FinSenseSurfaceHigh)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsCar,
                                    contentDescription = null,
                                    tint = FinSenseNavy,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = "Honda City V MT (2021)",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "KA-01-MJ-4029",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(FinSenseSurfaceContainer)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(StatusErrorBg)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(StatusError)
                            )
                            Text(
                                text = "Expiring in 28 days",
                                style = MaterialTheme.typography.labelSmall,
                                color = StatusError,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // IDV and renewal date sub-card
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(FinSenseSurfaceLow)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Current Insured Value (IDV)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted
                                )
                                com.example.ui.components.JargonChip(
                                    term = "IDV",
                                    onClick = {
                                        activeJargonDefinition = com.example.ui.components.JargonGlossary.items["IDV"]
                                    }
                                )
                            }
                            Text(
                                text = "₹7,20,000",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Renewal Due",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted
                            )
                            Text(
                                text = "18 Nov 2024",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Provider Quotes Header
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RECOMMENDED QUOTES (3)",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Customise IDV",
                        style = MaterialTheme.typography.labelMedium,
                        color = FinSenseNavy,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable {
                                scope.launch {
                                    snackbarHostState.showSnackbar("IDV Customiser slider opened (₹6,50,000 - ₹7,80,000)")
                                }
                            }
                            .testTag("customise_idv_button")
                    )
                }

                // AI Plain-Language Terms Decoder Row
                androidx.compose.foundation.lazy.LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val terms = listOf("Zero Depreciation", "NCB", "Engine Protector", "Consumables Cover")
                    items(terms) { termKey ->
                        com.example.ui.components.JargonChip(
                            term = termKey,
                            onClick = {
                                activeJargonDefinition = com.example.ui.components.JargonGlossary.items[termKey]
                            }
                        )
                    }
                }
            }
        }

        // Exactly 3 Provider Cards
        item {
            QuoteCard(
                provider = "CareGuard Auto",
                idv = "₹7,20,000",
                price = "₹8,450",
                badge = "Best Value",
                badgeColor = Color(0xFFC0E8FF),
                badgeText = FinSenseNavy,
                icon = Icons.Default.VerifiedUser,
                iconTint = FinSenseNavy,
                isSelected = selectedQuoteIndex == 0,
                feature1 = "Cashless garage network (4,200+ garages)",
                feature1Icon = Icons.Default.CarRepair,
                feature2 = "24/7 Roadside Assistance included",
                feature2Icon = Icons.Default.SupportAgent,
                onClick = { selectedQuoteIndex = 0 },
                tag = "quote_careguard"
            )
        }

        item {
            QuoteCard(
                provider = "ShieldPrime Motor",
                idv = "₹7,10,000",
                price = "₹7,920",
                sublabel = "Economy Pick",
                icon = Icons.Default.Shield,
                iconTint = Color(0xFF3F5BA3),
                isSelected = selectedQuoteIndex == 1,
                feature1 = "Zero depreciation available",
                feature1Icon = Icons.Default.CheckCircle,
                feature2 = "Instant claim settlement under ₹50,000",
                feature2Icon = Icons.Default.Bolt,
                onClick = { selectedQuoteIndex = 1 },
                tag = "quote_shieldprime"
            )
        }

        item {
            QuoteCard(
                provider = "SecureDrive Pro",
                idv = "₹7,20,000",
                price = "₹9,200",
                sublabel = "Maximum Cover",
                icon = Icons.Default.Security,
                iconTint = FinSenseNavy,
                isSelected = selectedQuoteIndex == 2,
                feature1 = "Engine & gearbox protection covered",
                feature1Icon = Icons.Default.Settings,
                feature2 = "Consumables & tyre cover included",
                feature2Icon = Icons.Default.TireRepair,
                onClick = { selectedQuoteIndex = 2 },
                tag = "quote_securedrive"
            )
        }

        // Progressive Disclosure Accordion: What is covered?
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderHairline, RoundedCornerShape(16.dp))
                    .testTag("what_is_covered_accordion")
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isCoverageExpanded = !isCoverageExpanded }
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
                                text = "What is covered?",
                                style = MaterialTheme.typography.headlineSmall,
                                color = TextPrimary
                            )
                        }

                        Icon(
                            imageVector = if (isCoverageExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    AnimatedVisibility(visible = isCoverageExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CoverageBulletItem("Third-party damage and bodily injury liabilities up to legal limits.")
                            CoverageBulletItem("Own vehicle damage caused by accidents, natural calamities, fire, or theft.")
                            CoverageBulletItem("Personal accident cover for owner-driver up to ₹15 Lakhs.")
                        }
                    }
                }
            }
        }

        // Issued Policy Success Notification Banner
        if (issuedPolicyNumber != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.StatusSuccessBg),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.StatusSuccess),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("policy_issued_success_banner")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = com.example.ui.theme.StatusSuccess,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "Policy Issued Instantly!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = com.example.ui.theme.StatusSuccess
                            )
                        }
                        Text(
                            text = "Policy #POL-88219 (CareGuard Auto) is active. Digital certificate deposited to DigiLocker and stored in Journeys Hub.",
                            fontSize = 13.sp,
                            color = TextPrimary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Primary CTA Buttons: Instant Issuance & Compare
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val names = listOf("CareGuard Auto", "ShieldPrime Motor", "SecureDrive Pro")
                val prices = listOf("₹8,450", "₹7,920", "₹9,200")

                Button(
                    onClick = {
                        issuedPolicyNumber = "POL-88219"
                        scope.launch {
                            snackbarHostState.showSnackbar("✓ 1-Tap Policy Delivery: ${names[selectedQuoteIndex]} issued and synced to DigiLocker!")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FinSenseCyan),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("instant_issue_policy_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "1-Tap Issue Policy (${prices[selectedQuoteIndex]}/yr)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Button(
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Comparing ${names.joinToString(", ")}: Zero Dep, IDV, and Cashless network side-by-side.")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FinSenseSurfaceContainer),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("compare_policies_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Balance,
                        contentDescription = null,
                        tint = FinSenseNavy,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Compare Policy Clauses",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = FinSenseNavy
                    )
                }

                Text(
                    text = "Zero paper claims • Instant digital policy delivery • DigiLocker ready",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Modal Bottom Sheet for Plain-Language Term Decoder
    com.example.ui.components.JargonDecoderBottomSheet(
        definition = activeJargonDefinition,
        onDismiss = { activeJargonDefinition = null }
    )
}

@Composable
private fun QuoteCard(
    provider: String,
    idv: String,
    price: String,
    badge: String? = null,
    badgeColor: Color = Color.Transparent,
    badgeText: Color = Color.Black,
    sublabel: String? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    isSelected: Boolean,
    feature1: String,
    feature1Icon: androidx.compose.ui.graphics.vector.ImageVector,
    feature2: String,
    feature2Icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    tag: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) FinSenseCyan else BorderHairline,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .testTag(tag)
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
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(FinSenseSurfaceLow)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = provider,
                            style = MaterialTheme.typography.headlineSmall,
                            color = TextPrimary
                        )
                        Text(
                            text = "IDV: $idv",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    if (badge != null) {
                        Text(
                            text = badge,
                            style = MaterialTheme.typography.labelSmall,
                            color = badgeText,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(badgeColor)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = price,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "/ yr",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                    if (sublabel != null) {
                        Text(
                            text = sublabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                }
            }

            // Features box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(FinSenseSurfaceLow.copy(alpha = 0.7f))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = feature1Icon,
                        contentDescription = null,
                        tint = FinSenseNavy,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = feature1,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = feature2Icon,
                        contentDescription = null,
                        tint = StatusWarning,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = feature2,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun CoverageBulletItem(text: String) {
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
