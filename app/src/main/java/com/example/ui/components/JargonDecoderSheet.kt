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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.FinSenseSurfaceLow
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessBg
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class JargonDefinition(
    val term: String,
    val category: String, // Insurance, Lending, Wealth
    val formalDefinition: String,
    val plainEnglish: String,
    val realWorldExample: String,
    val finSenseRecommendation: String
)

object JargonGlossary {
    val items = mapOf(
        "IDV" to JargonDefinition(
            term = "IDV (Insured Declared Value)",
            category = "Motor Insurance",
            formalDefinition = "The maximum sum assured fixed by the insurer which is provided on theft or total loss of vehicle.",
            plainEnglish = "Think of IDV as your car's official current market price on paper. If your car is stolen or totaled tomorrow, this is the maximum cash check the insurer writes to you.",
            realWorldExample = "Your 2021 Honda City has an IDV of ₹7,20,000. Setting IDV too low saves ₹300 on premium but loses you ₹80,000 in a claim.",
            finSenseRecommendation = "Never depress IDV just to get a cheaper quote. Keep it within 10% of current market valuation."
        ),
        "Zero Depreciation" to JargonDefinition(
            term = "Zero Depreciation (Bumper to Bumper)",
            category = "Motor Insurance",
            formalDefinition = "An add-on cover that eliminates depreciation deduction from replacement value of parts during claim settlement.",
            plainEnglish = "Standard policies only pay 50% for plastic and rubber parts due to 'wear & tear'. Zero Dep forces the insurer to pay 100% of part replacement costs.",
            realWorldExample = "A bumper replacement costs ₹12,000. Without Zero Dep you pay ₹6,000 from pocket. With Zero Dep you pay ₹0 (except ₹1,000 mandatory filing fee).",
            finSenseRecommendation = "Essential for all vehicles under 5 years old. Pays for itself in a single repair."
        ),
        "NCB" to JargonDefinition(
            term = "NCB (No Claim Bonus)",
            category = "Motor Insurance",
            formalDefinition = "A discount on own-damage premium given to the policyholder for not making any claims during previous policy years.",
            plainEnglish = "Your good-driver reward discount! It starts at 20% after year 1 and grows up to 50% after 5 claim-free years.",
            realWorldExample = "Rajesh has 3 claim-free years, saving 35% (approx ₹2,800) on his Honda City own-damage quote.",
            finSenseRecommendation = "NCB belongs to YOU, not the car or insurer. When switching insurers, always carry forward your 100% accumulated NCB."
        ),
        "Engine Protector" to JargonDefinition(
            term = "Engine & Gearbox Protection",
            category = "Motor Insurance",
            formalDefinition = "Covers hydrostatic lock and lubricating oil leakage damages to internal engine components.",
            plainEnglish = "Standard car insurance strictly rejects water entering the engine (hydrolock) during heavy rains or flash floods. This add-on covers full engine rebuilds.",
            realWorldExample = "Driving through waterlogged underpasses in monsoon can ruin an engine, costing ₹1,50,000. This add-on covers it completely.",
            finSenseRecommendation = "Recommended if you live in flood-prone cities or park outdoors during monsoon seasons."
        ),
        "Consumables Cover" to JargonDefinition(
            term = "Consumables Cover",
            category = "Motor Insurance",
            formalDefinition = "Covers the cost of items consumed during vehicle repair such as engine oils, coolants, nuts, bolts, and AC gas.",
            plainEnglish = "Standard insurance refuses to pay for lubricants, grease, washers, and AC gas used during repairs. Consumables cover picks up these miscellaneous shop bills.",
            realWorldExample = "A major repair bill can easily have ₹3,500 in oil, coolants, and clips that insurers usually reject. This covers it.",
            finSenseRecommendation = "Pair with Zero Dep for a genuine zero out-of-pocket claim experience."
        ),
        "Flat vs Reducing APR" to JargonDefinition(
            term = "1% Flat Rate vs Effective APR",
            category = "Lending & Loans",
            formalDefinition = "Flat rate calculates interest on initial principal for the full term. Diminishing/Reducing balance calculates interest only on remaining unpaid balance.",
            plainEnglish = "Lenders advertise 'Only 1% monthly!' which sounds like 12% a year. But on a flat rate, you pay interest on money you already repaid, making the true effective APR around 18-21%.",
            realWorldExample = "On a ₹1,00,000 loan, 1% flat costs ₹12,000 in annual interest, whereas 12% reducing balance costs only ~₹6,600.",
            finSenseRecommendation = "FinSense always displays the true transparent APR upfront so you are never tricked by nominal flat rate advertising."
        ),
        "Daily Auto-Settlement Debit" to JargonDefinition(
            term = "Daily Micro-Settlement Repayment",
            category = "Lending & Loans",
            formalDefinition = "Automated deduction of a fixed daily fraction from QR code merchant collections before end-of-day bank payout.",
            plainEnglish = "Instead of a scary ₹15,000 lump sum EMI on the 5th of every month, ₹450 is automatically deducted from your daily store QR payments as customers pay you.",
            realWorldExample = "Rajesh collects ₹12,000 today from customers. ₹450 goes towards his working capital loan, and ₹11,550 lands in his bank account at 6 PM.",
            finSenseRecommendation = "Prevents month-end liquidity crunches and keeps working capital loans completely stress-free."
        ),
        "Auto-Sweep Accretion" to JargonDefinition(
            term = "Auto-Sweep Fixed Deposit",
            category = "Savings & Wealth",
            formalDefinition = "A banking facility linking a savings account to a fixed deposit, automatically moving excess funds above a threshold.",
            plainEnglish = "Your idle bank cash over ₹10,000 automatically earns high FD interest (7.4% p.a.) instead of lazy savings interest (2.7%), but breaks instantly if you make a UPI payment.",
            realWorldExample = "Leaving ₹50,000 idle in savings yields ~₹1,350/yr. In Auto-Sweep, it yields ~₹3,700/yr with zero lock-in penalty.",
            finSenseRecommendation = "Turn on Auto-Sweep for all primary bank accounts to stop leaving inflation-eroded money on the table."
        ),
        "Waiting Period & Co-Pay" to JargonDefinition(
            term = "Waiting Period & Co-Payment",
            category = "Health Insurance",
            formalDefinition = "Specified duration before certain medical conditions are covered, and percentage of medical bill the policyholder agrees to pay out-of-pocket.",
            plainEnglish = "Pre-existing ailments usually have a 2 to 3 year waiting period before claims are accepted. Co-pay means if your hospital bill is ₹1,00,00, and co-pay is 10%, you pay ₹10,000.",
            realWorldExample = "A policy with '0% Co-Pay' means the insurer pays 100% of approved hospital bills without making you pay a slice.",
            finSenseRecommendation = "Always choose 0% co-pay policies for parents under 65 to avoid surprise hospital checkout deductions."
        ),
        "Section 80D Tax Benefit" to JargonDefinition(
            term = "Section 80D Tax Exemption",
            category = "Health Insurance & Tax",
            formalDefinition = "Income Tax Act deduction allowing up to ₹25,000 for self/family and up to ₹50,000 for senior citizen parents on health insurance premiums.",
            plainEnglish = "The government gives you a direct income tax refund on the premiums you pay for health insurance. In the 30% tax bracket, a ₹25,000 premium saves you ~₹7,500 in tax.",
            realWorldExample = "Rajesh pays ₹11,450/year for his family health floater. At tax filing time, he claims ₹11,450 under Sec 80D and gets ₹3,572 refunded.",
            finSenseRecommendation = "FinSense automatically bundles your 80D certificate for 1-click ITR filing."
        ),
        "Room Rent Sub-limits" to JargonDefinition(
            term = "Room Rent Sub-limits & Capping",
            category = "Health Insurance",
            formalDefinition = "A cap placed on daily hospital room tariff, often 1% of sum insured. Proportionate deduction applies to all medical fees if breached.",
            plainEnglish = "If your ₹5L policy has a 1% room rent limit (₹5,000/day) and you choose a ₹10,000 room, the insurer proportionately slashes doctor fees and ICU bills by 50%!",
            realWorldExample = "A ₹3,00,000 hospital bill gets cut down to ₹1,50,000 simply because the patient stayed in a deluxe room with sub-limits.",
            finSenseRecommendation = "Never buy a policy with room rent capping. FinSense only recommends policies with 'No Room Rent Limit'."
        )
    )
}

