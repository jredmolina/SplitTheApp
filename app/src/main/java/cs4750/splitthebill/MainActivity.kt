package cs4750.splitthebill

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import java.util.regex.Matcher
import java.util.regex.Pattern
import cs4750.splitthebill.Person


private const val KEY_NUMOFPEOPLE= "numOfPeople"
private const val KEY_TIPAMOUNT= "tipAmount"
private const val KEY_TAXRESULT= "taxResult"
private const val KEY_SUBTOTALRESULT = "subTotalResult"

class MainActivity : AppCompatActivity()
{    // initialize counters
    public var numberOfPeople = 0
    public var tipAmount = 0.00
    public var taxResult = 0.00
    public var subtotalResult = 0.00

    private lateinit var numberOfPeopleTextView: TextView
    //private lateinit var peopleSubtractImageView: ImageView
    private lateinit var peopleAddImageView: ImageView

    private lateinit var tipAmountTextView: TextView
    private lateinit var tipSubtractImageView: ImageView
    private lateinit var tipAddImageView: ImageView

    private lateinit var taxResultTextView: TextView
    private lateinit var subtotalResultTextView: TextView
    private lateinit var dollarsignImageView: ImageView
    private lateinit var dollarsignImageView2: ImageView

    private val personListViewModel: PersonListViewModel by lazy{
        ViewModelProviders.of(this).get(PersonListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bill_page)

        // Fragment for recycler view
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null){
            val fragment = PersonListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }

        // saved values of settings values
        var numberOfPeople = savedInstanceState?.getInt(KEY_NUMOFPEOPLE, 2) ?: 2
        personListViewModel.numberOfPeople = numberOfPeople
        var tipAmount = savedInstanceState?.getDouble(KEY_TIPAMOUNT, 0.0) ?: 0.0
        personListViewModel.tipAmount = tipAmount
        var taxResult = savedInstanceState?.getDouble(KEY_TAXRESULT, 0.0) ?: 0.0
        personListViewModel.taxResult = taxResult
        var subtotalResult = savedInstanceState?.getDouble(KEY_SUBTOTALRESULT, 0.0) ?: 0.0
        personListViewModel.subtotalResult=subtotalResult




        // Displays settings values
        numberOfPeopleTextView = findViewById(R.id.number_of_people_textView)
        peopleAddImageView = findViewById(R.id.people_add_imageView)

        tipAmountTextView = findViewById(R.id.tip_amount_textView)
        tipSubtractImageView = findViewById(R.id.tip_subtract_imageView)
        tipAddImageView = findViewById(R.id.tip_add_imageView)

