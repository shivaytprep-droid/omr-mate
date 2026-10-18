package com.omrmate.app

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object AnswerKeyAi {
    fun check(
        context: Context,
        imageUri: Uri,
        studentAnswers: String,
        provider: String,
        model: String,
        endpoint: String,
        apiKey: String,
        onResult: (String) -> Unit
    ) {
        try {
            val image = InputImage.fromFilePath(context, imageUri)
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS).process(image)
                .addOnSuccessListener { result ->
                    val keyText = result.text.trim()
                    if (keyText.isBlank()) {
                        onResult("No readable text found. Retake the answer-key photo with good light.")
                        return@addOnSuccessListener
                    }
                    thread {
                        val output = try {
                            request(provider, model, endpoint, apiKey, keyText, studentAnswers)
                        } catch (e: Exception) {
                            "AI request failed: ${e.message ?: "unknown error"}. Check provider, model, endpoint, key and internet."
                        }
                        android.os.Handler(context.mainLooper).post { onResult("OCR answer key:\n$keyText\n\nChecking result:\n$output") }
                    }
                }
                .addOnFailureListener { e -> onResult("Answer-key OCR failed: ${e.message ?: "unknown error"}") }
        } catch (e: Exception) {
            onResult("Could not open image: ${e.message ?: "unknown error"}")
        }
    }

    private fun request(provider: String, model: String, endpoint: String, apiKey: String, keyText: String, studentAnswers: String): String {
        require(apiKey.isNotBlank()) { "Add an API key in Settings first." }
        val prompt = "You are checking an OMR response against the official answer key. Do not invent questions. Extract only answer labels from the supplied OCR text, compare with the student's numbered answers, count correct, incorrect and blank, and calculate score only if marking rules are supplied. State ambiguities.\nOFFICIAL ANSWER KEY OCR:\n$keyText\nSTUDENT ANSWERS (question-numbered, blank answers included):\n$studentAnswers"
        val isGemini = provider.equals("Gemini", true)
        val url = if (endpoint.isNotBlank()) endpoint.trimEnd('/') else if (isGemini) {
            "https://generativelanguage.googleapis.com/v1beta/models/${model.ifBlank { "gemini-2.0-flash" }}:generateContent"
        } else "https://api.openai.com/v1/chat/completions"
        val body = if (isGemini) {
            JSONObject().put("contents", JSONArray().put(JSONObject().put("parts", JSONArray().put(JSONObject().put("text", prompt)))))
                .put("generationConfig", JSONObject().put("temperature", 0.1))
        } else {
            JSONObject().put("model", model.ifBlank { if (provider.equals("Groq", true)) "llama-3.3-70b-versatile" else "gpt-4o-mini" })
                .put("messages", JSONArray().put(JSONObject().put("role", "user").put("content", prompt)))
                .put("temperature", 0.1)
        }
        val connection = (URL(if (isGemini && !url.contains("key=")) "$url?key=$apiKey" else url).openConnection() as HttpURLConnection)
        connection.requestMethod = "POST"
        connection.connectTimeout = 20000
        connection.readTimeout = 45000
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json")
        if (!isGemini) connection.setRequestProperty("Authorization", "Bearer $apiKey")
        connection.outputStream.use { it.write(body.toString().toByteArray(Charsets.UTF_8)) }
        val status = connection.responseCode
        val stream = if (status in 200..299) connection.inputStream else connection.errorStream
        val response = stream.bufferedReader().use { it.readText() }
        connection.disconnect()
        if (status !in 200..299) return "Provider returned HTTP $status: $response"
        val json = JSONObject(response)
        return if (isGemini) {
            json.optJSONArray("candidates")?.optJSONObject(0)?.optJSONObject("content")?.optJSONArray("parts")
                ?.optJSONObject(0)?.optString("text").orEmpty().ifBlank { response }
        } else {
            json.optJSONArray("choices")?.optJSONObject(0)?.optJSONObject("message")?.optString("content").orEmpty().ifBlank { response }
        }
    }
}
