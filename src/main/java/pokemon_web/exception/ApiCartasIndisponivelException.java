package pokemon_web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class ApiCartasIndisponivelException extends RuntimeException {

	public ApiCartasIndisponivelException(Throwable causa) {
		super("A Pokémon TCG API não respondeu.", causa);
	}

}
