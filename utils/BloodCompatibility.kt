package com.example.jeeva_bindu.utils

fun isMedicallyCompatible(donorGroup: String, patientGroup: String): Boolean {
    return when (donorGroup) {
        "O-" -> true
        "O+" -> listOf("O+", "A+", "B+", "AB+").contains(patientGroup)
        "A-" -> listOf("A-", "A+", "AB-", "AB+").contains(patientGroup)
        "A+" -> listOf("A+", "AB+").contains(patientGroup)
        "B-" -> listOf("B-", "B+", "AB-", "AB+").contains(patientGroup)
        "B+" -> listOf("B+", "AB+").contains(patientGroup)
        "AB-" -> listOf("AB-", "AB+").contains(patientGroup)
        "AB+" -> patientGroup == "AB+"
        else -> false
    }
}
