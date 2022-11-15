package cs4750.splitthebill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity()
{
    // initialize counters
    var numberOfPeople = 2
    var tipAmount = 0.00
    var taxResult = 0.00
    var subtotalResult = 0.00

    private lateinit var numberOfPeopleTextView: TextView
    private lateinit var peopleSubtractImageView: ImageView
    private lateinit var peopleAddImageView: ImageView

    private lateinit var tipAmountTextView: TextView
    private lateinit var tipSubtractImageView: ImageView
    private lateinit var tipAddImageView: ImageView

    private lateinit var taxResultTextView: TextView
    private lateinit var subtotalResultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bill_page)

        numberOfPeopleTextView = findViewById(R.id.number_of_people_textView)
        peopleSubtractImageView = findViewById(R.id.people_subtract_imageView)
        peopleAddImageView = findViewById(R.id.people_add_imageView)

        tipAmountTextView = findViewById(R.id.tip_amount_textView)
        tipSubtractImageView = findViewById(R.id.tip_subtract_imageView)
        tipAddImageView = findViewById(R.id.tip_add_imageView)

        taxResultTextView = findViewById(R.id.tax_result_textView)
        subtotalResultTextView = findViewById(R.id.subtotal_result_textView)

        peopleSubtractImageView.setOnClickListener {
            removePerson()
            checkNumberOfPeople()
        }

        peopleAddImageView.setOnClickListener {
            addPerson()
            checkNumberOfPeople()
        }

        tipSubtractImageView.setOnClickListener {
            decreaseTaxAmount()
            checkTipAmount()
        }

        tipAddImageView.setOnClickListener {
            increaseTaxAmount()
            checkTipAmount()
        }

        checkNumberOfPeople()
        checkTipAmount()
    }   // end onCreate function

    private fun removePerson()
    {
        numberOfPeople--
        numberOfPeopleTextView.setText(numberOfPeople.toString())
    }   // end removePerson function

    private fun addPerson()
    {
        numberOfPeople++
        numberOfPeopleTextView.setText(numberOfPeople.toString())
    }   // end addPerson function

    private fun decreaseTaxAmount()
    {
        tipAmount -= 5
        tipAmountTextView.setText(tipAmount.toString())
    }   // end removePerson function

    private fun increaseTaxAmount()
    {
        tipAmount += 5
        tipAmountTextView.setText(tipAmount.toString())
    }   // end removePerson function

    private fun checkNumberOfPeople()
    {
        if (numberOfPeople == 2)    // hide subtract button so user can't go below 2 people
        {
            peopleSubtractImageView.setVisibility(View.INVISIBLE)
        }
        else
        {
            peopleSubtractImageView.setVisibility(View.VISIBLE)
        }
    }

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

}   // end MainActivity class