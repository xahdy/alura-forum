package br.com.alura.forum.service

import br.com.alura.forum.dto.AtualizacaoTopicoForm
import br.com.alura.forum.dto.NovoTopicoForm
import br.com.alura.forum.dto.TopicoView
import br.com.alura.forum.mapper.TopicoFormMapper
import br.com.alura.forum.mapper.TopicoViewMapper
import br.com.alura.forum.model.Topico
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors

//permite que o spring gerencia e injete essa classe em outras classes controladas pelo spring
@Service
class TopicoService(
    private var topicos: List<Topico> = ArrayList(),
    private val topicoViewMapper: TopicoViewMapper,
    private val topicoFormMapper: TopicoFormMapper
) {


    fun listar(): List<TopicoView> {
        //mapeia a stream, para cada topico retorna um topico view
        return topicos.stream().map {
            //chama o topicoviewmapper passando o topico como parametro para conversão.
                t ->
            topicoViewMapper.map(t)
        }.collect(Collectors.toList())
    }

    fun buscarPorId(id: Long): TopicoView {
        //api de stream e filtro para filtrar o topico que tem o id equivalente
        val topico = topicos.stream().filter {
            //passado o topico t, encontrar o topico que tem o t.id == id
                t ->
            t.id == id
            //pegar o primeiro elemento (unico tbm pq filtrou pelo id)
        }.findFirst().get()

        return topicoViewMapper.map(topico)
    }

    fun cadastrar(form: NovoTopicoForm) {
        val topico = topicoFormMapper.map(form)
        topico.id = topicos.size.toLong() + 1
        //plus adiciona elementos e retorna o List com os novos valores
        topicos = topicos.plus(topico)
    }

    fun atualizar(form: AtualizacaoTopicoForm) {

        val topico = topicos.stream().filter { t ->
            t.id == form.id
        }.findFirst().get()
        //remove o topico da lista e adiciona um novo, utilizando o form para os dados modificados
        // e utilizando as informações dentro do topico velho para as infos que não foram alteradas
        topicos = topicos.minus(topico).plus(Topico(
            id = form.id,
            titulo = form.titulo,
            mensagem = form.mensagem,
            autor = topico.autor,
            curso = topico.curso,
            respostas = topico.respostas,
            status = topico.status,
            dataCriacao = topico.dataCriacao

        ))

    }
}