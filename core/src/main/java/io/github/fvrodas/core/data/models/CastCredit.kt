package io.github.fvrodas.core.data.models

data class CastCredit(val self: Boolean, val voice: Boolean) : BaseResponse() {
    private val listOfSeries: List<Series>?
        get() {
            return (this._embedded?.get("shows") as List<Any?>?)?.map { it as Series }
        }
}