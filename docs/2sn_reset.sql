-- ============================================================================
--  Очистка засеянных данных группы 2SN — для чистого повторного сида.
--  Удаляет: студентов 2sn01..2sn30, засеянных преподавателей, группу 2SN,
--  её уроки/шаблоны/события и зависимые оценки/посещаемость/заметки/комментарии.
--  НЕ трогает: администратора, курсы, семестры и праздники (их сид добавит идемпотентно).
--
--  Порядок запуска для полного пересева:
--    1) docs/2sn_reset.sql   (этот файл)
--    2) docs/2sn_seed.sql    (создаёт всё заново)
-- ============================================================================

BEGIN;

-- Оценки/посещаемость: по студентам 2SN и по урокам группы 2SN
DELETE FROM grade      g USING app_user u WHERE g.student_id = u.id AND u.username LIKE '2sn%';
DELETE FROM attendance a USING app_user u WHERE a.student_id = u.id AND u.username LIKE '2sn%';
DELETE FROM student_note n USING app_user u WHERE n.student_id = u.id AND u.username LIKE '2sn%';

DELETE FROM grade g
    USING lesson l JOIN school_class c ON c.id = l.school_class_id
    WHERE g.lesson_id = l.id AND c.study_year = 2 AND c.group_code = 'SN';
DELETE FROM attendance a
    USING lesson l JOIN school_class c ON c.id = l.school_class_id
    WHERE a.lesson_id = l.id AND c.study_year = 2 AND c.group_code = 'SN';
DELETE FROM teacher_comment tc
    USING lesson l JOIN school_class c ON c.id = l.school_class_id
    WHERE tc.lesson_id = l.id AND c.study_year = 2 AND c.group_code = 'SN';
DELETE FROM file_attachment fa
    USING lesson l JOIN school_class c ON c.id = l.school_class_id
    WHERE fa.lesson_id = l.id AND c.study_year = 2 AND c.group_code = 'SN';

-- События, уроки и шаблоны группы 2SN
DELETE FROM school_event e USING school_class c
    WHERE e.school_class_id = c.id AND c.study_year = 2 AND c.group_code = 'SN';
DELETE FROM lesson l USING school_class c
    WHERE l.school_class_id = c.id AND c.study_year = 2 AND c.group_code = 'SN';
DELETE FROM schedule_template t USING school_class c
    WHERE t.school_class_id = c.id AND c.study_year = 2 AND c.group_code = 'SN';

-- Студенты 2SN
DELETE FROM app_user WHERE username LIKE '2sn%';

-- Снять классного руководителя и удалить группу
UPDATE school_class SET supervisor_id = NULL WHERE study_year = 2 AND group_code = 'SN';
DELETE FROM school_class WHERE study_year = 2 AND group_code = 'SN';

-- Засеянные преподаватели (после удаления их уроков/шаблонов/групп)
DELETE FROM app_user
    WHERE username IN ('horiuchi','suenaga','kuroda','hashimoto','tsuchiya','miyajima')
      AND role = 'TEACHER';

COMMIT;
