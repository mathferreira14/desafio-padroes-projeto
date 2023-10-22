package one.digitalinnovation.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinnovation.model.Cliente;
import one.digitalinnovation.model.ClienteRepository;
import one.digitalinnovation.model.Endereco;
import one.digitalinnovation.model.EnderecoRepository;
import one.digitalinnovation.service.ClienteService;
import one.digitalinnovation.service.ViaCepService;

@Service
public class ClienteServiceImpl implements ClienteService{

    @Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;

    public Iterable<Cliente> buscarTodos(){
        return clienteRepository.findAll();
    }
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.get();
    }
    public void inserir(Cliente cliente){
        salvarClienteComCep(cliente);
    }
    public void atualizar (Long id, Cliente cliente) {
        Optional<Cliente> clienteAtt = clienteRepository.findById(id);
        if (clienteAtt.isPresent()) {
            salvarClienteComCep(cliente);
        }
    }
    
    public void deletar (Long id) {
        clienteRepository.deleteById(id);
    }
    private void salvarClienteComCep(Cliente cliente) {
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		clienteRepository.save(cliente);
	}
}
