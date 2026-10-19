package com.omrmate.app

import android.content.Context
import android.net.Uri
import android.util.Base64
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object AnswerKeyAi {
    fun check(context: Context, imageUri: Uri, studentAnswers: String, provider: String, model: String, endpoint: String, apiKey: String, onResult: (String) -> Unit) {
        thread {
            val output = try {
                require(apiKey.isNotBlank()) { "Add an API key in Settings first." }
                val bytes = context.contentResolver.openInputStream(imageUri)?.use { it.readBytes() } ?: error("Could not read selected image.")
                require(bytes.isNotEmpty()) { "Selected image is empty." }
                val mime = context.contentResolver.getType(imageUri)?.takeIf { it.startsWith("image/") } ?: "image/jpeg"
                request(provider, model, endpoint, apiKey, Base64.encodeToString(bytes, Base64.NO_WRAP), mime, studentAnswers)
            } catch (e: Exception) {
                "AI request failed: ${e.message ?: "unknown error"}. Check provider, model, endpoint, key and internet."
            }
            android.os.Handler(context.mainLooper).post { onResult(output) }
        }
    }

    private fun request(provider: String, model: String, endpoint: String, apiKey: String, imageBase64: String, mime: String, studentAnswers: String): String {
        val prompt = "Analyze this official answer-key image directly. Read answer labels from the image, compare them with the student's numbered answers, count correct, incorrect and blank, and calculate score only if marking rules are supplied. Do not invent unreadable answers; state ambiguities. Student answers: $studentAnswers"
        val isGemini = provider.equals("Gemini", true)
        val url = if (endpoint.isNotBlank()) endpoint.trimEnd('/') else if (isGemini) "https://generativelanguage.googleapis.com/v1beta/models/${model.ifBlank { "gemini-2.0-flash" }}:generateContent" else "https://api.openai.com/v1/chat/completions"
        val body = if (isGemini) {
            JSONObject().put("contents", JSONArray().put(JSONObject().put("parts", JSONArray()
                .put(JSONObject().put("text", prompt))
                .put(JSONObject().put("inline_data", JSONObject().put("mime_type", mime).put("data", imageBase64)))))
                .put("generationConfig", JSONObject().put("temperature", 0.1))
        } else {
            JSONObject().put("model", model.ifBlank { if (provider.equals("Groq", true)) "llama-3.2-90b-vision-preview" else "gpt-4o-mini" })
                .put("messages", JSONArray().put(JSONObject().put("role", "user").put("content", JSONArray()
                    .put(JSONObject().put("type", "text").put("text", prompt))
                    .put(JSONObject().put("type", "image_url").put("image_url", JSONObject().put("url", "data:$mime;base64,$imageBase64"))))))
                .put("temperature", 0.1)
        }
        val connection = (URL(if (isGemini && !url.contains("key=")) "$url?key=$apiKey" else url).openConnection() as HttpURLConnection)
        connection.requestMethod = "POST"
        connection.connectTimeout = 20000
        connection.readTimeout = 60000
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
        return if (isGemini) json.optJSONArray("candidates")?.optJSONObject(0)?.optJSONObject("content")?.optJSONArray("parts")?.optJSONObject(0)?.optString("text").orEmpty().ifBlank { response }
        else json.optJSONArray("choices")?.optJSONObject(0)?.optJSONObject("message")?.optString("content").orEmpty().ifBlank { response }
    }
}
