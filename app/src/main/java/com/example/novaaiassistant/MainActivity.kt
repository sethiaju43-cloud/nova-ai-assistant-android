package com.example.novaaiassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

class MainActivity : ComponentActivity() {

 override fun onCreate(savedInstanceState: Bundle?) {
 super.onCreate(savedInstanceState)

 setContent {
 MaterialTheme {
 NovaApp()
 }
 }
 }
}

@Composable
fun NovaApp() {
 var userMessage by remember { mutableStateOf("") }
 var novaReply by remember {
 mutableStateOf(
 "Hello! Main Nova hoon, tumhari AI assistant. Aaj main tumhari kis tarah help kar sakti hoon?"
 )
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
 text = "Your friendly AI assistant",
 color = softText,
 fontSize = 15.sp
 )

 Spacer(modifier = Modifier.height(30.dp))

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
 onValueChange = { userMessage = it },
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
 novaReply = "Mic feature abhi coming soon hai. Pehle text chat ko fully working banate hain."
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
 return "Please apna message likho. Main help karne ke liye yahan hoon."
 }

 return when {
 text.contains("udaas") ||
 text.contains("sad") ||
 text.contains("depressed") -> {
 "Mujhe afsos hai ki tum udaas mehsoos kar rahe ho. Tum akelay nahi ho. Agar tum comfortable ho, to mujhe bata sakte ho ki kya hua."
 }

 text.contains("galti") ||
 text.contains("mistake") ||
 text.contains("error") -> {
 "Koi baat nahi, galti hona normal hai. Hum calmly dekh sakte hain ki kya hua aur agali baar ise kaise improve kiya ja sakta hai."
 }

 text.contains("hello") ||
 text.contains("hi") ||
 text.contains("namaste") -> {
 "Hello! Main Nova hoon. Aaj tumhe kis cheez mein help chahiye?"
 }

 text.contains("kaise ho") ||
 text.contains("how are you") -> {
 "Main theek hoon aur help ke liye ready hoon. Main AI assistant hoon, isliye meri feelings human jaisi nahi hain, lekin main tumhari baat dhyan se samajhne ki koshish karungi."
 }

 else -> {
 "Maine tumhara message padha: \"$message\". Abhi main basic offline mode mein hoon, lekin jaldi hi intelligent AI chat, voice commands, notes aur reminders add honge."
 }
 }
}
