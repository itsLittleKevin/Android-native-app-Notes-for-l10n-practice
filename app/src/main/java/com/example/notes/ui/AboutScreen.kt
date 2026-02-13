package com.example.notes.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About Notes") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Notes",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Version 1.0.0",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            AboutSection(
                label = "Description",
                content = "A simple yet powerful note-taking app. Organize your thoughts and master Android l10n."
            )

            AboutSection(
                label = "Developed By",
                content = "itsLittleKevin Intl",
                url = "https://itslittlekevin.com"
            )

            AboutSection(
                label = "With the Help of",
                content = "Google Gemini",
            )

            AboutSection(
                label = "Github",
                content = "Notes, Android Native App for L10n Practice",
                url = "https://itslittlekevin.com"
            )
            
            AboutSection(
                label = "License",
                content = "Apache License 2.0"
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(onClick = { /* TODO */ }) {
                Text("Contact Us")
            }
        }
    }
}

@Composable
fun AboutSection(
    label: String,
    content: String,
    url: String? = null // Add an optional URL parameter
) {
    val uriHandler = LocalUriHandler.current // Get the URI handler

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge.copy(
                // Change color and underline if it's a link
                color = if (url != null) Color.Blue else MaterialTheme.colorScheme.onSurface,
                textDecoration = if (url != null) TextDecoration.Underline else TextDecoration.None
            ),
            textAlign = TextAlign.Center,
            modifier = if (url != null) {
                Modifier.clickable { uriHandler.openUri(url) }
            } else {
                Modifier
            }
        )
    }
}