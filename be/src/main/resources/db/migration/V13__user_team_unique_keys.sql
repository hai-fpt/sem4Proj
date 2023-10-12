alter table user_team
    add constraint unique_user_team
        unique (user_id, team_id);

