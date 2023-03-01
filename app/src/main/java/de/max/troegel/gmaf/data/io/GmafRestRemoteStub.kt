package de.max.troegel.gmaf.data.io

import de.max.troegel.gmaf.data.model.GmafQuery
import de.swa.mmfg.MMFG
import java.util.*

class GmafRestRemoteStub : GmafRestRemote {
    override suspend fun updateAuthToken() {

    }

    override suspend fun executeQuery(query: GmafQuery): Vector<MMFG?> {
        val vector = Vector<MMFG?>()
        vector.add(MMFG())
        return vector
    }
}