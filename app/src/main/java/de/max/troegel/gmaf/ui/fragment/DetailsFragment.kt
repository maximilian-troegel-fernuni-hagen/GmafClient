package de.max.troegel.gmaf.ui.fragment

import android.graphics.Color
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TableRow
import android.widget.TableRow.LayoutParams
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.Gson
import de.max.troegel.gmaf.R
import de.max.troegel.gmaf.app.extractWord
import de.max.troegel.gmaf.databinding.FragmentDetailsBinding
import de.max.troegel.gmaf.viewmodel.DetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class DetailsFragment : Fragment(), OnChartValueSelectedListener {

    private lateinit var binding: FragmentDetailsBinding

    private val args: DetailsFragmentArgs by navArgs()

    private val viewModel: DetailsViewModel by viewModel()

    private val onValueSelectedRectF = RectF()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        setupButtonObserver()
        viewModel.clearColorMap()
        Timber.i("Show details for ${Gson().toJson(args.media.gc)}")
        if (args.media.gc.dictionary.isNullOrEmpty()) {
            showEmptyMessage()
        } else {
            binding.emptyText.visibility = View.GONE
            generateTable()
            generateQuery()
            generateChart()
        }
        return binding.root
    }

    private fun showEmptyMessage() {
        binding.emptyText.visibility = View.VISIBLE
        binding.table.visibility = View.GONE
        binding.queryText.visibility = View.GONE
        binding.chart.visibility = View.GONE
    }

    private fun generateTable() {
        binding.table.visibility = View.VISIBLE
        binding.table.removeAllViews()
        val graphCode = args.media.gc
        val dictionary = graphCode.dictionary
        val titleRow = TableRow(context)
        val tableTitleCell = createTextView()
        tableTitleCell.text = getString(R.string.graph_code)
        titleRow.addView(tableTitleCell)
        val columns = mutableListOf<String>()
        dictionary.forEach {
            val word = it.extractWord()
            if (columns.none { column ->
                    column.contains(word)
                }) {
                columns.add(word)
                val titleCell = createTextView(viewModel.getColorForWord(word))
                titleCell.text = word
                titleRow.addView(titleCell)
            }
        }
        titleRow.background = createCellBackground(Color.TRANSPARENT)
        binding.table.addView(titleRow, LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        val rows = mutableListOf<String>()
        dictionary.forEachIndexed { x, element ->
            val word = element.extractWord()
            if (rows.none { row ->
                    row.contains(word)
                }) {
                rows.add(word)
                val valueRow = TableRow(context)
                val rowTitle = createTextView(viewModel.getColorForWord(word))
                rowTitle.text = word
                valueRow.addView(rowTitle)
                for (y in 0 until dictionary.size) {
                    val valueCell = createTextView(Color.TRANSPARENT)
                    valueCell.text = "${graphCode.getValue(x, y)}"
                    valueRow.addView(valueCell)
                    if (x == y) {
                        valueRow.background = createCellBackground(viewModel.getColorForWord(word))
                    }
                }
                valueRow.background = createCellBackground(Color.TRANSPARENT)
                binding.table.addView(valueRow, LayoutParams(MATCH_PARENT, WRAP_CONTENT))
                binding.table.background = createCellBackground(Color.TRANSPARENT, 4)
            }
        }
    }

    private fun generateQuery() {
        binding.queryText.visibility = View.VISIBLE
        binding.queryText.text = ""
        val query = args.query
        val queryTextPrefix = viewModel.getQueryPrefixFor(context, query.getQueryType())
        val queryTextKeyWords = query.getQueryText()
        val spannableString = SpannableString(queryTextPrefix + queryTextKeyWords)
        var colorIndexFrom = queryTextPrefix.length
        queryTextKeyWords.split(" ").forEach {
            val color = viewModel.getColorForWord(it.extractWord(), false, Color.TRANSPARENT)
            val colorIndexTo = colorIndexFrom + it.length
            spannableString.setSpan(BackgroundColorSpan(color), colorIndexFrom, colorIndexTo, 0)
            colorIndexFrom = colorIndexTo + 1
        }
        binding.queryText.text = spannableString
    }

    private fun generateChart() {
        binding.chart.visibility = View.VISIBLE
        val query = args.query
        val graphCode = args.media.gc
        val queryWordOccurrence = viewModel.calculateOccurrence(query, graphCode)
        val entries: ArrayList<BarEntry> = ArrayList()
        val labels = mutableListOf<String>()
        val colors = mutableListOf<Int>()
        val title = getString(R.string.query_chart)
        var entryIndex = 0
        queryWordOccurrence.forEach { (title, value) ->
            val barEntry = BarEntry(entryIndex.toFloat(), value.toFloat())
            entries.add(barEntry)
            labels.add(title)
            var color = viewModel.getColorForWord(title, false, Color.GRAY)
            if (color == Color.WHITE) {
                color = Color.GRAY
            }
            colors.add(color)
            entryIndex++
        }
        val barDataSet = BarDataSet(entries, title)
        val data = BarData(barDataSet)
        binding.chart.setVisibleXRangeMaximum(12.0f)
        binding.chart.data = data
        binding.chart.invalidate()
        barDataSet.colors = colors
        barDataSet.formSize = 15f
        barDataSet.valueTextSize = 12f
        val description = Description()
        description.isEnabled = false
        binding.chart.description = description
        val xAxis: XAxis = binding.chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawAxisLine(true)
        xAxis.valueFormatter = AxisLabelGenerator(labels)
        val leftAxis: YAxis = binding.chart.axisLeft
        leftAxis.setDrawAxisLine(false)
        val rightAxis: YAxis = binding.chart.axisRight
        rightAxis.setDrawAxisLine(false)
        binding.chart.legend.isEnabled = false
        binding.chart.setOnChartValueSelectedListener(this)
    }

    private fun createTextView(color: Int = Color.TRANSPARENT, strokeWidth: Int = 2): TextView {
        val textView = TextView(context)
        textView.setTextColor(Color.BLACK)
        textView.background = createCellBackground(color, strokeWidth)
        textView.setPadding(4, 4, 4, 4)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        return textView
    }

    private fun createCellBackground(color: Int, strokeWidth: Int = 2): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.setColor(color)
        drawable.setStroke(strokeWidth, Color.BLACK)
        return drawable
    }

    private fun setupButtonObserver() {
        binding.backButton.setOnClickListener {
            onBackButtonClicked()
        }
    }

    private fun onBackButtonClicked() {
        findNavController().navigateUp()
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null)
            return
        val bounds = onValueSelectedRectF
        binding.chart.getBarBounds(e as BarEntry?, bounds)
        Toast.makeText(context, "${e.y.toInt()}", Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected() {
    }

    class AxisLabelGenerator(private val values: List<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val label = values[value.toInt()]
            return if (label.length > 8) label.substring(0, 8) + "." else label
        }
    }
}