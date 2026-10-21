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

  campoDescricao.addEventListener("input", function () {
    atualizarContadorDescricao();
    UI.limparErroDoCampo(campoDescricao);
  });
  campoPreco.addEventListener("input", function () {
    UI.limparErroDoCampo(campoPreco);
  });
  campoCondicao.addEventListener("change", function () {
    UI.limparErroDoCampo(campoCondicao);
  });

  UI.preencherCampoMoeda(campoPreco, Number(campoPreco.dataset.valorInicial || 0));
  UI.aplicarMascaraMoeda(campoPreco);
  atualizarContadorDescricao();
})();
