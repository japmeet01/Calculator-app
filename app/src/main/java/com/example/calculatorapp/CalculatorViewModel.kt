package com.example.calculatorapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    private val inputLiveData= MutableLiveData<String>()
    private var lastNumeric: Boolean = false
    private var lastDecimal: Boolean = false
    private var isToggled=false

    init {
        inputLiveData.value = ""
    }

    fun getInputLiveData(): LiveData<String> {
        return inputLiveData
    }

    fun onDig(value: String) {
        val currentInput = inputLiveData.value ?: ""
        if(currentInput == "Infinity" ||currentInput == "-Infinity"){
        }else{
            inputLiveData.value = currentInput + value
            lastNumeric = true
        }
    }
    // to check the condition if operator can be used
    fun check(value:String){
        val str=inputLiveData.value?:""
        if (lastNumeric && !isOperatorAdded(str)) {
            lastNumeric = false
            lastDecimal = false
            onOperate(value)
        }
    }

    fun onOperate(value: String) {
        val currentInput = inputLiveData.value ?: ""
        if(currentInput == "Infinity" ||currentInput == "-Infinity"){}
        else{
            inputLiveData.value = currentInput + value
            lastNumeric = false
            lastDecimal = false
        }

    }

    fun clearInput() {
        inputLiveData.value = ""
        lastNumeric = false
        lastDecimal = false
        isToggled=false
    }



    fun decimalPt(){
        val str=inputLiveData.value ?: ""
        if(str=="Infinity" || str=="-Infinity"){

        }else{
            var containsOperator=false
            if(isOperatorAdded(str)){
                containsOperator=true
            }

            // there is no second operand, so no decimal req as already present
            if(!containsOperator && str.contains('.')){
                lastDecimal = true
            }
            if (lastNumeric && !lastDecimal) {
                inputLiveData.value=inputLiveData.value+"."
                lastNumeric = false
                lastDecimal = true
            }
        }

    }

    fun removeLastInput() {
        var str = inputLiveData.value ?: ""
        if(str=="Infinity"||str=="-Infinity"){
            inputLiveData.value = ""
            lastNumeric = false
            lastDecimal = false
        }else{

            // if only one element->clear
        if (str.length == 1) {
            clearInput()
            lastNumeric = false
            lastDecimal = false
        } else if (str.isNotEmpty()) {
            //second last is not decimal, so updating lastNumeric
            if(isLastOperator(str)){
                if(str.substring(str.length-2,str.length-1)!="."){
                    lastNumeric = true
                }
            }
            // if last element is decimal
            if(str.substring(str.length-1,str.length)=="." && (!isOperatorAdded(str) && !str.contains('.'))){
                lastNumeric = true
                lastDecimal = false
            }
            //if second last decimal
            if(str.substring(str.length-2,str.length-1)=="."){
                lastDecimal=true
                lastNumeric = false
            }

            if(str.substring(str.length-1,str.length)=="."){
                lastNumeric = true
                lastDecimal = false
            }
            str=str.substring(0,str.length-1)
            inputLiveData.value = str
        }
    }
    }

    private fun isLastOperator(str:String):Boolean{
        if(str.substring(str.length-1,str.length)=="/"||str.substring(str.length-1,str.length)=="*"||str.substring(str.length-1,str.length)=="-"||str.substring(str.length-1,str.length)=="+"){
            return true
        }
        return false
    }
    private fun isOperatorAdded(value: String): Boolean {
        if(value.startsWith("-")){
            var str=value.substring(1)
            if (!(str.contains("/") || str.contains("*") || str.contains("-") || str.contains("+"))){
                return false
            }
        }
        return (value.contains("/") || value.contains("*") || value.contains("-") || value.contains("+"))

    }

    fun toggle() {
        val currentValue = inputLiveData.value

        //if we already have ex -1234, remove the -ve
        if (currentValue != null && currentValue.startsWith("-")) {
            inputLiveData.value = currentValue.substring(1)
            isToggled = false
        }
        // else concatinate -ve
        else if (!isToggled) {
            if (currentValue != null) {
                inputLiveData.value = "-$currentValue"
                isToggled = true
            }
        }
//        else {
//            if (currentValue != null && currentValue.startsWith("-")) {
//                inputLiveData.value = currentValue.substring(1)
//                isToggled = false
//            }
//        }
    }

    fun calculateResult() {
        if (lastNumeric) {
                var currentInput = inputLiveData.value ?: ""
                var prefix = ""
                if (currentInput.startsWith("-")) {
                    prefix = "-"
                    currentInput=currentInput.substring(1);
                }

                when {
                    currentInput.contains("/") -> {
                        val splitedValue = currentInput.split("/")

                        var one = splitedValue[0]
                        val two = splitedValue[1]

                        if (prefix.isNotEmpty()) {
                            one = prefix + one
                        }

                        var res= ((one.toDouble() / two.toDouble()).toString())
                            inputLiveData.value=res

                        // As output may contain a decimal, so to keep a check of that
                        if(currentInput.contains(".")){
                            lastDecimal=true
                        }
                        lastDecimal=false

                    }
                    currentInput.contains("*") -> {
                        val splitedValue = currentInput.split("*")

                        var one = splitedValue[0]
                        val two = splitedValue[1]

                        if (prefix.isNotEmpty()) {
                            one = prefix + one
                        }

                        var res= ((one.toDouble() * two.toDouble()).toString())

                            inputLiveData.value=res

                        if(currentInput.contains(".")){
                            lastDecimal=true
                        }
                        lastDecimal=false
                    }
                    currentInput.contains("-") -> {
                        val splitedValue = currentInput.split("-")

                        var one = splitedValue[0]
                        val two = splitedValue[1]

                        if (prefix.isNotEmpty()) {
                            one = prefix + one
                        }

                        var res= ((one.toBigDecimal() - two.toBigDecimal()).toString())

                            inputLiveData.value=res


                        if(currentInput.contains(".")){
                            lastDecimal=true

                        }
                        lastDecimal=false
                    }
                    currentInput.contains("+") -> {
                        val splitedValue = currentInput.split("+")

                        var one = splitedValue[0]
                        val two = splitedValue[1]

                        if (prefix.isNotEmpty()) {
                            one = prefix + one
                        }

                        var res= ((one.toBigDecimal() + two.toBigDecimal()).toString())

                            inputLiveData.value=res

                        if(currentInput.contains(".")){
                            lastDecimal=true
                        }
                        lastDecimal=false
                    }
                }

        }
    }

    // (3/1.5) gives 2.0 solves it but 2.02*2 gives 4.
//    private fun removeZeroAfterDot(result: String): String {
//
//        var value = result
//
//        if (result.contains(".0")) {
//            value = result.substring(0, result.length - 2)
//        }
//
//        return value
//    }
}
