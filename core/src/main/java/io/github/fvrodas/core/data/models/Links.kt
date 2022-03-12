package io.github.fvrodas.core.data.models

data class Links (
    val self: Link,
    val previousepisode: Link
)

data class Link(
    val href: String
)