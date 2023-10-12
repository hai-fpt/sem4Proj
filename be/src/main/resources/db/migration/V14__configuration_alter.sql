alter table configuration
    add host_address varchar;

alter table configuration
    add google_client_id varchar;

UPDATE configuration SET host_address = 'http://192.168.1.73/', google_client_id = '73440768880-n6qrafg7tln8hvc2aj8t2lkf74r29kra.apps.googleusercontent.com' WHERE id = 1;
