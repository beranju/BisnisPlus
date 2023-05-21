package com.beran.bisnisplus.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.beran.bisnisplus.ui.navigation.Screen
import com.beran.bisnisplus.ui.screen.onboarding.FirstOnBoardingScreen
import com.beran.bisnisplus.ui.screen.onboarding.OnBoardViewModel
import com.beran.bisnisplus.ui.screen.onboarding.SecondOnBoardingScreen
import com.beran.bisnisplus.ui.screen.onboarding.ThirdOnBoardingScreen
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreen(viewModel: OnBoardViewModel, navController: NavHostController) {
    /**
     * pager digunakan untuk membuat slider
     * reff => https://developer.android.com/jetpack/compose/layouts/pager
     */
    Box(
        modifier = Modifier.padding(24.dp)
    ) {
        val pagerState = rememberPagerState()
        val coroutineScope = rememberCoroutineScope()
        val pageCount = 3

        HorizontalPager(pageCount = pageCount, state = pagerState) { page ->
            Box(modifier = Modifier.graphicsLayer {
                val pageOffset =
                    ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
                translationX = pageOffset * size.width
                alpha = 1 - pageOffset.absoluteValue
            }) {
                when (page) {
                    0 -> FirstOnBoardingScreen(
                        onNext = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        },
                        onSkip = {
                            viewModel.setOnBoardState(false)
                            navController.navigate(Screen.SignUp.route) {
                                popUpTo(Screen.SignUp.route) {
                                    inclusive = true
                                }
                            }
                        }
                    )

                    1 -> SecondOnBoardingScreen(
                        onNext = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(2)
                            }
                        },
                        onSkip = {
                            viewModel.setOnBoardState(false)
                            navController.navigate(Screen.SignUp.route) {
                                popUpTo(Screen.SignUp.route) {
                                    inclusive = true
                                }
                            }
                        }
                    )

                    2 -> ThirdOnBoardingScreen(onNext = {
                        viewModel.setOnBoardState(false)
                        navController.navigate(Screen.SignUp.route) {
                            popUpTo(Screen.SignUp.route) {
                                inclusive = true
                            }
                        }
                    })
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun OnBoardingScreenPrev() {
    BisnisPlusTheme {
        OnBoardingScreen(viewModel = koinViewModel(), navController = rememberNavController())
    }
}