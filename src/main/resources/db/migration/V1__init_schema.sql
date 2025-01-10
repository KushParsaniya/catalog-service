create table books (
    id bigserial primary key not null ,
    author varchar(255) not null ,
    isbn varchar(255) unique not null,
    price float8 not null ,
    title varchar(255) not null,
    created_at timestamp not null,
    modified_at timestamp not null,
    version integer not null
);