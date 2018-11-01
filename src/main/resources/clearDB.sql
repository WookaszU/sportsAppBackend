TRUNCATE TABLE public.chat
    CONTINUE IDENTITY
    CASCADE;
TRUNCATE TABLE public.chat_messages
    CONTINUE IDENTITY
    CASCADE;
TRUNCATE TABLE public.event
    CONTINUE IDENTITY
    CASCADE;
TRUNCATE TABLE public.event_photo
    CONTINUE IDENTITY
    CASCADE;
TRUNCATE TABLE public.message
    CONTINUE IDENTITY
    CASCADE;
TRUNCATE TABLE public.photo
    CONTINUE IDENTITY
    CASCADE;
TRUNCATE TABLE public.profile_photo
    CONTINUE IDENTITY
    CASCADE;
TRUNCATE TABLE public.token
    CONTINUE IDENTITY
    CASCADE;
TRUNCATE TABLE public.user_chats
    CONTINUE IDENTITY
    CASCADE;
TRUNCATE TABLE public.user_events
    CONTINUE IDENTITY
    CASCADE;
TRUNCATE TABLE public.user_rating
    CONTINUE IDENTITY
    CASCADE;
TRUNCATE TABLE public.users
    CONTINUE IDENTITY
    CASCADE;

INSERT INTO users(email, enabled, first_name, last_name, password, rating) VALUES ('test@test.com',true, 'Tomasz','Nowak','$2a$04$LUCZ9Co/YSdhrG.nXs2pZO.R1FgkE21xz6VDWbsEPeKI7ehqTMlsS', '0');
INSERT INTO users(email, enabled, first_name, last_name, password, rating) VALUES ('test2@test.com',true, 'Anna','Kowal','$2a$04$LUCZ9Co/YSdhrG.nXs2pZO.R1FgkE21xz6VDWbsEPeKI7ehqTMlsS', '0');
