CREATE TABLE public.places
(
  id integer NOT NULL DEFAULT nextval('places_id_seq'::regclass),
  p_name text NOT NULL,
  lat real,
  lon real,
  description text,
  created timestamp with time zone,
  updated timestamp with time zone,
  created_by integer,
  updated_by integer,
  CONSTRAINT places_pkey PRIMARY KEY (id)
);

CREATE TABLE public.users
(
  id integer NOT NULL DEFAULT nextval('users_id_seq'::regclass),
  name text NOT NULL,
  email text NOT NULL,
  first_name text,
  last_name text,
  password text,
  role text NOT NULL,
  status integer NOT NULL,
  CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE public.images
(
  id serial,
  title text,
  description text,
  created timestamp with time zone,
  updated timestamp with time zone,
  author text,
  created_by integer,
  updated_by integer,
  content_type text,
  filename text,
  place_id integer,
  CONSTRAINT images_pkey PRIMARY KEY (id),
  CONSTRAINT images_place_id_fkey FOREIGN KEY (place_id)
      REFERENCES public.places (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);