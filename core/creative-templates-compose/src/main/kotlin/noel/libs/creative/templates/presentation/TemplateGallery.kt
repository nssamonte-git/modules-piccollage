package noel.libs.creative.templates.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noel.libs.creative.model.*
import noel.libs.creative.templates.domain.*
import noel.libs.creative.ui.CreativePanel

@Composable
fun TemplateGallery(
    recipes: List<Recipe>,
    photoCount: Int,
    onApply: (Recipe) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }
    CreativePanel("Templates", modifier) {
        OutlinedTextField(query, { query = it }, label = { Text("Find a template") })
        val matches = FilterRecipes()(recipes, query, photoCount)
        if (matches.isEmpty()) Text("No matching templates")
        matches.forEach { r -> OutlinedButton(onClick = { onApply(r) }) { Text(r.id) } }
    }
}
