# Minject

In short, Minject is a simple dependency injection library for Java inspired by frameworks like [Guice](https://github.com/google/guice) or [Spring](https://github.com/spring-projects/spring-framework) with full support for the [JSR-330](https://docs.oracle.com/javaee/7/api/javax/inject/package-summary.html) standard.

> It was supposed to be a simple practice project at first so I could learn java reflection the hard way, but somehow, I ended up reading the whole [book](https://www.amazon.com/Dependency-Injection-Design-patterns-Spring-ebook/dp/B0977ZS7P8) about dependency injection and inspecting how the whole concept was implemented by the frameworks mentioned above. And then, with that knowledge in mind, I decided to create such a library on my own. I was heavily inspired by Guice though, so a lot of mechanisms are very similar if not identical to those provided by Guice (especially the `TypeLiteral<T>` class).

## Usage
### Injector creation
To create a general-purpose injector simply use the `Minject` facade:

```java
public static void main(String[] args) {
    Injector injector = Minject.createInjector();
    injector.getInstanceOf(MyApp.class).start();
}
```

### Dependency injection
To mark a member of the class or its constructor as an injection point simply annotate it with `@Inject` annotation. As of JSR-330 standard, three types of dependency injection are supported:
* **Constructor injection**
	```java
	public class MyClass {
        private String secret;
        
	    @Inject
        public MyClass(@Named("some-secret") String secret) {
	        this.secret = secret;
	    }	
       
	    // ...
	}
	```
  
  > Note: If your class has only one constructor (including the default one) you can omit the annotation, but if there are multiple constructors in the class you have to annotate one constructor with `@Inject` annotation so the injector knows which one to use.

* **Field injection**
	```java
	public class MyClass {
        @Inject @Named("some-secret")
        private String secret;
		
        // ...
	}
	```
	
* **Method injection**
    ```java
    public class MyClass {
        private String secret;
        
	    @Inject
	    public void setString(@Named("some-secret") String secret) {
	        this.secret = secret;
        }
        
	    // ...
    }
    ```
	
When a new instance of a class is created, constructor injection is done first, followed by fields and then methods. Fields and methods in superclasses are injected before those in subclasses. It's worth noting that methods in superclasses annotated with `@Inject` that are overridden by methods in subclasses that are also annotated with `@Inject` are ignored. In other words, only the method from the subclass is injected.

Apart from that for every key with type `<T>` that is known to the injector, you can also inject a `Provider<T>` dependency into your class. `Provider<T>` interface is a simple functional interface with `get()` method that's when called returns an object of type `<U extends T>` to you (which is not necessarily a new object). You can use it to break circular dependencies in your object graph or to reinject dependencies into your class on-demand:

```java
public class MyClass {
    private Provider<AnotherClass> anotherClassProvider;
    
    @Inject
    public MyClass(Provider<AnotherClass> anotherClassProvider) {
        this.anotherClassProvider = anotherClassProvider;
    }
    
    public void foo() {
        // ...
        AnotherClass a = anotherClassProvider.get();
        AnotherClass b = anotherClassProvider.get();
        // ...
    }
}
```

### Bindings
Bindings in Minject are very similar to those offered by Guice. In short, they are used to let the injector know that when during the injection process it stumbles upon the type `<T>` that is to be injected, it should inject some type `<U extends T>` in that place.

#### Explicit bindings

To create an explicit binding, you first have to create a module by implementing the `Module` interface. Then in `configure()` method, you have access to the `Binder` interface that allows you to create bindings of all kinds by using simple API:

```java
class MyModule implements Module {
    @Override
    public void configure(Binder binder) {
        // Binds key <SomeInterface> to instance of <SomeInterfaceImpl> in Singleton scope
        binder.bind(SomeInterface.class)
              .to(SomeInterfaceImpl.class)
              .in(Singleton.class);
          
        // Binds key <@Named("secret-token"), String> to "my-secret-token"
        binder.bind(ClassKey.of(String.class, "secret-token"))
              .toInstance("my-secret-token");
          
        // Binds key <@Named("empty"), SomeClass> to value returned from provider
        binder.bind(ClassKey.of(SomeClass.class, "empty"))
              .toProvider(() -> new SomeClass(new LinkedList<>()))
              .unscoped();
    }
}
```
In the above example, the only qualifiers I've used are names, but you're able to create your own qualifier by creating an annotation that has `@Qualifier` meta-annotation on top of it:

```java
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface Empty {

}
```

And if you bind your class like this:

```java
class MyModule implements Module {
    // Unfortunately, the only way to get an instance of your annotation is to 
    // get it via reflection. To do that, we're creating a field that will have
    // all qualifier annotations used by us on top of it.
    @Empty 
    private int qualifiers;

    @Override
    public void configure(Binder binder) {
        Annotation emptyQualifier = MyModule.class.getDeclaredField("qualifiers")
						  .getAnnotation(Empty.class);
        
        // Binds key <@Empty, SomeClass> to value returned from provider
        binder.bind(ClassKey.of(SomeClass.class, emptyQualifier))
              .toProvider(() -> new SomeClass(new LinkedList<>()))
              .unscoped();
    }
}
```

It will only be injected by an injector if you annotate a field or method parameter with this qualifier annotation like this:

```java
public class MyClass {
    @Inject @Empty
    private SomeClass someClass;
    
    // ...
}
```

#### Generics

If you want to create a binding for a generic type, you'll have to explicitly use a `TypeLiteral<T>` class:

```java
class MyModule implements Module {
    @Override
    public void configure(Binder binder) {
        TypeLiteral<List<Integer>> type = new TypeLiteral<List<Integer>>() {};
        
        // Binds key <@Named("empty"), List<Integer>>> to value returned from provider
        binder.bind(ClassKey.of(type, "empty"))
              .toProvider(() -> new ArrayList())
              .unscoped();
          
        // Binds key <@Named("initialized"), List<Integer>>> to value returned from provider
        binder.bind(ClassKey.of(type, "initialized"))
              .toProvider(() -> new ArrayList(List.of(1, 2, 3)))
              .unscoped();
    }
}
```

> Notice the empty brackets (`{}`) after the creation of a type literal as they are very important - by putting them there, you are creating something that's called anonymous nested class, which superclass is the `TypeLiteral<List<Integer>>` itself. Because of the type erasure in java, the majority of type information is lost and we cannot access it at runtime. However, references to generic types in places like class definitions or method declarations are available as class metadata at runtime and we can use this fact to keep some of the generic type information at runtime.

#### Provider Methods

If the creation of an object is a bit more complicated, you can also create some method annotated with `@Provides` in your module and return some value from it:

```java
class MyModule implements Module {
    @Override
    public void configure(Binder binder) {
        // ...
    }
    
    @Provides @Singleton
    SomeInterface provideSomeInterface(A a, B b) {
        SomeInterfaceImpl impl = new SomeInterfaceImpl();
        // Do something with a and b
        return impl;
    }
}
```

> Notice that you can specify dependencies you need to create the object as method arguments and the injector will provide them to you when the method is called

#### Implicit bindings

If the class you're trying to obtain from the injector is not bound explicitly, the injector will try to create binding for it implicitly. If a class you're trying to get is *not qualified* and is *not parameterized* the injector will look at the class definition in search for scope annotation and will create proper binding for it. It's equivalent to such manual binding:

```java
class MyModule implements Module {
    @Override
    public void configure(Binder binder) {
        var scope = getScopeOf(MyClass.class).orElse(Unscoped.class);
        
        binder.bind(MyClass.class)
              .to(MyClass.class)
              .in(scope);
    }
}

```

### Scopes
By default, 3 scopes are known to the injector:
* Singleton (`@Singleton`)\
    Dependencies that are singleton scoped are created only once during the whole lifetime of an injector. That is, whenever the injector needs to inject a singleton scoped instance of type `<T>` it does so using the same object (that's either created by the injector or supplied via `bind(...).toInstance(...)` binder method.
* Eager singleton (`@EagerSingleton`)\
    Same as singleton scope, except eager singletons are instantiated at injector creation.
* Default scope (`@Unscoped`)\
    Dependencies with default scope are basically the opposite of singleton objects. Every time an unscoped instance of type `<T>` is requested injector creates a new instance. If you don't annotate your class with any other scope annotation, this is the scope it will get.

To create your own scope you'll need to create your own scope annotation with `@Scope` meta-annotation on top of it and implement a `ScopeHandler` interface for your scope. It works pretty much the same way as in Guice so take look at [this](https://github.com/google/guice/wiki/CustomScopes). Then by using a `Binder` in your configuration module simply bind a scope to your scope handler:

```java
public class MyModule implements Module {
    @Override
    public void configure(Binder binder) {
        MyScopeHandler myScopeHandler = new MyScopeHandler();
        binder.bindScope(MyScopeAnnotation.class, myScopeHandler);
    }
}
```

### Injector configuration

If you want to create an injector with some custom configuration you can use the `GenericInjectorFactory` for that:

```java
public static void main(String[] args) {
    // ...
    Injector injector = GenericInjectorFactory.empty()
                                              .addModules(...)
                                              .addModuleProcessors(...)
                                              .setBindingRegistrationStrategy(...)
                                              .setScopeRegistrationStrategy(...)
                                              .create();
    // ...
}
```

However, it will create an unconfigured injector with no defaults set (like default scopes or module processor responsible for processing methods annotated with `@Provides` annotation) so a better option might be to create an injector via `withDefaults()` method:

```java
public static void main(String[] args) {
    // ...
    Injector injector = GenericInjectorFactory.withDefaults()
                                              .addModules(...)
                                              .addModuleProcessors(...)
                                              .setBindingRegistrationStrategy(...)
                                              .setScopeRegistrationStrategy(...)
                                              .create();
    // ...
}
```

#### Module processors

You can create your own module processor by implementing the `ModuleProcessor` interface:

```java
public class ModuleCounter implements ModuleProcessor {
    private static final Logger LOG = ...
    private int installedModulesCounter;
    
    @Override
    public void before(Binder binder, Injector injector) {
        installedModulesCounter = 0;
    }

    @Override
    public void process(Module module, Binder binder, Injector injector) {
        installedModulesCounter++;
    }

    @Override
    public void after(Binder binder, Injector injector) {
        LOG.info("Number of modules installed: " + installedModulesCounter);
    }
}
```

> Note: Check `GenericModuleProcessor` class to see how a real-world module processors may look like

#### Registration strategies

By default, Minject has two registration strategies available:
* `RegistrationStrategies.THROW`\
   When trying to bind a key or scope that is already bound an exception will be thrown (default behavior)
* `RegistrationStrategies.REPLACE`\
   When trying to bind a key or scope that is already bound the previous one will be replaced by a new one
   
You can create your own registration strategy by implementing the `RegistrationStrategy` interface:
```java
public class LoggingReplacingRegistrationStrategy implements RegistrationStrategy {
    private static final Logger LOG = ...
    private RegistrationStrategy replacingStrategy = RegistrationStrategies.REPLACE;

    @Override
    public <T> T register(Supplier<T> registrationCallback, boolean exists, String key) {
        if (exists) {
            LOG.info("Replaced binding for " + key);
        }
        return replacingStrategy.register(registrationCallback, exists, key);
    }
}
```

