INSERT INTO system_settings (key, value, description)
VALUES ('min_advance_notice_absence_days', '3', 'Antecedência mínima em dias para solicitar indisponibilidade temporária')
    ON CONFLICT (key) DO NOTHING;