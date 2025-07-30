package com.earningformula.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.earningformula.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateConfigurationDialog(
    onDismiss: () -> Unit,
    onCreateConfiguration: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var configurationName by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Создать новую конфигурацию",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = configurationName,
                    onValueChange = { 
                        configurationName = it
                        isError = false
                    },
                    label = { Text("Название конфигурации") },
                    placeholder = { Text("Введите название...") },
                    isError = isError,
                    supportingText = if (isError) {
                        { Text("Название не может быть пустым") }
                    } else null,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Отмена")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            val trimmedName = configurationName.trim()
                            if (trimmedName.isNotEmpty()) {
                                onCreateConfiguration(trimmedName)
                            } else {
                                isError = true
                            }
                        }
                    ) {
                        Text("Создать")
                    }
                }
            }
        }
    }
}
