package com.example.Biblioteca.literatura.universal;


import com.example.Biblioteca.literatura.universal.controller.BookController;
import com.example.Biblioteca.literatura.universal.service.implementation.MenuService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


	@SpringBootApplication
	public class Application implements CommandLineRunner {

		private final MenuService menuService;

		public Application(MenuService menuService) {
			this.menuService = menuService;
		}

		public static void main(String[] args) {
			SpringApplication.run(Application.class, args);
		}

		@Override
		public void run(String... args) throws Exception {
			// Llamamos al método showMenu() de MenuService
			menuService.showMenu();
		}
	}
