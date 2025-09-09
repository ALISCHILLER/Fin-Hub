package com.msa.finhub.feature.auth.login.domain.usecase

class ValidateCredentials {
    operator fun invoke(code: String, pass: String): String? {
        if (code.isBlank()) return "کد پرسنلی را وارد کنید"
        if (pass.isBlank()) return "رمز عبور را وارد کنید"
        if (pass.length < 6) return "طول رمز عبور حداقل ۶ کاراکتر"
        return null
    }
}