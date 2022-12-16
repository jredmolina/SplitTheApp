package cs4750.splitthebill

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import androidx.lifecycle:lifecycle-livedata-core

const val TAG = "PersonListFragment"
class PersonListFragment: Fragment() {

    private lateinit var personRecyclerView: RecyclerView
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var peopleAddImageView: ImageView
    private var adapter: PersonAdapter? = null

    private val personListViewModel: PersonListViewModel by lazy {
        ViewModelProviders.of(this).get(PersonListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

/*        val peopleObserver = Observer<Int> { num ->
           personListViewModel.persons.add(Person())
           personRecyclerView.adapter!!.notifyItemInserted(num - 1)
       }

       // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
       personListViewModel.numOfPeople.observe(this, peopleObserver)*/
    }

    fun addPerson(position : Int){
        personRecyclerView.adapter!!.notifyItemInserted(position)
    }

    fun removePerson(position : Int){
        personRecyclerView.adapter!!.notifyItemRemoved(position)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_person_list, container, false)


        personRecyclerView =
            view.findViewById(R.id.person_recycler_view) as RecyclerView
        personRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()
        return view
    }

    fun calculateIndividualSubtotal(position: Int): Double {

        var individualSubtotal = personListViewModel.persons[position].items.sumOf{ it.price }   // sumOf is a built in method that sums all elements in an object
        return individualSubtotal

    }


    // Updates UI by passing persons array back to adapter
    fun updateUI() {
        val persons = personListViewModel.persons
        val itemCount = persons.size
        for (i in 0 until itemCount){
            persons[i].total = calculateIndividualSubtotal(i)
        }
        adapter = PersonAdapter(persons)
        personRecyclerView.adapter = adapter
    }

    // Displays view for each person
    private inner class PersonHolder(view: View)
        : RecyclerView.ViewHolder(view){
        val titleTextView: TextView = itemView.findViewById(R.id.person_title)
        val itemRecycler: RecyclerView = view.findViewById(R.id.item_recycler_view)
        val totalTextView: TextView = view.findViewById(R.id.person_total)
        val addItemImage: ImageView = view.findViewById(R.id.item_add_imageView)
        val editPersonImage: ImageView = view.findViewById(R.id.editImageView)
        val deletePersonImage: ImageView = view.findViewById(R.id.deletePersonImage)
        val personTitleEditText: EditText = view.findViewById(R.id.person_title_edit)
        val minimizeImage: ImageView = view.findViewById(R.id.minimizeImageView)
        val minimizeImageUp: ImageView = view.findViewById(R.id.minimizeImageViewUp)
    }

