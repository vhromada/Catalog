package com.github.vhromada.catalog.web.validator

import com.github.vhromada.catalog.web.fo.AccountFO
import com.github.vhromada.catalog.web.validator.constraints.Password
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * A class represents validator for password constraint.
 *
 * @author Vladimir Hromada
 */
class PasswordValidator : ConstraintValidator<Password, AccountFO> {

    override fun isValid(account: AccountFO?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        if (account == null) {
            return false
        }
        if (account.password.isNullOrEmpty() || account.copyPassword.isNullOrEmpty()) {
            return true
        }
        return account.password == account.copyPassword
    }

}
