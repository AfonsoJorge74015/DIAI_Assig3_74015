package pt.unl.fct.iadi.novaevents.security

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.model.Role
import pt.unl.fct.iadi.novaevents.model.User
import pt.unl.fct.iadi.novaevents.repository.UserRepository

@Service
class AppUserDetailsManager(
    private val userRepository: UserRepository
): UserDetailsManager {

    override fun loadUserByUsername(username: String?): UserDetails? {
        val user: User = userRepository.findByUsername(username!!) ?:
        throw UsernameNotFoundException(username)

        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            user.roles.map { SimpleGrantedAuthority(it.name) }
        )
    }

    override fun createUser(userDetails: UserDetails) {
        if (userExists(userDetails.username)) throw IllegalArgumentException("User already exists")

        val newUser = User().apply {
            username = userDetails.username
            password = userDetails.password
        }

        val newRoles = userDetails.authorities.map { auth ->
            Role().apply {
                name = auth.authority
                user = newUser
            }
        }
        newUser.roles.addAll(newRoles)

        userRepository.save(newUser)
    }

    override fun userExists(username: String): Boolean {
        return userRepository.findByUsername(username) != null
    }

    override fun updateUser(user: UserDetails) {
        val existingUser = userRepository.findByUsername(user.username) ?: return
        existingUser.password = user.password

        existingUser.roles.clear()
        val newRoles = user.authorities.map { auth ->
            Role().apply {
                name = auth.authority
                this.user = existingUser
            }
        }
        existingUser.roles.addAll(newRoles)
        userRepository.save(existingUser)
    }

    override fun deleteUser(username: String) {
        userRepository.findByUsername(username)?.let { userRepository.delete(it) }
    }

    override fun changePassword(oldPassword: String, newPassword: String) {
        throw UnsupportedOperationException("Not implemented")
    }
}