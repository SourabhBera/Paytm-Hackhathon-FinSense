package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Speaker
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.IndicLanguage
import com.example.service.SarvamAiService
import com.example.service.SarvamTtsResult
import com.example.service.VoiceSpeaker
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.FinSenseCyan
import com.example.ui.theme.FinSenseNavy
import com.example.ui.theme.FinSenseSurface
import com.example.ui.theme.FinSenseSurfaceHigh
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

data class VoicePreset(
    val title: String,
    val languageCode: String,
    val speakerId: String,
    val text: String,
    val badge: String
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SarvamIndicVoiceSheet(
    initialText: String? = null,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        SarvamAiService.initNativeTts(context)
    }

    DisposableEffect(Unit) {
        onDispose {
            SarvamAiService.stopPlayback()
        }
    }

    val presets = remember {
        listOf(
            VoicePreset(
                title = "Paytm Soundbox QR",
                languageCode = "hi-IN",
                speakerId = "shubh",
                text = "पेटीएम पर बारह हजार चार सौ रुपये प्राप्त हुए। राजेश इलेक्ट्रॉनिक्स के लिए वर्किंग कैपिटल लोन उपलब्ध है।",
                badge = "Soundbox Alert"
            ),
            VoicePreset(
                title = "Hindi Working Capital",
                languageCode = "hi-IN",
                speakerId = "shubh",
                text = "राजेश इलेक्ट्रॉनिक्स, आपकी दुकान के लिए ₹1,50,000 का वर्किंग कैपिटल लोन 2 मिनट में स्वीकृत है। दैनिक कटौती केवल ₹83।",
                badge = "Merchant Credit"
            ),
            VoicePreset(
                title = "Tamil Health & Tax",
                languageCode = "ta-IN",
                speakerId = "priya",
                text = "உங்கள் குடும்பத்திற்கான ₹10 லட்சம் மருத்துவக் காப்பீடு மற்றும் பிரிவு 80D கீழ் ₹7,500 வரி சேமிப்பு தயார்.",
                badge = "Family Cover"
            ),
            VoicePreset(
                title = "Bengali Smart Savings",
                languageCode = "bn-IN",
                speakerId = "priya",
                text = "স্মার্ট সেভিংস অটো-সুইপের মাধ্যমে আপনার অব্যবহৃত ব্যালেন্স থেকে পান ৭.৪% নিশ্চিত সুদ।",
                badge = "Auto-Sweep"
            ),
            VoicePreset(
                title = "English Soundbox Summary",
                languageCode = "en-IN",
                speakerId = "rahul",
                text = "Payment received on Paytm Soundbox. Weekend nodal settlement sweep completed via instant IMPS.",
                badge = "Settlement"
            )
        )
    }

    var selectedLanguage by remember {
        mutableStateOf(SarvamAiService.supportedLanguages.first())
    }
    var selectedSpeaker by remember {
        mutableStateOf(SarvamAiService.supportedSpeakers.first())
    }
    var customText by remember {
        mutableStateOf(initialText ?: presets.first().text)
    }

    var isSynthesizing by remember { mutableStateOf(false) }
    var isPlayingAudio by remember { mutableStateOf(false) }
    var lastResult by remember { mutableStateOf<SarvamTtsResult?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("sarvam_voice_sheet")
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(FinSenseNavy)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = null,
                            tint = FinSenseCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Sarvam AI Indic Voice Studio",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(StatusSuccessBg)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Bulbul v3",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusSuccess
                                )
                            }
                        }
                        Text(
                            text = "India's Sovereign Voice AI • Paytm Soundbox & Copilot TTS",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp).testTag("close_sarvam_sheet")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Preset Cards
            Text(
                text = "PAYTM SOUNDBOX & JOURNEY AUDIO PRESETS",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(presets) { preset ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (customText == preset.text) Color(0xFFE8F6FE) else FinSenseSurface
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .width(220.dp)
                            .border(
                                1.dp,
                                if (customText == preset.text) FinSenseCyan else BorderHairline,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                customText = preset.text
                                selectedLanguage = SarvamAiService.supportedLanguages.find { it.code == preset.languageCode }
                                    ?: selectedLanguage
                                selectedSpeaker = SarvamAiService.supportedSpeakers.find { it.id == preset.speakerId }
                                    ?: selectedSpeaker
                            }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = preset.badge,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FinSenseNavy
                                )
                                Text(
                                    text = preset.title,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextMuted
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = preset.text,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Indic Language Selector
            Text(
                text = "SELECT INDIC LANGUAGE (10 SUPPORTED)",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(SarvamAiService.supportedLanguages) { lang ->
                    val isSelected = selectedLanguage.code == lang.code
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) FinSenseNavy else FinSenseSurface)
                            .border(
                                1.dp,
                                if (isSelected) FinSenseNavy else BorderHairline,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedLanguage = lang }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = lang.nativeName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else TextPrimary)
                            Text(text = "(${lang.name})", fontSize = 10.sp, color = if (isSelected) Color(0xFFB0C4DE) else TextMuted)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Speaker Persona Selector
            Text(
                text = "SARVAM VOICE PERSONA (BULBUL v3)",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SarvamAiService.supportedSpeakers.forEach { speaker ->
                    val isSelected = selectedSpeaker.id == speaker.id
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) FinSenseCyan.copy(alpha = 0.15f) else FinSenseSurface)
                            .border(
                                1.dp,
                                if (isSelected) FinSenseCyan else BorderHairline,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedSpeaker = speaker }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(
                                imageVector = Icons.Default.Speaker,
                                contentDescription = null,
                                tint = if (isSelected) FinSenseNavy else TextMuted,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = speaker.displayName,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) FinSenseNavy else TextPrimary
                            )
                            Text(
                                text = "• ${speaker.gender}",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Text Input / Script Area
            Text(
                text = "VOICE ANNOUNCEMENT SCRIPT",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = customText,
                onValueChange = { customText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .testTag("sarvam_script_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = FinSenseSurface,
                    unfocusedContainerColor = FinSenseSurface,
                    focusedBorderColor = FinSenseCyan,
                    unfocusedBorderColor = BorderHairline
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, lineHeight = 18.sp),
                placeholder = { Text("Type custom financial announcement or question in any Indian language...", color = TextMuted, fontSize = 12.sp) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Playback and Waveform Card
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FinSenseNavy),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = FinSenseCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Soundbox Audio Engine",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = lastResult?.sampleRate ?: "24000 Hz • Bulbul v3",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFB0C4DE),
                            fontSize = 10.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Simulated / Active Waveform Visualizer
                    WaveformVisualizer(isPlaying = isPlayingAudio)

                    Spacer(modifier = Modifier.height(14.dp))

                    // Telemetry and status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isPlayingAudio) "▶ Playing Soundbox voice..."
                            else if (lastResult != null) lastResult?.message ?: "Ready"
                            else "Ready for synthesis",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isPlayingAudio) FinSenseCyan else Color(0xFFD2DEEF),
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        if (lastResult != null) {
                            Text(
                                text = "Latency: ${lastResult?.latencyMs}ms",
                                style = MaterialTheme.typography.bodySmall,
                                color = StatusSuccess,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action Buttons (Synthesize & Play / Stop)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                if (isPlayingAudio) {
                                    SarvamAiService.stopPlayback()
                                    isPlayingAudio = false
                                } else {
                                    scope.launch {
                                        isSynthesizing = true
                                        val result = SarvamAiService.synthesizeSpeech(
                                            context = context,
                                            text = customText,
                                            targetLanguageCode = selectedLanguage.code,
                                            speaker = selectedSpeaker.id
                                        )
                                        lastResult = result
                                        isSynthesizing = false

                                        SarvamAiService.playAudio(
                                            context = context,
                                            result = result,
                                            fallbackText = customText,
                                            targetLanguageCode = selectedLanguage.code,
                                            onPlaybackStarted = { isPlayingAudio = true },
                                            onPlaybackCompleted = { isPlayingAudio = false }
                                        )
                                    }
                                }
                            },
                            enabled = !isSynthesizing && customText.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isPlayingAudio) Color(0xFFFF5252) else FinSenseCyan
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("synthesize_play_button")
                        ) {
                            if (isSynthesizing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Synthesizing Bulbul...",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            } else if (isPlayingAudio) {
                                Icon(
                                    imageVector = Icons.Default.Stop,
                                    contentDescription = "Stop",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Stop Audio",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Synthesize & Play (${selectedLanguage.name})",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Footer note
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF0F4F8))
                    .padding(10.dp)
            ) {
                Text(
                    text = "🇮🇳 Sarvam AI Bulbul v3 supports 10+ Indian languages with natural code-mixing (Hinglish/Tanglish). Designed for zero-latency Paytm Soundbox and FinSense Vernacular Copilots.",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp,
                    color = TextMuted,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun WaveformVisualizer(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")

    val bar1 by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b1"
    )
    val bar2 by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.7f,
        animationSpec = infiniteRepeatable(tween(350, delayMillis = 50, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b2"
    )
    val bar3 by infiniteTransition.animateFloat(
        initialValue = 0.15f, targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(500, delayMillis = 100, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b3"
    )
    val bar4 by infiniteTransition.animateFloat(
        initialValue = 0.25f, targetValue = 0.85f,
        animationSpec = infiniteRepeatable(tween(420, delayMillis = 150, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b4"
    )
    val bar5 by infiniteTransition.animateFloat(
        initialValue = 0.1f, targetValue = 0.65f,
        animationSpec = infiniteRepeatable(tween(380, delayMillis = 80, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b5"
    )

    val heights = if (isPlaying) {
        listOf(bar1, bar2, bar3, bar4, bar5, bar2, bar4, bar3, bar1, bar5, bar3, bar2, bar4, bar1, bar3)
    } else {
        listOf(0.2f, 0.25f, 0.4f, 0.3f, 0.2f, 0.35f, 0.5f, 0.3f, 0.2f, 0.45f, 0.3f, 0.25f, 0.35f, 0.2f, 0.15f)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF0F1A30))
            .padding(horizontal = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        heights.forEach { fraction ->
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height((30 * fraction).dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        if (isPlaying) Brush.verticalGradient(listOf(Color(0xFF00E5FF), FinSenseCyan))
                        else Brush.verticalGradient(listOf(Color(0xFF5A738E), Color(0xFF384E66)))
                    )
            )
        }
    }
}
