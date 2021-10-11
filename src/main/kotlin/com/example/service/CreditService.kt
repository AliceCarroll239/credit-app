package com.example.service

import com.example.model.CreditRequest
import com.example.model.toCreditRequestModel
import com.example.repository.CreditRepository

class CreditService(private val repository: CreditRepository) {

    fun validateRequest(creditRequest: CreditRequest): Pair<Boolean, MutableList<String>> {
        var errors = mutableListOf<String>()
        var isValid = true
        if (creditRequest.age !in 0..Int.MAX_VALUE) {
            isValid = false
            errors.add("Check Age")
        }
        if (creditRequest.sex !in listOf("M", "F")) {
            isValid = false
            errors.add("Check Sex")
        }
        if (creditRequest.incomesource !in listOf("Passive income", "Employee", "Own business", "Unemployed")) {
            isValid = false
            errors.add("Check Income Source")
        }
        if (creditRequest.creditrating !in listOf(-2, -1, 0, 1, 2)) {
            isValid = false
            errors.add("Check Credit Rating")
        }
        if (creditRequest.amountrequested !in 100000.0..10000000.0) {
            isValid = false
            errors.add("Check Amount Requested")
        }
        if (creditRequest.maturity !in 1..20) {
            isValid = false
            errors.add("Check Maturity")
        }
        if (creditRequest.target !in listOf("Mortgage", "Business development", "Car loan", "Consumer")) {
            isValid = false
            errors.add("Check Target")
        }
        return Pair(isValid, errors)
    }

    fun calculateCredit(creditRequest: CreditRequest): Pair<Boolean, Double?> {
        val model = creditRequest.toCreditRequestModel()
        val canCredit = with(model) {
            repository.checkAge(age, maturity) &&
                    repository.checkYearAnnualIncome(amountrequested, maturity, lastyearincome) &&
                    repository.checkCreditRating(creditrating) &&
                    repository.checkWork(incomesource)
        }
        return when (canCredit) {
            true -> {
                model.amountrequested = repository.getMaxCreditLimit(model)
                val modificator = repository.getEmployedModificator(model.incomesource) +
                        repository.getTargetModification(model.target) +
                        repository.getAmmountModification(model.amountrequested) +
                        repository.getRatingModification(model.creditrating)
                val creditAmmount =
                    (model.amountrequested * (1 + (model.maturity * (10 + modificator) / 100))) / model.maturity
                if (repository.finalCheck(model.amountrequested, creditAmmount)) {
                    Pair(true, creditAmmount)
                } else {
                    Pair(false, null)
                }
            }
            false -> Pair(false, null)
        }
    }
}