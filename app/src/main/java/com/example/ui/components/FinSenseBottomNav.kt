package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.FinSenseCyan
import com.example.ui.theme.TextMuted

import androidx.compose.material.icons.filled.Explore

sealed class NavTab(val screen: AppScreen, val label: String, val icon: ImageVector, val tag: String) {
    object Home : NavTab(AppScreen.HOME, "Home", Icons.Default.Home, "bottom_nav_home")
    object Activity : NavTab(AppScreen.ACTIVITY, "Journeys", Icons.Default.Explore, "bottom_nav_journeys")
    object FinSense : NavTab(AppScreen.FINSENSE, "FinSense", Icons.Default.AutoAwesome, "bottom_nav_finsense")
    object Profile : NavTab(AppScreen.PROFILE, "Profile", Icons.Default.Person, "bottom_nav_profile")
}

@Composable
fun FinSenseBottomNav(
    currentScreen: AppScreen,
    onTabSelected: (AppScreen) -> Unit
) {
    val tabs = listOf(
        NavTab.Home,
        NavTab.Activity,
        NavTab.FinSense,
        NavTab.Profile
    )

    Surface(
        color = Color.White,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = BorderHairline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val isSelected = when (tab.screen) {
                    AppScreen.HOME -> currentScreen == AppScreen.HOME
                    AppScreen.ACTIVITY -> currentScreen == AppScreen.ACTIVITY ||
                            currentScreen == AppScreen.MERCHANT_ISSUE_RESOLUTION ||
                            currentScreen == AppScreen.MERCHANT_SUPPORT_CHAT ||
                            currentScreen == AppScreen.WORKING_CAPITAL_LOANS
                    AppScreen.FINSENSE -> currentScreen == AppScreen.FINSENSE ||
                            currentScreen == AppScreen.COPILOT ||
                            currentScreen == AppScreen.RECOMMENDATION_DETAILS ||
                            currentScreen == AppScreen.INSURANCE_DISCOVERY
                    AppScreen.PROFILE -> currentScreen == AppScreen.PROFILE
                    else -> false
                }

                val iconColor = if (isSelected) FinSenseCyan else TextMuted
                val textColor = if (isSelected) FinSenseCyan else TextMuted
                val weight = if (isSelected) FontWeight.Bold else FontWeight.Medium

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(CircleShape)
                        .clickable { onTabSelected(tab.screen) }
                        .testTag(tab.tag)
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label,
                        tint = iconColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = tab.label,
                        fontSize = 11.sp,
                        fontWeight = weight,
                        color = textColor,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
