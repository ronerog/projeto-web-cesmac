package pokemon_web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pokemon_web.model.Carta;

public interface CartaRepository extends JpaRepository<Carta, String> {

	List<Carta> findByNomeContainingIgnoreCase(String nome);

}
