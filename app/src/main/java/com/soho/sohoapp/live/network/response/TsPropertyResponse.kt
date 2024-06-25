package com.soho.sohoapp.live.network.response

import com.soho.sohoapp.live.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

@Serializable
data class TsPropertyResponse(
    @SerialName("found") val found: Int = 0,
    @SerialName("hits") val propertyList: List<Hit> = emptyList(),
    @SerialName("out_of") val outOf: Int = 0,
    @SerialName("page") val page: Int = 0,
    @SerialName("request_params") val requestParams: RequestParams = RequestParams(),
    @SerialName("search_cutoff") val searchCutoff: Boolean = false,
    @SerialName("search_time_ms") val searchTimeMs: Int = 0
)

@Serializable
data class Hit(
    @SerialName("document") val document: Document = Document()
)

@Serializable
data class Document(
    @SerialName("_geoloc") val geoloc: List<Double> = emptyList(),
    @SerialName("address_1") val address1: String? = null,
    @SerialName("address_2") val address2: String? = null,
    @SerialName("agency_id") val agencyId: Int = 0,
    @SerialName("agency_name") val agencyName: String? = null,
    @SerialName("agency_slug") val agencySlug: String? = null,
    @SerialName("agents") val agents: String? = null, // Consider parsing this JSON string into a proper list of objects
    @SerialName("ap_agents_ids") val apAgentsIds: List<Int> = emptyList(),
    @SerialName("ap_listing_state") val apListingState: String? = null,
    @SerialName("ap_property_type") val apPropertyType: String? = null,
    @SerialName("area_dimensions") val areaDimensions: AreaDimensions = AreaDimensions(),
    @SerialName("area_unit") val areaUnit: String? = null,
    @SerialName("bathroom_count") val bathroomCount: Double = 0.0,
    @SerialName("bedroom_count") val bedroomCount: Double = 0.0,
    @SerialName("block_address") val blockAddress: String? = null,
    @SerialName("building_name") val buildingName: String? = null,
    @SerialName("carspot_count") val carspotCount: Double = 0.0,
    @SerialName("closest_station") val closestStation: ClosestStation? = null,
    @SerialName("closest_station_area_id") val closestStationAreaId: Int = 0,
    @SerialName("closest_station_distance") val closestStationDistance: Double = 0.0,
    @SerialName("closest_station_name") val closestStationName: String? = null,
    @SerialName("closest_stations") val closestStations: String? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("country_iso") val countryIso: String? = null,
    @SerialName("currency_iso") val currencyIso: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("documents") val documents: String? = null,
    @SerialName("features_labels") val featuresLabels: List<String> = emptyList(),
    @SerialName("followed_for") val followedFor: String? = null,
    @SerialName("has_photos") val hasPhotos: Boolean = false,
    @SerialName("has_videos") val hasVideos: Boolean = false,
    @SerialName("id") val id: String? = null,
    @SerialName("is_list_trac") val isListTrac: Boolean = false,
    @SerialName("is_promoted") val isPromoted: Boolean = false,
    @SerialName("land_size") val landSize: Double? = null,
    @SerialName("list_trac_id") val listTracId: String? = null,
    @SerialName("listed_at") val listedAt: String? = null,
    @SerialName("listed_at_seconds") val listedAtSeconds: Long = 0,
    @SerialName("listing_state") val listingState: String? = null,
    @SerialName("listing_user_full_name") val listingUserFullName: String? = null,
    @SerialName("listing_user_id") val listingUserId: Int = 0,
    @SerialName("listing_user_slug") val listingUserSlug: String? = null,
    @SerialName("listing_user_slugs") val listingUserSlugs: List<String> = emptyList(),
    @SerialName("location_map_url") val locationMapUrl: String? = null,
    @SerialName("location_mask_address") val locationMaskAddress: Boolean = false,
    @SerialName("location_state") val locationState: String? = null,
    @SerialName("location_suburb") val locationSuburb: String? = null,
    @SerialName("maybe_for") val maybeFor: String? = null,
    @SerialName("new_project") val newProject: Boolean = false,
    @SerialName("not_interested_for") val notInterestedFor: String? = null,
    @SerialName("objectID") val objectId: String? = null,
    @SerialName("photos") val photos: String? = null,
    @SerialName("postcode") val postcode: String? = null,
    @SerialName("property_id") val propertyId: Int = 0,
    @SerialName("property_type") val propertyType: String? = null,
    @SerialName("rates") val rates: Rates = Rates(),
    @SerialName("slug") val slug: String? = null,
    @SerialName("street_address") val streetAddress: String? = null,
    @SerialName("street_name") val streetName: String? = null,
    @SerialName("suburb_id") val suburbId: Int = 0,
    @SerialName("suburb_slug") val suburbSlug: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("under_offer") val underOffer: Boolean = false,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("updated_at_seconds") val updatedAtSeconds: Long = 0,
    @SerialName("videos") val videos: String? = null,
    @SerialName("vr_urls") val vrUrls: List<String> = emptyList(),
    @SerialName("web_url") val webUrl: String? = null,
) {
    fun thumbnailUrl(): String? {
        if (photos.isNullOrEmpty() || photos == "[]") {
            return null
        } else {
            val photoList: List<Photo> = Json.decodeFromString(photos ?: "")
            return photoList.firstNotNullOf { it.getImageUrl() }
        }
    }

    fun fullAddress(): String {
        return if (address2.isNullOrEmpty()) {
            address1.orEmpty()
        } else {
            "$address1, $address2"
        }
    }

    fun areaSize(): Pair<String, Int>? {
        val sizeInfo = areaDimensions
        var resPair: Pair<String, Int>? = null

        val floorSize = sizeInfo.floorSize ?: 0.0
        val floorSizeUnit = sizeInfo.floorSizeUnit
        val landSize = sizeInfo.landSize ?: 0.0
        val landSizeUnit = sizeInfo.landSizeUnit

        if (floorSize > 0) {
            resPair = Pair(
                getFormatPropertySize(floorSize, floorSizeUnit),
                R.drawable.ic_floor_sizes
            )
        } else if (landSize > 0) {
            resPair = Pair(
                getFormatPropertySize(landSize, landSizeUnit),
                R.drawable.ic_land_size
            )
        }

        return resPair
    }
}

