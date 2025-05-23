package com.patch4code.logline.features.person_details.domain.model

import com.google.gson.annotations.SerializedName

/**
 * GNU GENERAL PUBLIC LICENSE, VERSION 3.0 (https://www.gnu.org/licenses/gpl-3.0.html)
 *
 * PersonMovieCredits - Representing movie credits for a person (cast/crew) from TMDB.
 *
 * @author Patch4Code
 */
data class PersonMovieCredits(
    @SerializedName("id") val id: Int = -1,
    @SerializedName("cast") val cast: List<MovieAsCastMember> = listOf(),
    @SerializedName("crew") val crew: List<MovieAsCrewMember> = listOf()
)

// Representing a movie in which a person appeared as a cast member.
data class MovieAsCastMember(
    @SerializedName("title") val title: String? = "N/A",
    @SerializedName("id") val id: Int = -1,
    @SerializedName("release_date") val releaseDate: String? = "N/A-date",
    @SerializedName("poster_path") val posterUrl: String? = "",
    @SerializedName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerializedName("original_language") val originalLanguage: String = "N/A",
    @SerializedName("original_title") val originalTitle: String = "N/A",
    @SerializedName("popularity") val popularity: Double = 0.0,
    @SerializedName("vote_average") val voteAverage: Double = 0.0
)

// Representing a movie in which a person appeared as a cew member.
data class MovieAsCrewMember(
    @SerializedName("title") val title: String? = "N/A",
    @SerializedName("id") val id: Int = -1,
    @SerializedName("release_date") val releaseDate: String? = "N/A-date",
    @SerializedName("poster_path") val posterUrl: String? = "",
    @SerializedName("department") val department: String = "N/A",
    @SerializedName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerializedName("original_language") val originalLanguage: String = "N/A",
    @SerializedName("original_title") val originalTitle: String = "N/A",
    @SerializedName("popularity") val popularity: Double = 0.0,
    @SerializedName("vote_average") val voteAverage: Double = 0.0
)
