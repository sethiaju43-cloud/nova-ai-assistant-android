package com.example.novaaiassistant

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

 private val microphonePermissionLauncher =
 registerForActivityResult(
 ActivityResultContracts.RequestPermission()
 ) { granted ->
 if (granted) {
 startVoiceRecognition()
 } else {
 voiceStatus?.invoke("Microphone permission allow karna zaroori hai.")
 }
 }

 private var speechRecognizer: SpeechRecognizer? = null
 private var latestVoiceText: ((String) -> Unit)? = null
 private var voiceStatus: ((String) -> Unit)? = null

 override fun onCreate(savedInstanceState: Bundle?) {
 super.onCreate(savedInstanceState)

 setContent {
 MaterialTheme {
 NovaApp(
 onMicClick = { textCallback, statusCallback ->
 latestVoiceText = textCallback
 voiceStatus = statusCallback

 val permission = ContextCompat.checkSelfPermission(
 this,
 Manifest.permission.RECORD_AUDIO
 )

 if (permission == PackageManager.PERMISSION_GRANTED) {
 startVoiceRecognition()
 } else {
 microphonePermissionLauncher.launch(
 Manifest.permission.RECORD_AUDIO
 )
 }
 }
 )
 }
 }
 }

 private fun startVoiceRecognition() {
 if (!SpeechRecognizer.isRecognitionAvailable(this)) {
 voiceStatus?.invoke(
 "Is phone par voice recognition available nahi hai."
 )
 return
 }

 if (speechRecognizer == null) {
 speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

 speechRecognizer?.setRecognitionListener(
 object : RecognitionListener {

 override fun onReadyForSpeech(params: Bundle?) {
 voiceStatus?.invoke("Nova sun rahi hai...")
 }

 override fun onBeginningOfSpeech() {
 voiceStatus?.invoke("Bolna shuru kijiye...")
 }

 override fun onRmsChanged(rmsdB: Float) {
 }

 override fun onBufferReceived(buffer: ByteArray?) {
 }

 override fun onEndOfSpeech() {
 voiceStatus?.invoke("Awaaz samjhi ja rahi hai...")
 }

 override fun onError(error: Int) {
 voiceStatus?.invoke(speechErrorMessage(error))
 }

 override fun onResults(results: Bundle?) {
 val matches = results?.getStringArrayList(
 SpeechRecognizer.RESULTS_RECOGNITION
 )

 val spokenText = matches?.firstOrNull()

 if (!spokenText.isNullOrBlank()) {
 latestVoiceText?.invoke(spokenText)
 voiceStatus?.invoke("Voice command complete.")
 } else {
 voiceStatus?.invoke(
 "Mujhe clear voice command nahi mili."
 )
 }
 }

 override fun onPartialResults(partialResults: Bundle?) {
 }

 override fun onEvent(eventType: Int, params: Bundle?) {
 }
 }
 )
 }

 val recognizerIntent = Intent(
 RecognizerIntent.ACTION_RECOGNIZE_SPEECH
 ).apply {
 putExtra(
 RecognizerIntent.EXTRA_LANGUAGE_MODEL,
 RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
 )

 putExtra(
 RecognizerIntent.EXTRA_LANGUAGE,
 "hi-IN"
 )

 putExtra(
 RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
 "hi-IN"
 )

 putExtra(
 RecognizerIntent.EXTRA_PROMPT,
 "Nova ko command dein"
 )
 }

 speechRecognizer?.startListening(recognizerIntent)
 }

 private fun speechErrorMessage(error: Int): String {
 return when (error) {
 SpeechRecognizer.ERROR_AUDIO ->
 "Microphone audio mein problem aayi."

 SpeechRecognizer.ERROR_CLIENT ->
 "Voice recognition client error aaya."

 SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
 "Microphone permission required hai."

 SpeechRecognizer.ERROR_NETWORK ->
 "Network problem ki wajah se voice recognize nahi hui."

 SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->
 "Network timeout ho gaya. Internet check karein."

 SpeechRecognizer.ERROR_NO_MATCH ->
 "Nova aapki baat samajh nahi paayi. Dobara boliye."

 SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->
 "Koi voice input nahi mila."

 SpeechRecognizer.ERROR_RECOGNIZER_BUSY ->
 "Voice recognizer busy hai. Dobara Mic dabayein."

 else ->
 "Voice recognition mein error aaya. Dobara try karein."
 }
 }

 override fun onDestroy() {
 speechRecognizer?.destroy()
 speechRecognizer = null
 super.onDestroy()
 }
}