    // Connects data to Display
    private inner class PersonAdapter(var persons: MutableList<Person>)
        : RecyclerView.Adapter<PersonHolder>(){

        // Combines Person RecyclerView and Item RecyclerView
        private var viewPool = RecyclerView.RecycledViewPool()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonHolder {
            val view = layoutInflater.inflate(R.layout.list_item_person, parent, false)

            return PersonHolder(view)
        }

        // Gets Person count for person recyclerView
        override fun getItemCount() = persons.size

        // Connects data of each individual person to display
        override fun onBindViewHolder(holder: PersonHolder, position: Int) {
            val person = persons[position]

            if(!person.isVisible){
                holder.itemRecycler.setVisibility(View.GONE)
                holder.minimizeImage.setVisibility(View.VISIBLE)
                holder.minimizeImageUp.setVisibility(View.INVISIBLE)
            }else{
                holder.itemRecycler.setVisibility(View.VISIBLE)
                holder.minimizeImage.setVisibility(View.INVISIBLE)
                holder.minimizeImageUp.setVisibility(View.VISIBLE)
            }

            holder.apply {
                var isEditable = false

                titleTextView.setText(person.name)
                totalTextView.setText("Total Owed: " + person.total.toString())

                addItemImage.setOnClickListener {
                    person.items.add(Item("Item", 0.00))
                    if(!person.isVisible){
                        person.isVisible = true
                    }
                    updateUI()
                }

                deletePersonImage.setOnClickListener{
                    persons.removeAt(position)
                    personRecyclerView.adapter!!.notifyItemRemoved(position)
                }

                editPersonImage.setOnClickListener{
                    if(!isEditable){
                        deletePersonImage.setVisibility(View.VISIBLE)
                        personTitleEditText.setVisibility(View.VISIBLE)
                        titleTextView.setVisibility(View.INVISIBLE)
                        addItemImage.setVisibility(View.INVISIBLE)
                        personTitleEditText.setText(titleTextView.text.toString())

                        val itemCount = holder.itemRecycler.adapter!!.itemCount
                        for (i in 0 until itemCount){
                            val newHolder = itemRecycler.findViewHolderForAdapterPosition(i)
                            val itemName: TextView = newHolder?.itemView!!.findViewById(R.id.item_title)
                            val itemPrice: TextView = newHolder.itemView.findViewById(R.id.item_price)
                            val itemNameEdit: EditText = newHolder.itemView.findViewById(R.id.item_title_edit)
                            val itemPriceEdit: EditText = newHolder.itemView.findViewById(R.id.item_price_edit)
                            val itemDeleteImage: ImageView = newHolder.itemView.findViewById(R.id.item_delete)

                            itemPriceEdit.setFilters(arrayOf<InputFilter>(MainActivity.DecimalDigitsInputFilter(8, 2)))

                            itemName.setVisibility(View.INVISIBLE)
                            itemPrice.setVisibility(View.INVISIBLE)
                            itemNameEdit.setVisibility(View.VISIBLE)
                            itemPriceEdit.setVisibility(View.VISIBLE)
                            itemDeleteImage.setVisibility(View.VISIBLE)

                            itemNameEdit.setText(itemName.text.toString())
                            itemPriceEdit.setText(itemPrice.text.toString())
                        }

                        isEditable = true
                    }
                    else{
                        titleTextView.setVisibility(View.VISIBLE)
                        addItemImage.setVisibility(View.VISIBLE)
                        deletePersonImage.setVisibility(View.INVISIBLE)
                        personTitleEditText.setVisibility(View.INVISIBLE)
                        titleTextView.text = personTitleEditText.text.toString()
                        person.name = titleTextView.text.toString()

                        val itemCount = holder.itemRecycler.adapter!!.itemCount
                        for (i in 0 until itemCount){
                            val newHolder = itemRecycler.findViewHolderForAdapterPosition(i)
                            val itemName: TextView = newHolder?.itemView!!.findViewById(R.id.item_title)
                            val itemPrice: TextView = newHolder.itemView.findViewById(R.id.item_price)
                            val itemNameEdit: EditText = newHolder.itemView.findViewById(R.id.item_title_edit)
                            val itemPriceEdit: EditText = newHolder.itemView.findViewById(R.id.item_price_edit)
                            val itemDeleteImage: ImageView = newHolder.itemView.findViewById(R.id.item_delete)

                            itemPriceEdit.setFilters(arrayOf<InputFilter>(MainActivity.DecimalDigitsInputFilter(8, 2)))

                            itemName.setVisibility(View.VISIBLE)
                            itemPrice.setVisibility(View.VISIBLE)
                            itemNameEdit.setVisibility(View.INVISIBLE)
                            itemPriceEdit.setVisibility(View.INVISIBLE)
                            itemDeleteImage.setVisibility(View.INVISIBLE)

                            itemName.text = itemNameEdit.text.toString()
                            itemPrice.text = itemPriceEdit.text.toString()
                            person.items[i].name = itemName.text.toString()
                            person.items[i].price = itemPrice.text.toString().toDouble()

                            updateUI()
                        }

                        isEditable = false
                    }
                }

                minimizeImage.setOnClickListener{
                    if(itemRecycler.adapter!!.itemCount > 0){
                        if(itemRecycler.isVisible){
                            itemRecycler.setVisibility(View.GONE)
                            person.isVisible = false
                            minimizeImage.setVisibility(View.VISIBLE)
                            minimizeImageUp.setVisibility(View.INVISIBLE)
                        }else{
                            itemRecycler.setVisibility(View.VISIBLE)
                            person.isVisible = true
                            minimizeImage.setVisibility(View.INVISIBLE)
                            minimizeImageUp.setVisibility(View.VISIBLE)
                        }
                    }else{
                        //do nothing
                    }
                }

                minimizeImageUp.setOnClickListener(){
                    if(itemRecycler.adapter!!.itemCount > 0){
                        if(itemRecycler.isVisible){
                            itemRecycler.setVisibility(View.GONE)
                            person.isVisible = false
                            minimizeImage.setVisibility(View.VISIBLE)
                            minimizeImageUp.setVisibility(View.INVISIBLE)
                        }else{
                            itemRecycler.setVisibility(View.VISIBLE)
                            person.isVisible = true
                            minimizeImage.setVisibility(View.INVISIBLE)
                            minimizeImageUp.setVisibility(View.VISIBLE)
                        }
                    }else{
                        //do nothing
                    }
                }
            }



            val layoutManager = LinearLayoutManager(holder.itemRecycler.context)
            layoutManager.setInitialPrefetchItemCount(person.items.size)


            // Sets recyclerView for person's items list
            var itemAdapter = ItemAdapter(person.items)
            holder.itemRecycler.setLayoutManager(layoutManager)
            holder.itemRecycler.setAdapter(itemAdapter)
            holder.itemRecycler.setRecycledViewPool(viewPool)
        }
    }
    private inner class ItemHolder(view: View)
        :RecyclerView.ViewHolder(view){
        val titleTextView: TextView = itemView.findViewById(R.id.item_title)
        val priceTextView: TextView = itemView.findViewById(R.id.item_price)
        val itemNameEdit: EditText = itemView.findViewById(R.id.item_title_edit)
        val itemPriceEdit: EditText = itemView.findViewById(R.id.item_price_edit)
        val itemDeleteImage: ImageView = itemView.findViewById(R.id.item_delete)
    }

    private inner class ItemAdapter(var items: MutableList<Item>)
        :RecyclerView.Adapter<ItemHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            val view = layoutInflater.inflate(R.layout.list_item_personitems, parent, false)
            return ItemHolder(view)
        }

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ItemHolder, position: Int){
            val item = items[position]
            holder.apply{
                titleTextView.text = item.name
                priceTextView.text = item.price.toString()

                itemDeleteImage.setOnClickListener{
                    items.removeAt(position)
                    updateUI()
                }

            }
        }

    }

    companion object{
        fun newInstance(): PersonListFragment {
            return PersonListFragment()
        }

    }
}
