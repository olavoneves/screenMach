package br.com.screenMach;

import br.com.screenMach.main.Main;
import br.com.screenMach.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class ScreenMachApplicationSemWeb implements CommandLineRunner{
//	@Autowired
//	private SerieRepository repository;
//
//	public static void main(String[] args) {
//		SpringApplication.run(ScreenMachApplicationSemWeb.class, args);
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		var menu = new Main(repository);
//
//		try {
//			menu.exibeMenu();
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//
//	}
//}
