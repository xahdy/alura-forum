package br.com.alura.forum.service

import br.com.alura.forum.dto.AtualizacaoTopicoForm
import br.com.alura.forum.dto.NovoTopicoForm
import br.com.alura.forum.dto.TopicoView
import br.com.alura.forum.exception.NotFoundException
import br.com.alura.forum.mapper.TopicoFormMapper
import br.com.alura.forum.mapper.TopicoViewMapper
import br.com.alura.forum.model.Topico
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException.NotFound
import java.util.*
import java.util.stream.Collectors

//permite que o spring gerencia e injete essa classe em outras classes controladas pelo spring
@Service
class TopicoService(
    private var topicos: List<Topico> = ArrayList(),
    private val topicoViewMapper: TopicoViewMapper,
    private val topicoFormMapper: TopicoFormMapper,
    private val notFoundMessage: String = "Tópico não encontrado."
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
        }.findFirst().orElseThrow { NotFoundException(notFoundMessage) }

        return topicoViewMapper.map(topico)
    }

    //retorna um TopicoView para que possamos usar as informações do tópico cadastrado no nosso controller.
    fun cadastrar(form: NovoTopicoForm): TopicoView {
        val topico = topicoFormMapper.map(form)
        topico.id = topicos.size.toLong() + 1
        //plus adiciona elementos e retorna o List com os novos valores
        topicos = topicos.plus(topico)
        //ViewMapper mapeia o topico recem criado
        return topicoViewMapper.map(topico)
    }

    fun atualizar(form: AtualizacaoTopicoForm): TopicoView {

        val topico = topicos.stream().filter { t ->
            t.id == form.id
        }.findFirst().orElseThrow { NotFoundException(notFoundMessage) }
        //topicoAtualizado recebe as informações novas pelo form e mantem as antigas que estão dentro de topico
        val topicoAtualizado = Topico(
            id = form.id,
            titulo = form.titulo,
            mensagem = form.mensagem,
            autor = topico.autor,
            curso = topico.curso,
            respostas = topico.respostas,
            status = topico.status,
            dataCriacao = topico.dataCriacao

        )
        //remove o topico antigo da lista e adiciona o topico atualizado.
        topicos = topicos.minus(topico).plus(topicoAtualizado)
        return topicoViewMapper.map(topicoAtualizado)

    }

    fun deletar(id: Long) {
        //filtra todos os tópicos até encontrar o tópico que tem a id recebida por parametro e armazena essa informação dentro da val topico
        val topico = topicos.stream().filter { t ->
            t.id == id
            //se não encontrar o registro, lança a nossa exception.
        }.findFirst().orElseThrow { NotFoundException(notFoundMessage) }
        //minus remove o tópico informado da lista de topicos.
        topicos = topicos.minus(topico)
    }
}