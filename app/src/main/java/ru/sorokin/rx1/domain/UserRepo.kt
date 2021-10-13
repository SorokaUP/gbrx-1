package ru.sorokin.rx1.domain

interface UserRepo {
    fun saveUser(user: User)
    fun getUsers() : List<User>
}