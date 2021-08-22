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

class TimeLineAdapter : RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {

    private lateinit var mLayoutInflater: LayoutInflater
    private var orderTrackingModel: OrderTrackingModel? = null


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
            currentStatus = orderTrackingModel?.status,
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
                currentStatus = orderTrackingModel?.status,
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
        holder.subTitle.text = dataModel.subTitle
    }

    override fun getItemCount() = 5


    fun updateOrderStatus(orderTrackingModel: OrderTrackingModel?) {
        this.orderTrackingModel = orderTrackingModel
        notifyDataSetChanged()
    }

    private fun getDataList(context: Context, position: Int) = listOf(
        TimeLineModel(
            activatedImageResId = R.drawable.ic_order_placed,
            deactivatedImageResId = R.drawable.ic_order_placed_deactivated,
            context.getString(R.string.order_placed),
            context.getString(R.string.msg_order_placed)
        ),
        TimeLineModel(
            activatedImageResId = R.drawable.ic_confirmation,
            deactivatedImageResId = R.drawable.ic_confirmation_deactivated,
            context.getString(R.string.confirmed),
            context.getString(
                R.string.conc_estimated_for,
                orderTrackingModel?.deliveredEstimatedDateTime?.toFormattedDateTime(
                    context.getString(R.string.format_date_order_tracking)
                )
            )
        ),
        TimeLineModel(
            activatedImageResId = R.drawable.ic_ready_for_delivery,
            deactivatedImageResId = R.drawable.ic_ready_for_delivery_deactivated,
            context.getString(R.string.ready_for_delivery),
            context.getString(
                R.string.conc_estimated_for,
                orderTrackingModel?.deliveredEstimatedDateTime?.toFormattedDateTime(
                    context.getString(R.string.format_date_order_tracking)
                )
            )
        ),
        TimeLineModel(
            activatedImageResId = R.drawable.ic_out_for_delivery,
            deactivatedImageResId = R.drawable.ic_out_for_delivery_deactivated,
            context.getString(R.string.out_for_delivery),
            context.getString(
                R.string.conc_estimated_for,
                orderTrackingModel?.outForDeliveryEstimatedDateTime?.toFormattedDateTime(
                    context.getString(R.string.format_date_order_tracking)
                )
            )
        ),
        TimeLineModel(
            activatedImageResId = R.drawable.ic_order_delivered,
            deactivatedImageResId = R.drawable.ic_order_delivered_deactivated,
            context.getString(R.string.delivered),
            context.getString(
                R.string.conc_estimated_for,
                orderTrackingModel?.shippingEstimatedDateTime?.toFormattedDateTime(
                    context.getString(R.string.format_date_order_tracking)
                )
            )
        )
    )[position]


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
