package com.app.colorpalette

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.colorpalette.ui.theme.ColorPaletteTheme
import com.app.colorpicker.ColorPickerDialog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ColorPaletteTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.statusBars.asPaddingValues())) { _ ->

                    var showColorPicker by remember { mutableStateOf(false) }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎨 Color Palette Picker",
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Pick a color for your theme, UI customization, backgrounds, or user inputs. Supports Material shades and live previews.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(onClick = { showColorPicker = true }) {
                            Text("Open Color Picker")
                        }
                        if (showColorPicker) {
                            ColorPickerDialog(
                                showColorValues = true,
                                onDismissRequest = { showColorPicker = false },
                                getColorHexCode = { hex, rgb ->
                                    showColorPicker = false
                                }
                            )
                        }
                    }

                 }
            }
        }
    }
}



@Preview
@Composable
fun PreView() {
    ColorPaletteTheme {
        MainActivity()
    }
}







