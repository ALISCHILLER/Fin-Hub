package com.msa.finhub.feature.inquiry

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
import com.msa.finhub.core.network.error.NetworkResult
import com.msa.finhub.core.network.handler.NetworkHandler
import com.msa.finhub.ui.components.LoadingOverlay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun InquiryScreen(
    spec: EndpointSpec,
    onBack: () -> Unit = {}
){
    val scope = rememberCoroutineScope()

    // نگهداری مقادیر فیلدها
    val textValues = remember { mutableStateMapOf<String, String>() }
    val boolValues = remember { mutableStateMapOf<String, Boolean>() }
    LaunchedEffect(spec) {
        textValues.clear(); boolValues.clear()
        spec.fields.forEach { field ->
            when (field.type) {
                FieldType.Text -> textValues[field.key] = field.defaultText
                FieldType.Bool -> boolValues[field.key] = field.defaultBool
            }
        }
    }

    var resultText by remember { mutableStateOf<String?>(null) }
    var errorText by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

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
                        scope.launch {
                            loading = true
                            resultText = null
                            errorText = null
                            val body: JsonObject = buildJsonObject {
                                textValues.forEach { (k, v) -> put(k, JsonPrimitive(v)) }
                                boolValues.forEach { (k, v) -> put(k, JsonPrimitive(v)) }
                            }
                            when (val res = NetworkHandler.post<JsonObject, JsonObject>(spec.path, body)) {
                                is NetworkResult.Success -> resultText = res.data?.toString()
                                is NetworkResult.Error -> errorText = res.error.message
                                else -> {}
                            }
                            loading = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ارسال استعلام")
                }

                resultText?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
                errorText?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            }
        }

    LoadingOverlay(show = loading)
}

}