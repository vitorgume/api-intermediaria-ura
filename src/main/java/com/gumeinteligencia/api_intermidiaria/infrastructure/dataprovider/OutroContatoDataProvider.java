package com.gumeinteligencia.api_intermidiaria.infrastructure.dataprovider;

import com.gumeinteligencia.api_intermidiaria.application.gateways.OutroContatoGateway;
import com.gumeinteligencia.api_intermidiaria.domain.outroContato.OutroContato;
import com.gumeinteligencia.api_intermidiaria.infrastructure.exceptions.DataProviderException;
import com.gumeinteligencia.api_intermidiaria.infrastructure.mapper.OutroContatoMapper;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.OutroContatoRepository;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.OutroContatoEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutroContatoDataProvider implements OutroContatoGateway {

    private final String MENSAGEM_ERRO_LISTAR_OUTROS_CONTATOS = "Erro ao listar outros contatos.";
    private final OutroContatoRepository repository;

    @Override
    public List<OutroContato> listar() {
        List<OutroContatoEntity> outroContatoEntities;

        try {
            outroContatoEntities = repository.listar();
        } catch (Exception ex) {
            log.error(MENSAGEM_ERRO_LISTAR_OUTROS_CONTATOS, ex);
            throw new DataProviderException(MENSAGEM_ERRO_LISTAR_OUTROS_CONTATOS, ex.getCause());
        }

        return outroContatoEntities.stream().map(OutroContatoMapper::paraDomain).toList();
    }
}
