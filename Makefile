.PHONY: clean

up:
	docker-compose up

down:
	docker-compose down

delete-images:
	docker-compose down --rmi all

build:
	mvn clean package

clean: down delete-images build

start: down delete-images build up