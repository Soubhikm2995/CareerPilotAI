package com.careerpilot.ai.feature.resume.data

import android.content.Context
import android.net.Uri
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper

class ResumeTextExtractor(
    private val context: Context
) {

    fun extractText(uri: Uri): String {

        PDFBoxResourceLoader.init(context)

        context.contentResolver.openInputStream(uri).use { inputStream ->

            requireNotNull(inputStream) {
                "Unable to open the selected resume."
            }

            PDDocument.load(inputStream).use { document ->

                return PDFTextStripper()
                    .getText(document)
                    .trim()
            }
        }
    }
}