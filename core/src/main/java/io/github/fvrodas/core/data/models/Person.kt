package io.github.fvrodas.core.data.models

import io.github.fvrodas.core.domain.entities.PersonEntity

data class Person(
    val id: Long,
    val url: String,
    val name: String,
    val country: Country,
    val birthday: String,
    val deathday: Any? = null,
    val gender: String,
    val image: Image?,
    val updated: Long
) : BaseResponse() {
    fun asEntity(): PersonEntity = PersonEntity(id, name, gender, image)
}