package com.reactor.prueba.reactorPrueba;

import com.reactor.prueba.reactorPrueba.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ReactorPruebaApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(ReactorPruebaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ReactorPruebaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<String> nombres = Flux.just("David", "Angie", "Oscar", "Natalia", "Bruce Lee", "Bruce Banner")
				.map(name -> new Usuario(name.toLowerCase(), null))
				.doOnNext(x -> {
					if(x.getNombre() == null) {
						throw new RuntimeException("Los nombres no pueden ser vacios");
					}
					System.out.println(x.getNombre());
				})
				.map(usuario -> {
					String nombre = usuario.getNombre().toUpperCase();
					usuario.setNombre(nombre);
					return usuario.getNombre();
				})
				.filter(s -> s.toLowerCase().contains("bruce"))
				;

		nombres.subscribe(s -> log.info(s),
				throwable -> log.error(throwable.getMessage()),
				new Runnable() {
					@Override
					public void run() {
						log.info("Ha finalizado la ejecucion.");
					}
				});
	}
}
