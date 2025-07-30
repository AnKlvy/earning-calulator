package com.earningformula.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.earningformula.R
import com.earningformula.data.models.Job
import com.earningformula.ui.components.JobCard
import com.earningformula.ui.components.AddJobDialog
import com.earningformula.ui.components.ResultsCard
import com.earningformula.ui.components.FavoritesDialog
import com.earningformula.ui.components.CreateConfigurationDialog
import com.earningformula.ui.components.CurrencySelectionDialog
import com.earningformula.ui.components.LanguageSelectionDialog
import com.earningformula.ui.viewmodel.MainViewModel
import com.earningformula.utils.LocalizationHelper

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
    var showLanguageDialog by remember { mutableStateOf(false) }
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
                text = LocalizationHelper.getAppTitle(uiState.selectedLanguage),
                style = MaterialTheme.typography.titleMedium     , // Уменьшили размер
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f), // Занимает доступное место
                maxLines = 1, // Одна строка
                overflow = TextOverflow.Ellipsis // Обрезка если не помещается
            )
            
            // Компактный ряд кнопок
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp), // Минимальные отступы
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Очень компактная кнопка языка
                Surface(
                    onClick = { showLanguageDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = uiState.selectedLanguage.code.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Очень компактная кнопка валюты
                Surface(
                    onClick = { showCurrencyDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = uiState.selectedCurrency.symbol,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Компактная кнопка избранного
                Surface(
                    onClick = { showFavoritesDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = stringResource(R.string.favorites),
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }

                Box {
                    FloatingActionButton(
                        onClick = { showDropdownMenu = true },
                        modifier = Modifier.size(32.dp) // Такой же размер как у других кнопок
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Меню добавления",
                            modifier = Modifier.size(20.dp) // Уменьшили иконку
                        )
                    }

                    DropdownMenu(
                        expanded = showDropdownMenu,
                        onDismissRequest = { showDropdownMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(LocalizationHelper.getNewConfiguration(uiState.selectedLanguage)) },
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
                            text = { Text(LocalizationHelper.getAddJob(uiState.selectedLanguage)) },
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
                    language = uiState.selectedLanguage,
                    onEditClick = { jobToEdit = job },
                    onDeleteClick = { viewModel.deleteJob(job.id) }
                )
            }
            
            // Кнопка добавления работы (если нет работ)
            if (uiState.jobs.isEmpty()) {
                item {
                    Card(
                        onClick = { showAddJobDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 8.dp
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
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = LocalizationHelper.getAddJob(uiState.selectedLanguage),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Medium
                                )
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
                        currency = uiState.selectedCurrency,
                        language = uiState.selectedLanguage
                    )
                    Spacer(modifier = Modifier.height(16.dp)) // Добавляем отступ внизу
                }
            }
        }
    }
    
    // Диалоги
    if (showAddJobDialog) {
        AddJobDialog(
            language = uiState.selectedLanguage,
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
            language = uiState.selectedLanguage,
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
            language = uiState.selectedLanguage,
            currency = uiState.selectedCurrency,
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
            language = uiState.selectedLanguage,
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
            currentLanguage = uiState.selectedLanguage,
            onCurrencySelected = { currency ->
                viewModel.changeCurrency(currency)
                showCurrencyDialog = false
            },
            onDismiss = { showCurrencyDialog = false }
        )
    }

    // Диалог выбора языка
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = uiState.selectedLanguage,
            onLanguageSelected = { language ->
                viewModel.changeLanguage(language)
                showLanguageDialog = false
            },
            onDismiss = { showLanguageDialog = false }
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
