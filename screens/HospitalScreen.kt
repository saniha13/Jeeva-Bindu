package com.example.jeeva_bindu.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jeeva_bindu.utils.JeevaRed

@Composable
fun HospitalScreen(loc: String) {

    val context = LocalContext.current

    val hospitals = listOf(
        mapOf(
            "name" to "Taluka Govt Hospital",
            "contact" to "+91 9876543210",
            "address" to "Main Road, $loc"
        ),

        mapOf(
            "name" to "Panchayat Health Center",
            "contact" to "+91 8877665544",
            "address" to "Station Area, $loc"
        ),

        mapOf(
            "name" to "City Blood Bank",
            "contact" to "+91 9900112233",
            "address" to "Market Square, $loc"
        )
    )

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            "Taluka Directory",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {

            items(hospitals) { hospital ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),

                    elevation = CardDefaults.cardElevation(2.dp)
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            hospital["name"]!!,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Text(
                            "📞 ${hospital["contact"]}",
                            color = JeevaRed,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = {

                                val mapUri = Uri.parse(
                                    "geo:0,0?q=${hospital["name"]} ${hospital["address"]}"
                                )

                                val mapIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    mapUri
                                ).apply {
                                    setPackage("com.google.android.apps.maps")
                                }

                                context.startActivity(mapIntent)
                            },

                            modifier = Modifier.fillMaxWidth(),

                            border = BorderStroke(
                                1.dp,
                                JeevaRed
                            )
                        ) {

                            Icon(
                                Icons.Default.Place,
                                null,
                                tint = JeevaRed
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                "VIEW ON MAP",
                                color = JeevaRed
                            )
                        }
                    }
                }
            }
        }
    }
}
