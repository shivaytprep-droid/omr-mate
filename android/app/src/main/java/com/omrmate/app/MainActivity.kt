package com.omrmate.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import org.json.JSONObject

private val Navy = Color(0xFF142D4E)
private val Blue = Color(0xFF246BFD)
private val Pale = Color(0xFFF3F6FC)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { OMRMate(this) }
    }
}

@Composable
private fun OMRMate(context: Context) {
    val prefs = remember { context.getSharedPreferences("omr", Context.MODE_PRIVATE) }
    var hindi by remember { mutableStateOf(prefs.getBoolean("hi", false)) }
    var page by remember { mutableStateOf("home") }
    var name by remember { mutableStateOf("") }
    var count by remember { mutableIntStateOf(20) }
    var activeName by remember { mutableStateOf("") }
    var answers by remember { mutableStateOf(List(20) { "" }) }
    var saved by remember { mutableStateOf(readTests(prefs)) }
    var summary by remember { mutableStateOf<Pair<String, List<String>>?>(null) }
    val tr: (String, String) -> String = { en, hi -> if (hindi) hi else en }

    MaterialTheme(colorScheme = lightColorScheme(primary = Blue, background = Pale)) {
        Scaffold(
            containerColor = Pale,
            topBar = {
                Row(Modifier.fillMaxWidth().background(Color.White).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(42.dp).background(Navy, RoundedCornerShape(13.dp)), contentAlignment = Alignment.Center) {
                        Text("OMR", color = Color.White, fontWeight = FontWeight.Black, fontSize = 13.sp)
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text("OMR Mate", color = Navy, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(tr("Offline practice companion", "ऑफ़लाइन अभ्यास साथी"), color = Color.Gray, fontSize = 11.sp)
                    }
                    TextButton(onClick = { hindi = !hindi; prefs.edit().putBoolean("hi", hindi).apply() }) {
                        Text(if (hindi) "हिंदी" else "EN / हिंदी")
                    }
                }
            },
            bottomBar = {
                if (page != "test") Row(Modifier.fillMaxWidth().background(Color.White).padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    TextButton(onClick = { page = "home" }) { Text(tr("Home", "होम")) }
                    TextButton(onClick = { page = "history" }) { Text(tr("History", "इतिहास")) }
                }
            }
        ) { pad ->
            when (page) {
                "home" -> LazyColumn(Modifier.padding(pad).fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    item {
                        Column(Modifier.fillMaxWidth().background(Navy, RoundedCornerShape(24.dp)).padding(22.dp)) {
                            Text(tr("PRACTICE. MARK. IMPROVE.", "अभ्यास करें। उत्तर भरें।"), color = Color(0xFFB8CEFF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(12.dp))
                            Text(tr("Your exam, your pace.", "आपकी परीक्षा, आपकी गति।"), color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Text(tr("Make an OMR sheet and practise without internet.", "OMR शीट बनाएं और बिना इंटरनेट अभ्यास करें।"), color = Color(0xFFE0E8F8))
                            Spacer(Modifier.height(18.dp))
                            Button(onClick = { page = "create" }, modifier = Modifier.fillMaxWidth()) { Text(tr("+ Create new test", "+ नया टेस्ट बनाएं")) }
                        }
                    }
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Stat(tr("Saved tests", "सेव टेस्ट"), saved.size.toString(), Modifier.weight(1f))
                            Stat(tr("Offline", "ऑफ़लाइन"), "✓", Modifier.weight(1f))
                            Stat(tr("Languages", "भाषाएं"), "2", Modifier.weight(1f))
                        }
                    }
                    item { Text(tr("Recent tests", "हाल के टेस्ट"), color = Navy, fontSize = 19.sp, fontWeight = FontWeight.Bold) }
                    if (saved.isEmpty()) item {
                        Column(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(18.dp)).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("◎", color = Blue, fontSize = 36.sp)
                            Text(tr("No tests yet", "अभी कोई टेस्ट नहीं"), color = Navy, fontWeight = FontWeight.Bold)
                            Text(tr("Your saved tests appear here.", "आपके सेव टेस्ट यहां दिखेंगे।"), color = Color.Gray)
                        }
                    }
                    items(saved.take(3)) { item -> SavedCard(item.first, item.second, hindi, { activeName = item.first; answers = item.second; count = item.second.size; page = "test" }) }
                    item { AdSlot(hindi) }
                }
                "create" -> Column(Modifier.padding(pad).fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    TextButton(onClick = { page = "home" }) { Text("← " + tr("Back", "वापस")) }
                    Text(tr("Create a test", "टेस्ट बनाएं"), fontSize = 27.sp, fontWeight = FontWeight.Bold, color = Navy)
                    OutlinedTextField(name, { name = it }, modifier = Modifier.fillMaxWidth(), label = { Text(tr("Test name", "टेस्ट का नाम")) }, placeholder = { Text("Mock Test 01") }, singleLine = true)
                    Text(tr("Number of questions", "प्रश्नों की संख्या"), color = Navy, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(10, 20, 50, 100).forEach { n -> FilterChip(selected = count == n, onClick = { count = n }, label = { Text("$n") }) }
                    }
                    Button(onClick = { activeName = name.trim().ifBlank { tr("Practice Test", "अभ्यास टेस्ट") }; answers = List(count) { "" }; page = "test" }, modifier = Modifier.fillMaxWidth().height(52.dp)) { Text(tr("Start OMR sheet", "OMR शीट शुरू करें")) }
                }
                "test" -> Column(Modifier.padding(pad).fillMaxSize()) {
                    Row(Modifier.fillMaxWidth().background(Color.White).padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        TextButton(onClick = { page = "home" }) { Text("← " + tr("Exit", "बाहर")) }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(activeName, color = Navy, fontWeight = FontWeight.Bold)
                            Text("${answers.count { it.isNotBlank() }} / ${answers.size} " + tr("answered", "उत्तर भरे"), color = Color.Gray, fontSize = 11.sp)
                        }
                    }
                    LazyColumn(Modifier.weight(1f).padding(horizontal = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(vertical = 12.dp)) {
                        items(answers.indices.toList()) { i ->
                            Column(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(16.dp)).padding(14.dp)) {
                                Text(tr("Question", "प्रश्न") + " ${i + 1}", color = Navy, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(10.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    listOf("A", "B", "C", "D", "E").forEach { option ->
                                        val selected = answers[i] == option
                                        Box(Modifier.weight(1f).aspectRatio(1f).background(if (selected) Blue else Color(0xFFF3F6FC), CircleShape).clickable {
                                            answers = answers.toMutableList().also { it[i] = if (selected) "" else option }
                                        }, contentAlignment = Alignment.Center) { Text(option, color = if (selected) Color.White else Navy, fontWeight = FontWeight.Bold) }
                                    }
                                }
                            }
                        }
                    }
                    Button(onClick = {
                        saved = listOf(activeName to answers) + saved.filterNot { it.first == activeName }
                        writeTests(prefs, saved)
                        summary = activeName to answers
                        page = "result"
                    }, modifier = Modifier.fillMaxWidth().padding(14.dp).height(50.dp)) { Text(tr("Finish & save", "पूरा करें और सेव करें")) }
                }
                "result" -> Column(Modifier.padding(pad).fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(tr("Test summary", "टेस्ट सारांश"), color = Navy, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                    Column(Modifier.fillMaxWidth().background(Navy, RoundedCornerShape(24.dp)).padding(24.dp)) {
                        Text(summary?.first ?: activeName, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(16.dp))
                        Text(tr("Answered", "उत्तर भरे") + ": ${summary?.second?.count { it.isNotBlank() } ?: 0}", color = Color.White, fontSize = 18.sp)
                        Text(tr("Blank", "खाली") + ": ${(summary?.second?.count { it.isBlank() } ?: 0)}", color = Color.White, fontSize = 18.sp)
                        Text(tr("Total", "कुल") + ": ${summary?.second?.size ?: 0}", color = Color.White, fontSize = 18.sp)
                    }
                    Text(tr("Scoring needs an answer key. This version saves your marked answers only.", "अंक निकालने के लिए उत्तर कुंजी चाहिए। यह संस्करण अभी आपके उत्तर सेव करता है।"), color = Color.Gray)
                    Button(onClick = { page = "home" }, modifier = Modifier.fillMaxWidth()) { Text(tr("Back to home", "होम पर जाएं")) }
                    OutlinedButton(onClick = { page = "create" }, modifier = Modifier.fillMaxWidth()) { Text(tr("New test", "नया टेस्ट")) }
                }
                else -> LazyColumn(Modifier.padding(pad).fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    item { Text(tr("Test history", "टेस्ट इतिहास"), color = Navy, fontSize = 27.sp, fontWeight = FontWeight.Bold) }
                    if (saved.isEmpty()) item { Text(tr("No saved tests yet.", "अभी कोई टेस्ट सेव नहीं है।"), color = Color.Gray) }
                    items(saved) { item -> SavedCard(item.first, item.second, hindi, { activeName = item.first; answers = item.second; count = item.second.size; page = "test" }) }
                    item { AdSlot(hindi) }
                }
            }
        }
    }
}

