(function () {
  const formulario = document.querySelector("#formulario-edicao");
  if (!formulario) {
    return;
  }

  const campoPreco = document.querySelector("#preco");
  const campoPrecoReal = document.querySelector("#preco-real");
  const campoCondicao = document.querySelector("#condicao");
  const campoDescricao = document.querySelector("#descricao");
  const contadorDescricao = document.querySelector("#contador-descricao");

  function atualizarContadorDescricao() {
    contadorDescricao.textContent = campoDescricao.value.length;
  }

  function validarFormulario() {
    const precoValido = UI.validarCampoObrigatorio(campoPreco, function () {
      return UI.valorDoCampoMoeda(campoPreco) > 0;
    });
    const condicaoValida = UI.validarCampoObrigatorio(campoCondicao);
    return precoValido && condicaoValida;
  }

  formulario.addEventListener("submit", function (evento) {
    if (!validarFormulario()) {
      evento.preventDefault();
      return;
    }
    campoPrecoReal.value = UI.valorDoCampoMoeda(campoPreco);
  });

  campoDescricao.addEventListener("input", atualizarContadorDescricao);
  campoPreco.addEventListener("input", function () {
    campoPreco.closest(".campo").classList.remove("campo-erro");
  });
  campoCondicao.addEventListener("change", function () {
    campoCondicao.closest(".campo").classList.remove("campo-erro");
  });

  UI.preencherCampoMoeda(campoPreco, Number(campoPreco.dataset.valorInicial || 0));
  UI.aplicarMascaraMoeda(campoPreco);
  atualizarContadorDescricao();
})();
