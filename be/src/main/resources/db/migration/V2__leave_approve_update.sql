alter table leave_approval
alter column manager_id type integer using manager_id::integer;

