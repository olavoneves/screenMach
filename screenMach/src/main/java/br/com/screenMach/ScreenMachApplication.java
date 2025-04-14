package br.com.screenMach;

import br.com.screenMach.main.Main;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ScreenMachApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ScreenMachApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var menu = new Main();

		try {
			menu.exibeMenu();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