@Composable
private fun Stat(label: String, value: String, modifier: Modifier) {
    Column(modifier.background(Color.White, RoundedCornerShape(16.dp)).padding(12.dp)) {
        Text(value, color = Blue, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color.Gray, fontSize = 11.sp)
    }
}

@Composable
private fun SavedCard(name: String, answers: List<String>, hindi: Boolean, onResume: () -> Unit) {
    Column(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(16.dp)).padding(16.dp)) {
        Text(name, color = Navy, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text("${answers.size} " + if (hindi) "प्रश्न" else "questions" + " · ${answers.count { it.isNotBlank() }} " + if (hindi) "उत्तर भरे" else "answered", color = Color.Gray, fontSize = 12.sp)
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onResume, modifier = Modifier.fillMaxWidth()) { Text(if (hindi) "टेस्ट खोलें" else "Open test") }
    }
}

@Composable
private fun AdSlot(hindi: Boolean) {
    Column(Modifier.fillMaxWidth().background(Color(0xFFE8EDF5), RoundedCornerShape(14.dp)).padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(if (hindi) "विज्ञापन स्थान" else "AD PLACEHOLDER", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Text(if (hindi) "अभी कोई विज्ञापन SDK नहीं जोड़ा गया।" else "No ad SDK is configured yet.", color = Color.Gray, fontSize = 11.sp)
    }
}

private fun writeTests(context: android.content.SharedPreferences, tests: List<Pair<String, List<String>>>) {
    val arr = JSONArray()
    tests.forEach { (name, answers) ->
        val a = JSONArray()
        answers.forEach { a.put(it) }
        arr.put(JSONObject().put("name", name).put("answers", a))
    }
    context.edit().putString("tests", arr.toString()).apply()
}

private fun readTests(context: android.content.SharedPreferences): List<Pair<String, List<String>>> = try {
    val arr = JSONArray(context.getString("tests", "[]") ?: "[]")
    (0 until arr.length()).map { i ->
        val obj = arr.getJSONObject(i)
        val a = obj.getJSONArray("answers")
        obj.getString("name") to (0 until a.length()).map { a.getString(it) }
    }
} catch (_: Exception) { emptyList() }
