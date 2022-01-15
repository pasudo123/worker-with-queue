package com.example.processorworker.push

class PushResource {

    data class Request(
        val message: String
    )

    data class Response(
        val result: String
    )
}