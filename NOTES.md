So, I googled: docker compose spring boot angular postgresql

Followed the top result, this [dev.to article](https://dev.to/techtter/docker-compose-demo-example-with-angular-spring-boot-postgresql-3do8) that points
to this [YouTube](https://youtu.be/ZZ2Llh4NhaY) that points to this [github repo](https://github.com/techtter/scrum-board/tree/master).  I clone it at depth one because I want the docker-compose.yml and a few other things.... 
```
git clone --depth 1 https://github.com/techtter/scrum-board.git
```

## Failure to launch
```
docker compose up -d
```

fails (well, the repo is three years old).... :-(     Errors include:
```
 => [kapi_scrum-app internal] load .dockerignore                                                                                                                0.0s
 => => transferring context: 2B                                                                                                                                 0.0s
 => CANCELED [kapi_scrum-ui internal] load metadata for docker.io/library/nginx:1.17.1-alpine                                                                   0.7s
 => CANCELED [kapi_scrum-ui internal] load metadata for docker.io/library/node:12.7-alpine                                                                      0.7s
 => CANCELED [kapi_scrum-app internal] load metadata for docker.io/library/openjdk:8-alpine                                                                     0.6s
 => ERROR [kapi_scrum-app internal] load metadata for docker.io/library/maven:3.6.1-jdk-8-slim                                                                  0.6s
 => [auth] library/nginx:pull token for registry-1.docker.io
```

Will I fix it or get something else to build upon?




#####

30 May 2023 at 20:22 Omaha time: `docker compose up -d` now works.

