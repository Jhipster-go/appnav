# jhipster-xcx
基于jhipster构建的微信小程序导航

### 项目生成选项
    
    (1/15) Which *type* of application would you like to create? Monolithic application (recommended for simple projects)? 
    (2/15) Which *Framework* would you like to use for the client? AngularJS 1.x?   
    (3/15) What is the base name of your application? appnav? 
    (4/15) Would you like to install other generators from the JHipster Marketplace? No? 
    (5/15) What is your default Java package name? com.mycompany.myapp? 
    (6/15) Which *type* of authentication would you like to use? HTTP Session Authentication (stateful, default Spring Security mechanism)? 
    (7/15) Which *type* of database would you like to use? SQL (H2, MySQL, MariaDB, PostgreSQL, Oracle, MSSQL)? 
    (8/15) Which *production* database would you like to use? MySQL? 
    (9/15) Which *development* database would you like to use? MySQL? 
    (10/15) Do you want to use Hibernate 2nd level cache? Yes, with ehcache (local cache, for a single node)? 
    (11/15) Would you like to use Maven or Gradle for building the backend? Gradle? 
    (12/15) Which other technologies would you like to use? Social login (Google, Facebook, Twitter), Search engine using Elasticsearch, WebSockets using Spring Websocket? 
    (13/15) Would you like to use the LibSass stylesheet preprocessor for your CSS? No? 
    (14/15) Would you like to enable internationalization support? Yes? 
        Please choose the native language of the application? Chinese (Simplified)? 
        Please choose additional languages to install English? 
    (15/15) Besides JUnit and Karma, which testing frameworks would you like to use? Gatling, Cucumber, Protractor

### 实体类创建如下

    /**
     * 小程序实体.
     * @author Bruce true hipster
     */
    entity WxApp {
    	icon String,
        /** name */
        name String required,
        /** description */
        description String, 
        /**发布日期*/
        publicationDate LocalDate ,
        /**截图*/
        screenshot String ,
        qcode String required,
        score BigDecimal,
        views Long
    }
    
    entity Comment {
    	comment String required,
        publicationDate LocalDate
    }
    
    /**
     * 类别.
     * @author Bruce true hipster
     */
    entity Category {
        /** name */
        name String required
    }
    
    /**
     * 标签.
     * @author Bruce true hipster
     */
    entity Tag {
    	name String required
    }
    
    relationship ManyToOne {
      Comment{user(firstName)} to User,
      WxApp{user(firstName)} to User 
    }
    
    relationship OneToMany {
      WxApp{comment} to Comment{wxapp(name)},
      WxApp{category(name)} to Category,
      WxApp{tag(name)} to Tag
    }
    
    paginate WxApp, Comment, Category, Tag with infinite-scroll
    
    dto WxApp, Comment, Category, Tag with mapstruct
    
    service WxApp, Comment, Category, Tag with serviceClass
