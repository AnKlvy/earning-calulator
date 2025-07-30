package com.earningformula.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.earningformula.R
import com.earningformula.data.models.Job
import com.earningformula.ui.components.JobCard
import com.earningformula.ui.components.AddJobDialog
import com.earningformula.ui.components.ResultsCard
import com.earningformula.ui.components.FavoritesDialog
import com.earningformula.ui.components.CreateConfigurationDialog
import com.earningformula.ui.components.CurrencySelectionDialog
import com.earningformula.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddJobDialog by remember { mutableStateOf(false) }
    var showFavoritesDialog by remember { mutableStateOf(false) }
    var showCreateConfigDialog by remember { mutableStateOf(false) }
    var showDropdownMenu by remember { mutableStateOf(false) }
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var jobToEdit by remember { mutableStateOf<Job?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок и кнопки
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Row {
                // Кнопка выбора валюты
                TextButton(
                    onClick = { showCurrencyDialog = true }
                ) {
                    Text(
                        text = uiState.selectedCurrency.symbol,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                IconButton(onClick = { showFavoritesDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = stringResource(R.string.favorites)
                    )
                }

                Box {
                    FloatingActionButton(
                        onClick = { showDropdownMenu = true },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Меню добавления"
                        )
                    }

                    DropdownMenu(
                        expanded = showDropdownMenu,
                        onDismissRequest = { showDropdownMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Новая конфигурация") },
                            onClick = {
                                showDropdownMenu = false
                                showCreateConfigDialog = true
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Добавить работу") },
                            onClick = {
                                showDropdownMenu = false
                                showAddJobDialog = true
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Основной контент
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Карточки работ
            items(uiState.jobs) { job ->
                JobCard(
                    job = job,
                    currency = uiState.selectedCurrency,
                    onEditClick = { jobToEdit = job },
                    onDeleteClick = { viewModel.deleteJob(job.id) }
                )
            }
            
            // Кнопка добавления работы (если нет работ)
            if (uiState.jobs.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Добавьте первую работу",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                TextButton(
                                    onClick = { showAddJobDialog = true }
                                ) {
                                    Text(stringResource(R.string.add_job))
                                }
                            }
                        }
                    }
                }
            }
            
            // Карточка с результатами (если есть работы)
            if (uiState.jobs.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    ResultsCard(
                        totalResult = uiState.totalResult,
                        currentLoadedConfiguration = uiState.currentLoadedConfiguration,
                        originalLoadedConfiguration = uiState.originalLoadedConfiguration,
                        currency = uiState.selectedCurrency
                    )
                }
            }
        }
    }
    
    // Диалоги
    if (showAddJobDialog) {
        AddJobDialog(
            onDismiss = { showAddJobDialog = false },
            onSave = { job ->
                viewModel.addJob(job)
                showAddJobDialog = false
            }
        )
    }
    
    jobToEdit?.let { job ->
        AddJobDialog(
            job = job,
            onDismiss = { jobToEdit = null },
            onSave = { updatedJob ->
                viewModel.updateJob(updatedJob)
                jobToEdit = null
            }
        )
    }
    
    if (showFavoritesDialog) {
        FavoritesDialog(
            configurations = uiState.savedConfigurations,
            onDismiss = { showFavoritesDialog = false },
            onLoadConfiguration = { configuration ->
                viewModel.loadConfiguration(configuration)
                showFavoritesDialog = false
            },
            onDeleteConfiguration = { configId ->
                viewModel.deleteConfiguration(configId)
                // Диалог остается открытым, чтобы пользователь мог видеть обновленный список
            }
        )
    }

    if (showCreateConfigDialog) {
        CreateConfigurationDialog(
            onDismiss = { showCreateConfigDialog = false },
            onCreateConfiguration = { configName ->
                viewModel.createNewConfigurationWithName(configName)
                showCreateConfigDialog = false
            }
        )
    }

    // Диалог выбора валюты
    if (showCurrencyDialog) {
        CurrencySelectionDialog(
            currentCurrency = uiState.selectedCurrency,
            onCurrencySelected = { currency ->
                viewModel.changeCurrency(currency)
                showCurrencyDialog = false
            },
            onDismiss = { showCurrencyDialog = false }
        )
    }

    // Показ ошибок
    uiState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            // Здесь можно показать Snackbar с ошибкой
            viewModel.clearError()
        }
    }
}
