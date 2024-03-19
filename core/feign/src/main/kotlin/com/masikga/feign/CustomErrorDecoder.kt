package com.masikga.feign

import com.google.gson.JsonParser
import feign.Response
import feign.codec.ErrorDecoder

class CustomErrorDecoder : ErrorDecoder {
    override fun decode(methodKey: String?, response: Response?): Exception {
        val status = response?.status() ?: -1
        val body = response!!

        val responseJson = JsonParser.parseReader(body.body().asReader(Charsets.UTF_8)).asJsonObject


        val code = when {
            status in 400..499 -> 400
            status <= 500 -> 500
            else -> -1
        }

        return FeignException(code = code, customMessage = responseJson["message"].asString)
    }
}