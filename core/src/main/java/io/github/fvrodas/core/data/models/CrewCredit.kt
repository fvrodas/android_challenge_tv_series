package io.github.fvrodas.core.data.models

import io.github.fvrodas.core.domain.entities.CrewCreditEntity

data class CrewCredit(val type: String?, val self: Boolean, val voice: Boolean) : BaseResponse() {
    fun asEntity(): CrewCreditEntity = CrewCreditEntity(type ?: "Cast", _embedded?.show?.asEntity())
}