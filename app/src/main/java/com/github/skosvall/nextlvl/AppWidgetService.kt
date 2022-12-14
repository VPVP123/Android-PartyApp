package com.github.skosvall.nextlvl

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class AppWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return StackRemoteViewsFactory(this.applicationContext, intent)
    }


    internal inner class StackRemoteViewsFactory(private val context: Context, intent: Intent) :
        RemoteViewsFactory {
        private lateinit var statementList: MutableList<String>

        override fun onCreate() {
            val db = FirebaseFirestore.getInstance()
            val getStatements =
                db.collection("mobileGamesData").document("neverHaveIEver").collection("english")
                    .document("statements")

            statementList = mutableListOf()

            getData(getStatements)
        }

        override fun onDataSetChanged() {
        }

        private fun getData(getStatements: DocumentReference): Task<DocumentSnapshot> {
            return getStatements.get()
                .addOnSuccessListener { statement ->
                    if (statement != null) {
                        Log.d("exist", "DocumentSnapshot data: ${statement.data}")
                        val myArray = statement.get("statements") as List<String>?
                        if (myArray != null) {
                            for (item in myArray) {
                                statementList.add(item)
                            }
                            statementList.shuffle()
                            val updateWidgetIntent = Intent(context, AppWidget::class.java)
                            updateWidgetIntent.action = AppWidget.ACTION_DATA_UPDATED
                            context.sendBroadcast(updateWidgetIntent)
                        }
                    }
                }
                .addOnFailureListener {
                    statementList.add(getString(R.string.db_error_message))
                    val updateWidgetIntent = Intent(context, AppWidget::class.java)
                    updateWidgetIntent.action = AppWidget.ACTION_DATA_UPDATED
                    context.sendBroadcast(updateWidgetIntent)
                }
        }

        override fun onDestroy() {}

        override fun getCount(): Int {
            return statementList.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            return RemoteViews(context.packageName, R.layout.widget_item).apply {
                setTextViewText(R.id.widget_item_text, statementList[position])
            }
        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }

    }
}