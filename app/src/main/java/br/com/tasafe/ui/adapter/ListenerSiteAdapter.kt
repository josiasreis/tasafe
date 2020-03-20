package br.com.tasafe.ui.adapter

interface ListenerSiteAdapter {
    fun itemClicked(position: Int)
    fun itemUnSelected(position: Int)
    fun showPassClicked(position: Int)
    fun itemLongClicked(position: Int)
}