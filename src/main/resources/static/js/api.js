const API = (function () {
  const URL_BASE = "https://api.pokemontcg.io/v2";
  const CHAVE_API = "";
  const LIMITE_RESULTADOS = 24;
  const TENTATIVAS = 3;

  const CARTAS_FALLBACK = [
    { id: "pl1-2", nome: "Blastoise", imagemPequena: "https://images.pokemontcg.io/pl1/2.png", imagemGrande: "https://images.pokemontcg.io/pl1/2_hires.png", colecao: "Platinum", raridade: "Rare Holo", numero: "2/127" },
    { id: "dp3-2", nome: "Blastoise", imagemPequena: "https://images.pokemontcg.io/dp3/2.png", imagemGrande: "https://images.pokemontcg.io/dp3/2_hires.png", colecao: "Secret Wonders", raridade: "Rare Holo", numero: "2/132" },
    { id: "gym2-2", nome: "Blaine's Charizard", imagemPequena: "https://images.pokemontcg.io/gym2/2.png", imagemGrande: "https://images.pokemontcg.io/gym2/2_hires.png", colecao: "Gym Challenge", raridade: "Rare Holo", numero: "2/132" },
    { id: "dp3-3", nome: "Charizard", imagemPequena: "https://images.pokemontcg.io/dp3/3.png", imagemGrande: "https://images.pokemontcg.io/dp3/3_hires.png", colecao: "Secret Wonders", raridade: "Rare Holo", numero: "3/132" },
    { id: "pl4-1", nome: "Charizard", imagemPequena: "https://images.pokemontcg.io/pl4/1.png", imagemGrande: "https://images.pokemontcg.io/pl4/1_hires.png", colecao: "Arceus", raridade: "Rare Holo", numero: "1/99" },
    { id: "base4-4", nome: "Charizard", imagemPequena: "https://images.pokemontcg.io/base4/4.png", imagemGrande: "https://images.pokemontcg.io/base4/4_hires.png", colecao: "Base Set 2", raridade: "Rare Holo", numero: "4/130" },
    { id: "base6-3", nome: "Charizard", imagemPequena: "https://images.pokemontcg.io/base6/3.png", imagemGrande: "https://images.pokemontcg.io/base6/3_hires.png", colecao: "Legendary Collection", raridade: "Rare Holo", numero: "3/110" },
    { id: "mcd19-12", nome: "Eevee", imagemPequena: "https://images.pokemontcg.io/mcd19/12.png", imagemGrande: "https://images.pokemontcg.io/mcd19/12_hires.png", colecao: "McDonald's Collection 2019", raridade: "Não informada", numero: "12/12" },
    { id: "basep-11", nome: "Eevee", imagemPequena: "https://images.pokemontcg.io/basep/11.png", imagemGrande: "https://images.pokemontcg.io/basep/11_hires.png", colecao: "Wizards Black Star Promos", raridade: "Promo", numero: "11/53" },
    { id: "ex12-5", nome: "Gengar", imagemPequena: "https://images.pokemontcg.io/ex12/5.png", imagemGrande: "https://images.pokemontcg.io/ex12/5_hires.png", colecao: "Legend Maker", raridade: "Rare Holo", numero: "5/92" },
    { id: "base3-5", nome: "Gengar", imagemPequena: "https://images.pokemontcg.io/base3/5.png", imagemGrande: "https://images.pokemontcg.io/base3/5_hires.png", colecao: "Fossil", raridade: "Rare Holo", numero: "5/62" },
    { id: "pop6-2", nome: "Lucario", imagemPequena: "https://images.pokemontcg.io/pop6/2.png", imagemGrande: "https://images.pokemontcg.io/pop6/2_hires.png", colecao: "POP Series 6", raridade: "Rare", numero: "2/17" },
    { id: "dp1-6", nome: "Lucario", imagemPequena: "https://images.pokemontcg.io/dp1/6.png", imagemGrande: "https://images.pokemontcg.io/dp1/6_hires.png", colecao: "Diamond & Pearl", raridade: "Rare Holo", numero: "6/130" },
    { id: "basep-3", nome: "Mewtwo", imagemPequena: "https://images.pokemontcg.io/basep/3.png", imagemGrande: "https://images.pokemontcg.io/basep/3_hires.png", colecao: "Wizards Black Star Promos", raridade: "Promo", numero: "3/53" },
    { id: "ru1-9", nome: "Mewtwo", imagemPequena: "https://images.pokemontcg.io/ru1/9.png", imagemGrande: "https://images.pokemontcg.io/ru1/9_hires.png", colecao: "Pokémon Rumble", raridade: "Não informada", numero: "9/16" },
    { id: "basep-1", nome: "Pikachu", imagemPequena: "https://images.pokemontcg.io/basep/1.png", imagemGrande: "https://images.pokemontcg.io/basep/1_hires.png", colecao: "Wizards Black Star Promos", raridade: "Promo", numero: "1/53" },
    { id: "mcd19-6", nome: "Pikachu", imagemPequena: "https://images.pokemontcg.io/mcd19/6.png", imagemGrande: "https://images.pokemontcg.io/mcd19/6_hires.png", colecao: "McDonald's Collection 2019", raridade: "Não informada", numero: "6/12" },
    { id: "basep-4", nome: "Pikachu", imagemPequena: "https://images.pokemontcg.io/basep/4.png", imagemGrande: "https://images.pokemontcg.io/basep/4_hires.png", colecao: "Wizards Black Star Promos", raridade: "Promo", numero: "4/53" },
    { id: "pop1-3", nome: "Rayquaza", imagemPequena: "https://images.pokemontcg.io/pop1/3.png", imagemGrande: "https://images.pokemontcg.io/pop1/3_hires.png", colecao: "POP Series 1", raridade: "Rare", numero: "3/17" },
    { id: "ex9-9", nome: "Rayquaza", imagemPequena: "https://images.pokemontcg.io/ex9/9.png", imagemGrande: "https://images.pokemontcg.io/ex9/9_hires.png", colecao: "Emerald", raridade: "Rare Holo", numero: "9/106" },
    { id: "smp-SM05", nome: "Snorlax-GX", imagemPequena: "https://images.pokemontcg.io/smp/SM05.png", imagemGrande: "https://images.pokemontcg.io/smp/SM05_hires.png", colecao: "SM Black Star Promos", raridade: "Promo", numero: "SM05/248" },
    { id: "base2-11", nome: "Snorlax", imagemPequena: "https://images.pokemontcg.io/base2/11.png", imagemGrande: "https://images.pokemontcg.io/base2/11_hires.png", colecao: "Jungle", raridade: "Rare Holo", numero: "11/64" },
    { id: "hgss3-10", nome: "Umbreon", imagemPequena: "https://images.pokemontcg.io/hgss3/10.png", imagemGrande: "https://images.pokemontcg.io/hgss3/10_hires.png", colecao: "HS—Undaunted", raridade: "Rare Holo", numero: "10/90" },
    { id: "ex11-17", nome: "Umbreon δ", imagemPequena: "https://images.pokemontcg.io/ex11/17.png", imagemGrande: "https://images.pokemontcg.io/ex11/17_hires.png", colecao: "Delta Species", raridade: "Rare Holo", numero: "17/113" },
    { id: "ru1-1", nome: "Venusaur", imagemPequena: "https://images.pokemontcg.io/ru1/1.png", imagemGrande: "https://images.pokemontcg.io/ru1/1_hires.png", colecao: "Pokémon Rumble", raridade: "Não informada", numero: "1/16" },
    { id: "sm9-1", nome: "Celebi & Venusaur-GX", imagemPequena: "https://images.pokemontcg.io/sm9/1.png", imagemGrande: "https://images.pokemontcg.io/sm9/1_hires.png", colecao: "Team Up", raridade: "Rare Holo GX", numero: "1/181" }
  ];

  let ultimaBuscaUsouFallback = false;

  function montarCabecalhos() {
    return CHAVE_API ? { "X-Api-Key": CHAVE_API } : {};
  }

  function esperar(milissegundos) {
    return new Promise(function (resolver) {
      window.setTimeout(resolver, milissegundos);
    });
  }

  function normalizarCarta(carta) {
    const imagens = carta.images || {};
    const colecao = carta.set || {};
    const numero = carta.number || "-";
    return {
      id: carta.id,
      nome: carta.name || "Carta sem nome",
      imagemPequena: imagens.small || "",
      imagemGrande: imagens.large || imagens.small || "",
      colecao: colecao.name || "Coleção desconhecida",
      raridade: carta.rarity || "Não informada",
      numero: colecao.printedTotal ? numero + "/" + colecao.printedTotal : numero
    };
  }

  function buscarNoFallback(termo) {
    const busca = termo.trim().toLowerCase();
    return CARTAS_FALLBACK.filter(function (carta) {
      return carta.nome.toLowerCase().includes(busca);
    });
  }

  async function consultar(endereco) {
    for (let tentativa = 1; tentativa <= TENTATIVAS; tentativa += 1) {
      try {
        const resposta = await fetch(endereco, { headers: montarCabecalhos() });
        if (resposta.ok) {
          return await resposta.json();
        }
      } catch (erro) {
        ultimaBuscaUsouFallback = true;
      }
      if (tentativa < TENTATIVAS) {
        await esperar(500 * tentativa);
      }
    }
    return null;
  }

  async function buscarCartasPorNome(nome) {
    const termo = nome.trim();
    if (!termo) {
      return [];
    }
    const consulta = encodeURIComponent('name:"*' + termo + '*"');
    const conteudo = await consultar(URL_BASE + "/cards?q=" + consulta + "&pageSize=" + LIMITE_RESULTADOS);
    if (!conteudo) {
      ultimaBuscaUsouFallback = true;
      return buscarNoFallback(termo);
    }
    ultimaBuscaUsouFallback = false;
    return (conteudo.data || []).map(normalizarCarta);
  }

  async function buscarCartaPorId(id) {
    const conteudo = await consultar(URL_BASE + "/cards/" + encodeURIComponent(id));
    if (!conteudo || !conteudo.data) {
      ultimaBuscaUsouFallback = true;
      return CARTAS_FALLBACK.find(function (carta) {
        return carta.id === id;
      }) || null;
    }
    ultimaBuscaUsouFallback = false;
    return normalizarCarta(conteudo.data);
  }

  function usouFallback() {
    return ultimaBuscaUsouFallback;
  }

  return { buscarCartasPorNome, buscarCartaPorId, usouFallback };
})();
