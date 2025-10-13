package com.gumeinteligencia.api_intermidiaria.infrastructure.dataprovider;

import com.gumeinteligencia.api_intermidiaria.application.gateways.MidiaGateway;
import com.gumeinteligencia.api_intermidiaria.domain.MidiaCliente;
import com.gumeinteligencia.api_intermidiaria.infrastructure.exceptions.DataProviderException;
import com.gumeinteligencia.api_intermidiaria.infrastructure.mapper.MidiaClienteMapper;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.MidiaClienteRepository;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.MidiaClienteEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MidiaClienteDataProvider implements MidiaGateway {

    private final String MENSAGEM_ERRO_SALVAR_MIDIA = "Erro ao salvar midias do usuário";

    private final MidiaClienteRepository repository;

    @Override
    public void salvar(MidiaCliente midiaCliente) {
        MidiaClienteEntity midiaClienteEntity = MidiaClienteMapper.paraEntity(midiaCliente);

        try {
            repository.save(midiaClienteEntity);
        } catch (Exception ex) {
            log.error(MENSAGEM_ERRO_SALVAR_MIDIA, ex);
            throw new DataProviderException(MENSAGEM_ERRO_SALVAR_MIDIA, ex.getCause());
        }
    }
}
