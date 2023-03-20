package com.example.followtherecipe.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.lifecycle.ViewModel

class TabItemViewModel : ViewModel() {
    val items = listOf(
        TabItem("Home", Icons.Filled.Home,"Home"),
        TabItem("Converter",Icons.Filled.Edit,"Converter"),
        TabItem("Recipe",Icons.Filled.Search,"Recipe")
    )
}