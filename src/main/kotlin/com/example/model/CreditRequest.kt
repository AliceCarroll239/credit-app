package com.example.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreditRequest(
    @SerialName("age")
    val age: Int,
    @SerialName("amountrequested")
    val amountrequested: Double,
    @SerialName("creditrating")
    val creditrating: Int,
    @SerialName("incomesource")
    val incomesource: String,
    @SerialName("lastyearincome")
    val lastyearincome: Int,
    @SerialName("maturity")
    val maturity: Int,
    @SerialName("sex")
    val sex: String,
    @SerialName("target")
    val target: String
)

data class CreditRequestModel(
    val age: Int,
    var amountrequested: Double,
    val creditrating: CreditRating,
    val incomesource: IncomeSource,
    val lastyearincome: Int,
    val maturity: Int,
    val sex: Sex,
    val target: Target
)

fun CreditRequest.toCreditRequestModel() =
    CreditRequestModel(
        age = this.age,
        amountrequested = this.amountrequested,
        creditrating = this.creditrating.toCreditRating(),
        incomesource = this.incomesource.toIncomeSource(),
        lastyearincome = this.lastyearincome,
        maturity = this.maturity,
        sex = this.sex.toSex(),
        target = this.target.toTarget()
    )

enum class Sex {
    M, F
}

fun String.toSex() = when (this) {
    "M" -> Sex.M
    "F" -> Sex.F
    else -> throw IllegalArgumentException("Invalid Sex")
}

enum class CreditRating {
    EXLOW, LOW, NORMAL, HIGH, EXHIGH
}

fun Int.toCreditRating() = when (this) {
    -2 -> CreditRating.EXLOW
    -1 -> CreditRating.LOW
    0 -> CreditRating.NORMAL
    1 -> CreditRating.HIGH
    2 -> CreditRating.EXHIGH
    else -> throw IllegalArgumentException("Invalid Credit Rating")
}

enum class Target {
    MORTGAGE, BUSINESSDEVELOPMENT, CARLOAN, CONSUMER
}

fun String.toTarget() = when (this) {
    "Mortgage" -> Target.MORTGAGE
    "Business development" -> Target.BUSINESSDEVELOPMENT
    "Car loan" -> Target.CARLOAN
    "Consumer" -> Target.CONSUMER
    else -> throw IllegalArgumentException("Invalid Target")
}

enum class IncomeSource {
    PASSIVEINCOME, EMPLOYEE, OWNBUSINESS, UNEMPLOYED
}

fun String.toIncomeSource() = when (this) {
    "Passive income" -> IncomeSource.PASSIVEINCOME
    "Employee" -> IncomeSource.EMPLOYEE
    "Own business" -> IncomeSource.OWNBUSINESS
    "Unemployed" -> IncomeSource.UNEMPLOYED
    else -> throw IllegalArgumentException("Invalid Income Source")
}