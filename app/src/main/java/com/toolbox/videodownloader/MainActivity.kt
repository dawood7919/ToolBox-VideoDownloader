package com.toolbox.videodownloader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.toolbox.videodownloader.ui.AppNavGraph
import com.toolbox.videodownloader.ui.theme.ToolBoxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToolBoxTheme {
                AppNavGraph()
            }
        }
    }
}
