package com.example.dtls_android.card

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.dtls_android.R
import com.example.dtls_android.resources.MyResources
import com.example.dtls_android.service.response.RecordsResponse

class ViewCard {
    private var viewAuthor: TextView
    private var viewDesc: TextView
    private var viewDate: TextView
    private var viewStatusIndicator: ImageView
    private var viewStatusText: TextView
    val context: Context
    val style = MyResources

    constructor(context: Context, card: View) {
        this.context = context
        viewAuthor = card.findViewById(R.id.viewCardAuthor)
        viewDesc = card.findViewById(R.id.viewCardDesc)
        viewDate = card.findViewById(R.id.viewCardDate)
        viewStatusIndicator = card.findViewById(R.id.viewCardStatusCircle)
        viewStatusText = card.findViewById(R.id.viewCardStatusText)
    }

    fun setCardValues(record: RecordsResponse): ViewCard {
        val formattedDate = CardUtils.getFormattedDate(record.dateModified, "L/dd/yy")
        viewAuthor.text = record.author
        viewDesc.text = record.description
        viewDate.text = context.getString(R.string.viewDateTextString, formattedDate)
        viewStatusText.text = record.status

        return this
    }

    fun decorateView(status: String): ViewCard {
        CardUtils.setIndicatorColor(status, viewStatusIndicator)
        CardUtils.setTextColor(this.context, status, viewStatusText)

        return this
    }
}