package com.example.jeeva_bindu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.jeeva_bindu.screens.HomeScreen
import com.example.jeeva_bindu.screens.HospitalScreen
import com.example.jeeva_bindu.screens.RegistrationScreen
import com.example.jeeva_bindu.utils.JeevaGray
import com.example.jeeva_bindu.utils.JeevaRed
import java.time.LocalDate

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

@Composable
fun JeevaBinduApp() {

    var isRegistered by rememberSaveable {
        mutableStateOf(false)
    }

    var userName by rememberSaveable {
        mutableStateOf("")
    }

    var userAge by rememberSaveable {
        mutableStateOf("")
    }

    var userBloodGroup by rememberSaveable {
        mutableStateOf("O-")
    }

    var userLocation by rememberSaveable {
        mutableStateOf("")
    }

    var currentTab by rememberSaveable {
        mutableIntStateOf(0)
    }

    val respondedAlertIds = remember {
        mutableStateListOf<String>()
    }

    val lastDonationDate = remember {
        LocalDate.now().minusDays(100)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = JeevaGray
    ) {

        if (!isRegistered) {

            RegistrationScreen(
                onRegister = { name, age, group, loc ->

                    userName = name
                    userAge = age
                    userBloodGroup = group
                    userLocation = loc

                    isRegistered = true
                }
            )

        } else {

            Scaffold(

                bottomBar = {

                    NavigationBar(
                        containerColor = androidx.compose.ui.graphics.Color.White
                    ) {

                        NavigationBarItem(
                            selected = currentTab == 0,

                            onClick = {
                                currentTab = 0
                            },

                            label = {
                                Text("Home")
                            },

                            icon = {
                                Icon(Icons.Default.Home, null)
                            },

                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = JeevaRed,
                                selectedTextColor = JeevaRed
                            )
                        )

                        NavigationBarItem(
                            selected = currentTab == 1,

                            onClick = {
                                currentTab = 1
                            },

                            label = {
                                Text("Hospitals")
                            },

                            icon = {
                                Icon(Icons.Default.LocationOn, null)
                            },

                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = JeevaRed,
                                selectedTextColor = JeevaRed
                            )
                        )
                    }
                }

            ) { padding ->

                Box(
                    modifier = Modifier.padding(padding)
                ) {

                    if (currentTab == 0) {

                        HomeScreen(
                            userName,
                            userAge,
                            userBloodGroup,
                            userLocation,
                            lastDonationDate,
                            respondedAlertIds
                        )

                    } else {

                        HospitalScreen(userLocation)
                    }
                }
            }
        }
    }
}
