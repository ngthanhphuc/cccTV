package de.stefanmedack.ccctv.ui.cards

import android.support.v17.leanback.widget.ImageCardView
import android.support.v17.leanback.widget.Presenter
import android.support.v4.content.ContextCompat
import android.support.v7.view.ContextThemeWrapper
import android.view.ViewGroup
import com.bumptech.glide.Glide
import de.stefanmedack.ccctv.R
import de.stefanmedack.ccctv.persistence.entities.Conference
import kotlin.properties.Delegates

class ConferenceCardPresenter : Presenter() {

    private var selectedBackgroundColor: Int by Delegates.notNull()
    private var defaultBackgroundColor: Int by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        defaultBackgroundColor = ContextCompat.getColor(parent.context, R.color.teal_900)
        selectedBackgroundColor = ContextCompat.getColor(parent.context, R.color.amber_800)

        val cardView = object : ImageCardView(ContextThemeWrapper(parent.context, R.style.GridCardTheme)) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        updateCardBackgroundColor(cardView, false)
        return Presenter.ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        if (item is Conference) {
            (viewHolder.view as ImageCardView).let {
                it.titleText = item.title
                it.contentText = item.acronym
                Glide.with(viewHolder.view.context)
                        .load(item.logoUrl)
                        .fitCenter() // TODO check why fitCenter does not work
                        .error(R.drawable.voctocat)
                        .into(it.mainImageView)
            }
        }
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        (viewHolder.view as ImageCardView).let {
            it.badgeImage = null
            it.mainImage = null
        }
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        val color = if (selected) selectedBackgroundColor else defaultBackgroundColor
        // both background colors should be set because the view's background is temporarily visible during animations.
        view.setBackgroundColor(color)
        view.setInfoAreaBackgroundColor(color)
    }
}