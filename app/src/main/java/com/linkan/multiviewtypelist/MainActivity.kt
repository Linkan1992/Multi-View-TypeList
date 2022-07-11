package com.linkan.multiviewtypelist

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.linkan.multiviewtypelist.adapter.MultiViewListAdapter
import com.linkan.multiviewtypelist.databinding.ActivityMainBinding
import com.linkan.multiviewtypelist.util.UtilFunction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

     lateinit var mDataBinding : ActivityMainBinding
     lateinit var listItemAdapter : MultiViewListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initActionBar()
        initRecyclerView()
    }


    private fun initActionBar() {
        setSupportActionBar(mDataBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        title = getString(R.string.item_list)
    }

    private fun initRecyclerView() {
        mDataBinding.rviewItem.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            //   val dividerItemDecoration = DividerItemDecoration(context,DividerItemDecoration.VERTICAL)
            //   addItemDecoration(dividerItemDecoration)
            addItemDecoration(object : DividerItemDecoration(context, LinearLayoutManager.VERTICAL){
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val position = parent.getChildAdapterPosition(view)
                    // if layout reverse == true, hide the divider for the last child
                    // since layout is reversed hence last item is at first hence position == 0
                    // if layout reverse == false, position == state.itemCount - 1
                    if(position == state.itemCount - 1){
                        outRect.setEmpty()
                    }else{
                        super.getItemOffsets(outRect, view, parent, state)
                    }
                }
            }.apply {
                setDrawable(ContextCompat.getDrawable(context, R.drawable.divider)!!)
            })
        }
        listItemAdapter = MultiViewListAdapter()
        mDataBinding.rviewItem.adapter = listItemAdapter

        loadItemListFromAsset()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.multi_view_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        else if (item.itemId == R.id.action_print_id) {
                Toast.makeText(this, "Print Id Clicked", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    fun loadItemListFromAsset(){
        GlobalScope.launch(Dispatchers.Default) {
            val itemList = UtilFunction.getItemList(this@MainActivity)
            listItemAdapter.submitList(itemList.toMutableList())
        }
    }

}