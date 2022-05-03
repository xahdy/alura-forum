package br.com.alura.forum.model.mapper

interface Mapper<T, U> {

    fun map(t: T): U

}
