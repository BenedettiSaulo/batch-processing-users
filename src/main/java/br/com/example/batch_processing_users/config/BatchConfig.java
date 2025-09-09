package br.com.example.batch_processing_users.config;

import br.com.example.batch_processing_users.model.Usuario;
import br.com.example.batch_processing_users.processor.UsuarioProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return objectMapper;
	}

	@Bean
	public JsonItemReader<Usuario> jsonItemReader(ObjectMapper objectMapper) {
		JacksonJsonObjectReader<Usuario> jsonObjectReader = new JacksonJsonObjectReader<>(Usuario.class);
		jsonObjectReader.setMapper(objectMapper);

		return new JsonItemReaderBuilder<Usuario>()
				.name("usuarioJsonItemReader")
				.resource(new ClassPathResource("usuarios.json"))
				.jsonObjectReader(jsonObjectReader)
				.build();
	}

	@Bean
	public UsuarioProcessor usuarioProcessor() {
		return new UsuarioProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Usuario> jdbcBatchItemWriter(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Usuario>()
				.dataSource(dataSource)
				.sql("INSERT INTO usuario (nome, email, data_nascimento, idade) VALUES (:nome, :email, :dataNascimento, :idade)")
				.beanMapped()
				.build();
	}

	@Bean
	public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
					  JsonItemReader<Usuario> jsonItemReader, UsuarioProcessor usuarioProcessor, JdbcBatchItemWriter<Usuario> jdbcBatchItemWriter) {
		return new StepBuilder("step1", jobRepository)
				.<Usuario, Usuario>chunk(10, transactionManager)
				.reader(jsonItemReader)
				.processor(usuarioProcessor)
				.writer(jdbcBatchItemWriter)
				.build();
	}

	@Bean
	public Job processUserJob(JobRepository jobRepository, Step step1) {
		return new JobBuilder("processUserJob", jobRepository)
				.start(step1)
				.build();
	}
}
