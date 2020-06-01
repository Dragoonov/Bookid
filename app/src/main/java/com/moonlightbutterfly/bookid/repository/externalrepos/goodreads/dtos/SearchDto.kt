package com.moonlightbutterfly.bookid.repository.externalrepos.goodreads.dtos

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name = "search", strict = false)
data class SearchDto @JvmOverloads constructor(
    @field:Element(name = "results") var results: ResultsDto? = null)