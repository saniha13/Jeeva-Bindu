package com.example.jeeva_bindu.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jeeva_bindu.utils.JeevaRed

@Composable
fun EmergencyAlertCard(
    hospital: String,
    group: String,
    time: String,
    isResponded: Boolean,
    onRespond: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isResponded) Color.DarkGray else JeevaRed
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "URGENT $group NEEDED",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    time,
                    color = Color.White
                )
            }

            Text(
                "At $hospital",
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (!isResponded) {
                Button(
                    onClick = { onRespond() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "I'M COMING",
                        color = JeevaRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Text(
                    "✅ RESPONSE SENT: Thank you donor!",
                    color = Color.Green,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
