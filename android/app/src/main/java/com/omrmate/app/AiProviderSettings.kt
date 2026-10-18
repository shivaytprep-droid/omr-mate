package com.omrmate.app
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiProviderSettings(onAnswerKeyPhoto: (Uri) -> Unit = {}) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("omr_mate_settings", Context.MODE_PRIVATE) }
    var language by remember { mutableStateOf(prefs.getString("language", "English") ?: "English") }
    var provider by remember { mutableStateOf(prefs.getString("provider", "Gemini") ?: "Gemini") }
    var model by remember { mutableStateOf(prefs.getString("model", "") ?: "") }
    var endpoint by remember { mutableStateOf(prefs.getString("endpoint", "") ?: "") }
    var apiKey by remember { mutableStateOf(prefs.getString("api_key", "") ?: "") }
    var expanded by remember { mutableStateOf(false) }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let(onAnswerKeyPhoto)
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("OMR Mate Settings", style = MaterialTheme.typography.headlineSmall)
        Text("Language")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("English", "Hindi").forEach { item ->
                FilterChip(selected = language == item, onClick = { language = item }, label = { Text(item) })
            }
        }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = provider, onValueChange = {}, readOnly = true,
                label = { Text("AI provider") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                listOf("Gemini", "Groq", "OpenAI", "Custom OpenAI-compatible API").forEach { item ->
                    DropdownMenuItem(text = { Text(item) }, onClick = { provider = item; expanded = false })
                }
            }
        }
        OutlinedTextField(value = model, onValueChange = { model = it }, label = { Text("Model ID") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = endpoint, onValueChange = { endpoint = it }, label = { Text("Custom endpoint (optional)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = apiKey, onValueChange = { apiKey = it }, label = { Text("API key") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        Button(
            onClick = {
                prefs.edit().putString("language", language).putString("provider", provider)
                    .putString("model", model.trim()).putString("endpoint", endpoint.trim())
                    .putString("api_key", apiKey.trim()).apply()
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Save settings") }
        OutlinedButton(onClick = { picker.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
            Text("Upload answer-key photo")
        }
        Text("Copy question-numbered OMR answers, including blanks, into your preferred AI chat with the official answer key. No question text is needed.")
        Text("Photo selection is available; OCR and live AI scoring are not yet connected.", style = MaterialTheme.typography.bodySmall)
    }
}
