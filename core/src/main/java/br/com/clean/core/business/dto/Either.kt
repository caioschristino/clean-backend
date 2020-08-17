package br.com.clean.core.business.dto

class Either<A,B> private constructor(val left: Left<A>?, val right: Right<B>?)
{
    companion object {
        fun <A,B> left(value: A): Either<A, B> {
            return Either(Left(value))
        }

        fun <A,B> right(value: B): Either<A, B> {
            return Either(Right(value))
        }
    }

    private constructor(left: Left<A>): this(left, null)
    private constructor(right: Right<B>): this(null, right)

    fun isLeft() = left != null
}

class Left<A>(val value: A)
class Right<B>(val value: B)