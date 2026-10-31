package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Persona
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.FinSenseCyan
import com.example.ui.theme.FinSenseNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

import androidx.compose.material.icons.filled.Storage
import androidx.compose.runtime.collectAsState
import com.example.backend.BackendConnectionStatus
import com.example.backend.SupabaseRepository

const val PAYTM_LOGO_URL =
    "https://lh3.googleusercontent.com/aida-public/AB6AXuAV0_GdfTNAuZ7m-Xnsr3q3c5K-7vr-ntCxCAMOMtIdURxekdyWc0irWowvl1kYsV2GYmUajQVNslyFRRRDMnmTd8xeSPKAfFOeLbY-vmefT74GLlduZ2tA78k2ubCNNhqP5WejeDMCZX3bAIkJFeF7WtSctyxhS5HfLX3IEcaLI_jXPiwRRVRT2fwR-XuosCwfmApKGiAhjC-pJMAapJrLOveCenP_yYVN656zIroI"

const val PROFILE_AVATAR_URL =
    "https://lh3.googleusercontent.com/aida-public/AB6AXuB-5HogHoe6gcobFR_sQqe-Hhd5SlBnrmXDej5ZL3GzmRi-xIh7fDOJPie_3SS488BH6HVYpybdETwdrGFDBYm6T86kW-HSEwpwAZ_1ILsqlW68vTOPvJDaK03k6uZWdSU87K41gVL0qHGKNiHNwMweW2gyXCpK9CEgCKfn_NfsjhThtaJvDft46TG0v9ApVZ-4BsBcA-Rrmtt-3pgL1mkZ2PiGEBofoC1mmHgQxH4t"

@Composable
fun FinSenseHeader(
    currentPersona: Persona,
    onPersonaChange: (Persona) -> Unit,
    title: String? = null,
    showBackButton: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    onProfileClick: () -> Unit = {},
    onCopilotClick: () -> Unit = {},
    onBackendClick: () -> Unit = {},
    showPersonaToggle: Boolean = true
) {
    val backendStatus by SupabaseRepository.status.collectAsState()
    Surface(
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = BorderHairline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding() // Ensures header is positioned under system status bar
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Top Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    if (showBackButton && onBackClick != null) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .testTag("header_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Go back",
                                tint = TextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    // Paytm Logo
                    AsyncImage(
                        model = PAYTM_LOGO_URL,
                        contentDescription = "Paytm Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(24.dp)
                            .width(72.dp)
                    )

                    if (title != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Actions: Copilot, Notifications & Avatar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Supabase Cloud Backend Status & Config Button
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                when (backendStatus) {
                                    BackendConnectionStatus.CONNECTED_LIVE -> Color(0xFF3ECF8E).copy(alpha = 0.15f)
                                    BackendConnectionStatus.STANDALONE_SUBMISSION -> Color(0xFF1565C0).copy(alpha = 0.10f)
                                    else -> Color.LightGray.copy(alpha = 0.25f)
                                }
                            )
                            .border(
                                1.dp,
                                when (backendStatus) {
                                    BackendConnectionStatus.CONNECTED_LIVE -> Color(0xFF3ECF8E)
                                    BackendConnectionStatus.STANDALONE_SUBMISSION -> Color(0xFF1565C0).copy(alpha = 0.35f)
                                    else -> Color.Gray.copy(alpha = 0.3f)
                                },
                                CircleShape
                            )
                            .clickable { onBackendClick() }
                            .testTag("header_supabase_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = "Supabase Cloud Backend",
                            tint = when (backendStatus) {
                                BackendConnectionStatus.CONNECTED_LIVE -> Color(0xFF1E8E5A)
                                BackendConnectionStatus.STANDALONE_SUBMISSION -> Color(0xFF1565C0)
                                else -> TextPrimary
                            },
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    // FinSense Copilot Quick Jump Button
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(FinSenseCyan.copy(alpha = 0.12f))
                            .border(1.dp, FinSenseCyan.copy(alpha = 0.35f), CircleShape)
                            .clickable { onCopilotClick() }
                            .testTag("header_copilot_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "FinSense Copilot AI",
                            tint = FinSenseCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .clickable { /* Notification tap */ }
                            .testTag("notification_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = TextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        // Notification unread indicator
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 7.dp, end = 7.dp)
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(FinSenseCyan)
                        )
                    }

                    // Profile Avatar
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, FinSenseCyan, CircleShape)
                            .clickable { onProfileClick() }
                            .testTag("profile_avatar_button")
                    ) {
                        AsyncImage(
                            model = PROFILE_AVATAR_URL,
                            contentDescription = "Profile Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Global Persona Switcher Bar (Personal vs Business)
            if (showPersonaToggle) {
                Spacer(modifier = Modifier.height(8.dp))
                PersonaSwitch(
                    currentPersona = currentPersona,
                    onPersonaChange = onPersonaChange
                )
            }
        }
    }
}

@Composable
fun PersonaSwitch(
    currentPersona: Persona,
    onPersonaChange: (Persona) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(CircleShape)
            .background(Color(0xFFE8EDFF))
            .border(1.dp, Color(0xFFD2DEEF), CircleShape)
            .padding(3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isPersonal = currentPersona == Persona.PERSONAL
            val personalBg by animateColorAsState(
                targetValue = if (isPersonal) FinSenseCyan else Color.Transparent,
                label = "personalBg"
            )
            val personalText by animateColorAsState(
                targetValue = if (isPersonal) Color.White else TextPrimary,
                label = "personalText"
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(personalBg)
                    .clickable { onPersonaChange(Persona.PERSONAL) }
                    .testTag("persona_personal_tab")
            ) {
                Text(
                    text = "Personal",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (isPersonal) FontWeight.Bold else FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = personalText
                )
            }

            val isBusiness = currentPersona == Persona.BUSINESS
            val businessBg by animateColorAsState(
                targetValue = if (isBusiness) FinSenseCyan else Color.Transparent,
                label = "businessBg"
            )
            val businessText by animateColorAsState(
                targetValue = if (isBusiness) Color.White else TextPrimary,
                label = "businessText"
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(businessBg)
                    .clickable { onPersonaChange(Persona.BUSINESS) }
                    .testTag("persona_business_tab")
            ) {
                Text(
                    text = "Business",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (isBusiness) FontWeight.Bold else FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = businessText
                )
            }
        }
    }
}
