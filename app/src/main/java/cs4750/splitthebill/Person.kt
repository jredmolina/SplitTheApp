package cs4750.splitthebill

import android.util.Log
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList

class Person {

    // Name of person
    var name: String = "Person"

    // List of items a person is paying for
    val items = mutableListOf<Item>(
        Item("Item", 0.0),
    )

    // Total amount a person is paying
    var total: Double = 0.0


    // Adds blank item to item list
    fun addItem() {
        var item= Item()
        items+=item
    }

    // Calculates total owed by person
    fun calculateTotal(num:Double){
        total+=num
    }


}




