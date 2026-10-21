INSERT INTO cartas (id, nome, imagem_pequena, imagem_grande, colecao, raridade, numero) VALUES
    ('base1-4',  'Charizard', 'https://images.pokemontcg.io/base1/4.png',  'https://images.pokemontcg.io/base1/4_hires.png',  'Base', 'Rare Holo', '4/102'),
    ('base1-58', 'Pikachu',   'https://images.pokemontcg.io/base1/58.png', 'https://images.pokemontcg.io/base1/58_hires.png', 'Base', 'Common',    '58/102'),
    ('base1-2',  'Blastoise', 'https://images.pokemontcg.io/base1/2.png',  'https://images.pokemontcg.io/base1/2_hires.png',  'Base', 'Rare Holo', '2/102');

INSERT INTO anuncios (carta_id, preco, condicao, descricao, vendedor, status) VALUES
    ('base1-4',  2500, 'Excelente',    'Carta original de 1999, sem dobras, levíssimo desgaste na borda inferior.', 'Rônero',       'a-venda'),
    ('base1-58', 89.9, 'Boa',          'Carta bem conservada, guardada em sleeve desde que foi aberta.',            'Marina Alves', 'reservada'),
    ('base1-2',  1750, 'Nova/Lacrada', 'Carta recém-aberta de booster, encaminhada direto para toploader.',        'Diego Ramos',  'vendida');
