package com.example.aadhaar

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.style.BackgroundColorSpan
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.aadhaar.ui.theme.AadhaarTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.util.Objects
import androidx.compose.material3.Text as Text1


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AadhaarTheme {
                Surface(modifier = Modifier.padding(top = 25.dp)) {
                    Mainbody()
                }
            }
        }
    }
}

@Composable
fun MyImage() {
    val imagePainter = painterResource(id = R.drawable.aadharlogo)
    Image(
        painter = imagePainter,
        contentDescription = "My Image",
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
    )
}

@Preview
@Composable
fun PreviewMyImage() {
    MyImage()
}

@Composable
fun Footer(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp)
            .height(50.dp)
            .background(Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Hosted by: NIC, © 2023. All Rights Reserved.", color = Color.White)
    }
}

@Composable
fun Mainbody(){
    var inputValue by remember { mutableStateOf("") }

    val context = LocalContext.current
    val file = context.createImageFile()
    val uri= FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider" , file
    )

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher=
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri=uri
        }


    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {

            if (it){
                Toast.makeText(context,"Access Granted", Toast.LENGTH_LONG).show()
                cameraLauncher.launch(uri)
            }
            else{
                Toast.makeText(context,"Access Denied", Toast.LENGTH_LONG).show()
            }
        }






    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp) // Adjusted padding to accommodate the footer
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                PreviewMyImage()
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = { Text(text = "Aadhaar No.") }
                )
            }
            Spacer(modifier = Modifier.padding(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {

                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

                    if(permissionCheckResult == PackageManager.PERMISSION_GRANTED)
                    {
                        cameraLauncher.launch(uri)

                        val intent = Intent(context, MainActivity2::class.java)
                        context.startActivity(intent)
                    }
                    else{
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }

                }) {
                    Text(text = "Submit")
                }
            }
        }
        Footer(modifier = Modifier.align(Alignment.BottomCenter))
    }
}