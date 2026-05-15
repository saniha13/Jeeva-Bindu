package com.example.jeeva_bindu.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jeeva_bindu.components.EmergencyAlertCard
import com.example.jeeva_bindu.utils.JeevaGreen
import com.example.jeeva_bindu.utils.isMedicallyCompatible
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun HomeScreen(
    name: String,
    age: String,
    group: String,
    loc: String,
    lastDonated: LocalDate,
    respondedIds: MutableList<String>
) {

    val daysSince = ChronoUnit.DAYS.between(
        lastDonated,
        LocalDate.now()
    )

    val isEligible = daysSince >= 90

    val allEmergencies = listOf(

        mapOf(
            "id" to "1",
            "hosp" to "Taluka Govt Hospital",
            "group" to "O-",
            "time" to "2m ago"
        ),

        mapOf(
            "id" to "2",
            "hosp" to "City Medical Center",
            "group" to "B+",
            "time" to "5m ago"
        ),

        mapOf(
            "id" to "3",
            "hosp" to "Panchayat Clinic",
            "group" to "AB+",
            "time" to "10m ago"
        ),

        mapOf(
            "id" to "4",
            "hosp" to "District Hospital",
            "group" to "A-",
            "time" to "1h ago"
        )
    )

    val compatibleEmergencies = allEmergencies.filter {

        isMedicallyCompatible(
            donorGroup = group,
            patientGroup = it["group"]!!
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            "Welcome, $name",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Your Group: $group | Eligibility: $daysSince Days",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),

            colors = CardDefaults.cardColors(
                containerColor = if (isEligible)
                    JeevaGreen
                else
                    Color.Gray
            )
        ) {

            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    "STATUS: ${
                        if (isEligible)
                            "READY TO DONATE"
                        else
                            "RECOVERING"
                    }",

                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    "You are compatible with ${compatibleEmergencies.size} active requests",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Compatible Requests in $loc",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Text(
            "Showing only emergencies you can medically assist",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (compatibleEmergencies.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    "No matching emergencies for your group.",
                    color = Color.Gray
                )
            }
        }

        LazyColumn {

            items(compatibleEmergencies) { item ->

                EmergencyAlertCard(
                    hospital = item["hosp"]!!,
                    group = item["group"]!!,
                    time = item["time"]!!,

                    isResponded = respondedIds.contains(item["id"]),

                    onRespond = {
                        respondedIds.add(item["id"]!!)
                    }
                )
            }
        }
    }
}
