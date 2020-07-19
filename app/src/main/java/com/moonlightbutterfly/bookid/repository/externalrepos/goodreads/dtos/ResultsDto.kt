package com.moonlightbutterfly.bookid.repository.externalrepos.goodreads.dtos

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "results", strict = false)
data class ResultsDto @JvmOverloads constructor(
    @field:ElementList(inline = true) var works: List<WorkDto>? = null
)