@Composable
fun NovaApp(
 onMicClick: (
 onTextReceived: (String) -> Unit,
 onStatusChanged: (String) -> Unit
 ) -> Unit
) {
 var userMessage by remember {
 mutableStateOf("")
 }

 var novaReply by remember {
 mutableStateOf(
 "Hello! Main Nova hoon. Aaj main tumhari kis tarah help kar sakti hoon?"
 )
 }

 var voiceStatus by remember {
 mutableStateOf("Mic button dabakar command dein.")
 }

 val backgroundColor = Color(0xFF08111F)
 val cardColor = Color(0xFF111D30)
 val neonBlue = Color(0xFF38BDF8)
 val softText = Color(0xFFD9E7FF)

 Column(
 modifier = Modifier
.fillMaxSize()
.background(backgroundColor)
.padding(20.dp),
 horizontalAlignment = Alignment.CenterHorizontally
 ) {
 Spacer(modifier = Modifier.height(18.dp))

 Box(
 modifier = Modifier
.size(82.dp)
.clip(CircleShape)
.background(neonBlue),
 contentAlignment = Alignment.Center
 ) {
 Text(
 text = "N",
 color = Color(0xFF08111F),
 fontSize = 40.sp
 )
 }

 Spacer(modifier = Modifier.height(14.dp))

 Text(
 text = "NOVA",
 color = neonBlue,
 fontSize = 30.sp
 )

 Text(
 text = "Voice-enabled AI assistant",
 color = softText,
 fontSize = 15.sp
 )

 Spacer(modifier = Modifier.height(22.dp))

 Text(
 text = voiceStatus,
 color = Color(0xFF9DDFFF),
 fontSize = 14.sp,
 textAlign = TextAlign.Center
 )

 Spacer(modifier = Modifier.height(20.dp))

 Card(
 modifier = Modifier.fillMaxWidth(),
 shape = RoundedCornerShape(20.dp),
 colors = CardDefaults.cardColors(
 containerColor = cardColor
 )
 ) {
 Column(
 modifier = Modifier.padding(20.dp)
 ) {
 Text(
 text = "NOVA",
 color = neonBlue,
 fontSize = 13.sp
 )

 Spacer(modifier = Modifier.height(8.dp))

 Text(
 text = novaReply,
 color = softText,
 fontSize = 18.sp
 )
 }
 }

 Spacer(modifier = Modifier.weight(1f))

 OutlinedTextField(
 value = userMessage,
 onValueChange = {
 userMessage = it
 },
 modifier = Modifier.fillMaxWidth(),
 label = {
 Text("Type a message for Nova")
 },
 placeholder = {
 Text("Example: Main udaas hoon")
 },
 singleLine = true,
 keyboardOptions = KeyboardOptions(
 imeAction = ImeAction.Send
 ),
 colors = OutlinedTextFieldDefaults.colors(
 focusedTextColor = Color.White,
 unfocusedTextColor = Color.White,
 focusedBorderColor = neonBlue,
 unfocusedBorderColor = Color(0xFF5B6B85),
 focusedLabelColor = neonBlue,
 unfocusedLabelColor = Color(0xFFB0C4DE),
 cursorColor = neonBlue
 )
 )

 Spacer(modifier = Modifier.height(12.dp))

 Row(
 modifier = Modifier.fillMaxWidth(),
 horizontalArrangement = Arrangement.Center
 ) {
 Button(
 onClick = {
 novaReply = createNovaReply(userMessage)
 userMessage = ""
 },
 colors = ButtonDefaults.buttonColors(
 containerColor = neonBlue,
 contentColor = Color(0xFF08111F)
 ),
 shape = RoundedCornerShape(14.dp)
 ) {
 Text(
 text = "Send",
 fontSize = 16.sp
 )
 }

 Spacer(modifier = Modifier.width(12.dp))

 Button(
 onClick = {
 onMicClick(
 { recognizedText ->
 userMessage = recognizedText
 novaReply = createNovaReply(recognizedText)
 },
 { status ->
 voiceStatus = status
 }
 )
 },
 colors = ButtonDefaults.buttonColors(
 containerColor = Color(0xFF263B59),
 contentColor = Color.White
 ),
 shape = RoundedCornerShape(14.dp)
 ) {
 Text(
 text = "Mic",
 fontSize = 16.sp
 )
 }
 }

 Spacer(modifier = Modifier.height(12.dp))

 Text(
 text = "Nova is an AI assistant, not a real human.",
 color = Color(0xFF8EA4C0),
 fontSize = 12.sp,
 textAlign = TextAlign.Center
 )
 }
}

fun createNovaReply(message: String): String {
 val text = message.trim().lowercase()

 if (text.isEmpty()) {
 return "Please apna message bolkar ya likhkar bhejo."
 }

 return when {
 text.contains("udaas") ||
 text.contains("sad") ||
 text.contains("depressed") -> {
 "Mujhe afsos hai ki tum udaas mehsoos kar rahe ho. Agar comfortable ho, to mujhe bata sakte ho ki kya hua."
 }

 text.contains("galti") ||
 text.contains("mistake") ||
 text.contains("error") -> {
 "Koi baat nahi, galti hona normal hai. Hum calmly dekh sakte hain ki kya hua."
 }

 text.contains("hello") ||
 text.contains("hi") ||
 text.contains("namaste") -> {
 "Hello! Main Nova hoon. Aaj tumhe kis cheez mein help chahiye?"
 }

 text.contains("kaise ho") ||
 text.contains("how are you") -> {
 "Main help ke liye ready hoon. Main ek AI assistant hoon, real human nahi."
 }

 else -> {
 "Maine tumhari command suni: \"$message\". Abhi Nova basic offline mode mein hai."
 }
 }
}
