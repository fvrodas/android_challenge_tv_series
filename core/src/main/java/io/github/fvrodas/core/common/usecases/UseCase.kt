package io.github.fvrodas.core.common.usecases

abstract class UseCase<Type, Param> {
    abstract suspend operator fun invoke(params: Param): Result<Type>
}