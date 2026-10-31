package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.model.AppScreen
import com.example.model.Persona
import com.example.ui.components.FinSenseBottomNav
import com.example.ui.components.FinSenseHeader
import com.example.ui.screens.FinSenseCopilotScreen
import com.example.ui.screens.FinSenseDashboardScreen
import com.example.ui.screens.HealthCoverDetailsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InsuranceDiscoveryScreen
import com.example.ui.screens.MerchantDashboardScreen
import com.example.ui.screens.MerchantIssueResolutionScreen
import com.example.ui.screens.MerchantSupportChatScreen
import com.example.ui.screens.PersonalActivityScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RecommendationDetailsScreen
import com.example.ui.screens.ShopInsuranceDetailsScreen
import com.example.ui.screens.SmartSavingsDetailsScreen
import com.example.ui.screens.WorkingCapitalLoansScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = androidx.activity.SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = androidx.activity.SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            MyApplicationTheme {
                FinSenseApp()
            }
        }
    }
}

@Composable
fun FinSenseApp() {
    val screenStack = remember { mutableStateListOf(AppScreen.HOME) }
    val currentScreen = screenStack.lastOrNull() ?: AppScreen.HOME

    // Global Persona: Switches globally without forcing navigation away from the active screen!
    var currentPersona by remember { mutableStateOf(Persona.PERSONAL) }
    var showBackendSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    fun navigateTo(screen: AppScreen) {
        if (screenStack.lastOrNull() != screen) {
            screenStack.add(screen)
        }
    }

    fun popBack() {
        if (screenStack.size > 1) {
            screenStack.removeAt(screenStack.lastIndex)
        } else if (screenStack.firstOrNull() != AppScreen.HOME) {
            screenStack.clear()
            screenStack.add(AppScreen.HOME)
        }
    }

    fun switchTab(screen: AppScreen) {
        screenStack.clear()
        screenStack.add(screen)
        // Keeps the user's active persona selection untouched across bottom tab navigation!
    }

    // Hardware / System Back Button Handler
    BackHandler(enabled = screenStack.size > 1 || currentScreen != AppScreen.HOME) {
        popBack()
    }

    val isSubScreen = currentScreen in listOf(
        AppScreen.RECOMMENDATION_DETAILS,
        AppScreen.INSURANCE_DISCOVERY,
        AppScreen.SMART_SAVINGS_DETAILS,
        AppScreen.SHOP_INSURANCE_DETAILS,
        AppScreen.MERCHANT_ISSUE_RESOLUTION,
        AppScreen.MERCHANT_SUPPORT_CHAT,
        AppScreen.WORKING_CAPITAL_LOANS,
        AppScreen.HEALTH_COVER_DETAILS,
        AppScreen.SOUNDBOX_UPGRADE_DETAILS
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = com.example.ui.theme.FinSenseCanvas,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            FinSenseHeader(
                currentPersona = currentPersona,
                onPersonaChange = { newPersona ->
                    // Global switch: Updates active persona without forcing a jump to another screen!
                    currentPersona = newPersona
                },
                title = when (currentScreen) {
                    AppScreen.RECOMMENDATION_DETAILS -> "Contextual Recommendation"
                    AppScreen.INSURANCE_DISCOVERY -> "Insurance Discovery"
                    AppScreen.SMART_SAVINGS_DETAILS -> "Smart Savings Auto-Sweep"
                    AppScreen.SHOP_INSURANCE_DETAILS -> "Shop & Stock Insurance"
                    AppScreen.MERCHANT_ISSUE_RESOLUTION -> "Merchant Support"
                    AppScreen.MERCHANT_SUPPORT_CHAT -> "Merchant AI Support"
                    AppScreen.WORKING_CAPITAL_LOANS -> "Working Capital"
                    AppScreen.HEALTH_COVER_DETAILS -> "Health & Tax Cover"
                    AppScreen.SOUNDBOX_UPGRADE_DETAILS -> "Soundbox Support"
                    else -> null
                },
                showBackButton = isSubScreen || currentScreen == AppScreen.COPILOT,
                onBackClick = { popBack() },
                onProfileClick = { navigateTo(AppScreen.PROFILE) },
                onCopilotClick = { navigateTo(AppScreen.COPILOT) },
                onBackendClick = { showBackendSheet = true },
                showPersonaToggle = !isSubScreen
            )
        },
        bottomBar = {
            FinSenseBottomNav(
                currentScreen = currentScreen,
                onTabSelected = { selectedTab ->
                    switchTab(selectedTab)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(com.example.ui.theme.FinSenseCanvas)
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { targetScreen ->
                when (targetScreen) {
                    AppScreen.HOME -> {
                        HomeScreen(
                            currentPersona = currentPersona,
                            onNavigate = { navigateTo(it) },
                            snackbarHostState = snackbarHostState
                        )
                    }

                    AppScreen.FINSENSE -> {
                        FinSenseDashboardScreen(
                            currentPersona = currentPersona,
                            onNavigate = { navigateTo(it) },
                            snackbarHostState = snackbarHostState
                        )
                    }

                    AppScreen.ACTIVITY -> {
                        com.example.ui.screens.FinancialJourneysHubScreen(
                            currentPersona = currentPersona,
                            onNavigate = { navigateTo(it) },
                            snackbarHostState = snackbarHostState
                        )
                    }

                    AppScreen.COPILOT -> {
                        FinSenseCopilotScreen(
                            currentPersona = currentPersona,
                            onViewPolicies = { navigateTo(AppScreen.INSURANCE_DISCOVERY) },
                            onApplyLoan = { navigateTo(AppScreen.WORKING_CAPITAL_LOANS) },
                            onExploreSavings = { navigateTo(AppScreen.SMART_SAVINGS_DETAILS) },
                            onViewMerchantSupport = { navigateTo(AppScreen.MERCHANT_SUPPORT_CHAT) }
                        )
                    }

                    AppScreen.RECOMMENDATION_DETAILS -> {
                        RecommendationDetailsScreen(
                            onBack = { popBack() },
                            onExploreInsurance = { navigateTo(AppScreen.INSURANCE_DISCOVERY) },
                            snackbarHostState = snackbarHostState
                        )
                    }

                    AppScreen.INSURANCE_DISCOVERY -> {
                        InsuranceDiscoveryScreen(
                            onBack = { popBack() },
                            snackbarHostState = snackbarHostState
                        )
                    }

                    AppScreen.SMART_SAVINGS_DETAILS -> {
                        SmartSavingsDetailsScreen(
                            onBack = { popBack() },
                            snackbarHostState = snackbarHostState
                        )
                    }

                    AppScreen.SHOP_INSURANCE_DETAILS -> {
                        ShopInsuranceDetailsScreen(
                            onBack = { popBack() },
                            snackbarHostState = snackbarHostState
                        )
                    }

                    AppScreen.MERCHANT_ISSUE_RESOLUTION -> {
                        MerchantIssueResolutionScreen(
                            onBack = { popBack() },
                            onOpenMerchantAi = { navigateTo(AppScreen.MERCHANT_SUPPORT_CHAT) },
                            snackbarHostState = snackbarHostState
                        )
                    }

                    AppScreen.MERCHANT_SUPPORT_CHAT -> {
                        MerchantSupportChatScreen(
                            onBack = { popBack() },
                            snackbarHostState = snackbarHostState
                        )
                    }

                    AppScreen.WORKING_CAPITAL_LOANS -> {
                        WorkingCapitalLoansScreen(
                            onBack = { popBack() },
                            snackbarHostState = snackbarHostState
                        )
                    }

                    AppScreen.HEALTH_COVER_DETAILS -> {
                        HealthCoverDetailsScreen(
                            onBack = { popBack() },
                            snackbarHostState = snackbarHostState
                        )
                    }

                    AppScreen.SOUNDBOX_UPGRADE_DETAILS -> {
                        MerchantIssueResolutionScreen(
                            onBack = { popBack() },
                            onOpenMerchantAi = { navigateTo(AppScreen.COPILOT) },
                            snackbarHostState = snackbarHostState
                        )
                    }

                    AppScreen.PROFILE -> {
                        ProfileScreen(
                            currentPersona = currentPersona,
                            onPersonaChange = { newPersona ->
                                currentPersona = newPersona
                            },
                            snackbarHostState = snackbarHostState
                        )
                    }
                }
            }
        }

        if (showBackendSheet) {
            com.example.ui.components.SupabaseBackendSheet(
                onDismissRequest = { showBackendSheet = false }
            )
        }
    }
}
