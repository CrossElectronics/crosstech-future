package crosstech.future.logics.managers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import java.util.*

// TODO: Clean up
@SuppressLint("ClickableViewAccessibility")
abstract class SwipeManagerDeprecated(context: Context, private val recyclerView: RecyclerView) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
{
    val BUTTON_WIDTH = 32
    private var buttons: MutableList<UnderlayButton> = mutableListOf()
    private var gestureDetector: GestureDetector?
    private var swipedPos = -1
    private var swipeThreshold = 0.5f
    private val buttonsBuffer: MutableMap<Int, MutableList<UnderlayButton>> = mutableMapOf()
    private var recoverQueue: Queue<Int> = object : LinkedList<Int>()
    {
        override fun add(element: Int): Boolean
        {
            if (contains(element)) return false
            return super.add(element)
        }
    }
    private val gestureListener = object : GestureDetector.SimpleOnGestureListener()
    {
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean
        {
            for (button in buttons)
            {
                if (e != null && button.onClick(e.x, e.y)) break
            }
            return true
        }
    }

    init
    {
        gestureDetector = GestureDetector(context, gestureListener)
    }

    private val onTouchListener = object : View.OnTouchListener
    {
        override fun onTouch(view: View?, e: MotionEvent?): Boolean
        {
            if (swipedPos < 0 || e == null) return false
            val point = Point(e.x.toInt(), e.y.toInt())
            val swipedViewHolder = recyclerView.findViewHolderForAdapterPosition(swipedPos)!!
            val swipedItem = swipedViewHolder.itemView
            val rect = Rect()
            swipedItem.getGlobalVisibleRect(rect)

            if (e.action == MotionEvent.ACTION_DOWN || e.action == MotionEvent.ACTION_UP || e.action == MotionEvent.ACTION_MOVE)
            {
                if (rect.top < point.y && rect.bottom > point.y)
                {
                    gestureDetector?.onTouchEvent(e)
                }
                else
                {
                    recoverQueue.add(swipedPos)
                    swipedPos = -1
                    recoverSwipedItem()
                }
            }
            return false
        }
    }

    init
    {
        recyclerView.setOnTouchListener(onTouchListener)
        attachSwipe()
    }

    override fun onMove(r: RecyclerView, v: ViewHolder, t: ViewHolder) = false
    override fun getSwipeThreshold(viewHolder: ViewHolder) = swipeThreshold
    override fun getSwipeEscapeVelocity(defaultValue: Float) = 0.1f * defaultValue
    override fun getSwipeVelocityThreshold(defaultValue: Float) = 5f * defaultValue

    override fun onSwiped(viewHolder: ViewHolder, direction: Int)
    {
        val pos = viewHolder.adapterPosition
        if (swipedPos != pos) recoverQueue.add(swipedPos)
        swipedPos = pos
        if (buttonsBuffer.containsKey(swipedPos))
            buttons = buttonsBuffer[swipedPos]!!
        else
            buttons.clear()
        buttonsBuffer.clear()
        swipeThreshold = 0.5f * buttons.size * BUTTON_WIDTH
        recoverSwipedItem()
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    )
    {
        val pos = viewHolder.adapterPosition
        val translationX: Float
        val itemView = viewHolder.itemView

        if (pos < 0)
        {
            swipedPos = pos
            return
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE)
        {
            if (dX < 0)
            {
                var buffer = mutableListOf<UnderlayButton>()

                if (!buttonsBuffer.containsKey(pos))
                {
                    instantiateUnderlayButton(viewHolder, buffer)
                    buttonsBuffer[pos] = buffer
                }
                else
                {
                    buffer = buttonsBuffer[pos]!!
                }
                translationX = dX * buffer.size * BUTTON_WIDTH
                //translationX = dX * buffer.size * BUTTON_WIDTH / itemView.width
                drawButtons(c, itemView, buffer, pos, translationX)
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawButtons(
        c: Canvas,
        itemView: View,
        buffer: MutableList<UnderlayButton>,
        pos: Int,
        dX: Float
    )
    {
        var right = itemView.right.toFloat()
        val dButtonWidth = -dX / buffer.size
        for (button in buffer)
        {
            val left = right - dButtonWidth
            button.onDraw(
                c, RectF(
                    left, itemView.top.toFloat(), right,
                    itemView.bottom.toFloat()
                ), pos
            )
            right = left
        }
    }

    abstract fun instantiateUnderlayButton(
        viewHolder: ViewHolder,
        buffer: MutableList<UnderlayButton>
    )

    private fun attachSwipe()
    {
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    @Synchronized
    private fun recoverSwipedItem()
    {
        while (!recoverQueue.isEmpty())
        {
            val pos = recoverQueue.poll()!!
            if (pos > -1) recyclerView.adapter?.notifyItemChanged(pos)
        }
    }

    class UnderlayButton(
        private val text: String,
        private val color: Int,
        private val listener: UnderlayButtonOnClickListener,
        private val textSize: Float
    )
    {
        private lateinit var clickRegion: RectF
        private var pos: Int = 0

        fun onClick(x: Float, y: Float): Boolean
        {
            if (clickRegion.contains(x, y))
            {
                listener.onClick(pos)
                return true
            }
            return false
        }

        fun onDraw(c: Canvas, rect: RectF, pos: Int)
        {
            val p = Paint()
            // draws text
            p.color = color
            p.textSize = textSize
            p.textAlign = Paint.Align.LEFT

            val r = Rect()
            val cHeight = rect.height()
            val cWidth = rect.width()
            p.getTextBounds(text, 0, text.length, r)
            val x = cWidth / 2f - r.width() / 2f - r.left
            val y = cHeight / 2f + r.height() / 2f - r.bottom
            c.drawText(text.uppercase(Locale.getDefault()), rect.left + x, rect.top + y, p)

            clickRegion = rect
            this.pos = pos
        }
    }

    interface UnderlayButtonOnClickListener
    {
        fun onClick(pos: Int)
    }
}