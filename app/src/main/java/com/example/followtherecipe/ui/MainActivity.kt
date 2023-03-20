package com.example.followtherecipe.ui

import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker.OnValueChangeListener
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.followtherecipe.R
import com.example.followtherecipe.ui.theme.FollowTheRecipeTheme
import com.example.followtherecipe.viewmodel.ConverterViewModel
import com.example.followtherecipe.viewmodel.TabItem
import com.example.followtherecipe.viewmodel.TabItemViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FollowTheRecipeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                   BasicLayout()
                }
            }
        }
    }
}

@Composable
fun BasicLayout(tabItemViewmodel : TabItemViewModel = viewModel()){
    val navController = rememberNavController()
    Scaffold (
        topBar= { TopAppBar {
            Text(
                text=" FollowTheRecipe",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
            } },
        content={
                 paddingValues ->
            Log.d("Padding values" , "$paddingValues")
            Box(
               modifier = Modifier
                   .fillMaxSize()
                   .padding(paddingValues)
            ) {
                MyNavController(navController = navController)
            }},
        bottomBar = { MyBottomNavigation(tabItemViewmodel.items,navController) }
    )
}

@Composable
fun MyBottomNavigation(items: List<TabItem> , navController: NavController){
    var selectedItem by remember { mutableStateOf(0) }

    BottomNavigation() {
        items.forEachIndexed { index, item ->
            BottomNavigationItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem == index
                    navController.navigate(item.route)
                          },
                icon ={ Icon(item.icon, contentDescription = null)},
                label = {Text(item.label)}
            )

            }
        }
}

@Composable
fun MainScreen(){
    Card(
        modifier = Modifier.fillMaxWidth().padding(20.dp).fillMaxHeight(0.5f)

    ){
        Image(
            painter = painterResource(id = R.drawable.image),
            contentDescription = stringResource(id = R.string.home_image_description)
        )
    }

}

@Composable
fun Converter(converterViewModel: ConverterViewModel = viewModel()){
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy((8.dp))
    ){
        ConverterHeading()
        QuantityField(quantityInput = converterViewModel.quantityInput, onValueChange = {converterViewModel.changeQuantityInput(it.replace(",","."))} )
        ConverterType(milliliter = converterViewModel.milliliter, setTypeMilliliter = { converterViewModel.milliliter =it})
        IngredientList(onClick = { converterViewModel.unity = it })
        ConvertResult(converterViewModel.result)
    }
}

@Composable
fun Recipe(){
    Text("Recipe")
}

@Composable
fun MyNavController(navController:NavHostController){
    NavHost(
        navController = navController,
        startDestination = "Home"
    ){
        composable(route ="Home"){
            MainScreen()
        }
        composable(route="Converter"){
            Converter()
        }
        composable(route = "Recipe"){
            Recipe()
        }
    }
}



@Composable
fun ConverterHeading(){
    Text(
        text= stringResource(R.string.ConverterTitle),
        fontSize=24.sp,
        textAlign= TextAlign.Center,
        color = MaterialTheme.colors.secondary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
    )
    Text(
        text= stringResource(R.string.ConverterContent),
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        color = Color.Black,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 16.dp)
    )
}

@Composable
fun QuantityField(quantityInput : String, onValueChange : (String) -> Unit){
    OutlinedTextField(
        value = quantityInput ,
        onValueChange = onValueChange,
        label = { Text(text="Enter quantity")},
        singleLine = true ,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
        )
}

@Composable
fun ConverterType(milliliter : Boolean , setTypeMilliliter:(Boolean) -> Unit){
    Column(Modifier.selectableGroup()){
        Row(verticalAlignment = Alignment.CenterVertically){
            RadioButton(
                selected = milliliter,
                onClick = { setTypeMilliliter(true) }
            )
            Text(text = "Milliliter")
        }
        Row(verticalAlignment = Alignment.CenterVertically){
            RadioButton(
                selected =!milliliter ,
                onClick = { setTypeMilliliter(false) }
            )
            Text(text="Tablespoon")
        }
    }
}

@Composable
fun ConvertResult(  result : Float){

    Text(
        text= " The result : $result ",
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        fontWeight = FontWeight.Bold

    )

}

@Composable
fun IngredientList(onClick:(Float) -> Unit){

    var expanded by  remember {mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(" Flour ") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var items = listOf("Flour" , "Sugar" , " Butter" , "Salt" , " Bicarbonate of soda" )

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column {
        OutlinedTextField(
            readOnly = true,
            value = selectedText ,
            onValueChange = {selectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            label = { Text("Select Ingredient")},
            trailingIcon = {
                Icon(icon,"contentDescription",
                    Modifier.clickable { expanded =!expanded }
                )
            }
        )
        DropdownMenu(
            expanded =  expanded,
            onDismissRequest = { expanded =false },
            modifier = Modifier
                .width(with(LocalDensity.current){textFieldSize.width.toDp()})
        ) {
            items.forEach {label ->
                DropdownMenuItem(onClick = {
                    selectedText=label

                    var unity : Float = when(label){
                        "Flour" -> 1.89f
                        "Sugar" -> 1.11f
                        "Butter" -> 1.09f
                        "Salt" -> 0.72f
                        "Bicarbonate of salt" -> 1.45f
                        else -> 0.0f
                    }
                    onClick(unity)
                    expanded = false
                }) {
                    Text(text = label)
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FollowTheRecipeTheme {
       MainScreen()
    }
}