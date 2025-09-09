package br.com.example.batch_processing_users.processor;

import br.com.example.batch_processing_users.model.Usuario;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.Period;

public class UsuarioProcessor implements ItemProcessor<Usuario, Usuario> {

	@Override
	public Usuario process(Usuario item) throws Exception {
		if (item.getEmail() == null || !item.getEmail().contains("@")) {
			System.out.println("E-mail inválido, pulando item: " + item.getEmail());
			return null;
		}

		if (item.getDataNascimento() != null) {
			int idade = Period.between(item.getDataNascimento(), LocalDate.now()).getYears();
			item.setIdade(idade);
		}

		System.out.println("Processando usuário: " + item.getNome() + ", Idade calculada: " + item.getIdade());

		return item;
	}
}
