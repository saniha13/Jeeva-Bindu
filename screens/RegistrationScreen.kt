package com.example.jeeva_bindu.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jeeva_bindu.utils.JeevaRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    onRegister: (String, String, String, String) -> Unit
) {

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    var selectedGroup by remember { mutableStateOf("O-") }
    var expanded by remember { mutableStateOf(false) }

    val bloodGroups = listOf(
        "A+",
        "A-",
        "B+",
        "B-",
        "AB+",
        "AB-",
        "O+",
        "O-"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            "Jeeva-Bindu",
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            color = JeevaRed
        )

        Text(
            "Rapid Response Network",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth()) {

            OutlinedTextField(
                value = selectedGroup,
                onValueChange = {},
                label = { Text("Your Blood Group") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,

                trailingIcon = {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        null,
                        Modifier.clickable {
                            expanded = true
                        }
                    )
                }
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable {
                        expanded = true
                    }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {

                bloodGroups.forEach { group ->

                    DropdownMenuItem(
                        text = { Text(group) },

                        onClick = {
                            selectedGroup = group
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location (Taluka)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (name.isNotEmpty()) {
                    onRegister(
                        name,
                        age,
                        selectedGroup,
                        location
                    )
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = JeevaRed
            ),

            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                "REGISTER",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
