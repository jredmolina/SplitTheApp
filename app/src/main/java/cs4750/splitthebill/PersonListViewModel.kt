package cs4750.splitthebill

import androidx.lifecycle.ViewModel

class PersonListViewModel: ViewModel() {

    // List of Persons
    val persons = mutableListOf<Person>(
        Person(),
        Person()
    )

    // Number of persons initialized to 2
    var numberOfPeople = 2

    // Numerical Values initialized to 0
    var tipAmount = 0.00
    var taxResult = 0.00
    var subtotalResult = 0.00

    init {

    }
}