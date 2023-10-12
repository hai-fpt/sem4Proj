alter table user_leave add column inform_to_new jsonb;
alter table user_leave drop inform_to;
alter table user_leave rename column inform_to_new to inform_to;