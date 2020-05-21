package com.moonlightbutterfly.bookid.repository.externalrepos.dtos

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name = "GoodreadsResponse", strict = false)
data class GoodreadsResponseDto @JvmOverloads constructor(
    @field:Element(name = "search") var search: SearchDto? = null)