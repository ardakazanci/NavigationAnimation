package com.example.navigationanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInOutBounce
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.navigationanimation.ui.theme.NavigationAnimationTheme
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationAnimationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationSample(

                    )
                }
            }
        }
    }
}

@Composable
fun NavigationSample() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = "landing",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable("landing") {
            ScreenLanding(
                onNavigateToDetail = { photoUrl ->
                    val encodedUrl = URLEncoder.encode(photoUrl, StandardCharsets.UTF_8.toString())
                    navController.navigate("detail/$encodedUrl")
                }
            )
        }

        composable(
            "detail/{photoUrl}",
            arguments = listOf(navArgument("photoUrl") { type = NavType.StringType }),
            enterTransition = {
                scaleIn(
                    initialScale = 0.6f,
                    animationSpec = keyframes {
                        durationMillis = 1500
                        0.6f at 0 using FastOutSlowInEasing
                        1.1f at 500 using EaseInOutBounce
                        1.0f at 1000
                    }
                ) + fadeIn(
                    animationSpec = tween(
                        1500, easing = FastOutSlowInEasing
                    )
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(
                        1000, easing = FastOutSlowInEasing
                    )
                ) + fadeOut(
                    animationSpec = tween(
                        1000, easing = FastOutSlowInEasing
                    )
                )
            }
        ) { backStackEntry ->
            val photoUrl = backStackEntry.arguments?.getString("photoUrl")?.let {
                java.net.URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            } ?: ""
            ScreenDetails(navController = navController)
        }
    }
}

@Composable
fun ScreenLanding(onNavigateToDetail: (String) -> Unit) {
    val backgroundColor = MaterialTheme.colorScheme.primaryContainer
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .background(backgroundColor)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            onNavigateToDetail("https://example.com/photo.jpg")
        }) {
            Text(text = "Go to Detail Screen")
        }
    }
}



@Composable
fun ScreenDetails(navController: NavController) {
    val backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Detail Screen", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Go Back",
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}