@Composable
fun JargonChip(
    term: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = FinSenseSurfaceLow,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderHairline),
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .testTag("jargon_chip_${term.replace(" ", "_").lowercase()}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "Decode term",
                tint = FinSenseCyan,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = term,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = FinSenseNavy
            )
            Icon(
                imageVector = Icons.Default.HelpOutline,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JargonDecoderBottomSheet(
    definition: JargonDefinition?,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    if (definition != null) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = FinSenseSurface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .testTag("jargon_decoder_bottom_sheet"),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(FinSenseSurfaceContainer),
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
                            Text(
                                text = "PLAIN-LANGUAGE DECODER",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = FinSenseCyan,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = definition.term,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("jargon_sheet_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                // Plain English Card (Hero)
                Card(
                    colors = CardDefaults.cardColors(containerColor = FinSenseSurfaceContainer),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = StatusWarning,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "In Plain English",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = definition.plainEnglish,
                            fontSize = 14.sp,
                            color = TextPrimary,
                            lineHeight = 20.sp
                        )
                    }
                }

                // Real-world Example
                Card(
                    colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderHairline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "REAL-WORLD SCENARIO",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = definition.realWorldExample,
                            fontSize = 13.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }

                // FinSense Recommendation Tip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(StatusSuccessBg)
                        .padding(12.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = StatusSuccess,
                        modifier = Modifier.size(18.dp)
                    )
                    Column {
                        Text(
                            text = "FinSense Advice",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusSuccess
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = definition.finSenseRecommendation,
                            fontSize = 12.sp,
                            color = TextPrimary,
                            lineHeight = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