@Serializable
data class AreaDimensions(
    @SerialName("floor_size") val floorSize: Double? = null,
    @SerialName("floor_size_measurement") val floorSizeUnit: String? = null,
    @SerialName("land_size") val landSize: Double? = null,
    @SerialName("land_size_measurement") val landSizeUnit: String? = null
)

@Serializable
data class ClosestStation(
    @SerialName("area_id") val areaId: Int = 0,
    @SerialName("country") val country: String? = null,
    @SerialName("distance") val distance: Double? = null,
    @SerialName("meta") val meta: Meta = Meta(),
    @SerialName("name") val name: String? = null
)

@Serializable
data class Meta(
    @SerialName("line_names") val lineNames: List<String> = emptyList(),
    @SerialName("lines") val lines: List<String> = emptyList(),
    @SerialName("station_codes") val stationCodes: List<String> = emptyList()
)

@Serializable
data class Rates(
    @SerialName("council_rate") val councilRate: Double? = null,
    @SerialName("strata_rate") val strataRate: Double? = null,
    @SerialName("water_rate") val waterRate: Double? = null
)

@Serializable
data class RequestParams(
    @SerialName("collection_name") val collectionName: String? = null,
    @SerialName("per_page") val perPage: Int = 0,
    @SerialName("q") val query: String? = null
)

@Serializable
data class Photo(
    val url: String,
    val floorplan: Boolean,
    @SerialName("display_order") val displayOrder: Int
) {
    internal fun getImageUrl(): String? {
        return listOfNotNull(
            thumbnailUrl,
            mediumSmallUrl,
            mediumLargeUrl,
            smallImageUrl,
            mediumImageUrl,
            largeImageUrl,
            fullUrl,
            imageUrl
        ).firstOrNull()
    }

    private val imageUrl: String
        get() = url

    private val smallImageUrl: String
        get() = "$url?size=small"

    private val mediumImageUrl: String
        get() = "$url?size=medium"

    private val largeImageUrl: String
        get() = "$url?size=large"

    private val thumbnailUrl: String
        get() = "$url?size=thumbnail"

    private val mediumLargeUrl: String
        get() = "$url?size=medium_large"

    private val mediumSmallUrl: String
        get() = "$url?size=medium_small"

    private val fullUrl: String
        get() = "$url?size=full"
}

fun getFormatPropertySize(size: Double?, unit: String?): String {
    return if (size != null && unit != null) {
        val decimalFormat = NumberFormat.getNumberInstance(Locale.US) as DecimalFormat
        decimalFormat.applyPattern("###,###,##0.##")
        val formattedSize = decimalFormat.format(size)

        if (unit == "sqm") {
            "$formattedSize m²"
        } else {
            "$formattedSize $unit"
        }
    } else {
        ""
    }
}