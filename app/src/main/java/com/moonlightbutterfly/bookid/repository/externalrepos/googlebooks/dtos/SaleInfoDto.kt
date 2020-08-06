package com.moonlightbutterfly.bookid.repository.externalrepos.googlebooks.dtos

import java.util.*

data class SaleInfoDto (
    var country: String?,
    var saleability: String?,
    var onSaleDate: Date?,
    var isEbook: Boolean?,
    var listPrice: PriceDto?,
    var retailPrice: PriceDto?,
    var buyLink: String?
    )