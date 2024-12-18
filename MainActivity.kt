package com.example.sprkidentifiering

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.mlkit.nl.languageid.LanguageIdentification

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LanguageIdentifierApp()
            }
        }
    }
}

@Composable
fun LanguageIdentifierApp() {
    var inputText by remember { mutableStateOf("") }
    var detectedLanguage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Skriv något") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            identifyLanguage(inputText) { language ->
                detectedLanguage = language
            }
        }) {
            Text("Identifiera språk")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Identifierat språk: $detectedLanguage")
    }
}

fun identifyLanguage(inputText: String, onResult: (String) -> Unit) {
    val languageIdentifier = LanguageIdentification.getClient()

    languageIdentifier.identifyLanguage(inputText)
        .addOnSuccessListener { languageCode ->
            if (languageCode == "und") {
                onResult("Språket kunde inte identifieras")
            } else {
                onResult("Språkkod: $languageCode")
            }
        }
        .addOnFailureListener { exception ->
            onResult("Fel vid identifiering: ${exception.localizedMessage}")
        }
}
