package com.example.followtherecipe.viewmodel

import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.lang.Math.round

class ConverterViewModel : ViewModel() {

    var quantityInput by mutableStateOf("")
    var milliliter by mutableStateOf(true)
    var unity by mutableStateOf(1.89f)

    var result: Float = 0.0f
        get() {
            return calculate(quantity,unity, milliliter)
        }

    private var quantity: Float = 0.0f
        get() {
            return quantityInput.toFloatOrNull() ?: 0.0f
        }

    fun changeQuantityInput(value: String) {
        quantityInput = value
    }

    private fun calculate(quantity: Float, unity: Float , milliliter :Boolean ) : Float {
        var result : Float
        if (milliliter) {
            if (quantity > 0 ) {
                result=quantity * unity
            }else {
                result=0.0f
            }
        }else {
            if (quantity > 0 ) {
                result= round(quantity * 0.066f).toFloat()
            }else {
                result=0.0f
            }
        }
        return result
    }

}






