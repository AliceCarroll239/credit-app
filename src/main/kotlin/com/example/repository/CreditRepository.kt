package com.example.repository

import com.example.model.CreditRating
import com.example.model.CreditRequestModel
import com.example.model.IncomeSource
import com.example.model.Target
import kotlin.math.log10

class CreditRepository {

    val retirementAge = 65

    fun checkAge(age: Int, maturity: Int) = age + maturity < this.retirementAge

    fun checkYearAnnualIncome(amountrequested: Double, maturity: Int, lastyearincome: Int) =
        (amountrequested / maturity) < (lastyearincome / 3)

    fun checkCreditRating(creditrating: CreditRating) = creditrating != CreditRating.EXLOW

    fun checkWork(incomeSource: IncomeSource) = incomeSource != IncomeSource.UNEMPLOYED

    fun finalCheck(lastyearincome: Double, creditAmmount: Double) = lastyearincome / 2 > creditAmmount

    fun getMaxCreditLimit(creditRequestModel: CreditRequestModel): Double {
        val incomeLimit = when (creditRequestModel.incomesource) {
            IncomeSource.PASSIVEINCOME -> 1000000.0
            IncomeSource.EMPLOYEE -> 5000000.0
            IncomeSource.OWNBUSINESS -> 10000000.0
            IncomeSource.UNEMPLOYED -> throw IllegalArgumentException("Can not be Credit")
        }
        val ratingLimit = when (creditRequestModel.creditrating) {
            CreditRating.EXLOW -> throw IllegalArgumentException("Can not be Credit")
            CreditRating.LOW -> 1000000.0
            CreditRating.NORMAL -> 5000000.0
            CreditRating.HIGH -> 10000000.0
            CreditRating.EXHIGH -> 10000000.0
        }
        return listOf(creditRequestModel.amountrequested, incomeLimit, ratingLimit).minOrNull()!!
    }

    fun getTargetModification(target: Target) =
        when (target) {
            Target.MORTGAGE -> -2.0
            Target.BUSINESSDEVELOPMENT -> -0.5
            Target.CARLOAN -> 0.0
            Target.CONSUMER -> 1.5
        }

    fun getRatingModification(creditrating: CreditRating) =
        when (creditrating) {
            CreditRating.EXLOW -> throw IllegalArgumentException("Can not be Credit")
            CreditRating.LOW -> 1.5
            CreditRating.NORMAL -> 0.0
            CreditRating.HIGH -> -0.25
            CreditRating.EXHIGH -> -0.75
        }

    fun getAmmountModification(amountrequested: Double) = -log10(amountrequested / 1000000)

    fun getEmployedModificator(incomeSource: IncomeSource) =
        when (incomeSource) {
            IncomeSource.PASSIVEINCOME -> 0.5
            IncomeSource.EMPLOYEE -> -0.25
            IncomeSource.OWNBUSINESS -> 0.25
            IncomeSource.UNEMPLOYED -> throw IllegalArgumentException("Can not be Credit")
        }
}