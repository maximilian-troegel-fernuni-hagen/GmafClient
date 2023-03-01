package de.max.troegel.gmaf.data.io

import de.max.troegel.gmaf.data.model.GmafQuery
import de.swa.mmfg.MMFG
import java.util.*

interface GmafRestRemote {

    suspend fun updateAuthToken()

    suspend fun executeQuery(query: GmafQuery): Vector<MMFG?>?
}