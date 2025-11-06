package com.gumeinteligencia.api_intermidiaria.infrastructure.dataprovider;

import com.gumeinteligencia.api_intermidiaria.application.gateways.ClienteGateway;
import com.gumeinteligencia.api_intermidiaria.domain.Cliente;
import com.gumeinteligencia.api_intermidiaria.infrastructure.exceptions.DataProviderException;
import com.gumeinteligencia.api_intermidiaria.infrastructure.mapper.ClienteMapper;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.CLienteRepository;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.ClienteEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClienteDataProvider implements ClienteGateway {

    private final CLienteRepository cLienteRepository;
    private final String MENSAGEM_ERRO_CONSULTAR_POR_TELEFONE = "Erro ao consultar cliente pelo telefone.";

    @Override
    public Optional<Cliente> consultarPorTelefone(String telefone) {
        Optional<ClienteEntity> clienteEntity;

        try {
            clienteEntity = cLienteRepository.findByTelefoneAndInativoFalse(telefone);
        } catch (Exception ex) {
            log.error(MENSAGEM_ERRO_CONSULTAR_POR_TELEFONE, ex);
            throw new DataProviderException(MENSAGEM_ERRO_CONSULTAR_POR_TELEFONE, ex.getCause());
        }

        return clienteEntity.map(ClienteMapper::paraDomain);
    }
}
