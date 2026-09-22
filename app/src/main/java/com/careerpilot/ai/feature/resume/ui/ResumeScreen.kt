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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.careerpilot.ai.feature.resume.data.ResumeTextExtractor
import com.careerpilot.ai.feature.resume.model.AtsAnalysisResult
import com.careerpilot.ai.feature.resume.usecase.AnalyzeResumeUseCase
import com.careerpilot.ai.feature.resume.usecase.CalculateAiAtsScoreUseCase
import com.careerpilot.ai.feature.resume.usecase.CalculateScoreUseCase
import com.careerpilot.ai.feature.resume.usecase.ExtractKeywordsUseCase
import com.careerpilot.ai.feature.resume.usecase.SkillDetector
import com.careerpilot.ai.feature.resume.viewmodel.ResumeViewModel
import com.careerpilot.ai.ui.components.CPGradientBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ResumeScreen(
    viewModel: ResumeViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val aiState by viewModel.aiState.collectAsStateWithLifecycle()

    val calculateAiAtsScoreUseCase = remember {
        CalculateAiAtsScoreUseCase()
    }

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

    var jobDescription by remember {
        mutableStateOf("")
    }

    var analysisResult by remember {
        mutableStateOf<AtsAnalysisResult?>(null)
    }

    var isAnalyzing by remember {
        mutableStateOf(false)
    }

    var analysisError by remember {
        mutableStateOf<String?>(null)
    }

    val extractKeywordsUseCase = remember {
        ExtractKeywordsUseCase()
    }

    val calculateScoreUseCase = remember {
        CalculateScoreUseCase()
    }

    val skillDetector = remember {
        SkillDetector()
    }

    val analyzeResumeUseCase = remember {
        AnalyzeResumeUseCase(
            extractKeywordsUseCase = extractKeywordsUseCase,
            calculateScoreUseCase = calculateScoreUseCase,
            skillDetector = skillDetector
        )
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
            analysisResult = null
            analysisError = null

            viewModel.clearAiResult()
        }
    }

    LaunchedEffect(selectedFileUri) {

        val uri = selectedFileUri ?: return@LaunchedEffect

        if (
            selectedFileName?.endsWith(
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
                e.message
                    ?: "Unable to extract text from the resume."

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

            /*
             * JOB DESCRIPTION
             */

            if (extractedText.isNotBlank()) {

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Text(
                    text = "Job Description",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = jobDescription,
                    onValueChange = {
                        jobDescription = it
                        analysisResult = null
                        analysisError = null
                        viewModel.clearAiResult()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 7,
                    label = {
                        Text("Paste the job description")
                    },
                    placeholder = {
                        Text(
                            "Paste the job description here..."
                        )
                    }
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    onClick = {

                        isAnalyzing = true
                        analysisError = null

                        try {

                            analysisResult =
                                analyzeResumeUseCase(
                                    resumeText = extractedText,
                                    jobDescription = jobDescription
                                )

                            viewModel.analyzeWithAi(
                                resumeText = extractedText,
                                jobDescription = jobDescription
                            )

                            isAnalyzing = false

                        } catch (e: Exception) {

                            analysisResult = null

                            analysisError =
                                e.message
                                    ?: "Unable to analyze resume."

                            isAnalyzing = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = jobDescription.isNotBlank() &&
                            !isAnalyzing
                ) {

                    if (isAnalyzing) {

                        CircularProgressIndicator(
                            modifier = Modifier.height(20.dp)
                        )

                    } else {

                        Text("Analyze Resume")
                    }
                }
            }

            analysisError?.let { error ->

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            /*
             * LOCAL ATS RESULT
             */

            analysisResult?.let { result ->

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                AtsResultCard(result)
            }

            /*
             * AI LOADING
             */

            if (aiState.isLoading) {

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                CircularProgressIndicator()

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "AI is analyzing your resume..."
                )
            }

            /*
             * AI ERROR
             */

            aiState.error?.let { error ->

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "AI Analysis Error: $error",
                    color = MaterialTheme.colorScheme.error
                )
            }

            /*
             * AI RESULT
             */

            aiState.result?.let { aiResult ->

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "AI Resume Analysis",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        /*
                         * AI ATS SCORE
                         */

                        val aiAtsScore =
                            calculateAiAtsScoreUseCase(aiResult)

                        Text(
                            text = "$aiAtsScore%",
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "AI ATS Score",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        /*
                         * SKILLS
                         */

                        Text(
                            text = "Skills",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = if (aiResult.skills.isEmpty()) {
                                "No skills identified."
                            } else {
                                aiResult.skills.joinToString(", ")
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        /*
                         * MATCHED SKILLS
                         */

                        Text(
                            text = "Matched Skills",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = if (aiResult.matchedSkills.isEmpty()) {
                                "No matching skills identified."
                            } else {
                                aiResult.matchedSkills.joinToString(", ")
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        /*
                         * MISSING SKILLS
                         */

                        Text(
                            text = "Missing Skills",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = if (aiResult.missingSkills.isEmpty()) {
                                "No missing skills identified."
                            } else {
                                aiResult.missingSkills.joinToString(", ")
                            },
                            color = if (aiResult.missingSkills.isEmpty()) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.error
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        /*
                         * MATCHED KEYWORDS
                         */

                        Text(
                            text = "Matched Keywords",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = if (aiResult.matchedKeywords.isEmpty()) {
                                "No matching keywords identified."
                            } else {
                                aiResult.matchedKeywords.joinToString(", ")
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        /*
                         * MISSING KEYWORDS
                         */

                        Text(
                            text = "Missing Keywords",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = if (aiResult.missingKeywords.isEmpty()) {
                                "No missing keywords identified."
                            } else {
                                aiResult.missingKeywords.joinToString(", ")
                            },
                            color = if (aiResult.missingKeywords.isEmpty()) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.error
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        /*
                         * EXPERIENCE REQUIREMENTS
                         */

                        Text(
                            text = "Experience Requirements",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = if (
                                aiResult.experienceRequirements.isEmpty()
                            ) {
                                "No specific experience requirements identified."
                            } else {
                                aiResult.experienceRequirements.joinToString(
                                    separator = "\n• ",
                                    prefix = "• "
                                )
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        /*
                         * EDUCATION REQUIREMENTS
                         */

                        Text(
                            text = "Education Requirements",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = if (
                                aiResult.educationRequirements.isEmpty()
                            ) {
                                "No specific education requirements identified."
                            } else {
                                aiResult.educationRequirements.joinToString(
                                    separator = "\n• ",
                                    prefix = "• "
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AtsResultCard(
    result: AtsAnalysisResult
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "ATS Analysis",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "${result.score}%",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Overall ATS Score",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            /*
             * KEYWORD MATCH
             */

            Text(
                text = "Keyword Match",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "${result.keywordMatchPercentage}% of job-description keywords found in your resume"
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            /*
             * SKILLS MATCH
             */

            Text(
                text = "Skills Match",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            val totalSkills =
                result.matchedSkills.size +
                        result.missingSkills.size

            val skillsMatchPercentage =
                if (totalSkills == 0) {
                    0
                } else {
                    (
                            result.matchedSkills.size.toDouble() /
                                    totalSkills.toDouble() *
                                    100
                            ).toInt()
                        .coerceIn(0, 100)
                }

            Text(
                text = "$skillsMatchPercentage% of detected job skills found in your resume"
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            /*
             * MATCHED SKILLS
             */

            Text(
                text = "Matched Skills",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = if (result.matchedSkills.isEmpty()) {
                    "No matching skills detected."
                } else {
                    result.matchedSkills.joinToString(", ")
                }
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            /*
             * MISSING SKILLS
             */

            Text(
                text = "Missing Skills",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = if (result.missingSkills.isEmpty()) {
                    "No missing skills detected."
                } else {
                    result.missingSkills.joinToString(", ")
                },
                color = if (result.missingSkills.isEmpty()) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.error
                }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            /*
             * MATCHED KEYWORDS
             */

            Text(
                text = "Matched Keywords",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            if (result.matchedKeywords.isEmpty()) {

                Text(
                    text = "No matching keywords found.",
                    color = MaterialTheme.colorScheme.error
                )

            } else {

                Text(
                    text = result.matchedKeywords.joinToString(", ")
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            /*
             * MISSING KEYWORDS
             */

            Text(
                text = "Missing Keywords",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            if (result.missingKeywords.isEmpty()) {

                Text(
                    text = "Excellent! No missing keywords detected."
                )

            } else {

                Text(
                    text = result.missingKeywords.joinToString(", "),
                    color = MaterialTheme.colorScheme.error
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
                cursor.getColumnIndex(
                    OpenableColumns.DISPLAY_NAME
                )

            if (nameIndex >= 0) {
                fileName = cursor.getString(nameIndex)
            }
        }
    }

    return fileName
}