import com.google.gson.annotations.SerializedName

data class Equipo(
    @SerializedName("Rk") val Rk: Int,
    @SerializedName("Squad") val Squad: String,
    @SerializedName("MP") val MP: Int,
    @SerializedName("W") val W: Int,
    @SerializedName("D") val D: Int,
    @SerializedName("L") val L: Int,
    @SerializedName("GF") val GF: Int,
    @SerializedName("GA") val GA: Int,
    @SerializedName("GD") val GD: Int,
    @SerializedName("Pts") val Pts: Int,
    @SerializedName("Pts/MP") val PtsPerMP: Double,
    @SerializedName("xG") val xG: Double,
    @SerializedName("xGA") val xGA: Double,
    @SerializedName("xGD") val xGD: Double,
    @SerializedName("xGD/90") val xGDPer90: Double,
    @SerializedName("Last 5") val Last5: String,
    @SerializedName("Attendance") val Attendance: Int,
    @SerializedName("Top Team Scorer") val TopTeamScorer: String,
    @SerializedName("Goalkeeper") val Goalkeeper: String,
    @SerializedName("Notes") val Notes: String?
)
