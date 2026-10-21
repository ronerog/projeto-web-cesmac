package pokemon_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pokemon_web.model.Anuncio;

public interface AnuncioRepository extends JpaRepository<Anuncio, Long> {

}
