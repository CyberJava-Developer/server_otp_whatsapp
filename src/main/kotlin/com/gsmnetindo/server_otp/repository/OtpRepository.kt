package com.gsmnetindo.server_otp.repository

import com.gsmnetindo.server_otp.domain.Otp
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OtpRepository : CrudRepository<Otp, Long> {

    fun findByCode(code: String): Otp?

}
