package com.msa.finhub.feature.inquiry.presentation

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.msa.finhub.ui.components.LoadingOverlay
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.saveable.rememberSaveable
import com.msa.finhub.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InquiryScreen(
    spec: EndpointSpec,
    onBack: () -> Unit = {},
    viewModel: InquiryViewModel = koinViewModel()
) {


    // نگهداری مقادیر فیلدها
    val textValues = rememberSaveable(spec) { mutableStateMapOf<String, String>() }
    val boolValues = rememberSaveable(spec) { mutableStateMapOf<String, Boolean>() }
    LaunchedEffect(spec) {
        textValues.clear(); boolValues.clear()
        spec.fields.forEach { field ->
            when (field.type) {
                FieldType.Text -> textValues[field.key] = field.defaultText
                FieldType.Bool -> boolValues[field.key] = field.defaultBool
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(spec.title) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
                    }
                )
            }
        ) { inner ->
            Column(
                modifier = Modifier
                    .padding(inner)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                spec.fields.forEach { field ->
                    when (field.type) {
                        FieldType.Text -> {
                            OutlinedTextField(
                                value = textValues[field.key] ?: "",
                                onValueChange = { textValues[field.key] = it },
                                label = { Text(field.label) },
                                placeholder = field.hint?.let { { Text(it) } },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        FieldType.Bool -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(field.label)
                                Switch(
                                    checked = boolValues[field.key] ?: false,
                                    onCheckedChange = { boolValues[field.key] = it }
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        viewModel.submit(spec, textValues, boolValues)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.inquiry_submit))
                }

                state.result?.let {
                    Text(
                        it.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                state.error?.let {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        LoadingOverlay(show = state.loading)
    }

}