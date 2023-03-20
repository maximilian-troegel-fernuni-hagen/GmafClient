package de.max.troegel.gmaf.data.io

import de.max.troegel.gmaf.data.model.GmafQuery
import de.swa.mmfg.GeneralMetadata
import de.swa.mmfg.MMFG
import java.util.*

class GmafRestRemoteStub : GmafRestRemote {
    override suspend fun updateAuthToken() {

    }

    override suspend fun executeQuery(query: GmafQuery): Vector<MMFG?> {
        val vector = Vector<MMFG?>()
        val mmfg = MMFG()
        val generalMetadata = GeneralMetadata()
        generalMetadata.setGraphCode(query.getGraphCode())
        mmfg.generalMetadata = generalMetadata
        vector.add(mmfg)
        return vector
    }
}