package br.com.tasafe.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.tasafe.tasafe.R
import br.com.tasafe.tasafe.databinding.ActivityMySitesBinding
import br.com.tasafe.ui.adapter.ListenerSiteAdapter
import br.com.tasafe.ui.adapter.SiteAdapter
import br.com.tasafe.utils.NavigationUtil
import br.com.tasafe.utils.generateDashedLineDrawable
import br.com.tasafe.viewmodel.SiteViewModel
import kotlinx.android.synthetic.main.activity_my_sites.*

class MySitesActivity : BaseActivity() {

    private lateinit var adapter: SiteAdapter
    lateinit var viewModel: SiteViewModel
    var deleteView = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_sites)
        val actionbar = supportActionBar
        actionbar!!.title = resources.getString(R.string.titleMySytes)

        viewModel = ViewModelProvider(this).get(SiteViewModel::class.java)
        val binding: ActivityMySitesBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_my_sites)
        binding.lifecycleOwner = this
        setupRecycleView()
    }

    fun newSite(view: View) {
        val intent = Intent(this, NewSiteActivity::class.java)
        startActivity(intent)
    }

    private fun setupRecycleView() {
        this.let {
            recycleSites.layoutManager = LinearLayoutManager(it)
            recycleSites.layoutManager = LinearLayoutManager(this)

            val decoration =
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL).apply {
                    recycleSites.context.generateDashedLineDrawable(backgroundColor = Color.BLACK, strokeColor = Color.BLACK).let { it ->
                        setDrawable(it)
                    }
                }

            recycleSites.addItemDecoration(decoration)
            adapter = SiteAdapter(it)
            adapter.setMyListener(object : ListenerSiteAdapter {
                override fun itemClicked(position: Int) {
                    val intent = Intent(applicationContext, NewSiteActivity::class.java)
                    val site = viewModel.sites.value!![position]
                    intent.putExtra("idSite", site.idSite)
                    startActivity(intent)
                }

                override fun itemUnSelected(position: Int) {
                    deleteView = false
                    invalidateOptionsMenu()
                }

                override fun showPassClicked(position: Int) {
                    val site = viewModel.sites.value!![position]
                    val pass = viewModel.showPass(site)

                    val clipboardManager =
                        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData = ClipData.newPlainText("text", pass)
                    clipboardManager.setPrimaryClip(clipData)
                    Toast.makeText(applicationContext, resources.getString(R.string.senhaCopiada), Toast.LENGTH_LONG).show()
                }

                override fun itemLongClicked(position: Int) {
                    deleteView = true
                    invalidateOptionsMenu()
                }
            })

            recycleSites.adapter = adapter
            viewModel.sites.observe(this, Observer { sites ->
                sites?.let { adapter.updateListSetChange(sites) }
            })
        }
    }

    override fun onBackPressed() {
        removeSelecion()
        super.onBackPressed()
    }

    private fun removeSelecion() {
        deleteView = false
        invalidateOptionsMenu()
        adapter.unSelectedCurrentItem()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (deleteView) {
            menuInflater.inflate(R.menu.menu, menu)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemDelete -> {
                viewModel.delete(adapter.selectedItem!!)
                deleteView = false
                adapter.unSelectedCurrentItem()
                invalidateOptionsMenu()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
