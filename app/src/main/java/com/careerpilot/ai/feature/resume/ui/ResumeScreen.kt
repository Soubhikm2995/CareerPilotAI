package com.careerpilot.ai.feature.resume.ui

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.careerpilot.ai.feature.resume.data.ResumeTextExtractor
import com.careerpilot.ai.ui.components.CPGradientBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ResumeScreen() {

    val context = LocalContext.current

    var selectedFileName by remember {
        mutableStateOf<String?>(null)
    }

    var selectedFileUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var extractedText by remember {
        mutableStateOf("")
    }

    var isExtracting by remember {
        mutableStateOf(false)
    }

    var extractionError by remember {
        mutableStateOf<String?>(null)
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->

        uri?.let {

            selectedFileUri = it

            selectedFileName = getFileName(
                context = context,
                uri = it
            )

            extractedText = ""
            extractionError = null
        }
    }

    LaunchedEffect(selectedFileUri) {

        val uri = selectedFileUri ?: return@LaunchedEffect

        // Currently we support PDF extraction.
        if (selectedFileName?.endsWith(
                ".pdf",
                ignoreCase = true
            ) != true
        ) {
            extractionError = "Please select a PDF resume for now."
            return@LaunchedEffect
        }

        isExtracting = true
        extractionError = null

        try {

            val extractor = ResumeTextExtractor(context)

            extractedText = withContext(Dispatchers.IO) {
                extractor.extractText(uri)
            }

        } catch (e: Exception) {

            extractedText = ""

            extractionError =
                e.message ?: "Unable to extract text from the resume."

        } finally {

            isExtracting = false
        }
    }

    CPGradientBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = "Resume Optimizer",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Upload your resume and optimize it for ATS.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            ResumeUploadCard(
                fileName = selectedFileName,
                onUploadClick = {

                    filePickerLauncher.launch(
                        arrayOf(
                            "application/pdf",
                            "application/msword",
                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                        )
                    )
                }
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            if (selectedFileUri != null) {

                Text(
                    text = "Resume selected successfully ✓",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            if (isExtracting) {

                CircularProgressIndicator()

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Reading your resume..."
                )
            }

            extractionError?.let { error ->

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (extractedText.isNotBlank()) {

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Text(
                    text = "Extracted Resume Text",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = extractedText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun getFileName(
    context: Context,
    uri: Uri
): String? {

    var fileName: String? = null

    context.contentResolver.query(
        uri,
        arrayOf(OpenableColumns.DISPLAY_NAME),
        null,
        null,
        null
    )?.use { cursor ->

        if (cursor.moveToFirst()) {

            val nameIndex =
                cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)

            if (nameIndex >= 0) {
                fileName = cursor.getString(nameIndex)
            }
        }
    }

    return fileName
}