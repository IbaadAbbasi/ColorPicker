package com.app.colorpicker


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.app.colorpicker.ext.fullMaterialColorMap
import com.app.colorpicker.ext.materialShades


@Composable
fun ColorPickerScreen(
    showColorValues: Boolean = true,
    onOkClick: (String, String) -> Unit,
) {
    var hue by remember { mutableFloatStateOf(0f) }
    var saturation by remember { mutableFloatStateOf(1f) }
    var brightness by remember { mutableFloatStateOf(1f) }
    var alpha by remember { mutableFloatStateOf(1f) }

    var currentColor by remember { mutableStateOf(Color.Red) }
    var previousColor by remember { mutableStateOf(Color.Red) }

    var selectedShade by remember { mutableStateOf("500") }

    val color = com.app.colorpicker.ext.hsvToColor(hue, saturation, brightness, alpha)
    val hex = com.app.colorpicker.ext.colorToHex(color)
    val rgb = listOf(
        (color.red * 255).toInt(),
        (color.green * 255).toInt(),
        (color.blue * 255).toInt()
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize().padding(16.dp)

    )
    {
        item { Spacer(Modifier.height(8.dp)) }
        // Material Shades
        item {
            Text("Material Shades", style = MaterialTheme.typography.titleMedium)
        }
        item { Spacer(Modifier.height(8.dp)) }
        val chunks = materialShades.chunked(5)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            ) {
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth()
                    ) {
                        chunks.forEach { rowShades ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                rowShades.forEach { shade ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                if (selectedShade == shade) Color.Black else Color.LightGray,
                                                RoundedCornerShape(4.dp)
                                            )
                                            .clickable { selectedShade = shade }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = shade,
                                            color = if (selectedShade == shade) Color.White else Color.Black
                                        )
                                    }
                                }
                                // Fill remaining space if less than 5 items
                                repeat(5 - rowShades.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
        }
        item { Spacer(Modifier.height(8.dp)) }

        item {
            Text("Predefined Material Colors", style = MaterialTheme.typography.titleMedium)

        }
        item { Spacer(Modifier.height(8.dp)) }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            ) {
                MaterialColorSwatches(defaultShade = selectedShade) {
                    previousColor = currentColor
                    currentColor = it
                    val hsv = FloatArray(3)
                    android.graphics.Color.colorToHSV(it.toArgb(), hsv)
                    hue = hsv[0]
                    saturation = hsv[1]
                    brightness = hsv[2]
                    alpha = it.alpha
                }
            }
        }
        item { Spacer(Modifier.height(8.dp)) }
        item {
            Text("Hue-Saturation Picker", style = MaterialTheme.typography.titleMedium)
        }
        item { Spacer(Modifier.height(8.dp)) }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                HueSaturationBox(hue, saturation) { newHue, newSaturation ->
                    hue = newHue
                    saturation = newSaturation
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
        item {
            Text("Brightness (Color Percentage)", style = MaterialTheme.typography.titleMedium)
        }
        item {
            Slider(
                modifier = Modifier.padding(top = 0.dp),
                value = brightness,
                onValueChange = { brightness = it },
                valueRange = 0f..1f
            )
        }
        // item {Spacer(Modifier.height(8.dp))}
        item {
            Text("Alpha: ${(alpha * 100).toInt()}%", style = MaterialTheme.typography.titleMedium)
        }
        item {

            Slider(
                modifier = Modifier.padding(top = 0.dp),
                value = alpha,
                onValueChange = { alpha = it },
                valueRange = 0f..1f
            )
        }
        // Color Values
        item {
            if (showColorValues) {
                Text("Color Values", style = MaterialTheme.typography.titleMedium)
                ColorPreview(color = color, hex = hex, rgb = rgb)
            }
        }
        // Done Button

        item {
            Button(
                onClick = { onOkClick.invoke(hex, rgb.toString()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Done")
            }

        }

    }
}


@Composable
fun ColorPickerDialog(
    showColorValues: Boolean = true,
    onDismissRequest: () -> Unit,
    getColorHexCode: (hex: String, rgb: String) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        // This Box fills the entire screen without padding
        Box(
            modifier = Modifier
                .wrapContentSize().padding(8.dp).clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.background) // or your custom background
        ) {
            ColorPickerScreen(
                showColorValues = showColorValues,
                onOkClick = { hex, rgb ->
                    getColorHexCode(hex, rgb)
                    onDismissRequest()
                }
            )
        }
    }
}



@Composable
fun HueSaturationBox(
    hue: Float,
    saturation: Float,
    onChange: (Float, Float) -> Unit
) {
    val boxSize = 200.dp

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(boxSize)
            //  .border(1.dp, Color.Gray)
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val x = (change.position.x / size.width).coerceIn(0f, 1f)
                    val y = (change.position.y / size.height).coerceIn(0f, 1f)
                    onChange(x * 360f, 1f - y)
                }
            }
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            val gradient = Brush.horizontalGradient(
                colors = (0..360 step 15).map { Color.hsv(it.toFloat(), 1f, 1f) }
            )
            drawRect(brush = gradient)
            drawRect(
                brush = Brush.verticalGradient(listOf(Color.Transparent, Color.Black)),
                blendMode = BlendMode.Multiply
            )

            // Draw the selector pointer
            val x = (hue / 360f).coerceIn(0f, 1f) * size.width
            val y = (1f - saturation).coerceIn(0f, 1f) * size.height

            drawCircle(
                color = Color.White,
                radius = 10f,
                center = Offset(x, y),
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}

@Composable
fun ColorPreview(color: Color, hex: String, rgb: List<Int>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(
                Modifier
                    .size(48.dp)
                    .background(color)
            )
        }
        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                "HEX: $hex",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                "RGB: ${rgb.joinToString(", ")}",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun MaterialColorSwatches(
    defaultShade: String = "500",
    onSelect: (Color) -> Unit
) {
    val colorEntries = fullMaterialColorMap.mapNotNull { (_, shades) ->
        shades[defaultShade]
    }
    val colorChunks = colorEntries.chunked(8)
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        colorChunks.forEach { rowColors ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                rowColors.forEach { color ->
                    Box(
                        modifier = Modifier.size(32.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(color)
                            //  .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                            .clickable { onSelect(color) }
                    )
                }
                repeat(4 - rowColors.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Column {
        ColorPickerScreen(onOkClick = { _, _ -> }
        )
    }
}



