-- ============================================================================
--  school-system (Gakkyu Nisshi) - full database dump: schema + data.
--  DBMS: PostgreSQL. Ready-to-use database for local development / testing.
--
--  Contents:
--    - all tables with relations and indexes (incl. email + password reset);
--    - 3 courses, 2 terms, 12 holidays;
--    - class 2SN, 8 teachers, 30 students;
--    - 20 weekly templates, 312 lessons;
--    - an admin account.
--
--  How to load (into an empty database):
--    createdb school
--    psql -U postgres -d school -f school_db.sql
--
--  Demo logins:  admin / admin123 ,  teachers / teacher123 ,  students / student123
--  App connection: src/main/resources/application.properties
--    (default jdbc:postgresql://localhost:5432/school, postgres / 1234).
-- ============================================================================
--
-- PostgreSQL database dump
--

\restrict 51lKo9ccosqxn10808Fyo2HWXT6NpD4J0dzdecj4tE570cka6bz2Ufa5XTh2Q9h

-- Dumped from database version 18.4
-- Dumped by pg_dump version 18.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

ALTER TABLE IF EXISTS ONLY public.school_class DROP CONSTRAINT IF EXISTS fkpfncsj00sfl482sp0q972waqp;
ALTER TABLE IF EXISTS ONLY public.file_attachment DROP CONSTRAINT IF EXISTS fkn2s5433i89pdtufbvp11km0h7;
ALTER TABLE IF EXISTS ONLY public.password_reset_token DROP CONSTRAINT IF EXISTS fkli7wollcmb8tibymo3s94o57h;
ALTER TABLE IF EXISTS ONLY public.school_class DROP CONSTRAINT IF EXISTS fkl5ga0x3a5st9ykx4kj7ytj7bv;
ALTER TABLE IF EXISTS ONLY public.holiday DROP CONSTRAINT IF EXISTS fkiyk45qnpuduv6ukfwbe8jcxnv;
ALTER TABLE IF EXISTS ONLY public.attendance DROP CONSTRAINT IF EXISTS fkhs8518992gwyxg89g3fx57he5;
ALTER TABLE IF EXISTS ONLY public.day_override DROP CONSTRAINT IF EXISTS fkh5pki0bevslw0vok52dphorri;
ALTER TABLE IF EXISTS ONLY public.holiday DROP CONSTRAINT IF EXISTS fkh5lcxee803r4yfdrolv3fn5b8;
ALTER TABLE IF EXISTS ONLY public.schedule_template DROP CONSTRAINT IF EXISTS fkgg0orvceaw67hvflh8uci2oqc;
ALTER TABLE IF EXISTS ONLY public.student_note DROP CONSTRAINT IF EXISTS fkfcp3o12tsdghnu06p5b2o4rho;
ALTER TABLE IF EXISTS ONLY public.schedule_template DROP CONSTRAINT IF EXISTS fkdmn1q7de05ntlbpqyfc8g2iiw;
ALTER TABLE IF EXISTS ONLY public.schedule_template DROP CONSTRAINT IF EXISTS fkdeobdoxr9jfnqie8ksbcq7cw9;
ALTER TABLE IF EXISTS ONLY public.school_event DROP CONSTRAINT IF EXISTS fkcvbbawn9phmy4qkxih1tusxek;
ALTER TABLE IF EXISTS ONLY public.teacher_comment DROP CONSTRAINT IF EXISTS fkc49hlrtxhdr61x6sxwtg2mh4f;
ALTER TABLE IF EXISTS ONLY public.attendance DROP CONSTRAINT IF EXISTS fkam01ddvne08oa3exny156v7al;
ALTER TABLE IF EXISTS ONLY public.schedule_template DROP CONSTRAINT IF EXISTS fkalhd82llmd3kv2g9h4cdvedsn;
ALTER TABLE IF EXISTS ONLY public.file_attachment DROP CONSTRAINT IF EXISTS fkai1xp9mynk5nq3bihjykqp5si;
ALTER TABLE IF EXISTS ONLY public.app_user DROP CONSTRAINT IF EXISTS fkaa3shdfpsnn8nknnsn64lsjfm;
ALTER TABLE IF EXISTS ONLY public.teacher_comment DROP CONSTRAINT IF EXISTS fk85ffgvi1py41qwovvp3coppk5;
ALTER TABLE IF EXISTS ONLY public.lesson DROP CONSTRAINT IF EXISTS fk7ydr23s8y9j6lip5qrngoymx4;
ALTER TABLE IF EXISTS ONLY public.grade DROP CONSTRAINT IF EXISTS fk65by7eln3rv98qrechgx4rvgv;
ALTER TABLE IF EXISTS ONLY public.lesson DROP CONSTRAINT IF EXISTS fk62vogrd9ewhu7udvhl7c62e81;
ALTER TABLE IF EXISTS ONLY public.day_override DROP CONSTRAINT IF EXISTS fk5u3o1s3oujspuo6dahey1iuy7;
ALTER TABLE IF EXISTS ONLY public.grade DROP CONSTRAINT IF EXISTS fk5qvy9me7d0edvxl1qeguojkdt;
ALTER TABLE IF EXISTS ONLY public.school_event DROP CONSTRAINT IF EXISTS fk3ynxjpp86bt5vt4e6tuoaic5s;
ALTER TABLE IF EXISTS ONLY public.lesson DROP CONSTRAINT IF EXISTS fk3h6v3yr40e1yr44ukbnoovb20;
ALTER TABLE IF EXISTS ONLY public.subject DROP CONSTRAINT IF EXISTS uk_p1jgir6qcpmqnxt4a8105wsot;
ALTER TABLE IF EXISTS ONLY public.app_user DROP CONSTRAINT IF EXISTS uk_m00jdvc0voosp0jeo48e4a0m7;
ALTER TABLE IF EXISTS ONLY public.password_reset_token DROP CONSTRAINT IF EXISTS uk_g0guo4k8krgpwuagos61oc06j;
ALTER TABLE IF EXISTS ONLY public.course DROP CONSTRAINT IF EXISTS uk_4xqvdpkafb91tt3hsb67ga3fj;
ALTER TABLE IF EXISTS ONLY public.app_user DROP CONSTRAINT IF EXISTS uk_3k4cplvh82srueuttfkwnylq0;
ALTER TABLE IF EXISTS ONLY public.app_user DROP CONSTRAINT IF EXISTS uk_1j9d9a06i600gd43uu3km82jw;
ALTER TABLE IF EXISTS ONLY public.school_class DROP CONSTRAINT IF EXISTS uk94tu3x6oa7bbe9e3qrebbpi4l;
ALTER TABLE IF EXISTS ONLY public.term DROP CONSTRAINT IF EXISTS term_pkey;
ALTER TABLE IF EXISTS ONLY public.teacher_comment DROP CONSTRAINT IF EXISTS teacher_comment_pkey;
ALTER TABLE IF EXISTS ONLY public.subject DROP CONSTRAINT IF EXISTS subject_pkey;
ALTER TABLE IF EXISTS ONLY public.student_note DROP CONSTRAINT IF EXISTS student_note_pkey;
ALTER TABLE IF EXISTS ONLY public.school_event DROP CONSTRAINT IF EXISTS school_event_pkey;
ALTER TABLE IF EXISTS ONLY public.school_class DROP CONSTRAINT IF EXISTS school_class_pkey;
ALTER TABLE IF EXISTS ONLY public.schedule_template DROP CONSTRAINT IF EXISTS schedule_template_pkey;
ALTER TABLE IF EXISTS ONLY public.password_reset_token DROP CONSTRAINT IF EXISTS password_reset_token_pkey;
ALTER TABLE IF EXISTS ONLY public.lesson DROP CONSTRAINT IF EXISTS lesson_pkey;
ALTER TABLE IF EXISTS ONLY public.holiday DROP CONSTRAINT IF EXISTS holiday_pkey;
ALTER TABLE IF EXISTS ONLY public.grade DROP CONSTRAINT IF EXISTS grade_pkey;
ALTER TABLE IF EXISTS ONLY public.file_attachment DROP CONSTRAINT IF EXISTS file_attachment_pkey;
ALTER TABLE IF EXISTS ONLY public.day_override DROP CONSTRAINT IF EXISTS day_override_pkey;
ALTER TABLE IF EXISTS ONLY public.course DROP CONSTRAINT IF EXISTS course_pkey;
ALTER TABLE IF EXISTS ONLY public.attendance DROP CONSTRAINT IF EXISTS attendance_pkey;
ALTER TABLE IF EXISTS ONLY public.app_user DROP CONSTRAINT IF EXISTS app_user_pkey;
ALTER TABLE IF EXISTS public.term ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.teacher_comment ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.subject ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.student_note ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.school_event ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.school_class ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.schedule_template ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.password_reset_token ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.lesson ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.holiday ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.grade ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.file_attachment ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.day_override ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.course ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.attendance ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.app_user ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS public.term_id_seq;
DROP TABLE IF EXISTS public.term;
DROP SEQUENCE IF EXISTS public.teacher_comment_id_seq;
DROP TABLE IF EXISTS public.teacher_comment;
DROP SEQUENCE IF EXISTS public.subject_id_seq;
DROP TABLE IF EXISTS public.subject;
DROP SEQUENCE IF EXISTS public.student_note_id_seq;
DROP TABLE IF EXISTS public.student_note;
DROP SEQUENCE IF EXISTS public.school_event_id_seq;
DROP TABLE IF EXISTS public.school_event;
DROP SEQUENCE IF EXISTS public.school_class_id_seq;
DROP TABLE IF EXISTS public.school_class;
DROP SEQUENCE IF EXISTS public.schedule_template_id_seq;
DROP TABLE IF EXISTS public.schedule_template;
DROP SEQUENCE IF EXISTS public.password_reset_token_id_seq;
DROP TABLE IF EXISTS public.password_reset_token;
DROP SEQUENCE IF EXISTS public.lesson_id_seq;
DROP TABLE IF EXISTS public.lesson;
DROP SEQUENCE IF EXISTS public.holiday_id_seq;
DROP TABLE IF EXISTS public.holiday;
DROP SEQUENCE IF EXISTS public.grade_id_seq;
DROP TABLE IF EXISTS public.grade;
DROP SEQUENCE IF EXISTS public.file_attachment_id_seq;
DROP TABLE IF EXISTS public.file_attachment;
DROP SEQUENCE IF EXISTS public.day_override_id_seq;
DROP TABLE IF EXISTS public.day_override;
DROP SEQUENCE IF EXISTS public.course_id_seq;
DROP TABLE IF EXISTS public.course;
DROP SEQUENCE IF EXISTS public.attendance_id_seq;
DROP TABLE IF EXISTS public.attendance;
DROP SEQUENCE IF EXISTS public.app_user_id_seq;
DROP TABLE IF EXISTS public.app_user;
SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: app_user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.app_user (
    id bigint NOT NULL,
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    photo_name character varying(255),
    role character varying(255) NOT NULL,
    student_number character varying(255),
    username character varying(255) NOT NULL,
    school_class_id bigint,
    email character varying(255),
    CONSTRAINT app_user_role_check CHECK (((role)::text = ANY ((ARRAY['ADMIN'::character varying, 'TEACHER'::character varying, 'STUDENT'::character varying])::text[])))
);


--
-- Name: app_user_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.app_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: app_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.app_user_id_seq OWNED BY public.app_user.id;


--
-- Name: attendance; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.attendance (
    id bigint NOT NULL,
    status character varying(255) NOT NULL,
    lesson_id bigint NOT NULL,
    student_id bigint NOT NULL,
    CONSTRAINT attendance_status_check CHECK (((status)::text = ANY ((ARRAY['PRESENT'::character varying, 'LATE'::character varying, 'ABSENT'::character varying, 'EXCUSED'::character varying])::text[])))
);


--
-- Name: attendance_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.attendance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: attendance_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.attendance_id_seq OWNED BY public.attendance.id;


--
-- Name: course; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.course (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    order_index integer NOT NULL
);


--
-- Name: course_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.course_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: course_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.course_id_seq OWNED BY public.course.id;


--
-- Name: day_override; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.day_override (
    id bigint NOT NULL,
    date date NOT NULL,
    substitute_day_of_week character varying(255) NOT NULL,
    title character varying(255),
    course_id bigint,
    school_class_id bigint,
    CONSTRAINT day_override_substitute_day_of_week_check CHECK (((substitute_day_of_week)::text = ANY ((ARRAY['MONDAY'::character varying, 'TUESDAY'::character varying, 'WEDNESDAY'::character varying, 'THURSDAY'::character varying, 'FRIDAY'::character varying, 'SATURDAY'::character varying, 'SUNDAY'::character varying])::text[])))
);


--
-- Name: day_override_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.day_override_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: day_override_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.day_override_id_seq OWNED BY public.day_override.id;


--
-- Name: file_attachment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.file_attachment (
    id bigint NOT NULL,
    original_name character varying(255) NOT NULL,
    stored_name character varying(255) NOT NULL,
    uploaded_at timestamp(6) without time zone NOT NULL,
    lesson_id bigint NOT NULL,
    uploaded_by_id bigint NOT NULL
);


--
-- Name: file_attachment_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.file_attachment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: file_attachment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.file_attachment_id_seq OWNED BY public.file_attachment.id;


--
-- Name: grade; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.grade (
    id bigint NOT NULL,
    value integer NOT NULL,
    lesson_id bigint NOT NULL,
    student_id bigint NOT NULL
);


--
-- Name: grade_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.grade_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: grade_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.grade_id_seq OWNED BY public.grade.id;


--
-- Name: holiday; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.holiday (
    id bigint NOT NULL,
    end_date date NOT NULL,
    start_date date NOT NULL,
    title character varying(255),
    course_id bigint,
    school_class_id bigint
);


--
-- Name: holiday_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.holiday_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: holiday_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.holiday_id_seq OWNED BY public.holiday.id;


--
-- Name: lesson; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.lesson (
    id bigint NOT NULL,
    date date NOT NULL,
    end_time time(6) without time zone NOT NULL,
    room character varying(255),
    start_time time(6) without time zone NOT NULL,
    school_class_id bigint NOT NULL,
    subject_id bigint NOT NULL,
    teacher_id bigint NOT NULL
);


--
-- Name: lesson_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.lesson_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: lesson_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.lesson_id_seq OWNED BY public.lesson.id;


--
-- Name: password_reset_token; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.password_reset_token (
    id bigint NOT NULL,
    expires_at timestamp(6) without time zone NOT NULL,
    token character varying(255) NOT NULL,
    used boolean NOT NULL,
    user_id bigint NOT NULL
);


--
-- Name: password_reset_token_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.password_reset_token_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: password_reset_token_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.password_reset_token_id_seq OWNED BY public.password_reset_token.id;


--
-- Name: schedule_template; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.schedule_template (
    id bigint NOT NULL,
    day_of_week character varying(255) NOT NULL,
    end_time time(6) without time zone NOT NULL,
    room character varying(255),
    start_time time(6) without time zone NOT NULL,
    school_class_id bigint NOT NULL,
    subject_id bigint NOT NULL,
    teacher_id bigint NOT NULL,
    term_id bigint,
    CONSTRAINT schedule_template_day_of_week_check CHECK (((day_of_week)::text = ANY ((ARRAY['MONDAY'::character varying, 'TUESDAY'::character varying, 'WEDNESDAY'::character varying, 'THURSDAY'::character varying, 'FRIDAY'::character varying, 'SATURDAY'::character varying, 'SUNDAY'::character varying])::text[])))
);


--
-- Name: schedule_template_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.schedule_template_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: schedule_template_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.schedule_template_id_seq OWNED BY public.schedule_template.id;


--
-- Name: school_class; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.school_class (
    id bigint NOT NULL,
    group_code character varying(255) NOT NULL,
    study_year integer NOT NULL,
    course_id bigint,
    supervisor_id bigint
);


--
-- Name: school_class_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.school_class_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: school_class_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.school_class_id_seq OWNED BY public.school_class.id;


--
-- Name: school_event; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.school_event (
    id bigint NOT NULL,
    date date NOT NULL,
    description character varying(2000),
    title character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    created_by_id bigint,
    school_class_id bigint NOT NULL,
    CONSTRAINT school_event_type_check CHECK (((type)::text = ANY ((ARRAY['EXAM'::character varying, 'ACTIVITY'::character varying])::text[])))
);


--
-- Name: school_event_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.school_event_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: school_event_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.school_event_id_seq OWNED BY public.school_event.id;


--
-- Name: student_note; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.student_note (
    id bigint NOT NULL,
    date date NOT NULL,
    for_teacher boolean NOT NULL,
    text character varying(2000) NOT NULL,
    student_id bigint NOT NULL
);


--
-- Name: student_note_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.student_note_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: student_note_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.student_note_id_seq OWNED BY public.student_note.id;


--
-- Name: subject; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.subject (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);


--
-- Name: subject_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.subject_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: subject_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.subject_id_seq OWNED BY public.subject.id;


--
-- Name: teacher_comment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.teacher_comment (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    text character varying(2000) NOT NULL,
    author_id bigint NOT NULL,
    lesson_id bigint NOT NULL
);


--
-- Name: teacher_comment_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.teacher_comment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: teacher_comment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.teacher_comment_id_seq OWNED BY public.teacher_comment.id;


--
-- Name: term; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.term (
    id bigint NOT NULL,
    end_date date NOT NULL,
    name character varying(255) NOT NULL,
    start_date date NOT NULL
);


--
-- Name: term_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.term_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: term_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.term_id_seq OWNED BY public.term.id;


--
-- Name: app_user id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.app_user ALTER COLUMN id SET DEFAULT nextval('public.app_user_id_seq'::regclass);


--
-- Name: attendance id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.attendance ALTER COLUMN id SET DEFAULT nextval('public.attendance_id_seq'::regclass);


--
-- Name: course id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.course ALTER COLUMN id SET DEFAULT nextval('public.course_id_seq'::regclass);


--
-- Name: day_override id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.day_override ALTER COLUMN id SET DEFAULT nextval('public.day_override_id_seq'::regclass);


--
-- Name: file_attachment id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.file_attachment ALTER COLUMN id SET DEFAULT nextval('public.file_attachment_id_seq'::regclass);


--
-- Name: grade id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.grade ALTER COLUMN id SET DEFAULT nextval('public.grade_id_seq'::regclass);


--
-- Name: holiday id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.holiday ALTER COLUMN id SET DEFAULT nextval('public.holiday_id_seq'::regclass);


--
-- Name: lesson id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lesson ALTER COLUMN id SET DEFAULT nextval('public.lesson_id_seq'::regclass);


--
-- Name: password_reset_token id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.password_reset_token ALTER COLUMN id SET DEFAULT nextval('public.password_reset_token_id_seq'::regclass);


--
-- Name: schedule_template id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schedule_template ALTER COLUMN id SET DEFAULT nextval('public.schedule_template_id_seq'::regclass);


--
-- Name: school_class id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.school_class ALTER COLUMN id SET DEFAULT nextval('public.school_class_id_seq'::regclass);


--
-- Name: school_event id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.school_event ALTER COLUMN id SET DEFAULT nextval('public.school_event_id_seq'::regclass);


--
-- Name: student_note id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_note ALTER COLUMN id SET DEFAULT nextval('public.student_note_id_seq'::regclass);


--
-- Name: subject id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.subject ALTER COLUMN id SET DEFAULT nextval('public.subject_id_seq'::regclass);


--
-- Name: teacher_comment id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teacher_comment ALTER COLUMN id SET DEFAULT nextval('public.teacher_comment_id_seq'::regclass);


--
-- Name: term id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.term ALTER COLUMN id SET DEFAULT nextval('public.term_id_seq'::regclass);


--
-- Data for Name: app_user; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.app_user (id, first_name, last_name, password, photo_name, role, student_number, username, school_class_id, email) FROM stdin;
1	管理者	システム	$2a$10$ezy2FnYTqblJ3zy3s2f4pe5KLjCARN/PkQTeUhiz7smrkWkNFfifi	\N	ADMIN	\N	admin	\N	\N
2	一郎	田中	$2a$10$dKQLtgJMnvxZg7GobbEqV.YO271JL4G/x2vrg6yJX7Ydr1MRA3OIu	\N	TEACHER	\N	teacher1	\N	\N
3	花子	佐藤	$2a$10$IPBLiBhTynhsznYxtvjMPeNgmymeGMsNFbeLCJ4isE5QOzlrHgWcK	\N	TEACHER	\N	teacher2	\N	\N
157		ビサル	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn04	4	\N
148		堀内	$2a$10$bD/6pi48qOfiPEYj7BEpm.82NUp5odK/wmd1/thvZnAua6imFIRBm	\N	TEACHER	\N	horiuchi	\N	\N
149		末永	$2a$10$bD/6pi48qOfiPEYj7BEpm.82NUp5odK/wmd1/thvZnAua6imFIRBm	\N	TEACHER	\N	suenaga	\N	\N
150	啓	黒田	$2a$10$bD/6pi48qOfiPEYj7BEpm.82NUp5odK/wmd1/thvZnAua6imFIRBm	\N	TEACHER	\N	kuroda	\N	\N
151		橋本	$2a$10$bD/6pi48qOfiPEYj7BEpm.82NUp5odK/wmd1/thvZnAua6imFIRBm	\N	TEACHER	\N	hashimoto	\N	\N
152		土谷	$2a$10$bD/6pi48qOfiPEYj7BEpm.82NUp5odK/wmd1/thvZnAua6imFIRBm	\N	TEACHER	\N	tsuchiya	\N	\N
153		宮嶋	$2a$10$bD/6pi48qOfiPEYj7BEpm.82NUp5odK/wmd1/thvZnAua6imFIRBm	\N	TEACHER	\N	miyajima	\N	\N
154		ヴー	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn01	4	\N
155		ナズムル	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn02	4	\N
156		パトゥム	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn03	4	\N
158		シラ	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn05	4	\N
159		ビベク	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn06	4	\N
160		ビピン	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn07	4	\N
161		クムディ	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn08	4	\N
162		アレクス	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn09	4	\N
163		スディパ	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn10	4	\N
164		ミダ	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn11	4	\N
165		イスル	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn12	4	\N
166		チン ヒュウ ハイ	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn13	4	\N
167		ディパン	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn14	4	\N
168		リン	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn15	4	\N
169		プリタム	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn16	4	\N
170		ルペシュ	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn17	4	\N
171		ワトサラ	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn18	4	\N
172		ビベク	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn19	4	\N
173		アディカリ	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn20	4	\N
174		タルシ	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn21	4	\N
175		ヴィヴェク	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn22	4	\N
176		ディムトゥ	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn23	4	\N
177		ロシャニ	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn24	4	\N
178		スレスタ	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn25	4	\N
179		プージャニ	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn26	4	\N
180		アイン	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn27	4	\N
181		ホアン	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn28	4	\N
182		カッム	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn29	4	\N
183		アントン	$2a$10$n2hY4w9iE7068hLOBdqh0usAt4/bxpgTii4DftU7KoviNEkjv5F2K	\N	STUDENT	\N	2sn30	4	\N
\.


--
-- Data for Name: attendance; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.attendance (id, status, lesson_id, student_id) FROM stdin;
\.


--
-- Data for Name: course; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.course (id, name, order_index) FROM stdin;
1	Business Management Course	0
2	IT Management Course	1
3	Hotel Management Course	2
\.


--
-- Data for Name: day_override; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.day_override (id, date, substitute_day_of_week, title, course_id, school_class_id) FROM stdin;
\.


--
-- Data for Name: file_attachment; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.file_attachment (id, original_name, stored_name, uploaded_at, lesson_id, uploaded_by_id) FROM stdin;
\.


--
-- Data for Name: grade; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.grade (id, value, lesson_id, student_id) FROM stdin;
\.


--
-- Data for Name: holiday; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.holiday (id, end_date, start_date, title, course_id, school_class_id) FROM stdin;
1	2026-08-31	2026-08-01	夏休み	\N	\N
2	2026-04-29	2026-04-29	昭和の日	\N	\N
3	2026-09-22	2026-09-22	国民の休日	\N	\N
4	2026-11-23	2026-11-23	勤労感謝の日	\N	\N
5	2026-09-23	2026-09-23	秋分の日	\N	\N
6	2026-11-03	2026-11-03	文化の日	\N	\N
7	2026-05-04	2026-05-04	みどりの日	\N	\N
8	2026-10-19	2026-10-19	スポーツの日	\N	\N
9	2026-05-06	2026-05-06	振替休日	\N	\N
10	2026-05-05	2026-05-05	こどもの日	\N	\N
11	2026-09-21	2026-09-21	敬老の日	\N	\N
12	2026-07-20	2026-07-20	海の日	\N	\N
\.


--
-- Data for Name: lesson; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.lesson (id, date, end_time, room, start_time, school_class_id, subject_id, teacher_id) FROM stdin;
937	2026-05-14	14:40:00	811	13:10:00	4	3	153
938	2026-11-16	14:40:00	842	13:10:00	4	1	149
939	2026-05-07	14:40:00	811	13:10:00	4	3	153
940	2026-05-20	14:40:00	823	13:10:00	4	4	148
941	2026-05-29	14:40:00	822	13:10:00	4	5	148
942	2026-05-13	16:20:00	823	14:50:00	4	4	148
943	2026-06-30	14:40:00	811	13:10:00	4	2	152
944	2026-05-25	16:20:00	822	14:50:00	4	1	151
945	2026-04-15	14:40:00	823	13:10:00	4	4	148
946	2026-06-09	16:20:00	811	14:50:00	4	2	152
947	2026-07-07	14:40:00	811	13:10:00	4	2	152
948	2026-04-16	16:20:00	811	14:50:00	4	3	153
949	2026-09-17	14:40:00	811	13:10:00	4	3	153
950	2026-09-15	14:40:00	811	13:10:00	4	2	152
951	2026-07-24	16:20:00	822	14:50:00	4	5	148
952	2026-06-08	16:20:00	822	14:50:00	4	1	151
953	2026-07-02	16:20:00	811	14:50:00	4	3	153
954	2026-09-29	14:40:00	831	13:10:00	4	4	148
955	2026-04-22	16:20:00	823	14:50:00	4	4	148
956	2026-10-08	16:20:00	811	14:50:00	4	3	150
957	2026-04-17	14:40:00	822	13:10:00	4	5	148
958	2026-10-02	14:40:00	142	13:10:00	4	5	148
959	2026-10-27	16:20:00	831	14:50:00	4	4	148
960	2026-11-10	16:20:00	831	14:50:00	4	4	148
961	2026-07-21	14:40:00	811	13:10:00	4	2	152
962	2026-12-17	14:40:00	811	13:10:00	4	3	150
963	2026-06-24	14:40:00	823	13:10:00	4	4	148
964	2026-05-22	16:20:00	822	14:50:00	4	5	148
965	2026-09-10	16:20:00	811	14:50:00	4	3	153
966	2026-07-31	16:20:00	822	14:50:00	4	5	148
967	2026-06-22	14:40:00	822	13:10:00	4	1	151
968	2026-09-09	14:40:00	823	13:10:00	4	4	148
969	2026-05-21	16:20:00	811	14:50:00	4	3	153
970	2026-04-20	16:20:00	822	14:50:00	4	1	151
971	2026-10-13	16:20:00	831	14:50:00	4	4	148
972	2026-12-14	14:40:00	842	13:10:00	4	1	149
973	2026-05-12	16:20:00	811	14:50:00	4	2	152
974	2026-10-07	16:20:00	811	14:50:00	4	2	150
975	2026-09-30	14:40:00	811	13:10:00	4	2	150
976	2026-11-18	16:20:00	811	14:50:00	4	2	150
977	2026-10-28	16:20:00	811	14:50:00	4	2	150
978	2026-12-07	16:20:00	842	14:50:00	4	1	149
979	2026-11-02	16:20:00	842	14:50:00	4	1	149
980	2026-09-01	16:20:00	811	14:50:00	4	2	152
981	2026-04-24	16:20:00	822	14:50:00	4	5	148
982	2026-06-01	14:40:00	822	13:10:00	4	1	151
983	2026-10-22	16:20:00	811	14:50:00	4	3	150
984	2026-11-20	14:40:00	142	13:10:00	4	5	148
985	2026-06-02	16:20:00	811	14:50:00	4	2	152
986	2026-11-30	14:40:00	842	13:10:00	4	1	149
987	2026-09-02	16:20:00	823	14:50:00	4	4	148
988	2026-04-13	16:20:00	822	14:50:00	4	1	151
989	2026-12-16	14:40:00	811	13:10:00	4	2	150
990	2026-06-05	14:40:00	822	13:10:00	4	5	148
991	2026-10-16	14:40:00	142	13:10:00	4	5	148
992	2026-06-26	16:20:00	822	14:50:00	4	5	148
993	2026-09-28	16:20:00	842	14:50:00	4	1	149
994	2026-04-08	16:20:00	823	14:50:00	4	4	148
995	2026-06-10	16:20:00	823	14:50:00	4	4	148
996	2026-05-19	16:20:00	811	14:50:00	4	2	152
997	2026-12-02	14:40:00	811	13:10:00	4	2	150
998	2026-07-23	14:40:00	811	13:10:00	4	3	153
999	2026-06-11	16:20:00	811	14:50:00	4	3	153
1000	2026-10-01	16:20:00	811	14:50:00	4	3	150
1001	2026-07-14	14:40:00	811	13:10:00	4	2	152
1002	2026-07-30	14:40:00	811	13:10:00	4	3	153
1003	2026-12-08	16:20:00	831	14:50:00	4	4	148
1004	2026-07-10	14:40:00	822	13:10:00	4	5	148
1005	2026-07-27	16:20:00	822	14:50:00	4	1	151
1006	2026-09-04	14:40:00	822	13:10:00	4	5	148
1007	2026-12-23	14:40:00	811	13:10:00	4	2	150
1008	2026-09-14	14:40:00	822	13:10:00	4	1	151
1009	2026-10-05	16:20:00	842	14:50:00	4	1	149
1010	2026-10-26	14:40:00	842	13:10:00	4	1	149
1011	2026-04-06	14:40:00	822	13:10:00	4	1	151
1012	2026-05-27	14:40:00	823	13:10:00	4	4	148
1013	2026-05-08	16:20:00	822	14:50:00	4	5	148
1014	2026-12-09	14:40:00	811	13:10:00	4	2	150
1015	2026-06-19	16:20:00	822	14:50:00	4	5	148
1016	2026-05-26	14:40:00	811	13:10:00	4	2	152
1017	2026-09-30	16:20:00	811	14:50:00	4	2	150
1018	2026-10-07	14:40:00	811	13:10:00	4	2	150
1019	2026-11-18	14:40:00	811	13:10:00	4	2	150
1020	2026-10-28	14:40:00	811	13:10:00	4	2	150
1021	2026-12-07	14:40:00	842	13:10:00	4	1	149
1022	2026-11-02	14:40:00	842	13:10:00	4	1	149
1023	2026-09-01	14:40:00	811	13:10:00	4	2	152
1024	2026-10-22	14:40:00	811	13:10:00	4	3	150
1025	2026-04-24	14:40:00	822	13:10:00	4	5	148
1026	2026-06-01	16:20:00	822	14:50:00	4	1	151
1027	2026-11-30	16:20:00	842	14:50:00	4	1	149
1028	2026-09-02	14:40:00	823	13:10:00	4	4	148
1029	2026-04-13	14:40:00	822	13:10:00	4	1	151
1030	2026-11-20	16:20:00	142	14:50:00	4	5	148
1031	2026-06-02	14:40:00	811	13:10:00	4	2	152
1032	2026-06-26	14:40:00	822	13:10:00	4	5	148
1033	2026-12-16	16:20:00	811	14:50:00	4	2	150
1034	2026-06-05	16:20:00	822	14:50:00	4	5	148
1035	2026-10-16	16:20:00	142	14:50:00	4	5	148
1036	2026-07-23	16:20:00	811	14:50:00	4	3	153
1037	2026-06-11	14:40:00	811	13:10:00	4	3	153
1038	2026-10-01	14:40:00	811	13:10:00	4	3	150
1039	2026-09-28	14:40:00	842	13:10:00	4	1	149
1040	2026-04-08	14:40:00	823	13:10:00	4	4	148
1041	2026-06-10	14:40:00	823	13:10:00	4	4	148
1042	2026-05-19	14:40:00	811	13:10:00	4	2	152
1043	2026-12-02	16:20:00	811	14:50:00	4	2	150
1044	2026-12-08	14:40:00	831	13:10:00	4	4	148
1045	2026-07-14	16:20:00	811	14:50:00	4	2	152
1046	2026-07-30	16:20:00	811	14:50:00	4	3	153
1047	2026-09-04	16:20:00	822	14:50:00	4	5	148
1048	2026-12-23	16:20:00	811	14:50:00	4	2	150
1049	2026-09-14	16:20:00	822	14:50:00	4	1	151
1050	2026-07-10	16:20:00	822	14:50:00	4	5	148
1051	2026-07-27	14:40:00	822	13:10:00	4	1	151
1052	2026-04-06	16:20:00	822	14:50:00	4	1	151
1053	2026-10-05	14:40:00	842	13:10:00	4	1	149
1054	2026-10-26	16:20:00	842	14:50:00	4	1	149
1055	2026-05-27	16:20:00	823	14:50:00	4	4	148
1056	2026-05-08	14:40:00	822	13:10:00	4	5	148
1057	2026-12-09	16:20:00	811	14:50:00	4	2	150
1058	2026-06-19	14:40:00	822	13:10:00	4	5	148
1059	2026-05-26	16:20:00	811	14:50:00	4	2	152
1060	2026-05-14	16:20:00	811	14:50:00	4	3	153
1061	2026-05-20	16:20:00	823	14:50:00	4	4	148
1062	2026-11-16	16:20:00	842	14:50:00	4	1	149
1063	2026-05-07	16:20:00	811	14:50:00	4	3	153
1064	2026-05-29	16:20:00	822	14:50:00	4	5	148
1065	2026-05-13	14:40:00	823	13:10:00	4	4	148
1066	2026-06-30	16:20:00	811	14:50:00	4	2	152
1067	2026-05-25	14:40:00	822	13:10:00	4	1	151
1068	2026-06-09	14:40:00	811	13:10:00	4	2	152
1069	2026-04-15	16:20:00	823	14:50:00	4	4	148
1070	2026-09-17	16:20:00	811	14:50:00	4	3	153
1071	2026-07-07	16:20:00	811	14:50:00	4	2	152
1072	2026-04-16	14:40:00	811	13:10:00	4	3	153
1073	2026-09-15	16:20:00	811	14:50:00	4	2	152
1074	2026-07-24	14:40:00	822	13:10:00	4	5	148
1075	2026-06-08	14:40:00	822	13:10:00	4	1	151
1076	2026-07-02	14:40:00	811	13:10:00	4	3	153
1077	2026-10-08	14:40:00	811	13:10:00	4	3	150
1078	2026-09-29	16:20:00	831	14:50:00	4	4	148
1079	2026-04-22	14:40:00	823	13:10:00	4	4	148
1080	2026-10-02	16:20:00	142	14:50:00	4	5	148
1081	2026-10-27	14:40:00	831	13:10:00	4	4	148
1082	2026-04-17	16:20:00	822	14:50:00	4	5	148
1083	2026-11-10	14:40:00	831	13:10:00	4	4	148
1084	2026-07-21	16:20:00	811	14:50:00	4	2	152
1085	2026-12-17	16:20:00	811	14:50:00	4	3	150
1086	2026-06-24	16:20:00	823	14:50:00	4	4	148
1087	2026-05-22	14:40:00	822	13:10:00	4	5	148
1088	2026-06-22	16:20:00	822	14:50:00	4	1	151
1089	2026-09-09	16:20:00	823	14:50:00	4	4	148
1090	2026-09-10	14:40:00	811	13:10:00	4	3	153
1091	2026-07-31	14:40:00	822	13:10:00	4	5	148
1092	2026-05-12	14:40:00	811	13:10:00	4	2	152
1093	2026-05-21	14:40:00	811	13:10:00	4	3	153
1094	2026-04-20	14:40:00	822	13:10:00	4	1	151
1095	2026-10-13	14:40:00	831	13:10:00	4	4	148
1096	2026-12-14	16:20:00	842	14:50:00	4	1	149
1097	2026-12-22	16:20:00	831	14:50:00	4	4	148
1098	2026-06-03	16:20:00	823	14:50:00	4	4	148
1099	2026-10-12	16:20:00	842	14:50:00	4	1	149
1100	2026-09-25	16:20:00	822	14:50:00	4	5	148
1101	2026-12-15	16:20:00	831	14:50:00	4	4	148
1102	2026-04-21	14:40:00	811	13:10:00	4	2	152
1103	2026-05-15	14:40:00	822	13:10:00	4	5	148
1104	2026-06-17	16:20:00	823	14:50:00	4	4	148
1105	2026-04-14	14:40:00	811	13:10:00	4	2	152
1106	2026-09-08	16:20:00	811	14:50:00	4	2	152
1107	2026-10-06	14:40:00	831	13:10:00	4	4	148
1108	2026-06-18	16:20:00	811	14:50:00	4	3	153
1109	2026-05-11	14:40:00	822	13:10:00	4	1	151
1110	2026-05-01	16:20:00	822	14:50:00	4	5	148
1111	2026-11-19	14:40:00	811	13:10:00	4	3	150
1112	2026-10-23	14:40:00	142	13:10:00	4	5	148
1113	2026-11-17	14:40:00	831	13:10:00	4	4	148
1114	2026-06-12	16:20:00	822	14:50:00	4	5	148
1115	2026-04-27	16:20:00	822	14:50:00	4	1	151
1116	2026-10-30	16:20:00	142	14:50:00	4	5	148
1117	2026-09-18	14:40:00	822	13:10:00	4	5	148
1118	2026-11-12	14:40:00	811	13:10:00	4	3	150
1119	2026-06-16	14:40:00	811	13:10:00	4	2	152
1120	2026-06-15	16:20:00	822	14:50:00	4	1	151
1121	2026-04-07	14:40:00	811	13:10:00	4	2	152
1122	2026-09-03	14:40:00	811	13:10:00	4	3	153
1123	2026-06-04	14:40:00	811	13:10:00	4	3	153
1124	2026-10-14	14:40:00	811	13:10:00	4	2	150
1125	2026-05-28	14:40:00	811	13:10:00	4	3	153
1126	2026-10-20	16:20:00	831	14:50:00	4	4	148
1127	2026-11-11	16:20:00	811	14:50:00	4	2	150
1128	2026-07-01	14:40:00	823	13:10:00	4	4	148
1129	2026-09-11	16:20:00	822	14:50:00	4	5	148
1130	2026-06-25	16:20:00	811	14:50:00	4	3	153
1131	2026-11-05	16:20:00	811	14:50:00	4	3	150
1132	2026-10-21	16:20:00	811	14:50:00	4	2	150
1133	2026-09-16	16:20:00	823	14:50:00	4	4	148
1134	2026-09-24	16:20:00	811	14:50:00	4	3	153
1135	2026-07-09	14:40:00	811	13:10:00	4	3	153
1136	2026-04-09	14:40:00	811	13:10:00	4	3	153
1137	2026-11-09	14:40:00	842	13:10:00	4	1	149
1138	2026-12-18	14:40:00	142	13:10:00	4	5	148
1139	2026-12-03	16:20:00	811	14:50:00	4	3	150
1140	2026-04-23	16:20:00	811	14:50:00	4	3	153
1141	2026-07-17	14:40:00	822	13:10:00	4	5	148
1142	2026-10-15	16:20:00	811	14:50:00	4	3	150
1143	2026-07-22	16:20:00	823	14:50:00	4	4	148
1144	2026-12-04	16:20:00	142	14:50:00	4	5	148
1145	2026-07-13	14:40:00	822	13:10:00	4	1	151
1146	2026-07-29	16:20:00	823	14:50:00	4	4	148
1147	2026-11-04	14:40:00	811	13:10:00	4	2	150
1148	2026-07-06	16:20:00	822	14:50:00	4	1	151
1149	2026-07-03	16:20:00	822	14:50:00	4	5	148
1150	2026-10-29	14:40:00	811	13:10:00	4	3	150
1151	2026-12-01	16:20:00	831	14:50:00	4	4	148
1152	2026-11-25	16:20:00	811	14:50:00	4	2	150
1153	2026-07-08	14:40:00	823	13:10:00	4	4	148
1154	2026-04-10	16:20:00	822	14:50:00	4	5	148
1155	2026-11-13	16:20:00	142	14:50:00	4	5	148
1156	2026-11-26	14:40:00	811	13:10:00	4	3	150
1157	2026-12-11	16:20:00	142	14:50:00	4	5	148
1158	2026-07-15	14:40:00	823	13:10:00	4	4	148
1159	2026-11-24	14:40:00	831	13:10:00	4	4	148
1160	2026-11-06	16:20:00	142	14:50:00	4	5	148
1161	2026-05-18	16:20:00	822	14:50:00	4	1	151
1162	2026-04-28	16:20:00	811	14:50:00	4	2	152
1163	2026-11-27	14:40:00	142	13:10:00	4	5	148
1164	2026-04-30	16:20:00	811	14:50:00	4	3	153
1165	2026-07-28	16:20:00	811	14:50:00	4	2	152
1166	2026-07-16	16:20:00	811	14:50:00	4	3	153
1167	2026-06-23	16:20:00	811	14:50:00	4	2	152
1168	2026-06-29	16:20:00	822	14:50:00	4	1	151
1169	2026-12-10	16:20:00	811	14:50:00	4	3	150
1170	2026-09-07	16:20:00	822	14:50:00	4	1	151
1171	2026-12-21	14:40:00	842	13:10:00	4	1	149
1172	2026-10-09	14:40:00	142	13:10:00	4	5	148
1173	2026-07-22	14:40:00	823	13:10:00	4	4	148
1174	2026-12-04	14:40:00	142	13:10:00	4	5	148
1175	2026-07-13	16:20:00	822	14:50:00	4	1	151
1176	2026-11-04	16:20:00	811	14:50:00	4	2	150
1177	2026-07-29	14:40:00	823	13:10:00	4	4	148
1178	2026-07-06	14:40:00	822	13:10:00	4	1	151
1179	2026-07-03	14:40:00	822	13:10:00	4	5	148
1180	2026-12-01	14:40:00	831	13:10:00	4	4	148
1181	2026-10-29	16:20:00	811	14:50:00	4	3	150
1182	2026-11-25	14:40:00	811	13:10:00	4	2	150
1183	2026-07-08	16:20:00	823	14:50:00	4	4	148
1184	2026-04-10	14:40:00	822	13:10:00	4	5	148
1185	2026-11-24	16:20:00	831	14:50:00	4	4	148
1186	2026-11-06	14:40:00	142	13:10:00	4	5	148
1187	2026-05-18	14:40:00	822	13:10:00	4	1	151
1188	2026-11-13	14:40:00	142	13:10:00	4	5	148
1189	2026-11-26	16:20:00	811	14:50:00	4	3	150
1190	2026-12-11	14:40:00	142	13:10:00	4	5	148
1191	2026-07-15	16:20:00	823	14:50:00	4	4	148
1192	2026-11-27	16:20:00	142	14:50:00	4	5	148
1193	2026-04-28	14:40:00	811	13:10:00	4	2	152
1194	2026-04-30	14:40:00	811	13:10:00	4	3	153
1195	2026-07-28	14:40:00	811	13:10:00	4	2	152
1196	2026-07-16	14:40:00	811	13:10:00	4	3	153
1197	2026-06-23	14:40:00	811	13:10:00	4	2	152
1198	2026-06-29	14:40:00	822	13:10:00	4	1	151
1199	2026-12-10	14:40:00	811	13:10:00	4	3	150
1200	2026-09-07	14:40:00	822	13:10:00	4	1	151
1201	2026-10-09	16:20:00	142	14:50:00	4	5	148
1202	2026-12-21	16:20:00	842	14:50:00	4	1	149
1203	2026-10-12	14:40:00	842	13:10:00	4	1	149
1204	2026-09-25	14:40:00	822	13:10:00	4	5	148
1205	2026-12-15	14:40:00	831	13:10:00	4	4	148
1206	2026-12-22	14:40:00	831	13:10:00	4	4	148
1207	2026-06-03	14:40:00	823	13:10:00	4	4	148
1208	2026-04-21	16:20:00	811	14:50:00	4	2	152
1209	2026-09-08	14:40:00	811	13:10:00	4	2	152
1210	2026-10-06	16:20:00	831	14:50:00	4	4	148
1211	2026-06-18	14:40:00	811	13:10:00	4	3	153
1212	2026-05-11	16:20:00	822	14:50:00	4	1	151
1213	2026-05-01	14:40:00	822	13:10:00	4	5	148
1214	2026-05-15	16:20:00	822	14:50:00	4	5	148
1215	2026-06-17	14:40:00	823	13:10:00	4	4	148
1216	2026-04-14	16:20:00	811	14:50:00	4	2	152
1217	2026-11-17	16:20:00	831	14:50:00	4	4	148
1218	2026-06-12	14:40:00	822	13:10:00	4	5	148
1219	2026-11-19	16:20:00	811	14:50:00	4	3	150
1220	2026-10-23	16:20:00	142	14:50:00	4	5	148
1221	2026-11-12	16:20:00	811	14:50:00	4	3	150
1222	2026-04-27	14:40:00	822	13:10:00	4	1	151
1223	2026-10-30	14:40:00	142	13:10:00	4	5	148
1224	2026-09-18	16:20:00	822	14:50:00	4	5	148
1225	2026-06-16	16:20:00	811	14:50:00	4	2	152
1226	2026-06-15	14:40:00	822	13:10:00	4	1	151
1227	2026-04-07	16:20:00	811	14:50:00	4	2	152
1228	2026-05-28	16:20:00	811	14:50:00	4	3	153
1229	2026-10-20	14:40:00	831	13:10:00	4	4	148
1230	2026-11-11	14:40:00	811	13:10:00	4	2	150
1231	2026-09-03	16:20:00	811	14:50:00	4	3	153
1232	2026-06-04	16:20:00	811	14:50:00	4	3	153
1233	2026-10-14	16:20:00	811	14:50:00	4	2	150
1234	2026-07-01	16:20:00	823	14:50:00	4	4	148
1235	2026-09-11	14:40:00	822	13:10:00	4	5	148
1236	2026-06-25	14:40:00	811	13:10:00	4	3	153
1237	2026-10-21	14:40:00	811	13:10:00	4	2	150
1238	2026-11-05	14:40:00	811	13:10:00	4	3	150
1239	2026-07-09	16:20:00	811	14:50:00	4	3	153
1240	2026-09-16	14:40:00	823	13:10:00	4	4	148
1241	2026-09-24	14:40:00	811	13:10:00	4	3	153
1242	2026-04-09	16:20:00	811	14:50:00	4	3	153
1243	2026-12-03	14:40:00	811	13:10:00	4	3	150
1244	2026-11-09	16:20:00	842	14:50:00	4	1	149
1245	2026-12-18	16:20:00	142	14:50:00	4	5	148
1246	2026-07-17	16:20:00	822	14:50:00	4	5	148
1247	2026-04-23	14:40:00	811	13:10:00	4	3	153
1248	2026-10-15	14:40:00	811	13:10:00	4	3	150
\.


--
-- Data for Name: password_reset_token; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.password_reset_token (id, expires_at, token, used, user_id) FROM stdin;
\.


--
-- Data for Name: schedule_template; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.schedule_template (id, day_of_week, end_time, room, start_time, school_class_id, subject_id, teacher_id, term_id) FROM stdin;
61	MONDAY	14:40:00	822	13:10:00	4	1	151	2
62	MONDAY	16:20:00	822	14:50:00	4	1	151	2
63	MONDAY	14:40:00	842	13:10:00	4	1	149	1
64	MONDAY	16:20:00	842	14:50:00	4	1	149	1
65	TUESDAY	14:40:00	811	13:10:00	4	2	152	2
66	TUESDAY	16:20:00	811	14:50:00	4	2	152	2
67	WEDNESDAY	14:40:00	811	13:10:00	4	2	150	1
68	WEDNESDAY	16:20:00	811	14:50:00	4	2	150	1
69	THURSDAY	14:40:00	811	13:10:00	4	3	153	2
70	THURSDAY	16:20:00	811	14:50:00	4	3	153	2
71	THURSDAY	14:40:00	811	13:10:00	4	3	150	1
72	THURSDAY	16:20:00	811	14:50:00	4	3	150	1
73	WEDNESDAY	14:40:00	823	13:10:00	4	4	148	2
74	WEDNESDAY	16:20:00	823	14:50:00	4	4	148	2
75	TUESDAY	14:40:00	831	13:10:00	4	4	148	1
76	TUESDAY	16:20:00	831	14:50:00	4	4	148	1
77	FRIDAY	14:40:00	822	13:10:00	4	5	148	2
78	FRIDAY	16:20:00	822	14:50:00	4	5	148	2
79	FRIDAY	14:40:00	142	13:10:00	4	5	148	1
80	FRIDAY	16:20:00	142	14:50:00	4	5	148	1
\.


--
-- Data for Name: school_class; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.school_class (id, group_code, study_year, course_id, supervisor_id) FROM stdin;
4	SN	2	2	148
\.


--
-- Data for Name: school_event; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.school_event (id, date, description, title, type, created_by_id, school_class_id) FROM stdin;
\.


--
-- Data for Name: student_note; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.student_note (id, date, for_teacher, text, student_id) FROM stdin;
\.


--
-- Data for Name: subject; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.subject (id, name) FROM stdin;
1	ICTマネジメント
2	ICT実務
3	Web
4	キャリア
5	翻訳
\.


--
-- Data for Name: teacher_comment; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.teacher_comment (id, created_at, text, author_id, lesson_id) FROM stdin;
\.


--
-- Data for Name: term; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.term (id, end_date, name, start_date) FROM stdin;
1	2027-03-31	後期	2026-09-28
2	2026-09-27	前期	2026-04-01
\.


--
-- Name: app_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.app_user_id_seq', 183, true);


--
-- Name: attendance_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.attendance_id_seq', 1, false);


--
-- Name: course_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.course_id_seq', 18, true);


--
-- Name: day_override_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.day_override_id_seq', 1, false);


--
-- Name: file_attachment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.file_attachment_id_seq', 1, false);


--
-- Name: grade_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.grade_id_seq', 1, false);


--
-- Name: holiday_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.holiday_id_seq', 12, true);


--
-- Name: lesson_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.lesson_id_seq', 1248, true);


--
-- Name: password_reset_token_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.password_reset_token_id_seq', 1, true);


--
-- Name: schedule_template_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.schedule_template_id_seq', 80, true);


--
-- Name: school_class_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.school_class_id_seq', 4, true);


--
-- Name: school_event_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.school_event_id_seq', 1, false);


--
-- Name: student_note_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.student_note_id_seq', 1, false);


--
-- Name: subject_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.subject_id_seq', 25, true);


--
-- Name: teacher_comment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.teacher_comment_id_seq', 1, false);


--
-- Name: term_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.term_id_seq', 2, true);


--
-- Name: app_user app_user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.app_user
    ADD CONSTRAINT app_user_pkey PRIMARY KEY (id);


--
-- Name: attendance attendance_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.attendance
    ADD CONSTRAINT attendance_pkey PRIMARY KEY (id);


--
-- Name: course course_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT course_pkey PRIMARY KEY (id);


--
-- Name: day_override day_override_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.day_override
    ADD CONSTRAINT day_override_pkey PRIMARY KEY (id);


--
-- Name: file_attachment file_attachment_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.file_attachment
    ADD CONSTRAINT file_attachment_pkey PRIMARY KEY (id);


--
-- Name: grade grade_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.grade
    ADD CONSTRAINT grade_pkey PRIMARY KEY (id);


--
-- Name: holiday holiday_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.holiday
    ADD CONSTRAINT holiday_pkey PRIMARY KEY (id);


--
-- Name: lesson lesson_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lesson
    ADD CONSTRAINT lesson_pkey PRIMARY KEY (id);


--
-- Name: password_reset_token password_reset_token_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.password_reset_token
    ADD CONSTRAINT password_reset_token_pkey PRIMARY KEY (id);


--
-- Name: schedule_template schedule_template_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schedule_template
    ADD CONSTRAINT schedule_template_pkey PRIMARY KEY (id);


--
-- Name: school_class school_class_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.school_class
    ADD CONSTRAINT school_class_pkey PRIMARY KEY (id);


--
-- Name: school_event school_event_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.school_event
    ADD CONSTRAINT school_event_pkey PRIMARY KEY (id);


--
-- Name: student_note student_note_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_note
    ADD CONSTRAINT student_note_pkey PRIMARY KEY (id);


--
-- Name: subject subject_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.subject
    ADD CONSTRAINT subject_pkey PRIMARY KEY (id);


--
-- Name: teacher_comment teacher_comment_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teacher_comment
    ADD CONSTRAINT teacher_comment_pkey PRIMARY KEY (id);


--
-- Name: term term_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.term
    ADD CONSTRAINT term_pkey PRIMARY KEY (id);


--
-- Name: school_class uk94tu3x6oa7bbe9e3qrebbpi4l; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.school_class
    ADD CONSTRAINT uk94tu3x6oa7bbe9e3qrebbpi4l UNIQUE (study_year, group_code);


--
-- Name: app_user uk_1j9d9a06i600gd43uu3km82jw; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.app_user
    ADD CONSTRAINT uk_1j9d9a06i600gd43uu3km82jw UNIQUE (email);


--
-- Name: app_user uk_3k4cplvh82srueuttfkwnylq0; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.app_user
    ADD CONSTRAINT uk_3k4cplvh82srueuttfkwnylq0 UNIQUE (username);


--
-- Name: course uk_4xqvdpkafb91tt3hsb67ga3fj; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT uk_4xqvdpkafb91tt3hsb67ga3fj UNIQUE (name);


--
-- Name: password_reset_token uk_g0guo4k8krgpwuagos61oc06j; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.password_reset_token
    ADD CONSTRAINT uk_g0guo4k8krgpwuagos61oc06j UNIQUE (token);


--
-- Name: app_user uk_m00jdvc0voosp0jeo48e4a0m7; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.app_user
    ADD CONSTRAINT uk_m00jdvc0voosp0jeo48e4a0m7 UNIQUE (student_number);


--
-- Name: subject uk_p1jgir6qcpmqnxt4a8105wsot; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.subject
    ADD CONSTRAINT uk_p1jgir6qcpmqnxt4a8105wsot UNIQUE (name);


--
-- Name: lesson fk3h6v3yr40e1yr44ukbnoovb20; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lesson
    ADD CONSTRAINT fk3h6v3yr40e1yr44ukbnoovb20 FOREIGN KEY (teacher_id) REFERENCES public.app_user(id);


--
-- Name: school_event fk3ynxjpp86bt5vt4e6tuoaic5s; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.school_event
    ADD CONSTRAINT fk3ynxjpp86bt5vt4e6tuoaic5s FOREIGN KEY (school_class_id) REFERENCES public.school_class(id);


--
-- Name: grade fk5qvy9me7d0edvxl1qeguojkdt; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.grade
    ADD CONSTRAINT fk5qvy9me7d0edvxl1qeguojkdt FOREIGN KEY (student_id) REFERENCES public.app_user(id);


--
-- Name: day_override fk5u3o1s3oujspuo6dahey1iuy7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.day_override
    ADD CONSTRAINT fk5u3o1s3oujspuo6dahey1iuy7 FOREIGN KEY (school_class_id) REFERENCES public.school_class(id);


--
-- Name: lesson fk62vogrd9ewhu7udvhl7c62e81; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lesson
    ADD CONSTRAINT fk62vogrd9ewhu7udvhl7c62e81 FOREIGN KEY (school_class_id) REFERENCES public.school_class(id);


--
-- Name: grade fk65by7eln3rv98qrechgx4rvgv; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.grade
    ADD CONSTRAINT fk65by7eln3rv98qrechgx4rvgv FOREIGN KEY (lesson_id) REFERENCES public.lesson(id);


--
-- Name: lesson fk7ydr23s8y9j6lip5qrngoymx4; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lesson
    ADD CONSTRAINT fk7ydr23s8y9j6lip5qrngoymx4 FOREIGN KEY (subject_id) REFERENCES public.subject(id);


--
-- Name: teacher_comment fk85ffgvi1py41qwovvp3coppk5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teacher_comment
    ADD CONSTRAINT fk85ffgvi1py41qwovvp3coppk5 FOREIGN KEY (author_id) REFERENCES public.app_user(id);


--
-- Name: app_user fkaa3shdfpsnn8nknnsn64lsjfm; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.app_user
    ADD CONSTRAINT fkaa3shdfpsnn8nknnsn64lsjfm FOREIGN KEY (school_class_id) REFERENCES public.school_class(id);


--
-- Name: file_attachment fkai1xp9mynk5nq3bihjykqp5si; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.file_attachment
    ADD CONSTRAINT fkai1xp9mynk5nq3bihjykqp5si FOREIGN KEY (lesson_id) REFERENCES public.lesson(id);


--
-- Name: schedule_template fkalhd82llmd3kv2g9h4cdvedsn; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schedule_template
    ADD CONSTRAINT fkalhd82llmd3kv2g9h4cdvedsn FOREIGN KEY (school_class_id) REFERENCES public.school_class(id);


--
-- Name: attendance fkam01ddvne08oa3exny156v7al; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.attendance
    ADD CONSTRAINT fkam01ddvne08oa3exny156v7al FOREIGN KEY (lesson_id) REFERENCES public.lesson(id);


--
-- Name: teacher_comment fkc49hlrtxhdr61x6sxwtg2mh4f; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teacher_comment
    ADD CONSTRAINT fkc49hlrtxhdr61x6sxwtg2mh4f FOREIGN KEY (lesson_id) REFERENCES public.lesson(id);


--
-- Name: school_event fkcvbbawn9phmy4qkxih1tusxek; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.school_event
    ADD CONSTRAINT fkcvbbawn9phmy4qkxih1tusxek FOREIGN KEY (created_by_id) REFERENCES public.app_user(id);


--
-- Name: schedule_template fkdeobdoxr9jfnqie8ksbcq7cw9; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schedule_template
    ADD CONSTRAINT fkdeobdoxr9jfnqie8ksbcq7cw9 FOREIGN KEY (teacher_id) REFERENCES public.app_user(id);


--
-- Name: schedule_template fkdmn1q7de05ntlbpqyfc8g2iiw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schedule_template
    ADD CONSTRAINT fkdmn1q7de05ntlbpqyfc8g2iiw FOREIGN KEY (subject_id) REFERENCES public.subject(id);


--
-- Name: student_note fkfcp3o12tsdghnu06p5b2o4rho; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_note
    ADD CONSTRAINT fkfcp3o12tsdghnu06p5b2o4rho FOREIGN KEY (student_id) REFERENCES public.app_user(id);


--
-- Name: schedule_template fkgg0orvceaw67hvflh8uci2oqc; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schedule_template
    ADD CONSTRAINT fkgg0orvceaw67hvflh8uci2oqc FOREIGN KEY (term_id) REFERENCES public.term(id);


--
-- Name: holiday fkh5lcxee803r4yfdrolv3fn5b8; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.holiday
    ADD CONSTRAINT fkh5lcxee803r4yfdrolv3fn5b8 FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: day_override fkh5pki0bevslw0vok52dphorri; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.day_override
    ADD CONSTRAINT fkh5pki0bevslw0vok52dphorri FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: attendance fkhs8518992gwyxg89g3fx57he5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.attendance
    ADD CONSTRAINT fkhs8518992gwyxg89g3fx57he5 FOREIGN KEY (student_id) REFERENCES public.app_user(id);


--
-- Name: holiday fkiyk45qnpuduv6ukfwbe8jcxnv; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.holiday
    ADD CONSTRAINT fkiyk45qnpuduv6ukfwbe8jcxnv FOREIGN KEY (school_class_id) REFERENCES public.school_class(id);


--
-- Name: school_class fkl5ga0x3a5st9ykx4kj7ytj7bv; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.school_class
    ADD CONSTRAINT fkl5ga0x3a5st9ykx4kj7ytj7bv FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: password_reset_token fkli7wollcmb8tibymo3s94o57h; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.password_reset_token
    ADD CONSTRAINT fkli7wollcmb8tibymo3s94o57h FOREIGN KEY (user_id) REFERENCES public.app_user(id);


--
-- Name: file_attachment fkn2s5433i89pdtufbvp11km0h7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.file_attachment
    ADD CONSTRAINT fkn2s5433i89pdtufbvp11km0h7 FOREIGN KEY (uploaded_by_id) REFERENCES public.app_user(id);


--
-- Name: school_class fkpfncsj00sfl482sp0q972waqp; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.school_class
    ADD CONSTRAINT fkpfncsj00sfl482sp0q972waqp FOREIGN KEY (supervisor_id) REFERENCES public.app_user(id);


--
-- PostgreSQL database dump complete
--

\unrestrict 51lKo9ccosqxn10808Fyo2HWXT6NpD4J0dzdecj4tE570cka6bz2Ufa5XTh2Q9h

