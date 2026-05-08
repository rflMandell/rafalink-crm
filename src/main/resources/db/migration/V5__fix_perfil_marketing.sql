-- Corrige o typo 'MARKENTING' para 'MARKETING' nos dados existentes
UPDATE usuarios SET perfil = 'MARKETING' WHERE perfil = 'MARKENTING';