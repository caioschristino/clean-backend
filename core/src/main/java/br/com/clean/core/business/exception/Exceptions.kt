package br.com.clean.core.business.exception

import java.lang.RuntimeException

class AuthenticationException: RuntimeException()

class InternetConnectionException: RuntimeException()

class HttpException(val code: Int, message: String = ""): RuntimeException(message)

class GuardException(message: String = ""): RuntimeException(message)