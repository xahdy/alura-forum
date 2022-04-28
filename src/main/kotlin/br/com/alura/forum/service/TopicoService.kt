package br.com.alura.forum.service

import br.com.alura.forum.dto.NovoTopicoDto
import br.com.alura.forum.model.Topico
import org.springframework.stereotype.Service
import java.util.*

//permite que o spring gerencia e injete essa classe em outras classes controladas pelo spring
@Service
class TopicoService(
    private var topicos: List<Topico> = ArrayList(),
    private val cursoService: CursoService,
    private val usuarioService: UsuarioService
) {


    fun listar(): List<Topico> {
        return topicos
    }

    fun buscarPorId(id: Long): Topico {
        //api de stream e filtro para filtrar o topico que tem o id equivalente
        return topicos.stream().filter({
            //passado o topico t, encontrar o topico que tem o t.id == id
                t ->
            t.id == id
            //pegar o primeiro elemento (unico tbm pq filtrou pelo id)
        }).findFirst().get()
    }

    fun cadastrar(dto: NovoTopicoDto) {
        //plus adiciona elementos e retorna o List com os novos valores
        topicos = topicos.plus(
            Topico(
                id = topicos.size.toLong() + 1,
                titulo = dto.titulo,
                mensagem = dto.mensagem,
                curso = cursoService.buscarPorId(dto.idCurso),
                autor = usuarioService.buscarPorId(dto.idAutor)
            )
        )
    }

}