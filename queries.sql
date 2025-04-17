-- 1) Получить всех пользователей, у которых баланс на всех счетах больше 10 000.
SELECT u.id, u.name, u.email
FROM users u
         JOIN accounts a ON u.id = a.user_id
GROUP BY u.id, u.name, u.email
HAVING SUM(a.balance) > 10000;

-- 2)Найти пользователя по email
SELECT id, name, email, registration_date
FROM users
WHERE email = 'name@email.com';

-- 3) Получить сумму всех средств в системе
SELECT SUM(balance) AS total_funds
FROM accounts;
