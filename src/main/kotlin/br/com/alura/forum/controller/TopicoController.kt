package br.com.alura.forum.controller

import br.com.alura.forum.dto.NovoTopicoDto
import br.com.alura.forum.model.Topico
import br.com.alura.forum.service.TopicoService
import org.springframework.web.bind.annotation.*
import java.util.*

//diz que é um controller rest, que vai trabalhar com as requisições recebidas.
@RestController
//diz a uri do controller
@RequestMapping("/topicos")
class TopicoController(private val service: TopicoService) {

    //requisições get vão cair para o método listar
    @GetMapping
    fun listar(): List<Topico> {
        return service.listar()
    }

    @GetMapping("/{id}")
//recebe id por parametro e retorna topico como resposta
    fun listarPorId(@PathVariable id: Long): Topico{
    return service.buscarPorId(id)
    }

    @PostMapping
    //request body diz que é pra buscar o topico no corpo da requisição
    fun cadastrar(@RequestBody dto: NovoTopicoDto){
        service.cadastrar(dto)

    }
}