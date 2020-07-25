/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory;

import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

/**
 * The root interface for accessing a Spring bean container.
 * This is the basic client view of a bean container;
 * further interfaces such as {@link ListableBeanFactory} and
 * {@link org.springframework.beans.factory.config.ConfigurableBeanFactory}
 * are available for specific purposes.
 * 访问spring bean容器的根接口。这是一个bean容器的基本客户端视图
 * 更多的接口，例如ListableBeanFactory和ConfigurableBeanFactory，是用于特定目的的
 *
 * <p>This interface is implemented by objects that hold a number of bean definitions,
 * each uniquely identified by a String name. Depending on the bean definition,
 * the factory will return either an independent instance of a contained object
 * (the Prototype design pattern), or a single shared instance (a superior
 * alternative to the Singleton design pattern, in which the instance is a
 * singleton in the scope of the factory). Which type of instance will be returned
 * depends on the bean factory configuration: the API is the same. Since Spring
 * 2.0, further scopes are available depending on the concrete application
 * context (e.g. "request" and "session" scopes in a web environment).
 * 这个接口是通过持有一系列bean定义的对象来实现的，
 * 每一个都是通过一个字符串名称来作为唯一标识符。依赖于bean定义
 * 这个工厂将返回一个所包含对象的一个独立实例（原型设计模式），或者一个单例共享实例（一种优于单例设计模式的替代方案，单例设计模式的实例在工厂范围scope内是单例的）
 * 所返回的实例类型，是由bean工厂的配置所决定的。这个API是一样的。
 * 自从spring2.0开始，根据具体的应用上下文，更多范围scopes有效（例如在web环境中的request, session）
 * 
 * 
 * scope为single单例（single instance）；为prototype原型，即每次都创建一个实例(independent instance)；
 * 为request
 * 为session
 *
 * <p>The point of this approach is that the BeanFactory is a central registry
 * of application components, and centralizes configuration of application
 * components (no more do individual objects need to read properties files,
 * for example). See chapters 4 and 11 of "Expert One-on-One J2EE Design and
 * Development" for a discussion of the benefits of this approach.
 * 这个方法的要点是，这个BeanFactory是应用组件的一个核心注册器，是应用组件的集中配置（例如不再需要单个对象读取属性文件）
 * 查看书Expert One-on-One J2EE Design and Development
 *
 * <p>Note that it is generally better to rely on Dependency Injection
 * ("push" configuration) to configure application objects through setters
 * or constructors, rather than use any form of "pull" configuration like a
 * BeanFactory lookup. Spring's Dependency Injection functionality is
 * implemented using this BeanFactory interface and its subinterfaces.
 * 注意，通过setters或者constructors来配置应用对象，通常最好的方法是，依靠依赖注入DI（推配置），而不是使用像BeanFactory查找一样，使用任意形式的“拉配置”
 * Spring的依赖注入功能是通过这个BeanFactory接口和它的子接口来实现的
 * 
 *
 * <p>Normally a BeanFactory will load bean definitions stored in a configuration
 * source (such as an XML document), and use the {@code org.springframework.beans}
 * package to configure the beans. However, an implementation could simply return
 * Java objects it creates as necessary directly in Java code. There are no
 * constraints on how the definitions could be stored: LDAP, RDBMS, XML,
 * properties file, etc. Implementations are encouraged to support references
 * amongst beans (Dependency Injection).
 * 通常，一个BeanFactory将加载存储在一个配置源中的bean定义(例如一个XML文档)，并使用org.springframework.beans包来配置这些beans.
 * 但是，一个实现可简单的返回它直接在Java代码中需要创建的Java对象。
 * 这里没有约束这个定义要怎样存储：LDAP, RDBMS, XML属性文件等。
 * 这个实现支持在beans之间的引用（依赖注入）
 *
 * <p>In contrast to the methods in {@link ListableBeanFactory}, all of the
 * operations in this interface will also check parent factories if this is a
 * {@link HierarchicalBeanFactory}. If a bean is not found in this factory instance,
 * the immediate parent factory will be asked. Beans in this factory instance
 * are supposed to override beans of the same name in any parent factory.
 * 与ListableBeanFactory中的方法相反，
 * 所有HierarchicalBeanFactory接口中的操作都会检查父工长
 * 如果一个bean在工厂实例中找不到，将会直接访问父工厂。
 * 这个工厂实例的bean支持重写任何父工厂中相同名称的bean
 *
 * <p>Bean factory implementations should support the standard bean lifecycle interfaces
 * as far as possible. The full set of initialization methods and their standard order is:
 * Bean工厂的实现应该尽可能的支持标准的bean生命周期接口。所有的初始化方法，和他们的标准顺序是：
 * <ol>
 * <li>BeanNameAware's {@code setBeanName}
 * <li>BeanClassLoaderAware's {@code setBeanClassLoader}
 * <li>BeanFactoryAware's {@code setBeanFactory}
 * <li>EnvironmentAware's {@code setEnvironment}
 * <li>EmbeddedValueResolverAware's {@code setEmbeddedValueResolver}
 * <li>ResourceLoaderAware's {@code setResourceLoader}
 * (only applicable when running in an application context)
 * <li>ApplicationEventPublisherAware's {@code setApplicationEventPublisher}
 * (only applicable when running in an application context)
 * <li>MessageSourceAware's {@code setMessageSource}
 * (only applicable when running in an application context)
 * <li>ApplicationContextAware's {@code setApplicationContext}
 * (only applicable when running in an application context)
 * <li>ServletContextAware's {@code setServletContext}
 * (only applicable when running in a web application context)
 * <li>{@code postProcessBeforeInitialization} methods of BeanPostProcessors
 * <li>InitializingBean's {@code afterPropertiesSet}
 * <li>a custom init-method definition
 * <li>{@code postProcessAfterInitialization} methods of BeanPostProcessors
 * </ol>
 *
 * <p>On shutdown of a bean factory, the following lifecycle methods apply:
 * <ol>
 * <li>{@code postProcessBeforeDestruction} methods of DestructionAwareBeanPostProcessors
 * <li>DisposableBean's {@code destroy}
 * <li>a custom destroy-method definition
 * </ol>
 * 
 * 关闭bean工厂，下面的生命周期方法可用：
 * 1）postProcessBeforeDestruction
 * 2）DisposableBean
 * 3）自定义的销毁方法定义
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 13 April 2001
 * @see BeanNameAware#setBeanName
 * @see BeanClassLoaderAware#setBeanClassLoader
 * @see BeanFactoryAware#setBeanFactory
 * @see org.springframework.context.ResourceLoaderAware#setResourceLoader
 * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher
 * @see org.springframework.context.MessageSourceAware#setMessageSource
 * @see org.springframework.context.ApplicationContextAware#setApplicationContext
 * @see org.springframework.web.context.ServletContextAware#setServletContext
 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization
 * @see InitializingBean#afterPropertiesSet
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getInitMethodName
 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization
 * @see DisposableBean#destroy
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getDestroyMethodName
 */
