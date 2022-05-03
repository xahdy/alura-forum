package br.com.alura.forum.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
data class Usuario(

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        val nome: String,
        val email: String,
        val password: String,

        @JsonIgnore
        //quando carregar o usuario, carregar tbm todas as roles
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinColumn(name = "usuario_role")
        val role: List<Role> = mutableListOf()
)
