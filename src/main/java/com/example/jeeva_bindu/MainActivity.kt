package com.example.jeeva_bindu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.temporal.ChronoUnit

// --- 6. SUCCESS CRITERIA COLORS ---
val JeevaRed = Color(0xFFD32F2F)
val JeevaGreen = Color(0xFF2E7D32)
val JeevaGray = Color(0xFFF5F5F5)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                JeevaBinduApp()
            }
        }
    }
}

// --- MEDICAL COMPATIBILITY ENGINE ---
// This function determines if a donor can give blood to a specific patient group
fun isMedicallyCompatible(donorGroup: String, patientGroup: String): Boolean {
    return when (donorGroup) {
        "O-" -> true // Universal Donor: Can give to everyone
        "O+" -> listOf("O+", "A+", "B+", "AB+").contains(patientGroup)
        "A-" -> listOf("A-", "A+", "AB-", "AB+").contains(patientGroup)
        "A+" -> listOf("A+", "AB+").contains(patientGroup)
        "B-" -> listOf("B-", "B+", "AB-", "AB+").contains(patientGroup)
        "B+" -> listOf("B+", "AB+").contains(patientGroup)
        "AB-" -> listOf("AB-", "AB+").contains(patientGroup)
        "AB+" -> patientGroup == "AB+" // Can only give to AB+
        else -> false
    }
}

@Composable
fun JeevaBinduApp() {
    var isRegistered by rememberSaveable { mutableStateOf(false) }
    var userName by rememberSaveable { mutableStateOf("") }
    var userAge by rememberSaveable { mutableStateOf("") }
    var userBloodGroup by rememberSaveable { mutableStateOf("O-") }
    var userLocation by rememberSaveable { mutableStateOf("") }
    var currentTab by rememberSaveable { mutableIntStateOf(0) }

    val respondedAlertIds = remember { mutableStateListOf<String>() }
    val lastDonationDate = remember { LocalDate.now().minusDays(100) }

    Surface(modifier = Modifier.fillMaxSize(), color = JeevaGray) {
        if (!isRegistered) {
            RegistrationScreen(onRegister = { name, age, group, loc ->
                userName = name
                userAge = age
                userBloodGroup = group
                userLocation = loc
                isRegistered = true
            })
        } else {
            Scaffold(
                bottomBar = {
                    NavigationBar(containerColor = Color.White) {
                        NavigationBarItem(
                            selected = currentTab == 0,
                            onClick = { currentTab = 0 },
                            label = { Text("Home") },
                            icon = { Icon(Icons.Default.Home, null) },
                            colors = NavigationBarItemDefaults.colors(selectedIconColor = JeevaRed, selectedTextColor = JeevaRed)
                        )
                        NavigationBarItem(
                            selected = currentTab == 1,
                            onClick = { currentTab = 1 },
                            label = { Text("Hospitals") },
                            icon = { Icon(Icons.Default.LocationOn, null) },
                            colors = NavigationBarItemDefaults.colors(selectedIconColor = JeevaRed, selectedTextColor = JeevaRed)
                        )
                    }
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    if (currentTab == 0) {
                        HomeScreen(userName, userAge, userBloodGroup, userLocation, lastDonationDate, respondedAlertIds)
                    } else {
                        HospitalScreen(userLocation)
                    }
                }
            }
        }
    }
}

