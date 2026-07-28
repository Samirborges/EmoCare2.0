INSERT INTO public.system_settings (key, value, description)
VALUES
    ('appointment_confirmation_hours', '24', 'Prazo para confirmação da presença na consulta'),
    ('appointment_reminder_hours', '24', 'Antecedência para envio do lembrete da consulta'),
    ('max_cancel_hours', '24', 'Prazo mínimo para cancelamento da consulta'),
    ('max_reschedule_hours', '24', 'Prazo mínimo para reagendamento da consulta'),
    ('session_timeout_minutes', '30', 'Tempo máximo de inatividade da sessão'),
    ('email_notifications_enabled', 'true', 'Habilita envio de notificações por e-mail'),
    ('system_name', 'EmoCare 2.0', 'Nome da plataforma')
    ON CONFLICT (key) DO NOTHING;