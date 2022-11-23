package com.example.SMB

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), DialogFragmentItem.AddDialogListener {

    private var shopList: MutableList<ShoppingListItem> = ArrayList()
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var db: RoomDB
    private lateinit var sp: SharedPreferences

    override fun onStart(){
        super.onStart()


        setFab()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        sp = getSharedPreferences("Prefs", Context.MODE_PRIVATE)



        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewAdapter(shopList)
        recyclerView.adapter = adapter

        db = Room.databaseBuilder(applicationContext, RoomDB::class.java, "sl_db").build()
        loadShopListItems()

        var fab = findViewById<FloatingActionButton>(R.id.fab)
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {

            val dialog = DialogFragmentItem()
            dialog.show(supportFragmentManager, "AskNewItemDialogFragment")
        }

        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {

            Google(this)
        }




        initSwipe()
        checkTheme()
        setFab()

    }

    private fun initSwipe() {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val handler = Handler(Handler.Callback {
                    Toast.makeText(
                        applicationContext,
                        it.data.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                    adapter.update(shopList)
                    true
                })
                val id = shopList[position].id
                shopList.removeAt(position)
                Thread(Runnable {
                    db.DAO().delete(id)
                    val message = Message.obtain()
                    message.data.putString("message", "Item removed")
                    handler.sendMessage(message)
                }).start()
            }

            override fun onMove(
                p0: RecyclerView,
                p1: RecyclerView.ViewHolder,
                p2: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun loadShopListItems() {
        val handler = Handler(Handler.Callback {
            Toast.makeText(applicationContext, it.data.getString("message"), Toast.LENGTH_SHORT)
                .show()
            adapter.update(shopList)
            true
        })
        Thread(Runnable {
            shopList = db.DAO().getAll()
            val message = Message.obtain()
            if (shopList.size > 0)
                message.data.putString("message", "List loaded")
            else
                message.data.putString("message", "List is empty")
            handler.sendMessage(message)
        }).start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        if(id == R.id.action_settings) Setting(this)
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun Setting(context: Context) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)

    }

    fun Google(context: Context) {
        val intent = Intent(this, WebActivity::class.java)
        startActivity(intent)


    }

    override fun onDialogPositiveClick(item: ShoppingListItem) {
        val handler = Handler(Handler.Callback {
            Toast.makeText(applicationContext, it.data.getString("message"), Toast.LENGTH_SHORT)
                .show()
            adapter.update(shopList)
            true
        })
        Thread(Runnable {
            val id = db.DAO().insert(item)
            item.id = id.toInt()
            shopList.add(item)
            val message = Message.obtain()
            message.data.putString("message", "Item added to list")
            handler.sendMessage(message)
            val intent1 = Intent()
            intent1.component = ComponentName("com.example.smb2", "com.example.smb2.MyReceiver")
            intent1.putExtra("name", item.name)
            intent1.putExtra("count", item.count)
            intent1.putExtra("price", item.price)
            sendBroadcast(intent1,"smb.permission")



        }).start()

        
    }

    private fun checkTheme() {

        if (sp.getBoolean("darkMode", true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            delegate.applyDayNight()
        } else if (sp.getBoolean("darkMode", false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            delegate.applyDayNight()
        }
    }
    fun setFab(){
        var fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.show()
        if(sp.getBoolean("bigFont",true)){
            fab.show()
        }
        if(sp.getBoolean("bigFont",false)){
            fab.hide()
        }

    }



}