public interface BeanFactory {

	/**
	 * Used to dereference a {@link FactoryBean} instance and distinguish it from
	 * beans <i>created</i> by the FactoryBean. For example, if the bean named
	 * {@code myJndiObject} is a FactoryBean, getting {@code &myJndiObject}
	 * will return the factory, not the instance returned by the factory.
	 * 
	 * 用来间接访问FactoryBean实例，并用来区分由FactoryBean创建的beans
	 * 例如，如果名称为myJndiObject的bean是一个FactoryBean，获取&myJndiObject将会返回来factory，而不是factory返回来的实例
	 * 
	 * 如果那个bean不是继承于FactoryBean，在通过getBean("&bean名称")获取bean时，则抛异常
	 */
	String FACTORY_BEAN_PREFIX = "&";


	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>This method allows a Spring BeanFactory to be used as a replacement for the
	 * Singleton or Prototype design pattern. Callers may retain references to
	 * returned objects in the case of Singleton beans.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to retrieve
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no bean with the specified name
	 * @throws BeansException if the bean could not be obtained
	 * 
	 * 返回一个指定bean的实例，这个实例可能是共享的，或独立的，
	 * 这个方法允许一个Spring BeanFactory作为单例或原型设计模式的替代品
	 * 在单例bean的情况下，调用者可能保留返回对象的引用
	 * 将别名转换成相对应的规范bean名称。
	 * 如果不能在这个factory实例中找到bean，将会访问父factory
	 * 
	 * 
	 */
	Object getBean(String name) throws BeansException;

	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>Behaves the same as {@link #getBean(String)}, but provides a measure of type
	 * safety by throwing a BeanNotOfRequiredTypeException if the bean is not of the
	 * required type. This means that ClassCastException can't be thrown on casting
	 * the result correctly, as can happen with {@link #getBean(String)}.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to retrieve
	 * @param requiredType type the bean must match; can be an interface or superclass
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition
	 * @throws BeanNotOfRequiredTypeException if the bean is not of the required type
	 * @throws BeansException if the bean could not be created
	 * 
	 * 返回一个指定bean的实例，这个实例可能是共享的，或独立的
	 * 跟getBean(String)的行为相似，但如果这个bean不是所需要的类型，将会通过抛出BeanNotOfRequiredTypeException来提供一个类型安全的度量
	 * 这个意味着ClassCastException异常不会在转换成正确结果时抛出，但却在getBean(String)中可能抛出
	 * 
	 * 将别名转换成相对应的规范bean名称
	 * 如果不能在这个factory实例中找到bean，将会访问父factory
	 * 
	 */
	<T> T getBean(String name, Class<T> requiredType) throws BeansException;

	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>Allows for specifying explicit constructor arguments / factory method arguments,
	 * overriding the specified default arguments (if any) in the bean definition.
	 * 
	 * @param name the name of the bean to retrieve
	 * @param args arguments to use when creating a bean instance using explicit arguments
	 * (only applied when creating a new instance as opposed to retrieving an existing one)
	 * 仅用于创建一个新的实例，而不是检索一个已经存在的实例
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition
	 * @throws BeanDefinitionStoreException if arguments have been given but
	 * the affected bean isn't a prototype
	 * @throws BeansException if the bean could not be created
	 * @since 2.5
	 * 
	 * 返回一个指定bean的实例，这个实例可能是共享的，或独立的
	 * 允许显式指定构造器参数，工厂方法参数，
	 * 重写bean定义中指定的默认参数（如果有）
	 * 
	 */
	Object getBean(String name, Object... args) throws BeansException;

	/**
	 * Return the bean instance that uniquely matches the given object type, if any.
	 * <p>This method goes into {@link ListableBeanFactory} by-type lookup territory
	 * but may also be translated into a conventional by-name lookup based on the name
	 * of the given type. For more extensive retrieval operations across sets of beans,
	 * use {@link ListableBeanFactory} and/or {@link BeanFactoryUtils}.
	 * @param requiredType type the bean must match; can be an interface or superclass
	 * @return an instance of the single bean matching the required type
	 * @throws NoSuchBeanDefinitionException if no bean of the given type was found
	 * @throws NoUniqueBeanDefinitionException if more than one bean of the given type was found
	 * @throws BeansException if the bean could not be created
	 * @since 3.0
	 * @see ListableBeanFactory
	 * 
	 * 返回唯一匹配给定类型的bean实例（如果有）
	 * 这个方法按类型查找区域进入ListableBeanFactory， 但也可能转换成基于给定类型的名称的，按常规名称查找
	 * 要横跨一系列bean的更广泛的检索操作，请使用ListableBeanFactory 和BeanFactoryUtils
	 * 
	 */
	<T> T getBean(Class<T> requiredType) throws BeansException;

	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>Allows for specifying explicit constructor arguments / factory method arguments,
	 * overriding the specified default arguments (if any) in the bean definition.
	 * <p>This method goes into {@link ListableBeanFactory} by-type lookup territory
	 * but may also be translated into a conventional by-name lookup based on the name
	 * of the given type. For more extensive retrieval operations across sets of beans,
	 * use {@link ListableBeanFactory} and/or {@link BeanFactoryUtils}.
	 * @param requiredType type the bean must match; can be an interface or superclass
	 * @param args arguments to use when creating a bean instance using explicit arguments
	 * (only applied when creating a new instance as opposed to retrieving an existing one)
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition
	 * @throws BeanDefinitionStoreException if arguments have been given but
	 * the affected bean isn't a prototype
	 * @throws BeansException if the bean could not be created
	 * @since 4.1
	 * 
	 * 返回一个指定bean的实例，这个实例可能是独立的
	 * 
	 * 允许显式指定构造器参数，或方法参数
	 * 重写bean定义中指定的默认参数（如果有）
	 * 这个方法按类型查找区域进入ListableBeanFactory， 但也可能转换成基于给定类型的名称的，按常规名称查找
	 * 
	 * 要横跨一系列bean的更广泛的检索操作，请使用ListableBeanFactory 和BeanFactoryUtils
	 */
	<T> T getBean(Class<T> requiredType, Object... args) throws BeansException;

	/**
	 * Return a provider for the specified bean, allowing for lazy on-demand retrieval
	 * of instances, including availability and uniqueness options.
	 * @param requiredType type the bean must match; can be an interface or superclass 接口或子类
	 * @return a corresponding provider handle
	 * @since 5.1
	 * @see #getBeanProvider(ResolvableType)
	 * 
	 * 对指定的bean返回一个provider，允许延迟按需检索实例，包含可用性和唯一性选项
	 */
	<T> ObjectProvider<T> getBeanProvider(Class<T> requiredType);

	/**
	 * Return a provider for the specified bean, allowing for lazy on-demand retrieval
	 * of instances, including availability and uniqueness options.
	 * @param requiredType type the bean must match; can be a generic type declaration泛型类型声明.
	 * 
	 * Note that collection types are not supported here, in contrast to reflective
	 * injection points. For programmatically retrieving a list of beans matching a
	 * specific type, specify the actual bean type as an argument here and subsequently
	 * use {@link ObjectProvider#orderedStream()} or its lazy streaming/iteration options.
	 * 注意，与反射注入点不同，这里不支持集合类型。
	 * 对于用编程方式检索一系列bean匹配一个特定的类型，指定真实bean类型作为参数，然后使用ObjectProvider.orderedStream()或者它的延迟流，或迭代选项
	 * 
	 * @return a corresponding provider handle
	 * @since 5.1
	 * @see ObjectProvider#iterator()
	 * @see ObjectProvider#stream()
	 * @see ObjectProvider#orderedStream()
	 * 
	 * 对指定的bean返回一个provider，允许延迟按需检索实例，包含可用性和唯一性选项
	 * 
	 * 
	 */
	<T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType);

	/**
	 * Does this bean factory contain a bean definition or externally registered singleton
	 * instance with the given name?
	 * <p>If the given name is an alias, it will be translated back to the corresponding
	 * canonical bean name.
	 * <p>If this factory is hierarchical, will ask any parent factory if the bean cannot
	 * be found in this factory instance.
	 * <p>If a bean definition or singleton instance matching the given name is found,
	 * this method will return {@code true} whether the named bean definition is concrete
	 * or abstract, lazy or eager, in scope or not. Therefore, note that a {@code true}
	 * return value from this method does not necessarily indicate that {@link #getBean}
	 * will be able to obtain an instance for the same name.
	 * @param name the name of the bean to query
	 * @return whether a bean with the given name is present
	 * 
	 * 这个bean factory是否包含一个bean定义，或者另外注册了所给定名称的单例实例
	 * 如果所给定的名称是一个别名，它将会转换成相对应的常规名称
	 * 
	 * 如果这个工厂是层次结构hierarchical， 如果在这个工厂实例中找不到bean， 将会访问任意父级factory
	 * 
	 * 如果找到匹配所给定名称的bean定义，或单实例。
	 * 这个方法将会返回true，不管这个命名的bean定义时具体的，或抽象的，懒加载的，或饥饿的，不管是否是scope
	 * 因此，注意返回true值不是必定指getBean(...)将会返回一个相同名称的实例
	 */
	boolean containsBean(String name);

	/**
	 * Is this bean a shared singleton? That is, will {@link #getBean} always
	 * return the same instance?
	 * <p>Note: This method returning {@code false} does not clearly indicate
	 * independent instances. It indicates non-singleton instances, which may correspond
	 * to a scoped bean as well. Use the {@link #isPrototype} operation to explicitly
	 * check for independent instances.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @return whether this bean corresponds to a singleton instance
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @see #getBean
	 * @see #isPrototype
	 * 
	 * 
	 * single instances
	 * 
	 * 这个bean是共享单例？这样getBean(...)将会返回相同的实例？
	 * 注意：这个方法返回false并不就是明确的指独立的实例。
	 * 它是只没有单例的实例，它也可能相当于一个作用域的bean.
	 * 使用isPrototype操作来显式的检查独立实例
	 * 
	 * 将别名转换成相对应的常规bean名称。
	 * 如果不能在这个factory实例中找到bean，将会访问父factory
	 */
	boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Is this bean a prototype? That is, will {@link #getBean} always return
	 * independent instances?
	 * <p>Note: This method returning {@code false} does not clearly indicate
	 * a singleton object. It indicates non-independent instances, which may correspond
	 * to a scoped bean as well. Use the {@link #isSingleton} operation to explicitly
	 * check for a shared singleton instance.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @return whether this bean will always deliver independent instances
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 2.0.3
	 * @see #getBean
	 * @see #isSingleton
	 * 
	 * 
	 * independent instances
	 * 
	 * 这个bean是否为原型？这样getBean(...)将会返回独立的实例？
	 * 
	 * 注意，这个方法返回false并不就是明确的指单实例对象。
	 * 它指的是没有独立的实例，它也可能相当于一个作用域bean
	 * 使用isSingleton(...)操作来明确的检查一个共享单例实例
	 * 
	 * 将别名转换成相对应的常规bean名称。
	 * 如果不能在这个factory实例中找到bean，将会访问父factory
	 */
	boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Check whether the bean with the given name matches the specified type.
	 * More specifically, check whether a {@link #getBean} call for the given name
	 * would return an object that is assignable to the specified target type.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @param typeToMatch the type to match against (as a {@code ResolvableType})
	 * @return {@code true} if the bean type matches,
	 * {@code false} if it doesn't match or cannot be determined yet
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 4.2
	 * @see #getBean
	 * @see #getType
	 * 
	 * 检查给定名称的bean是否匹配指定的类型
	 * 更具体的说，检查对于给定名称的getBean(...)的调用，将会返回一个对象是否为指定目标类型的同类或子类
	 * 
	 * 将别名转换成相对应的常规bean名称。
	 * 如果不能在这个factory实例中找到bean，将会访问父factory
	 */
	boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException;

	/**
	 * Check whether the bean with the given name matches the specified type.
	 * More specifically, check whether a {@link #getBean} call for the given name
	 * would return an object that is assignable to the specified target type.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @param typeToMatch the type to match against (as a {@code Class})
	 * @return {@code true} if the bean type matches,
	 * {@code false} if it doesn't match or cannot be determined yet
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 2.0.1
	 * @see #getBean
	 * @see #getType
	 * 
	 * 检查给定名称的bean是否匹配指定的类型
	 * 更具体的说，检查对于给定名称的getBean(...)的调用，将会返回一个对象是否为指定目标类型的同类或子类
	 * 将别名转换成相对应的常规bean名称。
	 * 如果不能在这个factory实例中找到bean，将会访问父factory
	 */
	boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException;

	/**
	 * Determine the type of the bean with the given name. More specifically,
	 * determine the type of object that {@link #getBean} would return for the given name.
	 * <p>For a {@link FactoryBean}, return the type of object that the FactoryBean creates,
	 * as exposed by {@link FactoryBean#getObjectType()}.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @return the type of the bean, or {@code null} if not determinable
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 1.1.2
	 * @see #getBean
	 * @see #isTypeMatch
	 * 
	 * 确定给定名称的bean的类型。
	 * 更具体的说，决定对于给定名称的getBean(...)返回对象的类型
	 * 对于FactoryBean， 返回FactoryBean创建的对象的类型，它是通过FactoryBean.getObjectType()来暴露的
	 * 将别名转换成相对应的常规bean名称。
	 * 如果不能在这个factory实例中找到bean，将会访问父factory
	 * 
	 */
	@Nullable
	Class<?> getType(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Return the aliases for the given bean name, if any.
	 * All of those aliases point to the same bean when used in a {@link #getBean} call.
	 * <p>If the given name is an alias, the corresponding original bean name
	 * and other aliases (if any) will be returned, with the original bean name
	 * being the first element in the array.
	 * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the bean name to check for aliases
	 * @return the aliases, or an empty array if none
	 * @see #getBean
	 * 
	 * 返回给定bean名称的别名（如果有）
	 * 但在调用getBean(...)时，所有那些别名都指向相同的bean
	 * 如果给定的名称是一个别名，则对应的原始bean名称和其他别名（如果有）都会一起返回，并且原始bean名称是作为返回数组的第一个元素
	 * 如果不能在这个factory实例中找到bean，将会访问父factory
	 */
	String[] getAliases(String name);

}
