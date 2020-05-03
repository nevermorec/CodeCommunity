# SpringBoot框架搭建的社区


# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/maven-plugin/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
  <update id="increaseViewCount" parameterType="com.community.community.model.Question">
    update QUESTION
    set
    VIEW_COUNT = VIEW_COUNT + #{viewCount,jdbcType=INTEGER}
    where ID=#{id}
  </update>
  
    <update id="increaseCommentCount" parameterType="com.community.community.model.Question">
      update QUESTION
      set
      COMMENT_COUNT = COMMENT_COUNT + #{commentCount,jdbcType=INTEGER}
      where ID=#{id}
    </update>
    
    
int increaseLikeCount(Comment record);

int increaseCommentCount(Comment record);

int increaseViewCount(Question record);

int increaseCommentCount(Question record);

List<Question> selectRelated(Question question);