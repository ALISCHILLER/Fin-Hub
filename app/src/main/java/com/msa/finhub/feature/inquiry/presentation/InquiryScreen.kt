package com.msa.finhub.feature.inquiry.presentation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.Info
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.msa.finhub.R
import com.msa.finhub.ui.components.LoadingOverlay
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber


private fun <T> stateMapSaver() = Saver<SnapshotStateMap<String, T>, Map<String, T>>(
    save = { HashMap(it) },
    restore = { saved -> mutableStateMapOf<String, T>().apply { putAll(saved) } }
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InquiryScreen(
    spec: EndpointSpec,
    onBack: () -> Unit = {},
    viewModel: InquiryViewModel = koinViewModel()
) {


    // نگهداری مقادیر فیلدها
    val textValues = rememberSaveable(spec, saver = stateMapSaver<String>()) {
        mutableStateMapOf<String, String>()
    }
    val boolValues = rememberSaveable(spec, saver = stateMapSaver<Boolean>()) {
        mutableStateMapOf<String, Boolean>()
    }
    var isHelpVisible by rememberSaveable(spec) { mutableStateOf(false) }
    LaunchedEffect(spec) {
        Timber.d("InquiryScreen launched for spec: ${'$'}{spec.path}")
        textValues.clear(); boolValues.clear()
        spec.fields.forEach { field ->
            when (field.type) {
                FieldType.Text -> textValues[field.key] = field.defaultText
                FieldType.Bool -> boolValues[field.key] = field.defaultBool
            }
        }
        isHelpVisible = false
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(spec.titleRes)) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
                    },
                    actions = {
                        spec.help?.let {
                            IconButton(onClick = { isHelpVisible = true }) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = stringResource(R.string.inquiry_help_content_description)
                                )
                            }
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
                                label = { Text(stringResource(field.labelRes)) },
                                placeholder = field.hintRes?.let { hintRes ->
                                    { Text(stringResource(hintRes)) }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        FieldType.Bool -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(stringResource(field.labelRes))
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
                        Timber.d("Submitting inquiry with text=${'$'}textValues bool=${'$'}boolValues")
                        viewModel.submit(spec, textValues, boolValues)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.inquiry_submit))
                }

                val result = state.result
                AnimatedVisibility(
                    visible = result != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    result?.let { json ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.inquiry_result_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            ElevatedCard(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                )
                            ) {
                                SelectionContainer {
                                    JsonViewer(
                                        element = json,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                val errorMessage = state.error
                AnimatedVisibility(
                    visible = errorMessage != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    errorMessage?.let { err ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.inquiry_error_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.error
                            )
                            ElevatedCard(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                )
                            ) {
                                Text(
                                    text = err,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        LoadingOverlay(show = state.loading)
        if (isHelpVisible) {
            spec.help?.let { help ->
                AlertDialog(
                    onDismissRequest = { isHelpVisible = false },
                    confirmButton = {
                        TextButton(onClick = { isHelpVisible = false }) {
                            Text(stringResource(R.string.inquiry_help_acknowledge))
                        }
                    },
                    title = {
                        Text(
                            text = stringResource(help.headlineRes),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    text = {
                        Text(
                            text = stringResource(help.descriptionRes),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
            }
        }
    }

}