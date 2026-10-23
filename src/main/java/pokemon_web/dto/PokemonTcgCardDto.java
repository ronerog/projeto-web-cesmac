package pokemon_web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonTcgCardDto {

	private String id;
	private String name;
	private String number;
	private String rarity;
	private Imagens images;
	private ColecaoDto set;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getRarity() {
		return rarity;
	}

	public void setRarity(String rarity) {
		this.rarity = rarity;
	}

	public Imagens getImages() {
		return images;
	}

	public void setImages(Imagens images) {
		this.images = images;
	}

	public ColecaoDto getSet() {
		return set;
	}

	public void setSet(ColecaoDto set) {
		this.set = set;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Imagens {

		private String small;
		private String large;

		public String getSmall() {
			return small;
		}

		public void setSmall(String small) {
			this.small = small;
		}

		public String getLarge() {
			return large;
		}

		public void setLarge(String large) {
			this.large = large;
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ColecaoDto {

		private String name;
		private Integer printedTotal;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getPrintedTotal() {
			return printedTotal;
		}

		public void setPrintedTotal(Integer printedTotal) {
			this.printedTotal = printedTotal;
		}

	}

}
