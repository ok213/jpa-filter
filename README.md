# jpa-filter

_based on_  
_https://spring.io/blog/2011/04/26/advanced-spring-data-jpa-specifications-and-querydsl/_
_https://www.baeldung.com/spring-rest-api-query-search-language-tutorial_  

1. cd ENV  
2. docker-compose up -d  
3. mvn clean compile *
4. JpaFilterApplication start... 


_**search request:**_  
_(postman collection: ENV/jpa-filter.postman_collection.json)_  

http://localhost:8080/my/persons?search=&sort=id,desc  
http://localhost:8080/my/persons?search=city:Piter,building:2  
http://localhost:8080/my/persons?search=city:Piter&sort=dob,desc 
http://localhost:8080/my/persons?search=street:Ma*&sort=age,desc
http://localhost:8080/my/persons?search=dob>19850101&sort=age,desc       
http://localhost:8080/my/persons?search=firstName:Jo*
http://localhost:8080/my/persons?search=lastName:F*&sort=age,asc
http://localhost:8080/my/persons?search=city:Moscow&sort=lastName,asc
http://localhost:8080/my/persons?search=firstName:Jo*,dob>19750101

#
###API
#####REST Query Language with Spring and JPA Criteria:
**GET** _http://localhost:8080/api/person?search=lastName:John,age>20_
**GET** _http://localhost:8080/api/person?search=age<39_  
**GET** _http://localhost:8080/api/person?search=age>50_  

#####REST Query Language with Spring Data JPA Specifications:
**GET** _http://localhost:8080/api/passport?search=series>B_  
**GET** _http://localhost:8080/api/passport?search=no:3_  
**GET** _http://localhost:8080/api/passport?search=series:CC,no:8_  
 
#####REST Query Language with Spring Data JPA and QueryDSL:
**GET** _http://localhost:8080/api/qdsl/person?search=age<50_  
**GET** _http://localhost:8080/api/qdsl/person?search=lastName:First_  
**GET** _http://localhost:8080/api/qdsl/person?search=lastName:First,age>30_  


#
*Для того, чтобы Spring мог работать с объектным представлением табличных сущностей,
ему необходимо создать связь между ними. Все связи он по умолчанию помещает 
в папку build.generated.source.apt.структура_проекта. Для того, чтобы создать эти связи
нужно очистить проект и собрать его классы.
В maven это достигается последовательным исполнением задач clean и compile:  
 (jpa-filter -> Lifecycle -> clean, compile).  
В gradle это достигается последовательным исполнением задач clean и classes:  
 (gradle -> Tasks -> build -> clean, classes). 

Связь между табличными сущностями в QueryDSL формируется в соответствующем классе с приставкой Q.
Для класса Person это будет QPerson. QPerson — это весь репозиторий. В нём есть person:
 QPerson person, у person есть имена: QPerson person.firstName, возраст: QPerson person.age и т.п.

