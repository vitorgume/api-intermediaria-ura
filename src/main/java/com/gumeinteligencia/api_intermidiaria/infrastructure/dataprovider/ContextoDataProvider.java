package com.gumeinteligencia.api_intermidiaria.infrastructure.dataprovider;

import com.gumeinteligencia.api_intermidiaria.application.gateways.ContextoGateway;
import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import com.gumeinteligencia.api_intermidiaria.infrastructure.exceptions.DataProviderException;
import com.gumeinteligencia.api_intermidiaria.infrastructure.mapper.ContextoMapper;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.ContextoRepository;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.ContextoEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContextoDataProvider implements ContextoGateway {

    private final ContextoRepository repository;
    private final String MENSAGEM_ERRO_CONSULTAR_CONTEXTO_PELO_TELEFONE = "Erro ao consultar contexto pelo seu telefone.";
    private final String MENSAGEM_ERRO_SALVAR_CONTEXTO = "Erro ao salvar contexto.";

    @Override
    public Optional<Contexto> consultarPorTelefone(String telefone) {
        Optional<ContextoEntity> contextoEntity;

        try {
            contextoEntity = repository.buscarPorTelefone(telefone);
        } catch (Exception ex) {
            log.error(MENSAGEM_ERRO_CONSULTAR_CONTEXTO_PELO_TELEFONE, ex);
            throw new DataProviderException(MENSAGEM_ERRO_CONSULTAR_CONTEXTO_PELO_TELEFONE, ex.getCause());
        }

        return contextoEntity.map(ContextoMapper::paraDomain);
    }

    @Override
    public Contexto salvar(Contexto contexto) {
        ContextoEntity contextoEntity = ContextoMapper.paraEntity(contexto);

        try {
            contextoEntity = repository.salvar(contextoEntity);
        } catch (Exception ex) {
            log.error(MENSAGEM_ERRO_SALVAR_CONTEXTO, ex);
            throw new DataProviderException(MENSAGEM_ERRO_SALVAR_CONTEXTO, ex.getCause());
        }

        return ContextoMapper.paraDomain(contextoEntity);
    }
}
