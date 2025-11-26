-- Criar alguns usuários (senhas fictícias, na real seriam hashes)
INSERT INTO users (name, email, password_hash) VALUES 
('Alice Dev', 'alice@example.com', 'hash_senha_secreta'),
('Bob Junior', 'bob@example.com', 'hash_senha_secreta');

-- Criar alguns produtos
INSERT INTO products (name, description, price, stock_quantity) VALUES 
('Teclado Mecânico', 'Teclado RGB com switches azuis', 250.00, 10),
('Mouse Gamer', 'Mouse 12000 DPI', 120.50, 50),
('Monitor 24"', 'Monitor IPS 75Hz', 899.90, 5),
('Cadeira Ergonômica', 'Cadeira preta de escritório', 1200.00, 2);

-- Simular itens no carrinho da Alice (User ID 1)
-- Alice quer 1 Teclado e 2 Mouses
INSERT INTO cart_items (user_id, product_id, quantity) VALUES 
(1, 1, 1), -- 1x Teclado
(1, 2, 2); -- 2x Mouse