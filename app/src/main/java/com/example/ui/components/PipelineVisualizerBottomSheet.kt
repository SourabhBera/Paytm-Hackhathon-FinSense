package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
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
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessBg
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class PipelineJourneyType(val label: String, val persona: String) {
    PERSONAL_CAR_INSURANCE("Vehicle Insurance", "Consumer"),
    PERSONAL_HEALTH_TAX("Family Health & 80D", "Consumer"),
    MERCHANT_WORKING_CAPITAL("Working Capital Loan", "Merchant"),
    MERCHANT_SETTLEMENT_SWEEP("Settlement Instant Sweep", "Merchant")
}

data class PipelineStepData(
    val stepNumber: String,
    val stepTitle: String,
    val stepSubtitle: String,
    val telemetryTag: String,
    val telemetryBadge: String,
    val payloadFields: List<Pair<String, String>>,
    val deterministicRule: String,
    val explanation: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PipelineVisualizerBottomSheet(
    initialPipelineType: PipelineJourneyType = PipelineJourneyType.PERSONAL_CAR_INSURANCE,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var selectedPipelineType by remember { mutableStateOf(initialPipelineType) }
    var selectedStepIndex by remember { mutableIntStateOf(0) }
    var isSimulating by remember { mutableStateOf(false) }
    var simulationStep by remember { mutableIntStateOf(4) } // Default fully executed
    val coroutineScope = rememberCoroutineScope()

    // Handle auto-simulation progression
    LaunchedEffect(isSimulating) {
        if (isSimulating) {
            for (step in 0..4) {
                simulationStep = step
                selectedStepIndex = step
                delay(900)
            }
            isSimulating = false
        }
    }

    val pipelineSteps = remember(selectedPipelineType) {
        getPipelineStepsForType(selectedPipelineType)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = FinSenseSurface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
                .testTag("pipeline_visualizer_sheet"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(FinSenseNavy),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = FinSenseCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "5-Step AI Pipeline",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFE8F4FC)
                            ) {
                                Text(
                                    text = "Slide 3 USP",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FinSenseNavy,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Unified Intelligence Layer • Signal to Completion",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(FinSenseSurfaceHigh)
                        .testTag("close_pipeline_sheet_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Pipeline Preset Switcher Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(PipelineJourneyType.values()) { type ->
                    val isSelected = selectedPipelineType == type
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) FinSenseNavy else FinSenseSurfaceLow,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) FinSenseNavy else BorderHairline
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                selectedPipelineType = type
                                selectedStepIndex = 0
                                simulationStep = 4
                            }
                            .testTag("pipeline_preset_${type.name.lowercase()}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) FinSenseCyan else TextMuted)
                            )
                            Text(
                                text = "${type.persona}: ${type.label}",
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextPrimary
                            )
                        }
                    }
                }
            }

            // 5-Step Stepper Cards
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                pipelineSteps.forEachIndexed { index, stepData ->
                    val isCurrentStep = index <= simulationStep
                    val isSelected = selectedStepIndex == index

                    val borderColor by animateColorAsState(
                        targetValue = if (isSelected) FinSenseCyan else BorderHairline,
                        label = "border_anim"
                    )

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFFF3FAFD) else FinSenseSurface
                        ),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(if (isSelected) 2.dp else 1.dp, borderColor, RoundedCornerShape(14.dp))
                            .clickable { selectedStepIndex = index }
                            .testTag("pipeline_step_${stepData.stepNumber}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                // Step Number Badge with state
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isCurrentStep) FinSenseNavy else FinSenseSurfaceLow
                                        )
                                ) {
                                    if (isCurrentStep) {
                                        Text(
                                            text = stepData.stepNumber,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    } else {
                                        Text(
                                            text = stepData.stepNumber,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextMuted
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = stepData.stepTitle,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = stepData.stepSubtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            // Telemetry Badge
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isCurrentStep) StatusSuccessBg else FinSenseSurfaceLow
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    if (isCurrentStep) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = StatusSuccess,
                                            modifier = Modifier.size(11.dp)
                                        )
                                    }
                                    Text(
                                        text = if (isCurrentStep) stepData.telemetryBadge else "Queued",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrentStep) StatusSuccess else TextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Detailed Inspector for Selected Step
            val activeStep = pipelineSteps[selectedStepIndex]
            Card(
                colors = CardDefaults.cardColors(containerColor = FinSenseSurfaceLow),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderHairline),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("step_telemetry_inspector")
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
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = null,
                                tint = FinSenseNavy,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Step ${activeStep.stepNumber} Execution Telemetry",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = FinSenseNavy
                            )
                        }

                        Text(
                            text = activeStep.telemetryTag,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = TextMuted,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // Payload Fields Grid
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(FinSenseSurface)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        activeStep.payloadFields.forEach { (key, value) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = key,
                                    fontSize = 11.sp,
                                    color = TextMuted,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = value,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }

                    // Deterministic Rule Banner
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE8F4FC))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = FinSenseNavy,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Guardrail: ${activeStep.deterministicRule}",
                            fontSize = 11.sp,
                            color = FinSenseNavy,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Text(
                        text = activeStep.explanation,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 15.sp
                    )
                }
            }

            // Bottom Actions: Simulate Pipeline + Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        isSimulating = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FinSenseNavy),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1.2f)
                        .height(44.dp)
                        .testTag("btn_simulate_pipeline")
                ) {
                    Icon(
                        imageVector = if (isSimulating) Icons.Default.Refresh else Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isSimulating) "Simulating..." else "Simulate Pipeline",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = FinSenseSurfaceHigh),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(0.8f)
                        .height(44.dp)
                ) {
                    Text(
                        text = "Done",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

private fun getPipelineStepsForType(type: PipelineJourneyType): List<PipelineStepData> {
    return when (type) {
        PipelineJourneyType.PERSONAL_CAR_INSURANCE -> listOf(
            PipelineStepData(
                stepNumber = "01",
                stepTitle = "Detect Signal",
                stepSubtitle = "Auto maintenance payment milestone",
                telemetryTag = "SIGNAL_TRIGGER",
                telemetryBadge = "Captured",
                payloadFields = listOf(
                    "Event" to "UPI Payment Authorized",
                    "Amount" to "₹6,450.00",
                    "Merchant" to "SpeedX Motor Works",
                    "Category" to "MCC 5511 (Automotive)"
                ),
                deterministicRule = "Frequency cap check passed (0 prompts in past 7 days)",
                explanation = "A transaction at an authorized auto-workshop signals routine service completion and enters the optimal renewal advisory window."
            ),
            PipelineStepData(
                stepNumber = "02",
                stepTitle = "Understand Context",
                stepSubtitle = "Vehicle registry & policy expiry audit",
                telemetryTag = "CONTEXT_ENRICHMENT",
                telemetryBadge = "Enriched",
                payloadFields = listOf(
                    "Asset Model" to "Honda City 1.5 i-VTEC",
                    "Asset IDV" to "₹7,20,000",
                    "Current Expiry" to "28 Days Remaining",
                    "Accrued NCB" to "35% (Good Driver)"
                ),
                deterministicRule = "Privacy data minimization applied (only vehicle attributes stored)",
                explanation = "FinSense checks policy expiration records without requesting broad permissions, identifying potential forfeiture of 35% No-Claim Bonus if delayed."
            ),
            PipelineStepData(
                stepNumber = "03",
                stepTitle = "Recommend Journey",
                stepSubtitle = "Deterministic suitability matching",
                telemetryTag = "AI_ORCHESTRATION",
                telemetryBadge = "Synthesized",
                payloadFields = listOf(
                    "Recommendation" to "Zero-Depreciation Renewal",
                    "Annual Savings" to "Save ~₹2,200/yr",
                    "Out-of-Pocket Risk" to "₹12,000 on plastic parts",
                    "Suitability Score" to "94% High"
                ),
                deterministicRule = "Math engine verified: NCB 35% locks ₹2,200 discount",
                explanation = "Hybrid AI pairs LLM natural phrasing with deterministic math, guaranteeing transparent calculations without hallucinated discounts."
            ),
            PipelineStepData(
                stepNumber = "04",
                stepTitle = "Guide Execution",
                stepSubtitle = "Plain-language terms translation",
                telemetryTag = "JARGON_DECODER",
                telemetryBadge = "Decoded",
                payloadFields = listOf(
                    "Jargon Decoded" to "IDV, Zero Dep, NCB",
                    "Insurers Compared" to "Tata AIG, ICICI Lombard, HDFC",
                    "Recommended Plan" to "Tata AIG AutoSecure (₹8,450)",
                    "Cashless Garages" to "450+ Verified"
                ),
                deterministicRule = "No-claim bonus ownership rule: Ported 100% to new insurer",
                explanation = "Jargon Decoder translates complex terms into plain English so the user understands why Zero-Dep pays 100% on repairs."
            ),
            PipelineStepData(
                stepNumber = "05",
                stepTitle = "Complete",
                stepSubtitle = "1-click issuance & policy vault sync",
                telemetryTag = "LIFECYCLE_COMPLETE",
                telemetryBadge = "Verified",
                payloadFields = listOf(
                    "Policy Number" to "TATA-MOT-2026-88192",
                    "Status" to "Active & Underwritten",
                    "DigiLocker" to "E-Insurance Account Synced",
                    "Audit Trail" to "Txn #TXN-88219 Linked"
                ),
                deterministicRule = "Zero paper verification: Instant digital PDF delivered",
                explanation = "Journey completes seamlessly without multi-step drop-offs, syncing policy documentation into the digital vault."
            )
        )
        PipelineJourneyType.PERSONAL_HEALTH_TAX -> listOf(
            PipelineStepData(
                stepNumber = "01",
                stepTitle = "Detect Signal",
                stepSubtitle = "Recurring medical & clinic expenditures",
                telemetryTag = "SIGNAL_TRIGGER",
                telemetryBadge = "Captured",
                payloadFields = listOf(
                    "Quarterly Medical" to "₹12,400 across 3 Txns",
                    "Merchants" to "Apollo Pharmacy, Fortis Clinic",
                    "Category" to "MCC 5912 (Health & Diagnostic)"
                ),
                deterministicRule = "Consent model validated: Medical category opt-in active",
                explanation = "FinSense detects significant out-of-pocket medical expenditures without an active family health buffer."
            ),
            PipelineStepData(
                stepNumber = "02",
                stepTitle = "Understand Context & Entity Resolution",
                stepSubtitle = "Payer ≠ Patient check & 80D tax bracket",
                telemetryTag = "ENTITY_RESOLUTION",
                telemetryBadge = "Disambiguated",
                payloadFields = listOf(
                    "Entity Resolution" to "Payer ≠ Patient Disambiguated",
                    "Beneficiary" to "Senior Citizen Parent (Age 62)",
                    "Tax Bracket" to "30% Slab (Section 80D eligible)",
                    "Clinical Inference" to "Zero Medical Assumptions"
                ),
                deterministicRule = "Non-diagnosis principle: Models tax rights & cash protection, never illness",
                explanation = "FinSense solves the hallucination trap: It never assumes the payer is ill. It disambiguates the beneficiary (senior citizen parent) to unlock ₹50,000 under Section 80D."
            ),
            PipelineStepData(
                stepNumber = "03",
                stepTitle = "Recommend Journey",
                stepSubtitle = "Parental 80D Tax & Critical Care Shield",
                telemetryTag = "AI_ORCHESTRATION",
                telemetryBadge = "Synthesized",
                payloadFields = listOf(
                    "Coverage Plan" to "Parental Senior Citizen Shield + OPD",
                    "80D Tax Refund" to "₹15,600 Net Savings (30% slab of ₹50k)",
                    "Cash Outflow" to "Protects liquid mutual funds"
                ),
                deterministicRule = "Formula: Section 80D senior citizen limit (₹50k) * 30% slab = ₹15,000+",
                explanation = "Recommends IRDAI-compliant parental coverage with zero waiting period on acute OPD consults, maximizing Section 80D tax deductions."
            ),
            PipelineStepData(
                stepNumber = "04",
                stepTitle = "Guide Execution",
                stepSubtitle = "Transparent hospital network breakdown",
                telemetryTag = "JARGON_DECODER",
                telemetryBadge = "Decoded",
                payloadFields = listOf(
                    "Decoded" to "Waiting Period & Room Rent Sub-limits",
                    "Hospitals" to "9,500+ Cashless Facilities",
                    "Selected Plan" to "Care Supreme Floater (₹850/mo)"
                ),
                deterministicRule = "Room rent capping check: Unlimited single private room",
                explanation = "Educates user on room rent proportionate deductions so they choose plans with zero unexpected deductions."
            ),
            PipelineStepData(
                stepNumber = "05",
                stepTitle = "Complete",
                stepSubtitle = "Instant cashless e-card issuance",
                telemetryTag = "LIFECYCLE_COMPLETE",
                telemetryBadge = "Verified",
                payloadFields = listOf(
                    "Policy Ref" to "CARE-HLT-2026-99214",
                    "80D Tax Receipt" to "Generated for ITR filing",
                    "E-Cards" to "4 Family Member Cards Issued"
                ),
                deterministicRule = "Audit record stamped with DigiLocker verification hash",
                explanation = "Policy active immediately with downloadable 80D tax exemption certificate."
            )
        )
        PipelineJourneyType.MERCHANT_WORKING_CAPITAL -> listOf(
            PipelineStepData(
                stepNumber = "01",
                stepTitle = "Detect Signal",
                stepSubtitle = "Steady daily QR code collections",
                telemetryTag = "MERCHANT_SIGNAL",
                telemetryBadge = "Captured",
                payloadFields = listOf(
                    "Avg Daily Inflow" to "₹12,400 via Paytm Soundbox",
                    "Monthly Volume" to "₹2,84,900 across 324 orders",
                    "Merchant Store" to "Rajesh Electronics (#402)"
                ),
                deterministicRule = "Min collections threshold check: >₹1L/mo passed",
                explanation = "Consistent merchant Soundbox transaction telemetry triggers instant working capital eligibility."
            ),
            PipelineStepData(
                stepNumber = "02",
                stepTitle = "Understand Context",
                stepSubtitle = "Bureau score & cashflow velocity audit",
                telemetryTag = "CONTEXT_ENRICHMENT",
                telemetryBadge = "Enriched",
                payloadFields = listOf(
                    "Credit Score" to "CIBIL 790 (Prime Merchant)",
                    "Repayment Capacity" to "₹450/day micro-deduction",
                    "Inventory Peak" to "Diwali stock turnover"
                ),
                deterministicRule = "Debt-service coverage ratio (DSCR): 3.8x (safe liquidity)",
                explanation = "Enriches business profile to ensure repayment will not strangle daily inventory buying power."
            ),
            PipelineStepData(
                stepNumber = "03",
                stepTitle = "Recommend Journey",
                stepSubtitle = "Pre-approved credit line synthesis",
                telemetryTag = "AI_ORCHESTRATION",
                telemetryBadge = "Synthesized",
                payloadFields = listOf(
                    "Pre-Approved Limit" to "₹1,50,000 Instant",
                    "Monthly Rate" to "1.0% Flat (12.4% APR Reducing)",
                    "Processing Fee" to "₹0 Special Diwali Waiver"
                ),
                deterministicRule = "APR disclosure rule: Flat vs Reducing balance displayed",
                explanation = "Transparent credit proposal with clear APR disclosure, completely eliminating predatory hidden fees."
            ),
            PipelineStepData(
                stepNumber = "04",
                stepTitle = "Guide Execution",
                stepSubtitle = "Daily auto-settlement micro-debit decoder",
                telemetryTag = "JARGON_DECODER",
                telemetryBadge = "Decoded",
                payloadFields = listOf(
                    "Decoded" to "Daily Micro-Settlement Deduction",
                    "Daily Deduction" to "₹450 from evening QR sweep",
                    "Tenure" to "12 Months (Flexi-prepay ₹0 penalty)"
                ),
                deterministicRule = "Daily debit cap: Never exceeds 10% of daily inflow",
                explanation = "Explains how deducting a fraction of daily customer payments prevents stressful month-end lump sums."
            ),
            PipelineStepData(
                stepNumber = "05",
                stepTitle = "Complete",
                stepSubtitle = "Instant disbursal to current account",
                telemetryTag = "LIFECYCLE_COMPLETE",
                telemetryBadge = "Verified",
                payloadFields = listOf(
                    "Disbursed Amount" to "₹1,50,000 to HDFC ••9012",
                    "Disbursal Time" to "Instant (under 2 minutes)",
                    "Loan Account" to "LN-PYTM-88219"
                ),
                deterministicRule = "Mandate registered with NPCI e-NACH clearing",
                explanation = "Funds credited instantly to merchant current account to purchase inventory."
            )
        )
        PipelineJourneyType.MERCHANT_SETTLEMENT_SWEEP -> listOf(
            PipelineStepData(
                stepNumber = "01",
                stepTitle = "Detect Signal",
                stepSubtitle = "Weekend nodal clearing delay flagged",
                telemetryTag = "SETTLEMENT_ALERT",
                telemetryBadge = "Captured",
                payloadFields = listOf(
                    "Held Batch" to "Batch #SET-88310",
                    "Accumulated Inflow" to "₹24,800 across 14 QR payments",
                    "Delay Duration" to "4 hours over bank holiday"
                ),
                deterministicRule = "SLA monitor triggered: Latency > 180 min",
                explanation = "Automated settlement telemetry catches weekend bank nodal hold before merchant experiences liquidity pinch."
            ),
            PipelineStepData(
                stepNumber = "02",
                stepTitle = "Understand Context",
                stepSubtitle = "Bank clearing status & vendor payment urgency",
                telemetryTag = "CONTEXT_ENRICHMENT",
                telemetryBadge = "Enriched",
                payloadFields = listOf(
                    "Destination Bank" to "State Bank of India ••4412",
                    "Upcoming Payables" to "Distributor invoice due tomorrow",
                    "Merchant Liquidity" to "Low weekend operating cash"
                ),
                deterministicRule = "Account reconciliation: All 14 sales slips matched",
                explanation = "Correlates store receivables with distributor obligations to prioritize payout speed."
            ),
            PipelineStepData(
                stepNumber = "03",
                stepTitle = "Recommend Journey",
                stepSubtitle = "Instant IMPS sweep bypass",
                telemetryTag = "AI_ORCHESTRATION",
                telemetryBadge = "Synthesized",
                payloadFields = listOf(
                    "Recommended Action" to "Instant IMPS Settlement Sweep",
                    "Transfer Fee" to "₹0 Promo Fee Waiver",
                    "Expected Credit" to "< 30 Seconds"
                ),
                deterministicRule = "Risk check passed: Merchant KYC valid & unencumbered",
                explanation = "Proactively suggests an immediate IMPS nodal bypass sweep rather than waiting for Monday morning clearing."
            ),
            PipelineStepData(
                stepNumber = "04",
                stepTitle = "Guide Execution",
                stepSubtitle = "Settlement UTR & ledger verification",
                telemetryTag = "JARGON_DECODER",
                telemetryBadge = "Decoded",
                payloadFields = listOf(
                    "Reconciliation" to "14 Txns mapped to single UTR",
                    "UTR Number" to "UTR-SBI-20261031-99214",
                    "Audit Slip" to "Available in 1 click"
                ),
                deterministicRule = "Dual ledger check: Paytm ledger = Merchant balance",
                explanation = "Prepares single consolidated UTR for simple bookkeeping."
            ),
            PipelineStepData(
                stepNumber = "05",
                stepTitle = "Complete",
                stepSubtitle = "Direct payout to SBI account",
                telemetryTag = "LIFECYCLE_COMPLETE",
                telemetryBadge = "Verified",
                payloadFields = listOf(
                    "Payout" to "₹24,800 Credited Successfully",
                    "Bank Ref" to "IMPS-20261031-4412-998",
                    "Store Status" to "Ready for Monday distributor bills"
                ),
                deterministicRule = "NPCI IMPS acknowledgment received",
                explanation = "Instant settlement successfully executes, resolving merchant operational anxiety."
            )
        )
    }
}
