package de.max.troegel.gmaf.data.model

import android.os.Parcelable
import de.swa.gc.GraphCode
import kotlinx.android.parcel.Parcelize

@Parcelize
class GmafQuery(
    private val date: Long,
    private val queryType: QueryType,
    private val queryText: String,
    private val graphCode: GraphCode
) : Parcelable {

    fun getDate(): Long {
        return date
    }

    fun getQueryType(): QueryType {
        return queryType
    }

    fun getQueryText(): String {
        return queryText
    }

    fun getGraphCode(): GraphCode {
        return graphCode
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GmafQuery

        if (queryType != other.queryType) return false
        if (graphCode != other.graphCode) return false
        return queryText == other.queryText
    }

    override fun hashCode(): Int {
        var result = queryType.hashCode()
        result = 31 * result + graphCode.hashCode()
        result = 31 * result + queryText.hashCode()
        return result
    }

    override fun toString(): String {
        return "{queryType: $queryType, queryText: $queryText, graphCode: $graphCode}"
    }

    fun getDatee() {
        getDate()
    }
}