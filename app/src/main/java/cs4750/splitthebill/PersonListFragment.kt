package cs4750.splitthebill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

const val TAG = "PersonListFragment"
class PersonListFragment: Fragment() {

    private lateinit var personRecyclerView: RecyclerView
    private lateinit var itemRecyclerView: RecyclerView
    private var adapter: PersonAdapter? = null

    private val personListViewModel: PersonListViewModel by lazy {
        ViewModelProviders.of(this).get(PersonListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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


    // Updates UI by passing persons array back to adapter
    private fun updateUI() {
        val persons = personListViewModel.persons
        adapter = PersonAdapter(persons)
        personRecyclerView.adapter = adapter

    }

    // Displays view for each person
    private inner class PersonHolder(view: View)
        : RecyclerView.ViewHolder(view){
            val titleTextView: TextView = itemView.findViewById(R.id.person_title)
            val itemRecycler: RecyclerView = view.findViewById(R.id.item_recycler_view)
            val totalTextView: TextView = view.findViewById(R.id.person_total)
        }

    // Connects data to Display
    private inner class PersonAdapter(var persons: List<Person>)
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
            holder.apply {
                titleTextView.setText(person.name)
                totalTextView.setText("Total Owed: " + person.total.toString())
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
    }

    private inner class ItemAdapter(var items: List<Item>)
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

                }
            }

        }


    companion object{
        fun newInstance(): PersonListFragment {
            return PersonListFragment()
        }
    }


}