        taxResultTextView = findViewById(R.id.tax_result_textView)
        // Limit input value digits before zero to 8 and 2 after zero
        taxResultTextView.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))

        subtotalResultTextView = findViewById(R.id.subtotal_result_textView)
        // Limit input value digits before zero to 8 and 2 after zero
        subtotalResultTextView.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))

        dollarsignImageView = findViewById(R.id.dollarsign_imageView)
        dollarsignImageView2 = findViewById(R.id.dollarsign_imageView2)

        peopleAddImageView.setOnClickListener {
            addPerson()
        }

        tipSubtractImageView.setOnClickListener {
            decreaseTaxAmount()
            checkTipAmount()
        }

        tipAddImageView.setOnClickListener {
            increaseTaxAmount()
            checkTipAmount()
        }

        // Tap on the taxResultTextView to edit value
        taxResultTextView.setOnClickListener {

            val userText = taxResultTextView.text
            taxResultTextView.setText(userText)
            taxResultTextView.addTextChangedListener(textWatcher)
            taxResultTextView.clearFocus()

        }

        // Tap on the subtotalResultTextView to edit value
        subtotalResultTextView.setOnClickListener {

            val userText = subtotalResultTextView.text
            subtotalResultTextView.setText(userText)
            subtotalResultTextView.addTextChangedListener(textWatcher)
            subtotalResultTextView.clearFocus()
        }

        checkTipAmount()

    }   // end onCreate function

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (!tipAmountTextView.text.isEmpty() && !subtotalResultTextView.text.isEmpty()) {
                calculateIndividualTip()

            }

        }
        override fun beforeTextChanged(s: CharSequence, start: Int,
                                       count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int,
                                   before: Int, count: Int) {

        }
    }

    private fun removePerson()
    {
        numberOfPeople--
        numberOfPeopleTextView.setText(numberOfPeople.toString())

        //personListViewModel.numOfPeople.set(personListViewModel.numOfPeople.get() - 1)
        personListViewModel.persons.removeAt(personListViewModel.numberOfPeople - 1)
        personListViewModel.numberOfPeople--
        //Notify Recyler View Of Change

    }   // end removePerson function

    private fun addPerson()
    {
        numberOfPeople++
        numberOfPeopleTextView.setText(numberOfPeople.toString())

        //personListViewModel.numOfPeople.set(personListViewModel.numOfPeople.get() + 1)

        personListViewModel.persons.add(Person())
        personListViewModel.numberOfPeople++
        //Update Recycler View Of Change

    }


    private fun decreaseTaxAmount()
    {
        tipAmount -= 5
        tipAmountTextView.setText(tipAmount.toString()+"%")
    }   // end removePerson function

    private fun increaseTaxAmount()
    {
        tipAmount += 5
        tipAmountTextView.setText(tipAmount.toString()+"%")
    }   // end removePerson function

    private fun checkTipAmount()
    {
        if (tipAmount == 0.00)  // hide subtract button if tip amount is currently 0%
        {
            tipSubtractImageView.setVisibility(View.INVISIBLE)
        }
        else
        {
            tipSubtractImageView.setVisibility(View.VISIBLE)
        }

        if (tipAmount == 100.00)    // hide add button if tip amount is currently 100%
        {
            tipAddImageView.setVisibility(View.INVISIBLE)
        }
        else
        {
            tipAddImageView.setVisibility(View.VISIBLE)
        }

    }   // end checkTipAmount function

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(KEY_NUMOFPEOPLE,  personListViewModel.numberOfPeople)
        savedInstanceState.putDouble(KEY_TIPAMOUNT,  personListViewModel.tipAmount)
        savedInstanceState.putDouble(KEY_TAXRESULT,  personListViewModel.taxResult)
        savedInstanceState.putDouble(KEY_SUBTOTALRESULT,  personListViewModel.subtotalResult)

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    // Calculate the tip amount a person has to pay by ((tip percentage) *
    // (Entire bill subtotal)) / (number of people in the party)
    private fun calculateIndividualTip() {

        var individualTip = ((tipAmount / 100) * subtotalResultTextView.text.toString().toDouble()) / numberOfPeople
        Log.i(TAG, "Individual tip : $individualTip")
    }

    /**
     * Currently this is hardcoded to calculate the subtotal for the first person.( person in the [0] index )
     * I think we can change this function and have it call whenever a user edits their
     * food items in their Person object so each person has their own individualSubtotal
     * */

    private fun calculateIndividualSubtotal(): Double {

        var individualSubtotal = personListViewModel.persons[0].items.sumOf{ it.price }   // sumOf is a built in method that sums all elements in an object
        Log.i(TAG, "Individual subtotal : $individualSubtotal")
        return individualSubtotal

    }
    
    private fun calculateIndividualTax() {

        var individualSubtotal = calculateIndividualSubtotal()
        var individualTax = (individualSubtotal / subtotalResultTextView.text.toString().toDouble()) * taxResultTextView.text.toString().toDouble()
        Log.i(TAG, "Individual tax amount : $individualTax")

    }

    // Filter to limit tax and subtotal input decimal values
    class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) :
        InputFilter {
        var mPattern: Pattern
        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            val matcher: Matcher = mPattern.matcher(dest)
            return if (!matcher.matches()) "" else null
        }

        init {
            mPattern =
                Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?")
        }
    }

}   // end MainActivity class