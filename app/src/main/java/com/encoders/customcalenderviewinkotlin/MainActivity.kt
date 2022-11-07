package com.encoders.customcalenderviewinkotlin


import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    var selectedDates: List<Date>? = null
    var start: Date? = null
    var end: Date? = null
    var layoutCalender: LinearLayout? = null
    var custom_view: View? = null
    var initialDate: Date? = null
    var lastDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setInitializations()
        setCalenderView()
    }

    private fun setInitializations() {
        custom_view = findViewById<View>(R.id.custom_view)
        layoutCalender = findViewById<View>(R.id.layoutCalender) as LinearLayout
    }

    fun setCalenderView() {
        //Custom Events
        val eventObjects = EventObjects(1, "Birth", Date())
        eventObjects.color = R.color.purple_700
        val mEvents: MutableList<EventObjects> = ArrayList()
        mEvents.add(eventObjects)
        val parent = custom_view!!.parent as ViewGroup
        parent.removeView(custom_view)
        layoutCalender!!.removeAllViews()
        layoutCalender!!.orientation = LinearLayout.VERTICAL
        val calendarCustomView = CalendarCustomView(this@MainActivity, mEvents)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        calendarCustomView.layoutParams = layoutParams
        layoutCalender!!.addView(calendarCustomView)
        calendarCustomView.calendarGridView!!.onItemClickListener =
            OnItemClickListener { adapterView, view, i, l ->
                if (adapterView.adapter.getView(l.toInt(), null, null).alpha == 0.4f) {
                    Log.d("hello", "hello")
                } else {
                    val today = Calendar.getInstance()
                    today.time = Date()
                    val tapedDay = Calendar.getInstance()
                    tapedDay.time = adapterView.adapter.getItem(l.toInt()) as Date
                    val sameDay = tapedDay[Calendar.YEAR] == tapedDay[Calendar.YEAR] &&
                            today[Calendar.DAY_OF_YEAR] == tapedDay[Calendar.DAY_OF_YEAR]
                    if (today.after(tapedDay) && !sameDay) {
                        Toast.makeText(
                            this@MainActivity,
                            "You can't select previous date.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        if (initialDate == null && lastDate == null) {
                            lastDate = adapterView.adapter.getItem(l.toInt()) as Date
                            initialDate = lastDate
                        } else {
                            initialDate = lastDate
                            lastDate = adapterView.adapter.getItem(l.toInt()) as Date
                        }
                        if (initialDate != null && lastDate != null) calendarCustomView.setRangesOfDate(
                            makeDateRanges()!!
                        )
                    }
                }
                try {
                    Toast.makeText(
                        this@MainActivity, """Start Date: ${initialDate.toString()}
 End Date: ${lastDate.toString()}""", Toast.LENGTH_LONG
                    ).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
    }

    fun makeDateRanges(): MutableList<EventObjects>? {
        if (lastDate!!.after(initialDate)) {
            start = initialDate
            end = lastDate
        } else {
            start = lastDate
            end = initialDate
        }
        val eventObjectses: MutableList<EventObjects> = ArrayList()
        val gcal = GregorianCalendar()
        gcal.time = start
        while (!gcal.time.after(end)) {
            val d = gcal.time
            val eventObject = EventObjects("", d)
            eventObject.color = resources.getColor(R.color.purple_200)
            eventObjectses.add(eventObject)
            gcal.add(Calendar.DATE, 1)
        }
        return eventObjectses
    }
}
