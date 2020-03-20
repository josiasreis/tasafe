package br.com.tasafe.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.tasafe.model.data.Site
import br.com.tasafe.tasafe.R


class SiteAdapter (var c: Context): RecyclerView.Adapter<SiteAdapter.MyHolder>() {
    var listener: ListenerSiteAdapter? = null
    var contas = emptyList<Site>() // Cached copy of words
    var selectedItem:Site? = null
    lateinit var currentView:View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

        val mView =
            LayoutInflater.from(parent.context).inflate(R.layout.site_list_item, parent, false)

        return MyHolder(mView)
    }

    override fun getItemCount(): Int = contas.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        if(contas.isNotEmpty()){
            val item = contas[position]
            holder.tvNomeSite.text = item.nomeSite
            holder.tvUrlSite.text = item.urlSite
            holder.tvUsuario.text = item.usuario
            //CASO QUEIRA MOSTRAR A SENHA NA PROPRIA LINHA DESCOMENTAR
          /*  holder.tvPass.text = item.decrypt*/
            if("".equals(item.imagem)){
                item.nomeSite.let {
                    if(item.nomeSite.length > 1){
                        holder.tvIcon.text = item.nomeSite.subSequence(0,1).toString().toUpperCase()
                    }
                }
            }else{
                //TODO IMPLEMENTAR UM SERVICE PRA PEGAR O ICONE DO SITE
                //esconde o tvIcon e mostra a image view com o picasso
            }
            // Click item
            currentView = holder.container
            holder.itemView.setOnClickListener {
                if(selectedItem != null && selectedItem!!.idSite == contas.get(position).idSite){
                    applyUnSelectedItemStyle(holder.container)
                    selectedItem =null
                    listener?.itemUnSelected(position)
                }else if(selectedItem != null && selectedItem!!.idSite != contas.get(position).idSite){
                    unSelectedCurrentItem()
                    selectedItem =null
                    listener?.itemUnSelected(position)
                }else{
                    listener?.itemClicked(position)
                    unSelectedCurrentItem()
                }
            }

            holder.itemView.setOnLongClickListener{
                applySelectedItemStyle(holder.container)
                selectedItem = contas.get(position)
                listener?.itemLongClicked(position)
                true
            }

            holder.ivShowPass.setOnClickListener {
                listener?.showPassClicked(position)
                //CASO QUEIRA MOSTRAR A SENHA NA PROPRIA LINHA DESCOMENTAR
           /*     holder.tvPass.visibility = View.VISIBLE*/
            }
        }
    }

    /*fun showPass(position: Int,pass:String){
        contas.get(position).decrypt = pass
       notifyDataSetChanged()
    }
*/


    fun setMyListener(listener: ListenerSiteAdapter) {
        this.listener = listener
    }

    fun applySelectedItemStyle(container: View) {
        container.setBackgroundColor(ContextCompat.getColor(c,
            R.color.colorPrimaryLight))
    }

    fun applyUnSelectedItemStyle(container: View) {
        container.setBackgroundColor(ContextCompat.getColor(c,
            R.color.colorBackground))
    }

    fun updateListSetChange(sites: List<Site>) {
        contas.let {
            this.contas = sites
            notifyDataSetChanged()
        }
    }

    fun unSelectedCurrentItem() {
        currentView.setBackgroundColor(ContextCompat.getColor(c,
            R.color.colorBackground))
    }

    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNomeSite: TextView = view.findViewById(R.id.tvNomeSite)
        val tvUrlSite: TextView = view.findViewById(R.id.tvUrlSite)
        val tvUsuario: TextView = view.findViewById(R.id.tvUsuario)
        val tvIcon: TextView = view.findViewById(R.id.tvIcon)
        //CASO QUEIRA MOSTRAR A SENHA NA PROPRIA LINHA DESCOMENTAR
    /*    val tvPass: TextView = view.findViewById(R.id.tvPass)*/
        val ivShowPass: ImageView = view.findViewById(R.id.ivShowPass)
        val container: View = view.findViewById(R.id.container)
    }
}

