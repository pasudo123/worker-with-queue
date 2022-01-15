# springboot with quartz
* Job, JobDetail, Trigger, Scheduler 가 존재
* 스프링을 통해 관리하지만, 관리하는 방식은 두가지
  * 스프링 (편의 클래스 사용)
  * 쿼츠

# SpringBeanJobFactory vs QuartzJobBean
* Job 을 빈으로 등록하기 위해 2가지 방식이 제공되고 있다. 차이가 궁금하다.   
  * 찾아보면 두 개는 유사하다. 
* bean-style 로 `SpringBeanJobFactory` 를 이용하여 잡을 빈으로 등록할 수 있다. (이때는 constructor injection 에 제약이 있음)
* QuartzJobBean 은 Job interface 를 별도로 구현해서 한번더 래핑한 클래스다.
  * 근데 왜 설명글에는 종속성 주입을 선호하는 방법이, `Note that the preferred way to apply dependency injection to Job instances is via a JobFactory` 이라고 하는걸까
  * QuartzJobBean 은 잡을 빈으로 등록이 되긴하던데.. ?

[QuartzJobBean 설명글](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/quartz/QuartzJobBean.html)
```java
Simple implementation of the Quartz Job interface, 
applying the passed-in JobDataMap and also the SchedulerContext as bean property values. 
This is appropriate because a new Job instance will be created for each execution. 
JobDataMap entries will override SchedulerContext entries with the same keys.
```

[SpringBeanJobFactory 설명글](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/quartz/SpringBeanJobFactory.html)
```java
Subclass of AdaptableJobFactory that also supports Spring-style dependency injection on bean properties. 
This is essentially the direct equivalent of Spring's QuartzJobBean in the shape of a Quartz JobFactory.
Applies scheduler context, job data map and trigger data map entries as bean property values. 
If no matching bean property is found, the entry is by default simply ignored. 
This is analogous to QuartzJobBean's behavior.
```

# reference
* https://www.baeldung.com/spring-quartz-schedule
* https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#io.quartz