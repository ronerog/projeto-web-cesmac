package pokemon_web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pokemon_web.model.Carta;
import pokemon_web.service.CartaService;

@RestController
public class CartaController {

	private final CartaService cartaService;

	public CartaController(CartaService cartaService) {
		this.cartaService = cartaService;
	}

	@GetMapping("/api/cartas/buscar")
	public List<Carta> buscar(@RequestParam String nome) {
		return cartaService.buscarPorNome(nome);
	}

	@GetMapping("/api/cartas/{id}")
	public ResponseEntity<Carta> buscarPorId(@PathVariable String id) {
		Carta carta = cartaService.buscarPorId(id);
		return carta != null ? ResponseEntity.ok(carta) : ResponseEntity.notFound().build();
	}

}
