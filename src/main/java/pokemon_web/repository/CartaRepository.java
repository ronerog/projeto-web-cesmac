package pokemon_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pokemon_web.model.Carta;

public interface CartaRepository extends JpaRepository<Carta, String> {

}
