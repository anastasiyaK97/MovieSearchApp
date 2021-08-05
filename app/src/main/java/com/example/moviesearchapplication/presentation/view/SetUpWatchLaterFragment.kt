package com.example.moviesearchapplication.presentation.view

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.presentation.utilities.AlarmReceiver
import com.example.moviesearchapplication.presentation.viewmodel.MainViewModelFactory
import com.example.moviesearchapplication.presentation.viewmodel.SetUpWatchLaterViewModel
import java.util.*
import javax.inject.Inject

class SetUpWatchLaterFragment :  Fragment() {

    companion object {
        const val FILM_ID_EXTRA = "FILM_ID_EXTRA"
        const val FILM_NAME_EXTRA = "film name"

        const val LOG_TAG = "SetUpWatchLaterFragment"
        const val ALARM_ACTION = "notification about film"

        @JvmStatic
        fun newInstance(filmId: Int): SetUpWatchLaterFragment {
            val args = Bundle()
            args.putInt(FILM_ID_EXTRA, filmId)

            val newFragment = SetUpWatchLaterFragment()
            newFragment.arguments = args
            return newFragment
        }

    }
    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private val viewModel: SetUpWatchLaterViewModel by viewModels{viewModelFactory}

    var alarmManager: AlarmManager? = null
    private lateinit var dateTimeTextView: TextView
    private var date: Calendar = Calendar.getInstance()
    private var filmId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        App.instance.applicationComponent.inject(this)
        return inflater.inflate(R.layout.fragment_watch_later_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        filmId = arguments?.getInt(FILM_ID_EXTRA) ?: 0
        viewModel.setFilm(filmId)

        dateTimeTextView = view.findViewById(R.id.textDate)
        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        view.findViewById<Button>(R.id.button).setOnClickListener {onButtonClick()}
        view.findViewById<View>(R.id.datePickerButton).setOnClickListener {onDatePickerButtonClick()}
        setInitialDateTime()

    }

    private fun onButtonClick(){
        val filmName = viewModel.film.value?.title ?: ""

        val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
                action = ALARM_ACTION
                putExtra(FILM_NAME_EXTRA, filmName)
                putExtra(FILM_ID_EXTRA, filmId)
            }
        val alarmIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.YEAR, date.get(Calendar.YEAR));
                set(Calendar.MONTH, date.get(Calendar.MONTH)); // January has value 0
                set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
        }

       alarmManager?.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmIntent
        )
        viewModel.updateNotificationSettings()
        requireActivity().supportFragmentManager.popBackStack()

        Toast.makeText(requireActivity(), R.string.success_toast_text, Toast.LENGTH_SHORT).show()
    }

    private fun setInitialDateTime() {
        dateTimeTextView.text = DateUtils.formatDateTime(
            requireContext(),
            date.timeInMillis,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
    }

    private fun onDatePickerButtonClick(){
        val listener =
            OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                date.set(Calendar.YEAR, year)
                date.set(Calendar.MONTH, monthOfYear)
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                setInitialDateTime()
            }
        DatePickerDialog(
            requireContext(), listener,
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)
        )
            .show()
    }
}
