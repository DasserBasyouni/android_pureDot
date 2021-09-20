package com.g7.soft.pureDot.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.ext.toFormattedDateTime
import com.g7.soft.pureDot.model.OrderTrackingModel
import com.g7.soft.pureDot.model.project.timeline.TimeLineModel
import com.github.vipulasri.timelineview.TimelineView
import kotlinx.android.synthetic.main.item_timeline.view.*

class TimeLineAdapter(private val currentStatus: Int?) : RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {

    private lateinit var mLayoutInflater: LayoutInflater
    private var orderTrackingList: List<OrderTrackingModel>? = null


    override fun getItemViewType(position: Int): Int =
        TimelineView.getTimeLineViewType(position, itemCount)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        if (!::mLayoutInflater.isInitialized)
            mLayoutInflater = LayoutInflater.from(parent.context)

        return TimeLineViewHolder(
            mLayoutInflater.inflate(R.layout.item_timeline, parent, false), viewType
        )
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        val context = holder.itemView.context
        val dataModel = getDataList(context, position)
        val isStatusReached = ApiConstant.OrderStatus.isReached(
            currentStatus = currentStatus,
            reachedStatus = ApiConstant.OrderStatus.fromPosition(position)?.value
        )
        val timelineColorResId = if (isStatusReached) R.color.warm_purple else R.color.divider

        holder.timeline.setMarkerColor(
            ContextCompat.getColor(
                context,
                timelineColorResId
            )
        )

        holder.timeline.setStartLineColor(
            ContextCompat.getColor(
                context,
                timelineColorResId
            ), getItemViewType(position)
        )

        val endLineColorResId = if (ApiConstant.OrderStatus.isBefore(
                currentStatus = currentStatus,
                reachedStatus = ApiConstant.OrderStatus.fromPosition(position)?.value
            )
        ) R.color.warm_purple else R.color.divider

        holder.timeline.setEndLineColor(
            ContextCompat.getColor(
                context,
                endLineColorResId
            ), getItemViewType(position)
        )


        holder.imageView.setImageResource(
            if (isStatusReached) dataModel.activatedImageResId
            else dataModel.deactivatedImageResId
        )
        holder.title.text = dataModel.title
        holder.subTitle.apply {
            if (dataModel.subTitle == null)
                this.visibility = View.GONE
            else
                this.text = dataModel.subTitle
        }
    }

    override fun getItemCount() = 6


    fun updateOrderStatus(orderTrackingModel: List<OrderTrackingModel>?) {
        this.orderTrackingList = orderTrackingModel
        notifyDataSetChanged()
    }

    private fun getDataList(context: Context, position: Int): TimeLineModel {
        val currentModel = orderTrackingList?.getOrNull(position)

        return listOf(
            TimeLineModel(
                activatedImageResId = R.drawable.ic_order_tracking_new,
                deactivatedImageResId = R.drawable.ic_order_tracking_new_deactivated,
                context.getString(R.string.label_new),
                //context.getString(R.string.msg_order_placed)
                if (currentModel?.status == ApiConstant.OrderStatus.NEW.value) context.getString(
                    R.string.conc_estimated_for,
                    orderTrackingList?.getOrNull(position)?.estimatedDate?.toFormattedDateTime(
                        context.getString(R.string.format_date_order_tracking)
                    )
                ) else null
            ),
            TimeLineModel(
                activatedImageResId = R.drawable.ic_confirmation,
                deactivatedImageResId = R.drawable.ic_confirmation_deactivated,
                context.getString(R.string.confirmed),
                if (currentModel?.status == ApiConstant.OrderStatus.CONFIRMED.value) context.getString(
                    R.string.conc_estimated_for,
                    orderTrackingList?.getOrNull(position)?.estimatedDate?.toFormattedDateTime(
                        context.getString(R.string.format_date_order_tracking)
                    )
                ) else null
            ),
            TimeLineModel(
                activatedImageResId = R.drawable.ic_being_shipped,
                deactivatedImageResId = R.drawable.ic_being_shipped_deactivated,
                context.getString(R.string.being_shipping),
                if (currentModel?.status == ApiConstant.OrderStatus.BEING_SHIPPED.value) context.getString(
                    R.string.conc_estimated_for,
                    orderTrackingList?.getOrNull(position)?.estimatedDate?.toFormattedDateTime(
                        context.getString(R.string.format_date_order_tracking)
                    )
                ) else null
            ),
            TimeLineModel(
                activatedImageResId = R.drawable.ic_order_tracking_picked,
                deactivatedImageResId = R.drawable.ic_order_tracking_picked_deactivated,
                context.getString(R.string.picked),
                if (currentModel?.status == ApiConstant.OrderStatus.PICKED.value) context.getString(
                    R.string.conc_estimated_for,
                    orderTrackingList?.getOrNull(position)?.estimatedDate?.toFormattedDateTime(
                        context.getString(R.string.format_date_order_tracking)
                    )
                ) else null
            ),
            TimeLineModel(
                activatedImageResId = R.drawable.ic_order_tracking_delivered,
                deactivatedImageResId = R.drawable.ic_order_tracking_delivered_deactivated,
                context.getString(R.string.delivered),
                if (currentModel?.status == ApiConstant.OrderStatus.DELIVERED.value) context.getString(
                    R.string.conc_estimated_for,
                    orderTrackingList?.getOrNull(position)?.estimatedDate?.toFormattedDateTime(
                        context.getString(R.string.format_date_order_tracking)
                    )
                ) else null
            ),
            TimeLineModel(
                activatedImageResId = R.drawable.ic_order_tracking_cancel,
                deactivatedImageResId = R.drawable.ic_order_tracking_cancel_deactivated,
                context.getString(R.string.canceled),
                if (currentModel?.status == ApiConstant.OrderStatus.CANCELED.value) context.getString(
                    R.string.conc_estimated_for,
                    orderTrackingList?.getOrNull(position)?.estimatedDate?.toFormattedDateTime(
                        context.getString(R.string.format_date_order_tracking)
                    )
                ) else null
            )
        )[position]
    }


    inner class TimeLineViewHolder(itemView: View, viewType: Int) :
        RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.imageView
        val title = itemView.titleTv
        val subTitle = itemView.subTitleTv
        val timeline = itemView.timeline

        init {
            timeline.initLine(viewType)
        }
    }
}
