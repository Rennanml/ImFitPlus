package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.R
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.UserEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LogAdapter(
    private val context: Context,
    private val dataSource: List<UserEntity>
) : BaseAdapter() {

    val df = DecimalFormat("0.00")

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.list_item_log, parent, false)

            viewHolder = ViewHolder()
            viewHolder.imcText = view.findViewById(R.id.log_imc_result_text_tv)
            viewHolder.tbmText = view.findViewById(R.id.log_tbm_result_text_tv)
            viewHolder.dateText = view.findViewById(R.id.log_date_tv)
            viewHolder.nameText = view.findViewById(R.id.log_name_tv)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val userEntity = getItem(position) as UserEntity

        val dateObject = LocalDateTime.parse(userEntity.registerDate)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

        viewHolder.imcText?.text = "IMC: ${df.format(userEntity.imc)}"
        viewHolder.tbmText?.text = "TMB: ${userEntity.tmb}"
        viewHolder.nameText?.text = userEntity.name
        viewHolder.dateText?.text = dateObject.format(formatter)

        return view
    }

    private class ViewHolder {
        var imcText: TextView? = null
        var tbmText: TextView? = null
        var nameText: TextView? = null
        var dateText: TextView? = null
    }
}