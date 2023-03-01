package de.max.troegel.gmaf.viewmodel

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import de.max.troegel.gmaf.R
import de.max.troegel.gmaf.app.adjustAlpha
import de.max.troegel.gmaf.app.extractWord
import de.max.troegel.gmaf.data.model.GmafQuery
import de.max.troegel.gmaf.data.model.QueryType
import de.swa.gc.GraphCode
import gurtek.com.offlinedictionary.Dictionary
import okhttp3.internal.immutableListOf
import java.lang.Integer.max

class DetailsViewModel(
    private val colors: List<Int> = immutableListOf(
        Color.RED,
        Color.BLUE,
        Color.GREEN,
        Color.YELLOW,
        Color.MAGENTA,
        Color.DKGRAY,
        Color.GRAY,
        Color.LTGRAY,
        Color.argb(255, 120, 120, 240),
        Color.argb(255, 60, 240, 180),
        Color.argb(255, 220, 150, 150),
        Color.argb(255, 120, 200, 200),
        Color.argb(255, 200, 200, 20),
        Color.argb(255, 240, 220, 60),
        Color.argb(255, 240, 180, 180),
        Color.argb(255, 120, 255, 120),
        Color.argb(255, 80, 200, 80),
        Color.argb(255, 255, 153, 0),
        Color.argb(255, 128, 0, 0),
        Color.argb(255, 153, 0, 204),
        Color.argb(255, 0, 102, 0)
    )
) : ViewModel() {

    private var colorMapper = mutableMapOf<String, Int>()

    fun clearColorMap() {
        colorMapper.clear()
    }

    fun getColorForWord(
        word: String,
        createWordIfMissing: Boolean = true,
        colorIfMissing: Int = Color.WHITE,
        alphaModifier: Float = 0.4F
    ): Int {
        return if (colorMapper.containsKey(word)) {
            colorMapper[word]!!
        } else if (createWordIfMissing) {
            val color = if (colors.size > colorMapper.size)
                colors[colorMapper.size].adjustAlpha(alphaModifier) else colorIfMissing
            colorMapper[word] = color
            color
        } else {
            colorIfMissing
        }
    }

    fun getQueryPrefixFor(context: Context?, queryType: QueryType): String {
        context?.let {
            return when (queryType) {
                QueryType.FIND_SIMILAR_MEDIA -> context.getString(R.string.search_similar_media)
                QueryType.FIND_RECOMMENDED_MEDIA -> context.getString(R.string.search_similar_media)
            }
        }
        return ""
    }

    fun calculateOccurrence(query: GmafQuery, graphCode: GraphCode): Map<String, Int> {
        val gson = Gson()
        val dictionary = Dictionary.getEnglishDictionary()
        val queryWordOccurrence = mutableMapOf<String, Int>()

        graphCode.dictionary.forEach {
            val word = it.extractWord()
            var occurrence = 0
            val dictEntries = dictionary.searchWord(word).map { dictEntry ->
                gson.toJson(dictEntry)
            }
            occurrence += dictEntries.filter { dictEntry -> dictEntry.contains(word) }.size
            query.getQueryText().split(" ").forEach { queryComponent ->
                val queryWord = queryComponent.extractWord()
                if (queryWord != word) {
                    val dictEntries = dictionary.searchWord(queryWord).map { dictEntry ->
                        gson.toJson(dictEntry)
                    }
                    occurrence += dictEntries.filter { dictEntry ->
                        dictEntry.contains(word)
                    }.size
                }
            }
            if (occurrence > 0) {
                queryWordOccurrence[word] = occurrence
            }
        }
        query.getQueryText().split(" ").forEach { queryComponent ->
            val queryWord = queryComponent.extractWord()
            var occurrence = 0
            graphCode.dictionary.forEach {
                val word = it.extractWord()
                if (queryWord != word) {
                    val dictEntries = dictionary.searchWord(word).map { dictEntry ->
                        gson.toJson(dictEntry)
                    }
                    occurrence += dictEntries.filter { dictEntry -> dictEntry.contains(queryWord) }.size // word
                }
            }
            if (queryWord in queryWordOccurrence) {
                occurrence = max(occurrence, queryWordOccurrence[queryWord] ?: 0)
                queryWordOccurrence[queryWord] = occurrence
            } else {
                queryWordOccurrence[queryWord] = occurrence
            }
        }
        return queryWordOccurrence
    }
}