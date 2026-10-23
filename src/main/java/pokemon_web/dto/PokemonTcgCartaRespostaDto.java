package pokemon_web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonTcgCartaRespostaDto {

	private PokemonTcgCardDto data;

	public PokemonTcgCardDto getData() {
		return data;
	}

	public void setData(PokemonTcgCardDto data) {
		this.data = data;
	}

}
