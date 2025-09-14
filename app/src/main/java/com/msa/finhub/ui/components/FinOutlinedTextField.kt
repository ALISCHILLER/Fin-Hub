package com.msa.finhub.ui.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.*
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.runtime.saveable.rememberSaveable
@Composable
fun FinOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    leading: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    supportingText: String? = null,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val cs = MaterialTheme.colorScheme
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        leadingIcon = leading,
        singleLine = true,
        isError = isError,
        supportingText = { if (isError && supportingText != null) Text(supportingText, color = cs.error) },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = cs.primary,
            unfocusedBorderColor = cs.outline,
            cursorColor = cs.primary,
            focusedLabelColor = cs.primary
        ),
        modifier = modifier
    )
}

@Composable
fun FinPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "رمز عبور",
    placeholder: String = "******",
    leading: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    supportingText: String? = null,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    val cs = MaterialTheme.colorScheme
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        leadingIcon = leading,
        trailingIcon = {
            IconButton(
                onClick = { visible = !visible },
                modifier = Modifier.semantics {
                    contentDescription = if (visible) "مخفی کردن رمز" else "نمایش رمز"
                    stateDescription = if (visible) "رمز قابل مشاهده است" else "رمز مخفی است"
                }
            ) {
                Icon(
                    imageVector = if (visible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                    contentDescription = null
                )
            }
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction, autoCorrect = false),
        keyboardActions = KeyboardActions(onAny = { onImeAction() }),
        isError = isError,
        supportingText = { if (isError && supportingText != null) Text(supportingText, color = cs.error) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = cs.primary,
            unfocusedBorderColor = cs.outline,
            cursorColor = cs.primary,
            focusedLabelColor = cs.primary
        ),
        modifier = modifier
    )
}
