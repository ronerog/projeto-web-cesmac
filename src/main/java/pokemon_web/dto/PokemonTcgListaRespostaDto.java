package pokemon_web.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonTcgListaRespostaDto {

	private List<PokemonTcgCardDto> data;

	public List<PokemonTcgCardDto> getData() {
		return data;
	}

	public void setData(List<PokemonTcgCardDto> data) {
		this.data = data;
	}

}
