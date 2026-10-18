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
fun AiProviderSettings(initialStudentAnswers: String = "", onAnswerKeyPhoto: (Uri) -> Unit = {}) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("omr_mate_settings", Context.MODE_PRIVATE) }
    var language by remember { mutableStateOf(prefs.getString("language", "English") ?: "English") }
    var provider by remember { mutableStateOf(prefs.getString("provider", "Gemini") ?: "Gemini") }
    var model by remember { mutableStateOf(prefs.getString("model", "") ?: "") }
    var endpoint by remember { mutableStateOf(prefs.getString("endpoint", "") ?: "") }
    var apiKey by remember { mutableStateOf(prefs.getString("api_key", "") ?: "") }
    var expanded by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var studentAnswers by remember { mutableStateOf(initialStudentAnswers) }
    var result by remember { mutableStateOf("") }
    var checking by remember { mutableStateOf(false) }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
        if (uri != null) onAnswerKeyPhoto(uri)
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
            OutlinedTextField(value = provider, onValueChange = {}, readOnly = true, label = { Text("AI provider") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth())
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                listOf("Gemini", "Groq", "OpenAI", "Custom OpenAI-compatible API").forEach { item ->
                    DropdownMenuItem(text = { Text(item) }, onClick = { provider = item; expanded = false })
                }
            }
        }
        OutlinedTextField(value = model, onValueChange = { model = it }, label = { Text("Model ID (optional)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = endpoint, onValueChange = { endpoint = it }, label = { Text("Custom endpoint (optional)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = apiKey, onValueChange = { apiKey = it }, label = { Text("API key") },
            visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            prefs.edit().putString("language", language).putString("provider", provider)
                .putString("model", model.trim()).putString("endpoint", endpoint.trim())
                .putString("api_key", apiKey.trim()).apply()
            result = "Settings saved on this device."
        }, modifier = Modifier.fillMaxWidth()) { Text("Save settings") }
        Divider()
        Text("Check OMR against official answer key", style = MaterialTheme.typography.titleMedium)
        OutlinedButton(onClick = { picker.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
            Text(if (imageUri == null) "Upload official answer-key photo" else "Change answer-key photo")
        }
        OutlinedTextField(value = studentAnswers, onValueChange = { studentAnswers = it },
            label = { Text("Your answers, e.g. 1-A, 2-C, 3-blank") },
            minLines = 4, modifier = Modifier.fillMaxWidth())
        Button(enabled = imageUri != null && studentAnswers.isNotBlank() && !checking,
            onClick = {
                val uri = imageUri ?: return@Button
                checking = true
                result = "Reading answer key and contacting provider&"
                AnswerKeyAi.check(context, uri, studentAnswers, provider, model, endpoint, apiKey) { response ->
                    result = response
                    checking = false
                }
            }, modifier = Modifier.fillMaxWidth()) {
            Text(if (checking) "Checking&" else "OCR + Check answers")
        }
        if (result.isNotBlank()) Text(result, style = MaterialTheme.typography.bodyMedium)
        Text("OCR runs on-device. AI checking sends OCR text and your entered answers to the selected provider. API keys are stored locally on this device.", style = MaterialTheme.typography.bodySmall)
    }
}
