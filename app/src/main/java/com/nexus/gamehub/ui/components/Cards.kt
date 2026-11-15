package com.nexus.gamehub.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nexus.gamehub.ui.theme.*
@Composable fun NeonCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) = Column(
    modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Card)
        .border(1.dp, Brush.linearGradient(listOf(Neon.copy(.6f), Pink.copy(.6f))), RoundedCornerShape(16.dp)).padding(16.dp), content = content)
@Composable fun StatBar(label: String, pct: Int, value: String, color: Color = Neon) = Column(Modifier.padding(vertical = 6.dp)) {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) { Text(label, color = Color.LightGray); Text(value, color = color) }
    Spacer(Modifier.height(4.dp))
    LinearProgressIndicator(progress = { pct.coerceIn(0, 100) / 100f }, modifier = Modifier.fillMaxWidth().height(8.dp), color = color, trackColor = Bg)
}
@Composable fun Title(t: String) = Text(t, style = MaterialTheme.typography.headlineSmall, color = Neon, modifier = Modifier.padding(bottom = 12.dp))