// --- TAB 1: HOME SCREEN (Filtered by Eligibility) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    name: String,
    age: String,
    group: String,
    loc: String,
    lastDonated: LocalDate,
    respondedIds: MutableList<String>
) {
    val daysSince = ChronoUnit.DAYS.between(lastDonated, LocalDate.now())
    val isEligible = daysSince >= 90

    // List of all active emergencies in the Taluka
    val allEmergencies = listOf(
        mapOf("id" to "1", "hosp" to "Taluka Govt Hospital", "group" to "O-", "time" to "2m ago"),
        mapOf("id" to "2", "hosp" to "City Medical Center", "group" to "B+", "time" to "5m ago"),
        mapOf("id" to "3", "hosp" to "Panchayat Clinic", "group" to "AB+", "time" to "10m ago"),
        mapOf("id" to "4", "hosp" to "District Hospital", "group" to "A-", "time" to "1h ago")
    )

    // --- REQUIREMENT 4.2: FILTER LOGIC (Medical Eligibility) ---
    // Only show requests where the registered donor is medically compatible
    val compatibleEmergencies = allEmergencies.filter {
        isMedicallyCompatible(donorGroup = group, patientGroup = it["group"]!!)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Welcome, $name", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Your Group: $group | Eligibility: $daysSince Days", color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))

        // Donor Health Eligibility Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = if (isEligible) JeevaGreen else Color.Gray)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("STATUS: ${if (isEligible) "READY TO DONATE" else "RECOVERING"}",
                    fontWeight = FontWeight.Bold, color = Color.White)
                Text("You are compatible with ${compatibleEmergencies.size} active requests", color = Color.White, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Compatible Requests in $loc", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text("Showing only emergencies you can medically assist", fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        if (compatibleEmergencies.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No matching emergencies for your group.", color = Color.Gray)
            }
        }

        LazyColumn {
            items(compatibleEmergencies) { item ->
                EmergencyAlertCard(
                    hospital = item["hosp"]!!,
                    group = item["group"]!!,
                    time = item["time"]!!,
                    isResponded = respondedIds.contains(item["id"]),
                    onRespond = { respondedIds.add(item["id"]!!) }
                )
            }
        }
    }
}

// --- TAB 2: HOSPITAL SCREEN (Directory & Google Maps) ---
@Composable
fun HospitalScreen(loc: String) {
    val context = LocalContext.current
    val hospitals = listOf(
        mapOf("name" to "Taluka Govt Hospital", "contact" to "+91 9876543210", "address" to "Main Road, $loc"),
        mapOf("name" to "Panchayat Health Center", "contact" to "+91 8877665544", "address" to "Station Area, $loc"),
        mapOf("name" to "City Blood Bank", "contact" to "+91 9900112233", "address" to "Market Square, $loc")
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Taluka Directory", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(hospitals) { hospital ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(hospital["name"]!!, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("📞 ${hospital["contact"]}", color = JeevaRed, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = {
                                val mapUri = Uri.parse("geo:0,0?q=${hospital["name"]} ${hospital["address"]}")
                                val mapIntent = Intent(Intent.ACTION_VIEW, mapUri).apply { setPackage("com.google.android.apps.maps") }
                                context.startActivity(mapIntent)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, JeevaRed)
                        ) {
                            Icon(Icons.Default.Place, null, tint = JeevaRed)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("VIEW ON MAP", color = JeevaRed)
                        }
                    }
                }
            }
        }
    }
}

// --- REGISTRATION SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(onRegister: (String, String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var selectedGroup by remember { mutableStateOf("O-") }
    var expanded by remember { mutableStateOf(false) }
    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Jeeva-Bindu", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, color = JeevaRed)
        Text("Rapid Response Network", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedGroup, onValueChange = {}, label = { Text("Your Blood Group") },
                modifier = Modifier.fillMaxWidth(), readOnly = true,
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, null, Modifier.clickable { expanded = true }) }
            )
            Box(modifier = Modifier.matchParentSize().clickable { expanded = true })
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                bloodGroups.forEach { group ->
                    DropdownMenuItem(text = { Text(group) }, onClick = { selectedGroup = group; expanded = false })
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location (Taluka)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { if (name.isNotEmpty()) onRegister(name, age, selectedGroup, location) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = JeevaRed),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("REGISTER", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

// --- EMERGENCY ALERT CARD ---
@Composable
fun EmergencyAlertCard(
    hospital: String,
    group: String,
    time: String,
    isResponded: Boolean,
    onRespond: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isResponded) Color.DarkGray else JeevaRed
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("URGENT $group NEEDED", color = Color.White, fontWeight = FontWeight.ExtraBold)
                Text(time, color = Color.White, fontSize = 12.sp)
            }
            Text("At $hospital", color = Color.White)
            Spacer(modifier = Modifier.height(12.dp))
            if (!isResponded) {
                Button(
                    onClick = { onRespond() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("I'M COMING", color = JeevaRed, fontWeight = FontWeight.Bold)
                }
            } else {
                Text("✅ RESPONSE SENT: Thank you donor!", color = Color.Green, fontWeight = FontWeight.Bold)
            }
        }
    }
}