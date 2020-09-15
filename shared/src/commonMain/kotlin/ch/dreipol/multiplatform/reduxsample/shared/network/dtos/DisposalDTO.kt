package ch.dreipol.multiplatform.reduxsample.shared.network.dtos

import ch.dreipol.multiplatform.reduxsample.shared.database.DisposalType
import ch.dreipol.multiplatform.reduxsample.shared.delight.Disposal
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DisposalDTO(
    @SerialName("_id")
    val id: Int,
    @SerialName("PLZ")
    val zip: Int?,
    @SerialName("Abholdatum")
    val disposalDate: String?
) {

    fun toDisposal(disposalType: DisposalType): Disposal? {
        if (zip == null || disposalDate == null) {
            return null
        }
        return Disposal("${disposalType.name}_$id", disposalType, zip, disposalDate.toLocalDateTime().date)
    }
}